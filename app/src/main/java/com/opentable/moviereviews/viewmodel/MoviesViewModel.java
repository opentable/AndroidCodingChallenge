package com.opentable.moviereviews.viewmodel;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.google.gson.Gson;
import com.opentable.moviereviews.data.Result;
import com.opentable.moviereviews.service.MovieReviewsCallBack;
import com.opentable.moviereviews.service.MovieReviewsService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author ronan.oneill
 *
 * ViewModel implements LiveData models to link
 * the UI listeners to the data retrieved from the MovieService
 *
 * ViewModel offers the benefit of storing and managing the data in a lifecycle conscious way.
 *
 */
public class MoviesViewModel extends ViewModel {
    private String TAG = MoviesViewModel.class.getSimpleName();
    private Gson gson = new Gson();

    private MutableLiveData<ArrayList<Result>> itemList;
    private MutableLiveData<Throwable> errorLiveData;

    public LiveData<ArrayList<Result>> getItemList() {
        if (itemList == null) {
            itemList = new MutableLiveData<>();
            errorLiveData = new MutableLiveData<>();
            loadMovies();
        }
        return itemList;
    }

    public LiveData<Throwable> getStatus() {
        return errorLiveData;
    }

    public void refreshItemList() {
        loadMovies();
    }


    private void loadMovies() {
        MovieReviewsService.getInstance().getMovieReviews(new MovieReviewsCallBack() {
            @Override
            public void onSuccess(Object result) {
                //cast result to JSONArray
                JSONArray resultArray = (JSONArray) result;
                ArrayList<Result> itemsList = new ArrayList<>();
                for (int i = 0; i < resultArray.length(); i++) {

                    try {
                        JSONObject movie = resultArray.getJSONObject(i);

                        Result sMovie = gson.fromJson(movie.toString(), Result.class);
                        itemsList.add(sMovie);

                    } catch (JSONException e) {
                        errorLiveData.postValue(e);
                    }
                }
                itemList.setValue(itemsList);
            }

            @Override
            public void onFailure(Exception e) {
                errorLiveData.postValue(e);
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "on cleared called");
    }
}

