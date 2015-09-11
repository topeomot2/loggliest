package com.inrista.loggliest;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Temitope on 9/11/2015.
 */
public class BulkVolley {

    private static BulkVolley mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;


    private BulkVolley(Context context){

        mCtx = context;
        mRequestQueue = getRequestQueue();
    }



    public static synchronized BulkVolley getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new BulkVolley(context);
        }
        return mInstance;
    }


    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {

            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());

        }

        return mRequestQueue;
    }


    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
