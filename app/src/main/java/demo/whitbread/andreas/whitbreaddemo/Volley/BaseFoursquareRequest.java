package demo.whitbread.andreas.whitbreaddemo.Volley;

import android.content.res.Resources;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;

import java.util.Map;

import demo.whitbread.andreas.whitbreaddemo.R;

/**
 * Created by WestPlay on 19/6/2016.
 */
public abstract class BaseFoursquareRequest<T> extends Request<T> {

    public static final String TAG = BaseFoursquareRequest.class.getSimpleName();
    private static final String CLIENT_ID = "0OGERFK4PE1OTC4BKWU2EGYFFKYFFBIW5WDSSGARP2VWEQDK";
    private static final String CLIENT_SECRET = "3XRBYWECUEQ2SP3BRP0KU5FV10RISCB1D12XD1SED0DECFA2";
    private static final String API_VERSION = "20160619";

    private Map<String, String> headers;

    public BaseFoursquareRequest(int method, String secondaryUrlString, Response.ErrorListener listener, Resources resources) {
        //Create the API call URL by creating a String using the base API url and the secondary string
        super(method, String.format(resources.getString(R.string.foursquare_api_address), secondaryUrlString, CLIENT_ID, CLIENT_SECRET, API_VERSION), listener);
        setupHeaders();
        Log.d(TAG, String.format(resources.getString(R.string.foursquare_api_address), secondaryUrlString, CLIENT_ID, CLIENT_SECRET, API_VERSION));
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    protected abstract void setupHeaders();
}
