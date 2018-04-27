package edu.illinois.cs.cs125.mp7;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SearchView;

public class SearchActivity extends AppCompatActivity {
    /** Default logging tag for search activity */
    private static final String TAG = "MP7:Search";
    /** URL for API GET search cocktail by name */
    private static final String COCKTAIL_GET = "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=";
    /** URL for API GET search ingredient by name */
    private static final String INGREDIENT_GET = "https://www.thecocktaildb.com/api/json/v1/1/search.php?i=";
    /** Call when the search activity is first created */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        /*
         * Get request code used to start activity and set search hint text.
         * Make appropriate API GET request based on request code.
         */
        try {
            Bundle b = getIntent().getExtras();
            int searchCheck = b.getInt("searchKey");

            // Shouldn't need a default conditional, there aren't any other entry points
            // into the activity
            SearchView search = findViewById(R.id.searchText);
            if (searchCheck == MainActivity.COCKTAIL_SEARCH_CODE) {
                String searchText = "e.g. tequila sunrise, old fashioned...";
                search.setQueryHint(searchText);
            } else if (searchCheck == MainActivity.INGREDIENT_SEARCH_CODE) {
                String searchText = "e.g. vodka, rum, whiskey...";
                search.setQueryHint(searchText);
            }
        } catch (Exception e) {
            Log.w(TAG, e.toString());
        }
    }
}
