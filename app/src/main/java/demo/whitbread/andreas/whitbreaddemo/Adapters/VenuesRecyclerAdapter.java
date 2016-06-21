package demo.whitbread.andreas.whitbreaddemo.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import demo.whitbread.andreas.whitbreaddemo.Helpers.UnitConversionHelper;
import demo.whitbread.andreas.whitbreaddemo.Model.Category;
import demo.whitbread.andreas.whitbreaddemo.Model.Location;
import demo.whitbread.andreas.whitbreaddemo.Model.Venue;
import demo.whitbread.andreas.whitbreaddemo.R;

/**
 * Created by WestPlay on 19/6/2016.
 */
public class VenuesRecyclerAdapter extends RecyclerView.Adapter<VenuesRecyclerAdapter.VenueViewHolder> {

    public static final int METER_LIMIT_BEFORE_KILOMETERS = 1000;
    private static final float ALPHA_ENABLED_BUTTON = 1.0f;
    private static final float ALPHA_DISABLED_BUTTON = 0.25f;

    private ArrayList<Venue> venues;
    private Context context;
    protected OnVenueButtonClickListener onVenueButtonClickListener;

    public interface OnVenueButtonClickListener{
        void onTwitterButtonClick(String twitterId);
        void onFacebookButtonClick(String facebookId);
        void onMenuButtonClick(String menuUrl);
        void onPhoneButtonClick(String phoneNumber);
        void onNavigationButtonClick(double latitude, double longitude);
    }

    public VenuesRecyclerAdapter(Context context, OnVenueButtonClickListener onVenueButtonClickListener) {
        this.context = context;
        this.onVenueButtonClickListener = onVenueButtonClickListener;
    }

