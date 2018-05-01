package edu.illinois.cs.cs125.mp7;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;


public class SearchActivity extends ListActivity {
    /** Default logging tag for search activity */
    private static final String TAG = "MP7:Search";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            // search logic here
        }

    }

}
