package edu.illinois.cs.cs125.mp7;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResultsActivity extends AppCompatActivity {
    /** Default logging tag for results activity */
    private static final String TAG = "MP7:Results";
    /** URL for API GET filter by cocktails */
    private static final String COCKTAIL_URL
            = "https://www.thecocktaildb.com/api/json/v1/1/filter.php?c=Cocktail";
    /** URL for API GET filter by ordinary drinks */
    private static final String DRINK_URL =
            "https://www.thecocktaildb.com/api/json/v1/1/filter.php?c=Ordinary_Drink";
    /** URL for API GET filter by ingredients */
    private static final String INGREDIENT_URL =
            "https://www.thecocktaildb.com/api/json/v1/1/list.php?i=list";
    private static RequestQueue requestQueue;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private List<ResultsList> resultsLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        // Might not need a new request queue, but not sure how to set up a singleton handler
        // from the main activity
        requestQueue = Volley.newRequestQueue(this);
        // Initialize a handler for the RecyclerView layout
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        // Set a LinearLayout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // Specify an adapter
        resultsLists = new ArrayList<>();
        adapter = new ResultsAdapter(resultsLists, getApplicationContext());
        recyclerView.setAdapter(adapter);

        /*
         * Get request code used to start activity.
         * Make appropriate API GET request based on request code.
         */
        try {
            Bundle b = getIntent().getExtras();
            int searchCheck = b.getInt("searchKey");
            // Shouldn't need a default conditional, there aren't any other entry points
            // into the activity
            if (searchCheck == MainActivity.COCKTAIL_SEARCH_CODE) {
                setTitle(R.string.cocktails_tile);
                Log.d(TAG, "Initiating cocktail results.");
                loadCocktailJson();
            } else if (searchCheck == MainActivity.INGREDIENT_SEARCH_CODE) {
                setTitle(R.string.ingredients_title);
                Log.d(TAG, "Initiating ingredient results.");
                loadIngredientJson();
            }
        } catch (Exception e) {
            Log.w(TAG, e.toString());
        }
    }

    /**
     * GET cocktails and populate the RecyclerView.
     */
    private void loadCocktailJson() {
        Toast.makeText(this, "Loading cocktails...", Toast.LENGTH_SHORT).show();
        try {
            Log.d(TAG, "Getting cocktails");
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    COCKTAIL_URL,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            Log.d(TAG, "Got filtered cocktails.");
                            try {
                                JsonArray json = Tasks.getJsonObject(response.toString())
                                        .get("drinks").getAsJsonArray();
                                Log.d(TAG, "Converted response to JsonArray.");
                                for (JsonElement je : json) {
                                    JsonObject cocktail = je.getAsJsonObject();
                                    String name = cocktail.get("strDrink").getAsString();
                                    String imgURL = cocktail.get("strDrinkThumb").getAsString();
                                    ResultsList result = new ResultsList(name, imgURL);
                                    resultsLists.add(result);
                                }
                                adapter = new ResultsAdapter(resultsLists, getApplicationContext());
                                recyclerView.setAdapter(adapter);
                            } catch (Exception e) {
                                Log.w(TAG, e.toString());
                                Log.d(TAG, "Could not load results.");
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
     * GET ingredients and populate the RecyclerView.
     */
    private void loadIngredientJson() {
        Toast.makeText(this, "Loading ingredients...", Toast.LENGTH_SHORT).show();
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    INGREDIENT_URL,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            Log.d(TAG, "Got filtered ingredients.");
                            try {
                                JsonArray json = Tasks.getJsonObject(response.toString())
                                        .get("drinks").getAsJsonArray();
                                Log.d(TAG, "Converted response to JsonArray.");
                                for (JsonElement je : json) {
                                    JsonObject ingredient = je.getAsJsonObject();
                                    String name = ingredient.get("strIngredient1").getAsString();
                                    ResultsList result = new ResultsList(name, null);
                                    resultsLists.add(result);
                                }
                                adapter = new ResultsAdapter(resultsLists, getApplicationContext());
                                recyclerView.setAdapter(adapter);
                            } catch (Exception e) {
                                Log.w(TAG, e.toString());
                                Log.d(TAG, "Could not load results.");
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
     * Disable the search icon in the action bar. May bring back when search
     * functionality is implemented.
     *
     * @param menu menu to hide
     * @return false
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
