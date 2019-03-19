package com.opentable.moviereviews.service;

/**
 * @author ronan.oneill
 */
public interface MovieReviewsCallBack<T> {

    /**
     * An operation has completed successfully with the specified result
     */
    public void onSuccess(T result);

    /**
     * An operation has completed unsuccessfully with the specified exception
     */
    public void onFailure(Exception e);
}
