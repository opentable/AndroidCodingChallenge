package com.example.otchallenge.presentation.screens.books.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.otchallenge.R
import com.example.otchallenge.data.model.Book
import com.example.otchallenge.data.remote.RemoteRequestError
import com.example.otchallenge.databinding.FragmentBookListBinding
import com.example.otchallenge.presentation.components.AlertDialogFragment
import com.example.otchallenge.presentation.components.DaggerRetainedFragment
import com.example.otchallenge.presentation.extensions.isEmpty
import com.example.otchallenge.presentation.extensions.subscribeToEvent
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class BookListFragment : DaggerRetainedFragment(), BookListContract.View {

    @Inject
    lateinit var presenter: BookListContract.Presenter

    private var binding: FragmentBookListBinding? = null

    private var bookListAdapter: BookListAdapter? = null

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

    override fun retryLoading() {
        bookListAdapter?.retry()
    }

    override fun listItemCount(): Int {
        return bookListAdapter?.itemCount ?: 0
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
        return BookListAdapter()
            .also { adapter ->
                bookListAdapter = adapter
            }
            .also { adapter ->
                adapter.addLoadStateListener { loadStates ->
                    presenter.updateListState(
                        isEmpty = adapter.isEmpty,
                        loadStates = loadStates
                    )
                }
            }
    }

    private fun CompositeDisposable.listenAlertDialogFragmentEventBus() {
        AlertDialogFragment.eventBus
            .subscribeToEvent<AlertDialogFragment.Event.ButtonClick>(
                subscriptions = this,
                dialogTag = TAG_ERROR_DIALOG
            ) { event ->
                when (event.buttonType) {
                    AlertDialogFragment.ButtonType.Positive -> {
                        retryLoading()
                    }
                    AlertDialogFragment.ButtonType.Negative -> {
                        dismissErrorDialog()
                    }
                }
            }
    }

    override fun showIdleState() {
        binding?.apply {
            bookList.visibility = View.VISIBLE
            retry.root.visibility = View.GONE
            refreshLayout.isRefreshing = false
            loading.root.visibility = View.GONE
        }
    }

    override fun showFullScreenRefreshState() {
        binding?.apply {
            bookList.visibility = View.GONE
            refreshLayout.isRefreshing = false
            loading.root.visibility = View.VISIBLE
            retry.root.visibility = View.GONE
        }
    }

    override fun showRefreshIndicator() {
        binding?.apply {
            bookList.visibility = View.VISIBLE
            refreshLayout.isRefreshing = true
            loading.root.visibility = View.GONE
            retry.root.visibility = View.GONE
        }
    }

    override fun showFullScreenErrorState(error: Throwable?) {
        binding?.apply {
            bookList.isVisible = false
            loading.root.isVisible = false
            retry.root.isVisible = true
            retry.txtErrorMessage.text = getString(getErrorMessageId(error))
            refreshLayout.isRefreshing = false
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

    override fun showErrorDialog(
        error: Throwable?
    ) {
        binding?.refreshLayout?.isRefreshing = false
        AlertDialogFragment {
            titleId = R.string.common_dialog_title_error
            messageId = getErrorMessageId(error)
            positiveButtonTextId = R.string.common_button_label_retry
            negativeButtonTextId = R.string.common_button_label_tryAgainLater
        }.show(childFragmentManager, TAG_ERROR_DIALOG)
    }

    override fun dismissErrorDialog() {
        val fragment = childFragmentManager.findFragmentByTag(TAG_ERROR_DIALOG)

        if (fragment is AlertDialogFragment) {
            fragment.dismiss()
        }
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