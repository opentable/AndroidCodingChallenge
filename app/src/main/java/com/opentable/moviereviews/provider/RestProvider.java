package com.opentable.moviereviews.provider;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.Volley;

/**
 * @author ronan.oneill
 *
 * Singleton class that sets up the Volley RequestQueue
 * which lasts for the lifetime of the app.
 * Improves effeciency when dealing with multiple network calls.
 */

public class RestProvider {
  private static RestProvider instance;
  private RequestQueue requestQueue;
  private static Context ctx;
  private static final int DEFAULT_TIMEOUT = 3000; //timeout in ms
  private static final int DEFAULT_MAX_RETRIES = 2; //Number of times retry is attempted
  private static final float DEFAULT_BACKOFF_MULTIPLIER = 2.0f; //A multiplier which is used to determine exponential time set to socket for every retry attempt.


  private RestProvider(Context context) {
    ctx = context;
    requestQueue = getRequestQueue();
  }

  public static synchronized RestProvider getInstance(Context context) {
    if (instance == null) {
      instance = new RestProvider(context);
    }
    return instance;
  }

  public RequestQueue getRequestQueue() {
    if (requestQueue == null) {
      // getApplicationContext() is key, it keeps you from leaking the
      // Activity or BroadcastReceiver if someone passes one in.
      requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
    }
    return requestQueue;
  }

  public <T> void addToRequestQueue(Request<T> req) {
    getRequestQueue().add(req);
  }

  public RetryPolicy getRetryPolicy() {
    return new DefaultRetryPolicy(DEFAULT_TIMEOUT,
            DEFAULT_MAX_RETRIES,
            DEFAULT_BACKOFF_MULTIPLIER);
  }
}
