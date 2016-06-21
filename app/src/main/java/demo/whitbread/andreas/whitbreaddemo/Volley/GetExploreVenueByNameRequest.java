package demo.whitbread.andreas.whitbreaddemo.Volley;

import android.content.res.Resources;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;

import demo.whitbread.andreas.whitbreaddemo.Model.VenueSearchResponse;
import demo.whitbread.andreas.whitbreaddemo.R;

/**
 * Created by WestPlay on 19/6/2016.
 */
public class GetExploreVenueByNameRequest extends BaseFoursquareRequest<VenueSearchResponse> {

    public static final String TAG = GetExploreVenueByNameRequest.class.getSimpleName();

    private final Gson gson = new Gson();
    private final Response.Listener<VenueSearchResponse> successListner;

    public GetExploreVenueByNameRequest(String venueName, double latitude, double longitude, Response.ErrorListener errorListener,
                                        Response.Listener<VenueSearchResponse> successListner, Resources resources) {
        super(Method.GET, String.format(resources.getString(R.string.foursquare_venue_search),venueName, latitude, longitude), errorListener, resources);
        this.successListner = successListner;
        //Set a Tag for the request, in order to be able to be found in the request queue and be cancelled
        setTag(TAG);
    }

    @Override
    protected void setupHeaders() {

    }

    @Override
    protected Response<VenueSearchResponse> parseNetworkResponse(NetworkResponse response) {
//        Log.d(TAG, response.data.toString());
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(
                    gson.fromJson(json, VenueSearchResponse.class),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(VenueSearchResponse response) {
        successListner.onResponse(response);
    }
}
