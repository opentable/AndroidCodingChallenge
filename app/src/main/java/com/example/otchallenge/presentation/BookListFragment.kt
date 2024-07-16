package com.example.otchallenge.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.otchallenge.MyApplication
import com.example.otchallenge.data.entity.Book
import com.example.otchallenge.databinding.FragmentBookListBinding
import javax.inject.Inject

class BookListFragment : Fragment(), HardcoverFictionListContract.View {

    @Inject
    lateinit var presenter: HardcoverFictionListContract.Presenter

    private lateinit var binding: FragmentBookListBinding

    private val bookListAdapter = BookListAdapter().apply {
        addLoadStateListener { loadStates ->
            when  {
                loadStates.refresh is LoadState.Loading -> {
                    if (itemCount > 0) {
                        binding.bookList.visibility = View.VISIBLE
                        binding.refreshLayout.isRefreshing = true
                        binding.loading.root.visibility = View.GONE
                    } else {
                        binding.bookList.visibility = View.GONE
                        binding.refreshLayout.isRefreshing = false
                        binding.loading.root.visibility = View.VISIBLE
                    }
                    binding.retry.root.visibility = View.GONE
                }
                loadStates.hasError -> {
                    binding.bookList.visibility = View.GONE
                    binding.retry.root.visibility = View.VISIBLE
                    binding.refreshLayout.isRefreshing = false
                    binding.loading.root.visibility = View.GONE
                }
                loadStates.isIdle -> {
                    binding.bookList.visibility = View.VISIBLE
                    binding.retry.root.visibility = View.GONE
                    binding.refreshLayout.isRefreshing = false
                    binding.loading.root.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Deprecated but necessary to retain Presenter
        retainInstance = true
        injectDependencies()
        presenter.subscribeToList(lifecycleScope)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentBookListBinding.inflate(inflater)
            .also { binding ->
                setupView(binding)
            }.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.attachView(this)

        binding.bookList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
            )
            adapter = bookListAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    override fun submitPage(page: PagingData<Book>) {
        bookListAdapter.submitData(
            lifecycle = viewLifecycleOwner.lifecycle,
            pagingData = page
        )
    }

    private fun setupView(binding: FragmentBookListBinding) {
        with(binding) {
            this@BookListFragment.binding = this
            loading.root.visibility = View.GONE
            retry.root.visibility = View.GONE
            retry.btnRetry.setOnClickListener {
                bookListAdapter.retry()
            }
            refreshLayout.setOnRefreshListener {
                bookListAdapter.refresh()
            }
        }
    }

    private fun injectDependencies() {
        (requireActivity().application as MyApplication)
            .appComponent.inject(this)
    }

}