package com.quan.lam.nytimesmovies.ui.filter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.quan.lam.nytimesmovies.R
import com.quan.lam.nytimesmovies.model.MPAARating

/**
 * A fragment representing a list of Ratings for selection.
 */
class RatingFragment : Fragment(), OnListFragmentInteractionListener {
    override fun onListFragmentInteraction(item: MPAARatingItem?) {
        MPAARating.globalAgeLimit.value = item?.mpaa_rating?.let { MPAARating(it) }
        NavHostFragment.findNavController(this)
            .popBackStack()
    }
    private var listener: OnListFragmentInteractionListener? = this

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rating_list, container, false)

        // Set the adapter
        val recyclerView = view.findViewById<RecyclerView>(R.id.ratingList)

        with(recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = MyRatingRecyclerViewAdapter(MPAARatingItem.getItems(), listener)
        }

        return view
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
