package com.opentable.moviereviews.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.opentable.moviereviews.R;
import com.opentable.moviereviews.data.Result;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

/**
 * @author ronan.oneill
 *
 * Adapter class that manages retrieving the data from the Result (Movie review search object)
 * and populating each result view in the list correctly
 */
public class MoviesAdapter extends ArrayAdapter<Result> {

    public MoviesAdapter(Context context, ArrayList<Result> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get data from position
        Result item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_list_item, parent, false);
        }

        // Lookup view for data population
        TextView tvTitle = (TextView) convertView.findViewById(R.id.list_item_title);
        TextView tvMpaaRating = (TextView) convertView.findViewById(R.id.list_item_mpaa);
        TextView tvHeadline = (TextView) convertView.findViewById(R.id.list_item_headline);
        TextView tvSummary = (TextView) convertView.findViewById(R.id.list_item_summary);
        TextView tvReviewer = (TextView) convertView.findViewById(R.id.list_item_reviewer);
        TextView tvPubDate = (TextView) convertView.findViewById(R.id.list_item_pubdate);
        ImageView multimediaImage = (ImageView) convertView.findViewById(R.id.list_item_multimedia);

        // Populate the data into the view using the data object
        tvTitle.setText(item.getDisplayTitle());
        if (item.getMpaaRating()!=null && !item.getMpaaRating().isEmpty())
            tvMpaaRating.setText(item.getMpaaRating());
        else
            tvMpaaRating.setText(R.string.rating_na);
        tvHeadline.setText(item.getHeadline());
        tvSummary.setText(item.getSummaryShort());
        tvReviewer.setText(item.getByline());
        tvPubDate.setText(item.getPublicationDate());

        //use the url to populate the poster image
        String imageUrl = item.getMultimedia().getSrc();
        Picasso.get().load(imageUrl).into(multimediaImage);

        // Return the completed view to render on screen
        return convertView;
    }
}

