package demo.whitbread.andreas.whitbreaddemo.Volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by WestPlay on 19/6/2016.
 */
public class VolleyManager {

    //A singleton for keeping one request queue throughout the lifetime of the app was created, based on the recommendation here:
    //https://developer.android.com/training/volley/requestqueue.html

    private static VolleyManager volleyManagerInstance;
    private RequestQueue requestQueue;
    private Context context;

    private VolleyManager(Context context) {
        // getApplicationContext() is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        this.context = context.getApplicationContext();
        requestQueue = getRequestQueue();
    }

    public static synchronized VolleyManager getInstance(Context context) {
        if (volleyManagerInstance == null) {
            volleyManagerInstance = new VolleyManager(context);
        }
        return volleyManagerInstance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
