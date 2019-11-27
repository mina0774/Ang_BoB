package com.example.ang_bob;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class PushAlarm {
    private  static PushAlarm instance;
    private RequestQueue requestQueue;
    private Context ctx;

    private PushAlarm(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized PushAlarm getInstance(Context context) {
        if (instance == null) {
            instance = new PushAlarm(context);
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
}
