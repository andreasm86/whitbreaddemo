package demo.whitbread.andreas.whitbreaddemo.ContentProviders;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by WestPlay on 20/6/2016.
 */
public class VenueSuggestionsProvider extends SearchRecentSuggestionsProvider {

    public final static String AUTHORITY = "demo.whitbread.andreas.whitebreaddemo.VenueSuggestionsProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public VenueSuggestionsProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