    @Override
    public VenueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_list_item, parent, false);
        return new VenueViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(VenueViewHolder holder, int position) {
        Venue venue = venues.get(position);
        holder.venueNameTextView.setText(venue.getName());
        holder.venueDistanceTextView.setText(createDistanceText(venue.getLocation().getDistance()));

        //Set venue buttons state
        holder.twitterButton.setEnabled(venue.getContact().getTwitter() == null ? false : true);
        holder.twitterButton.setEnabled(venue.getContact().getFacebook() == null ? false : true);
        holder.menuButton.setEnabled(venue.getMenu() == null ? false : true);
        holder.phoneButton.setEnabled(venue.getContact().getPhone() == null ? false : true);
        holder.navigationButton.setEnabled(venue.getLocation() == null ? false : true);
        //Buttons alpha state
        holder.twitterButton.setAlpha(venue.getContact().getTwitter() == null ? ALPHA_DISABLED_BUTTON : ALPHA_ENABLED_BUTTON);
        holder.twitterButton.setAlpha(venue.getContact().getFacebook() == null ? ALPHA_DISABLED_BUTTON : ALPHA_ENABLED_BUTTON);
        holder.menuButton.setAlpha(venue.getMenu() == null ? ALPHA_DISABLED_BUTTON : ALPHA_ENABLED_BUTTON);
        holder.phoneButton.setAlpha(venue.getContact().getPhone() == null ? ALPHA_DISABLED_BUTTON : ALPHA_ENABLED_BUTTON);
        holder.navigationButton.setAlpha(venue.getLocation() == null ? ALPHA_DISABLED_BUTTON : ALPHA_ENABLED_BUTTON);

        Category venueCategory = getPrimaryCategoryFromVenue(venue);
        if(venueCategory == null)
            //If no category was found, return to avoid displaying an image and get a null pointer exception
            return;
        Glide.with(context).load(venueCategory.getIcon().getIconUrl(true, context.getResources())).into(holder.venueImageView);
    }

    @Override
    public int getItemCount() {
        if(venues == null)
            return 0;
        return venues.size();
    }

    private Category getPrimaryCategoryFromVenue(Venue venue){
        List<Category> venueCategories = venue.getCategories();
        for(Category category : venueCategories){
            if(category.getPrimary())
                return category;
        }
        return venueCategories.size() > 0 ? venueCategories.get(0) : null;
    }

    private String createDistanceText(int distance){
        int stringResource = distance < METER_LIMIT_BEFORE_KILOMETERS ? R.string.distance_text_meters : R.string.distance_text_kilometers;
        distance = UnitConversionHelper.convertMetersToKilometersIfOverLimit(distance, METER_LIMIT_BEFORE_KILOMETERS);
        return String.format(context.getString(stringResource), distance);
    }

    public void setAdapterData(List<Venue> venueList){
        setAdapterData((ArrayList<Venue>) venueList);
    }

    public void setAdapterData(ArrayList<Venue> venueList){
        this.venues = venueList;
        notifyDataSetChanged();
    }

    public ArrayList<Venue> getVenues() {
        return venues;
    }

    public Venue getVenue(int position){
        Venue selectedVenue = null;
        try {
            selectedVenue = venues.get(position);
        }
        catch (IndexOutOfBoundsException iex){}
        finally {
            return selectedVenue;
        }
    }


    public static class VenueViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private String TAG = VenueViewHolder.class.getSimpleName().toString();

        public ImageView venueImageView;
        public TextView venueNameTextView;
        public TextView venueDistanceTextView;
        //Venue Buttons
        public ImageButton twitterButton;
        public ImageButton facebookButton;
        public ImageButton menuButton;
        public ImageButton phoneButton;
        public ImageButton navigationButton;

        private VenuesRecyclerAdapter venuesRecyclerAdapter;

        public VenueViewHolder(View v, VenuesRecyclerAdapter venuesRecyclerAdapter) {
            super(v);
            this.venuesRecyclerAdapter = venuesRecyclerAdapter;
            venueImageView = (ImageView)v.findViewById(R.id.recycler_item_venue_image);
            venueNameTextView = (TextView) v.findViewById(R.id.recycler_item_venue_name);
            venueDistanceTextView = (TextView) v.findViewById(R.id.recycler_item_venue_distance);
            twitterButton = (ImageButton) v.findViewById(R.id.recycler_item_twitter_button);
            facebookButton = (ImageButton) v.findViewById(R.id.recycler_item_facebook_button);
            menuButton = (ImageButton) v.findViewById(R.id.recycler_item_menu_button);
            phoneButton = (ImageButton) v.findViewById(R.id.recycler_item_phone_button);
            navigationButton = (ImageButton) v.findViewById(R.id.recycler_item_navigate_button);
            //Set buttons click listener
            twitterButton.setOnClickListener(this);
            facebookButton.setOnClickListener(this);
            menuButton.setOnClickListener(this);
            phoneButton.setOnClickListener(this);
            navigationButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(venuesRecyclerAdapter.onVenueButtonClickListener == null)
                return;

            Venue holderReferencedVenue = venuesRecyclerAdapter.getVenue(getAdapterPosition());
            switch (v.getId()){
                case R.id.recycler_item_twitter_button:
                    venuesRecyclerAdapter.onVenueButtonClickListener.onTwitterButtonClick(holderReferencedVenue.getContact().getTwitter());
                    break;
                case R.id.recycler_item_facebook_button:
                    venuesRecyclerAdapter.onVenueButtonClickListener.onFacebookButtonClick(holderReferencedVenue.getContact().getFacebook());
                    break;
                case R.id.recycler_item_menu_button:
                    venuesRecyclerAdapter.onVenueButtonClickListener.onMenuButtonClick(holderReferencedVenue.getMenu().getMobileUrl());
                    break;
                case R.id.recycler_item_phone_button:
                    venuesRecyclerAdapter.onVenueButtonClickListener.onPhoneButtonClick(holderReferencedVenue.getContact().getPhone());
                    break;
                case R.id.recycler_item_navigate_button:
                    Location venueLocation = holderReferencedVenue.getLocation();
                    venuesRecyclerAdapter.onVenueButtonClickListener.onNavigationButtonClick(venueLocation.getLat(), venueLocation.getLng());
                    break;
            }
        }
    }

}
