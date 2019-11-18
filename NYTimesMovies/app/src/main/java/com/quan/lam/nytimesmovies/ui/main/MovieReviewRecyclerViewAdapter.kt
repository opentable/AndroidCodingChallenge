package com.quan.lam.nytimesmovies.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.quan.lam.nytimesmovies.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_review_list_item.view.*

/**
 * Adapter for MovieReviewListFragment's MovieReview List
 */
class MovieReviewRecyclerViewAdapter(
    private var mData: List<MovieReviewListItem>,
    private val mListener: OnMovieReviewRecyclerViewInteractionListener
) : RecyclerView.Adapter<MovieReviewRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as MovieReviewListItem
            mListener.onInteraction(item)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_review_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Populating the data accordingly
        //All the data field have their own default value in case of missing data
        //Due to restriction of time, empty field will be shown as such
        val item = mData[position]
        holder.mTitle.text = item.display_title
        holder.mRating.text = item.mpaa_rating.mpaa_rating
        holder.mHeadLine.text = item.headline
        holder.mSummaryShort.text = item.summary_short
        holder.mAuthorGroup.text = "by ${item.byline} ${item.publication_date}"
        //Checking if a poster is available, and if so, display it
        if (!item.multimedia.isEmpty()) {
            Picasso.get().load(item.multimedia).into(holder.mPoster);
            holder.mPoster.visibility = View.VISIBLE
        } else {
            holder.mPoster.visibility = View.INVISIBLE
        }

        //Right now clicking on item will do nothing. This is a placeholder for future feature.
        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mData.size

    //Constructing the viewholder
    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mTitle: TextView = mView.display_title
        val mRating: TextView = mView.mpaa_rating
        val mHeadLine: TextView = mView.headline
        val mSummaryShort: TextView = mView.summary_short
        val mPoster: ImageView = mView.poster
        val mAuthorGroup: TextView = mView.authorGroup
        override fun toString(): String {
            return "ViewHolder(mView=$mView, mTitle=$mTitle, mRating=$mRating, mHeadLine=$mHeadLine, mSummaryShort=$mSummaryShort)"
        }
    }
}