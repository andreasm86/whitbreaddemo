package demo.whitbread.andreas.whitbreaddemo.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;

import demo.whitbread.andreas.whitbreaddemo.Adapters.VenuesRecyclerAdapter;
import demo.whitbread.andreas.whitbreaddemo.ContentProviders.VenueSuggestionsProvider;
import demo.whitbread.andreas.whitbreaddemo.Helpers.IntentHelper;
import demo.whitbread.andreas.whitbreaddemo.Model.Venue;
import demo.whitbread.andreas.whitbreaddemo.Model.VenueSearchResponse;
import demo.whitbread.andreas.whitbreaddemo.R;
import demo.whitbread.andreas.whitbreaddemo.Volley.GetExploreVenueByNameRequest;
import demo.whitbread.andreas.whitbreaddemo.Volley.GetSearchVenueByNameRequest;
import demo.whitbread.andreas.whitbreaddemo.Volley.VolleyManager;

public class MainActivity extends LocationActivity implements VenuesRecyclerAdapter.OnVenueButtonClickListener, Response.ErrorListener, Response.Listener<VenueSearchResponse> {

    public static final String TAG = MainActivity.class.getSimpleName();
    private static final String SAVED_INSTANCE_LAYOUT_MANAGER = "SAVED_INSTANCE_LAYOUT_MANAGER";
    private static final String SAVED_INSTANCE_ADAPTER_DATA = "SAVED_INSTANCE_ADAPTER_DATA";
    private static final String SAVED_INSTANCE_SEARCH_VIEW_QUERY = "SAVED_INSTANCE_SEARCH_VIEW_QUERY";

    private RecyclerView recyclerView;
    private VenuesRecyclerAdapter venuesRecyclerAdapter;
    private Location lastLocation;
    private SearchView searchView;
    private View indeterminateProgressBar;
    private String savedInstanceQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.main_activity_recycler_view);
        indeterminateProgressBar = findViewById(R.id.activity_main_progress_bar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        venuesRecyclerAdapter = new VenuesRecyclerAdapter(this, this);
        recyclerView.setAdapter(venuesRecyclerAdapter);
        //Since the items will have the same size, we call this method to improve performance
        recyclerView.setHasFixedSize(true);
        //Restore the previous saved recycler layout manager instance and search results
        if(savedInstanceState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(SAVED_INSTANCE_LAYOUT_MANAGER));
            venuesRecyclerAdapter.setAdapterData((ArrayList<Venue>) savedInstanceState.getSerializable(SAVED_INSTANCE_ADAPTER_DATA));
            savedInstanceQuery = savedInstanceState.getString(SAVED_INSTANCE_SEARCH_VIEW_QUERY, "");
        }
        showProgressBar(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!super.onCreateOptionsMenu(menu))
            return false;
        //Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        //Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //Do not iconify the widget; expand it by default
        searchView.setIconifiedByDefault(false);
        //Restore an existing query from a saved instance
        searchView.setQuery(savedInstanceQuery, false);
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestLastLocation();
    }

    @Override
    protected void onLocationRetrievalFailed() {
        Log.d(TAG, "onLocationRetrievalFailed");
    }

    @Override
    protected void onNoLocationPermissionGranted() {
        Log.d(TAG, "onNoLocationPermissionGranted");
    }

    @Override
    protected void onLocationRetrieved(Location location) {
        Log.d(TAG, "Got location:\t" + location.getLatitude() + "," + location.getLongitude());
        lastLocation = location;
    }

    /**
     * Using the onNewIntent to get the search query because this activity has its launch mode to launchMode="singleTop",
     * which does not create a new instance if it already exists in memory/instantiated.
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // Get the new search query
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d(TAG, "Search intent received:\t" + query);
            //Save last search in order for it to appear in the search history when the user searches again
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    VenueSuggestionsProvider.AUTHORITY, VenueSuggestionsProvider.MODE);
            suggestions.saveRecentQuery(query, null);

            if(lastLocation == null){
                //Cannot search without location
                //Display a snackbar with the option to retry getting the phone's last location
                Snackbar snackbar = Snackbar
                        .make(recyclerView, R.string.activity_main_location_not_found, Snackbar.LENGTH_LONG)
                        .setAction(R.string.activity_main_try_again, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                requestLastLocation();
                            }
                        });
                snackbar.show();
                return;
            }

            showProgressBar(true);
            showRecyclerView(false);
            //Cancel any previous search API calls
            VolleyManager.getInstance(this).getRequestQueue().cancelAll(GetSearchVenueByNameRequest.TAG);
            //Start a new API call
            GetExploreVenueByNameRequest searchVenueByName = new GetExploreVenueByNameRequest(query, lastLocation.getLatitude(), lastLocation.getLongitude(),
                    this,  this, getResources());
            VolleyManager.getInstance(this).addToRequestQueue(searchVenueByName);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //Save the instance of the recycler layout manager and the current search results
        outState.putParcelable(SAVED_INSTANCE_LAYOUT_MANAGER, recyclerView.getLayoutManager().onSaveInstanceState());
        outState.putSerializable(SAVED_INSTANCE_ADAPTER_DATA, venuesRecyclerAdapter.getVenues());
        if(searchView != null)
            outState.putString(SAVED_INSTANCE_SEARCH_VIEW_QUERY, searchView.getQuery().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onTwitterButtonClick(String twitterId) {
        IntentHelper.startTwitterIntent(twitterId, this);
    }

    @Override
    public void onFacebookButtonClick(String facebookId) {
        IntentHelper.startFacebookIntent(facebookId, this);
    }

    @Override
    public void onMenuButtonClick(String menuUrl) {
        IntentHelper.startWebIntent(menuUrl, this);
    }

    @Override
    public void onPhoneButtonClick(String phoneNumber) {
        IntentHelper.startDialerIntent(phoneNumber, this);
    }

    @Override
    public void onNavigationButtonClick(double latitude, double longitude) {
        IntentHelper.startMapNavigationIntent(latitude, longitude, this);
    }

    /**
     * Volley error response
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d(TAG, error.toString());
        Snackbar.make(recyclerView, R.string.activity_main_search_failed, Snackbar.LENGTH_SHORT).show();
        showProgressBar(false);
    }

    /**
     * Volley successful response
     */
    @Override
    public void onResponse(VenueSearchResponse response) {
        venuesRecyclerAdapter.setAdapterData(response.getResponse().getVenues());
        //Scroll to the top after every search response
        recyclerView.scrollToPosition(0);
        showProgressBar(false);
        showRecyclerView(true);
    }

    private void showProgressBar(boolean isVisible){
        indeterminateProgressBar.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void showRecyclerView(boolean isVisible){
        recyclerView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
}
