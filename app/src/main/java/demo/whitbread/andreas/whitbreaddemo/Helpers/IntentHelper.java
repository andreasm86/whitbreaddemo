package demo.whitbread.andreas.whitbreaddemo.Helpers;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import demo.whitbread.andreas.whitbreaddemo.R;

/**
 * Created by WestPlay on 21/6/2016.
 */
public class IntentHelper {

    public static void startTwitterIntent(String twitterName, Activity activity){
        startWebIntent("https://twitter.com/" + twitterName, activity);
    }

    public static void startFacebookIntent(String facebookId, Activity activity){
        startWebIntent("https://www.facebook.com/" + facebookId, activity);
    }

    public static void startWebIntent(String url, Activity activity){
        startActivityWithIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(url)), activity);
    }

    public static void startDialerIntent(String phoneNumber, Activity activity){
        startActivityWithIntent(new Intent(Intent.ACTION_DIAL, Uri.parse(String.format("tel:%1$s", phoneNumber))), activity);
    }

    public static void startMapNavigationIntent(double latitude, double longitude, Activity activity){
        Uri gmmIntentUri = Uri.parse(String.format("google.navigation:q=%1$s,%2$s", latitude, longitude));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivityWithIntent(mapIntent, activity);
    }

    private static void startActivityWithIntent(Intent intent, Activity activity){
        try {
            activity.startActivity(intent);
        }
        catch (ActivityNotFoundException actNotFoundEx){
            actNotFoundEx.printStackTrace();
            Toast.makeText(activity, R.string.activity_not_found_error_message, Toast.LENGTH_SHORT).show();
        }
    }

}
