package com.example.otchallenge.presentation.screens.books.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.otchallenge.R
import com.example.otchallenge.data.model.Book
import com.example.otchallenge.data.remote.RemoteRequestError
import com.example.otchallenge.databinding.FragmentBookListBinding
import com.example.otchallenge.presentation.components.AlertDialogFragment
import com.example.otchallenge.presentation.components.DaggerRetainedFragment
import com.example.otchallenge.presentation.components.MarginItemDecoration
import com.example.otchallenge.presentation.extensions.WindowSizeClass
import com.example.otchallenge.presentation.extensions.getWidthSizeClass
import com.example.otchallenge.presentation.extensions.isEmpty
import com.example.otchallenge.presentation.extensions.subscribeToEvent
import io.reactivex.disposables.CompositeDisposable
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class BookListFragment : DaggerRetainedFragment(), BookListContract.View {

    @Inject
    lateinit var presenter: BookListContract.Presenter

    private val args by navArgs<BookListFragmentArgs>()

    private var binding: FragmentBookListBinding? = null

    private var bookListAdapter: BookListAdapter? = null

    private var eventBusSubscriptions: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.subscribeToList(
            coroutineScope = lifecycleScope,
            bookListId = args.bookListId,
            date = args.date
        )
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

    override fun submitPagingData(page: PagingData<Book>) {
        bookListAdapter?.submitData(
            lifecycle = viewLifecycleOwner.lifecycle,
            pagingData = page
        )
    }

    override fun retryLoading() {
        bookListAdapter?.retry()
    }

    override fun setActionBar(listType: BookListContract.ListType) {
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = listType.bookList.name
            subtitle = when (listType) {
                is BookListContract.ListType.Current -> {
                    getString(R.string.label_current)
                }
                is BookListContract.ListType.Date -> {
                    listType.date.format(
                        DateTimeFormatter.ofPattern(getString(R.string.date_format), Locale.getDefault())
                    )
                }
            }
        }
    }

    private fun FragmentBookListBinding.setupView() {
        binding = this
        setUpViewsVisibility()
        setUpList()
    }

    private fun FragmentBookListBinding.setUpViewsVisibility() {
        lytLoading.root.visibility = View.GONE
        lytRetry.root.visibility = View.GONE
        lytRetry.btnRetry.setOnClickListener {
            bookListAdapter?.retry()
        }
        lytRefresh.setOnRefreshListener {
            bookListAdapter?.refresh()
        }
    }

    private fun FragmentBookListBinding.setUpList() {
        rvBookList.apply {
            layoutManager = setupLayoutManager()
            addItemDecoration(
                MarginItemDecoration(
                    horizontal = 16,
                    vertical = 16
                )
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

    private fun setupLayoutManager(): LayoutManager {
        return if (requireActivity().getWidthSizeClass <= WindowSizeClass.Medium) {
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
        } else {
            GridLayoutManager(
                requireContext(),
                3,
                GridLayoutManager.VERTICAL,
                false
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
            .subscribeToEvent<AlertDialogFragment.Event>(
                subscriptions = this,
                dialogTag = TAG_ERROR_DIALOG
            ) { event ->
                when (event) {
                    is AlertDialogFragment.Event.ButtonClick -> {
                        when (event.buttonType) {
                            AlertDialogFragment.ButtonType.Positive -> {
                                retryLoading()
                                dismissErrorDialog()
                            }
                            AlertDialogFragment.ButtonType.Negative -> {
                                dismissErrorDialog()
                            }
                        }
                    }
                    is AlertDialogFragment.Event.Dismiss -> {
                        presenter.onErrorDialogDismissed()
                    }
                }
            }
    }

    override fun showIdleState() {
        binding?.apply {
            rvBookList.visibility = View.VISIBLE
            lytRetry.root.visibility = View.GONE
            lytRefresh.isRefreshing = false
            lytLoading.root.visibility = View.GONE
        }
    }

    override fun showFullScreenRefreshState() {
        binding?.apply {
            rvBookList.visibility = View.GONE
            lytRefresh.isRefreshing = false
            lytLoading.root.visibility = View.VISIBLE
            lytRetry.root.visibility = View.GONE
        }
    }

    override fun showRefreshIndicator() {
        binding?.apply {
            rvBookList.visibility = View.VISIBLE
            lytRefresh.isRefreshing = true
            lytLoading.root.visibility = View.GONE
            lytRetry.root.visibility = View.GONE
        }
    }

    override fun showFullScreenErrorState(error: Throwable?) {
        binding?.apply {
            rvBookList.isVisible = false
            lytLoading.root.isVisible = false
            lytRetry.root.isVisible = true
            lytRetry.txtErrorMessage.text = getString(getErrorMessageId(error))
            lytRefresh.isRefreshing = false
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
        binding?.lytRefresh?.isRefreshing = false
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