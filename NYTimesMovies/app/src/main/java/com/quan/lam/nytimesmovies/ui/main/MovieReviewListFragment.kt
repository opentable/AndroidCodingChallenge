package com.quan.lam.nytimesmovies.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.quan.lam.nytimesmovies.R
import com.quan.lam.nytimesmovies.model.MPAARating
import com.quan.lam.nytimesmovies.usecase.NYTimesMovieReviewsUseCase

/**
 * Main Fragment of the application.
 * At fragment start, load some reviews and display in a recycler view
 * Implementing using MVVM
 */
class MovieReviewListFragment : Fragment(), OnMovieReviewRecyclerViewInteractionListener {
    override fun onInteraction(item: MovieReviewListItem) {
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_mainFragment_to_ratingFragment)
    }

    private lateinit var viewModel: MovieReviewListViewModel
    private lateinit var recyclerViewAdapter: MovieReviewRecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView

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
        //Register observer for fragment state change
        viewModel.getState().observe(viewLifecycleOwner, Observer { it?.let { onStateChanged(it) } })
        //Register observer for movie age limit change
        val ageLimitObserver: Observer<MPAARating> = Observer { viewModel.onGlobalAgeLimitChange(it) }
        MPAARating.globalAgeLimit.observe(viewLifecycleOwner, ageLimitObserver)

    }

    private fun onStateChanged(state: MovieReviewListViewModel.State) = when (state) {
        is MovieReviewListViewModel.State.ReviewsLoaded -> showReviewsLoaded()
        is MovieReviewListViewModel.State.ReviewsLoadedMore -> showReviewsLoadedMore(state)
        is MovieReviewListViewModel.State.Loading -> showLoading()
        is MovieReviewListViewModel.State.LoadingMore -> showLoadingMore()
        is MovieReviewListViewModel.State.Error -> showError(state)
        else -> {}
    }

    //Refresh the whole list
    private fun showReviewsLoaded() {
        recyclerViewAdapter.notifyItemRangeChanged(0, viewModel.getFilteredItemCount())
        //Toast.makeText(this.context, "Loaded.  Item count ${viewModel.getFilteredItemCount()}.", Toast.LENGTH_SHORT).show()
    }

    //Refresh added part of the list
    private fun showReviewsLoadedMore(state: MovieReviewListViewModel.State.ReviewsLoadedMore) {
        recyclerViewAdapter.notifyItemRangeChanged(state.offset, viewModel.getFilteredItemCount())
        //Toast.makeText(this.context, "Loaded some more. Item count ${viewModel.getFilteredItemCount()}.", Toast.LENGTH_SHORT).show()
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
