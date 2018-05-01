package edu.illinois.cs.cs125.mp7;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    /** Default logging tag for main activity */
    private static final String TAG = "MP7:Main";
    /** URL for API GET random cocktail */
    private static final String RANDOM = "https://www.thecocktaildb.com/api/json/v1/1/random.php";
    /** Constant to initiate cocktails search */
    public static final int COCKTAIL_SEARCH_CODE = 23;
    /** Constant to initiate ingredients search */
    public static final int INGREDIENT_SEARCH_CODE = 34;
    /** Request queue for network requests */
    private static RequestQueue requestQueue;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        requestQueue = Volley.newRequestQueue(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * Search button handlers for UI.
         */
        final Button searchByCocktail = findViewById(R.id.searchCocktails);
        searchByCocktail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Search by cocktails button clicked.");
                startCocktailSearch(v);
            }
        });
        final Button searchByIngredient = findViewById(R.id.searchIngredients);
        searchByIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Search by ingredients button clicked.");
                startIngredientSearch(v);
            }
        });
        final Button getRandom = findViewById(R.id.searchRandom);
        getRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Get random cocktail button clicked.");
                getRandomCocktail();
            }
        });
        // Get a random cocktail each time the user opens the main activity
        getRandomCocktail();
        handleIntent(getIntent());
    }

    /**
     * Displays SearchView in the app bar.
     *
     * @param menu menu item to inflate as search bar
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        // Bit of pizzazz
        searchView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        // Search hint text isn't setting automatically for some reason, has to be set manually
        searchView.setQueryHint(getResources().getString(R.string.search_hint));

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d(TAG, query);
        }
    }

    /**
     * Get and display a random cocktail.
     */
    private void getRandomCocktail() {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    RANDOM,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            Log.d(TAG, "Got random cocktail.");
                            try {
                                JsonArray json = Tasks.getJsonObject(response.toString())
                                        .get("drinks").getAsJsonArray();
                                Log.d(TAG, "Converted response to JsonArray.");
                                TextView cocktailName = findViewById(R.id.cocktailName);
                                String name = json.get(0).getAsJsonObject()
                                        .get("strDrink").getAsString();
                                cocktailName.setText(name);
                                ImageView randomPhoto = findViewById(R.id.photoView);
                                String imgURL = json.get(0).getAsJsonObject()
                                        .get("strDrinkThumb").getAsString();
                                Picasso.get().load(imgURL).into(randomPhoto);
                                Log.d(TAG, "Cocktail name and image loaded and set.");
                            } catch (Exception e) {
                                Log.w(TAG, e.toString());
                                Log.d(TAG, "Could not load cocktail data.");
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError error) {
                            Log.w(TAG, error.toString());
                        }
                    });
            requestQueue.add(jsonObjectRequest);
            } catch (Exception e) {
                Log.w(TAG, e.toString());
                Log.d(TAG, "Unsuccessful API call.");
        }
    }

    /**
     * Start results activity for cocktail search.
     *
     * @param view current view
     */
    public void startCocktailSearch(View view) {
        Intent intent = new Intent(getApplicationContext(), ResultsActivity.class);
        intent.putExtra("searchKey", COCKTAIL_SEARCH_CODE);
        startActivity(intent);
        Log.d(TAG, "Switching to results activity.");
    }

    /**
     * Start results activity for ingredient search.
     *
     * @param view current view
     */
    public void startIngredientSearch(View view) {
        Intent intent = new Intent(getApplicationContext(), ResultsActivity.class);
        intent.putExtra("searchKey", INGREDIENT_SEARCH_CODE);
        startActivity(intent);
        Log.d(TAG, "Switching to results activity.");
    }
}
