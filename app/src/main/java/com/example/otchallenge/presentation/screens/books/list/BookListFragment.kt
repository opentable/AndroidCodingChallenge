package com.example.otchallenge.presentation.screens.books.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.otchallenge.R
import com.example.otchallenge.data.entity.Book
import com.example.otchallenge.data.remote.RemoteRequestError
import com.example.otchallenge.databinding.FragmentBookListBinding
import com.example.otchallenge.presentation.components.AlertDialogFragment
import com.example.otchallenge.presentation.components.DaggerRetainedFragment
import com.example.otchallenge.presentation.extensions.error
import com.example.otchallenge.presentation.extensions.isEmpty
import com.example.otchallenge.presentation.extensions.isLoading
import com.example.otchallenge.presentation.extensions.subscribeToButtonClickEvents
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class BookListFragment : DaggerRetainedFragment(), BookListContract.View {

    @Inject
    lateinit var presenter: BookListContract.Presenter

    private var binding: FragmentBookListBinding? = null

    private var bookListAdapter: BookListAdapter? = null

    private var isCurrentErrorDetailsShown = false

    private var isInNetworkErrorState = false

    private var eventBusSubscriptions: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.subscribeToList(lifecycleScope)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return LayoutInflater.from(requireContext())
            .inflate(R.layout.fragment_book_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentBookListBinding.bind(view)
            .apply { setupView() }

        eventBusSubscriptions = CompositeDisposable()
            .apply {
                listenAlertDialogFragmentEventBus()
            }

        presenter.attachView(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
        clearReferences()
    }

    override fun submitPage(page: PagingData<Book>) {
        bookListAdapter?.submitData(
            lifecycle = viewLifecycleOwner.lifecycle,
            pagingData = page
        )
    }

    override fun retryLoadingIfNecessary() {
        if (isInNetworkErrorState) {
            bookListAdapter?.retry()
        }
    }

    private fun FragmentBookListBinding.setupView() {
        binding = this
        setUpViewsVisibility()
        setUpList()
    }

    private fun FragmentBookListBinding.setUpViewsVisibility() {
        loading.root.visibility = View.GONE
        retry.root.visibility = View.GONE
        retry.btnRetry.setOnClickListener {
            bookListAdapter?.retry()
        }
        refreshLayout.setOnRefreshListener {
            bookListAdapter?.refresh()
        }
    }

    private fun FragmentBookListBinding.setUpList() {
        bookList.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = setupListAdapter().withLoadStateFooter(
                BookListFooterAdapter(
                    onDetailsClick = {
                        showErrorDialog(error = it.error)
                    },
                    onRetryClick = { bookListAdapter?.retry() }
                )
            )
        }
    }

    private fun setupListAdapter(): BookListAdapter {
        return BookListAdapter().apply {
            addLoadStateListener { loadStates ->
                when  {
                    loadStates.isLoading -> {
                        if (loadStates.refresh is LoadState.Loading) {
                            displayRefreshState()
                        }
                        isInNetworkErrorState = false
                        isCurrentErrorDetailsShown = false
                    }
                    loadStates.hasError -> {
                        loadStates.error?.let { error ->
                            isInNetworkErrorState = error is RemoteRequestError.NoConnection
                            displayErrorMessage(error = error)
                        }
                    }
                    loadStates.isIdle -> {
                        displayIdleState()
                    }
                }
            }
        }.also {
            bookListAdapter = it
        }
    }

    private fun CompositeDisposable.listenAlertDialogFragmentEventBus() {
        AlertDialogFragment.eventBus
            .subscribeToButtonClickEvents(
                subscriptions = this,
                dialogTag = TAG_ERROR_DIALOG
            ) { event ->
                when (event.buttonType) {
                    AlertDialogFragment.ButtonType.Positive -> {
                        bookListAdapter?.retry()
                    }
                    AlertDialogFragment.ButtonType.Negative -> {
                        event.dialog.dismiss()
                    }
                }
            }
    }

    private fun displayIdleState() {
        binding?.apply {
            bookList.visibility = View.VISIBLE
            retry.root.visibility = View.GONE
            refreshLayout.isRefreshing = false
            loading.root.visibility = View.GONE
        }
    }

    private fun displayRefreshState() {
        bookListAdapter?.let { bookListAdapter ->
            binding?.let { binding ->
                if (bookListAdapter.isEmpty) {
                    binding.bookList.visibility = View.GONE
                    binding.refreshLayout.isRefreshing = false
                    binding.loading.root.visibility = View.VISIBLE
                } else {
                    binding.bookList.visibility = View.VISIBLE
                    binding.refreshLayout.isRefreshing = true
                    binding.loading.root.visibility = View.GONE
                }
                binding.retry.root.visibility = View.GONE
            }
        }
    }

    private fun displayErrorMessage(error: Throwable?) {
        when {
            bookListAdapter?.isEmpty == true -> {
                showFullScreenErrorMessage(error)
            }
            !isCurrentErrorDetailsShown -> {
                isCurrentErrorDetailsShown = true
                showErrorDialog(error)
            }
        }
        binding?.refreshLayout?.isRefreshing = false
    }

    private fun showFullScreenErrorMessage(error: Throwable?) {
        binding?.apply {
            bookList.isVisible = false
            loading.root.isVisible = false
            retry.root.isVisible = true
            retry.txtErrorMessage.text = getString(getErrorMessageId(error))
        }
    }

    @StringRes
    private fun getErrorMessageId(error: Throwable?): Int {
        return when (error) {
            is RemoteRequestError.Server -> {
                R.string.common_list_books_loading_error_server
            }
            is RemoteRequestError.ConnectionTimeout, is RemoteRequestError.NoConnection -> {
                R.string.common_list_books_loading_error_network
            }
            else -> {
                R.string.common_list_books_loading_error_unknown
            }
        }
    }

    private fun showErrorDialog(
        error: Throwable?
    ) {
        AlertDialogFragment {
            titleId = R.string.common_dialog_title_error
            messageId = getErrorMessageId(error)
            positiveButtonTextId = R.string.common_button_label_retry
            negativeButtonTextId = R.string.common_button_label_tryAgainLater
        }.show(childFragmentManager, TAG_ERROR_DIALOG)
    }

    private fun clearReferences() {
        eventBusSubscriptions?.dispose()
        eventBusSubscriptions = null
        bookListAdapter = null
        binding = null
    }

    companion object {
        private const val TAG_ERROR_DIALOG = "BookListFragmentErrorDialog"
    }

}