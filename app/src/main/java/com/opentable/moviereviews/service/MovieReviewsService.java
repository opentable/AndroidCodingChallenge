package com.opentable.moviereviews.service;


import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.opentable.moviereviews.lifecycle.ApplicationWrapper;
import com.opentable.moviereviews.provider.RestProvider;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author ronan.oneill
 *
 * Service that provides the ability to retrieve movies reviews
 * from the New York Times open api.
 *
 */
public class MovieReviewsService {
    private static MovieReviewsService instance;
    // API key used to access the NY Times open api
    private final static String API_KEY = "KoRB4K5LRHygfjCL2AH6iQ7NeUqDAGAB";
    public static final String TAG = MovieReviewsService.class.getSimpleName();
    private final static String url ="http://api.nytimes.com/svc/movies/v2/reviews/dvd-picks.json/";


    private MovieReviewsService() {

    }

    public static synchronized MovieReviewsService getInstance() {
        if (instance == null) {
            instance = new MovieReviewsService();
        }
        return instance;
    }

    public void getMovieReviews(final MovieReviewsCallBack listener) {
        // Instantiate the RequestQueue.
        RestProvider restProvider = RestProvider.getInstance(ApplicationWrapper.getAppContext());

        //compile the url to retrieve movie reviews
        String reviewsUrl = url + "?order=by-date" + "&api-key=" + API_KEY;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, reviewsUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Success in call, response is: " + response.toString());
                        //convert json to array of movies(results)
                        try {
                            Gson gson = new Gson();
                            JSONArray searchArray = response.getJSONArray("results");
                            listener.onSuccess(searchArray);
                        } catch (Exception e) {
                            listener.onFailure(e);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Handle networking errors
                NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null)
                    listener.onFailure(new Exception("Error connecting to server, status code: " + response.statusCode));
                else if (error.getCause() != null && error.getCause().getMessage() != null)
                    //Display detailed message from error cause
                    // e.g. "Unable to resolve host....."
                    listener.onFailure(new Exception(error.getCause().getMessage() ));
                else
                    listener.onFailure(new Exception("Unknown networking error occurred"));
            }
        });

        jsonObjectRequest.setTag(TAG);
        //handle case of slow internet connection by setting up a Volley retry policy
        jsonObjectRequest.setRetryPolicy(restProvider.getRetryPolicy());
        // Add the request to the RequestQueue.
        restProvider.addToRequestQueue(jsonObjectRequest);
    }
}
