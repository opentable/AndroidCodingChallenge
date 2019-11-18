package com.quan.lam.nytimesmovies.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.quan.lam.nytimesmovies.R
import com.quan.lam.nytimesmovies.usecase.NYTimesMovieReviewsUseCase

/**
 * Main Fragment of the application.
 * At fragment start, load some reviews and display in a recycler view
 * Implementing using MVVM
 */
class MovieReviewListFragment : Fragment(), OnMovieReviewRecyclerViewInteractionListener {
    override fun onInteraction(item: MovieReviewListItem) {

    }

    companion object {
        fun newInstance() = MovieReviewListFragment()
    }

    private lateinit var viewModel: MovieReviewListViewModel
    private lateinit var recyclerViewAdapter: MovieReviewRecyclerViewAdapter
    lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        recyclerView = view.findViewById(R.id.reviewsList)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Get ViewModel from MovieReviewViewModelFactory, allowing customization of data source
        viewModel = ViewModelProviders.of(this,
            MovieReviewViewModelFactory(NYTimesMovieReviewsUseCase.provide())
        ).get(MovieReviewListViewModel::class.java)


        //Implementing State Machine pattern allow the View(fragment) to strictly update itself
        //via observing the ViewModel, thus reduce coupling.

        setUpViewModelStateObservers()

        //Setting up Movie Reviews Recycler View
        recyclerViewAdapter = MovieReviewRecyclerViewAdapter(
            viewModel.getReviewsList(),
            this)
        with(recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerViewAdapter
        }

        //On Recycler view hitting the bottom edge, automatically loading some more reviews
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.fetchMoreMovieReviews()
                }
            }
        })

        //When fragment first started and was in Uninitialized state, start fetching reviews
        if (viewModel.getState().value is MovieReviewListViewModel.State.Uninitialized) {
            viewModel.fetchMovieReviews()
        }
    }

    private fun setUpViewModelStateObservers() {
        viewModel.getState().observe(this, Observer { it?.let { onStateChanged(it) } })
    }

    private fun onStateChanged(state: MovieReviewListViewModel.State) = when (state) {
        is MovieReviewListViewModel.State.ReviewsLoaded -> showReviewsLoaded()
        is MovieReviewListViewModel.State.ReviewsLoadedMore -> showReviewsLoadedMore(state)
        is MovieReviewListViewModel.State.Loading -> showLoading()
        is MovieReviewListViewModel.State.LoadingMore -> showLoadingMore()
        is MovieReviewListViewModel.State.Error -> showError(state)
        else -> {}
    }

    private fun showReviewsLoaded() {
        recyclerViewAdapter.notifyItemRangeChanged(0, viewModel.getItemCount())
        Toast.makeText(this.context, "Loaded.  Item count ${viewModel.getItemCount()}.", Toast.LENGTH_SHORT).show()
    }

    private fun showReviewsLoadedMore(state: MovieReviewListViewModel.State.ReviewsLoadedMore) {
        recyclerViewAdapter.notifyItemRangeChanged(state.offset, viewModel.getItemCount())
        Toast.makeText(this.context, "Loaded some more. Item count ${viewModel.getItemCount()}.", Toast.LENGTH_SHORT).show()

    }

    private fun showLoading() {
        Toast.makeText(this.context, "Loading.", Toast.LENGTH_SHORT).show()
    }

    private fun showLoadingMore() {
        Toast.makeText(this.context, "Loading some more.", Toast.LENGTH_SHORT).show()
    }

    private fun showError(state: MovieReviewListViewModel.State.Error) {
        Toast.makeText(this.context, "Error: "+state.throwable.toString(), Toast.LENGTH_LONG).show()
    }
}
