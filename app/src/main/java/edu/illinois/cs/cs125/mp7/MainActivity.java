package edu.illinois.cs.cs125.mp7;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
    private static final String TAG = "MP7:Main";
    private static final String RANDOM = "https://www.thecocktaildb.com/api/json/v1/1/random.php";
    private static RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestQueue = Volley.newRequestQueue(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get random cocktail to display
        loadRandomCocktail();

        // Search cocktails
        final Button searchByCocktail = findViewById(R.id.searchCocktails);
        searchByCocktail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Search by cocktails button clicked.");
                setContentView(R.layout.activity_search);
            }
        });
        // Search ingredients
        final Button searchByIngredient = findViewById(R.id.searchIngredients);
        searchByIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Search by ingredients button clicked.");
                setContentView(R.layout.activity_search);
            }
        });
    }

    private void loadRandomCocktail() {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    RANDOM,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            Log.d(TAG, "Got random cocktail.");
                            try {
                                JsonArray json = Tasks.getJsonObject(response.toString()).get("drinks").getAsJsonArray();
                                Log.d(TAG, "Converted response to JsonArray.");
                                ImageView randomPhoto = findViewById(R.id.photoView);
                                Log.d(TAG, "ImageView set.");
                                String imgURL = json.get(0).getAsJsonObject().get("strDrinkThumb").getAsString();
                                Log.d(TAG, "Set image URL");
                                Picasso.get().load(imgURL).into(randomPhoto);
                                Log.d(TAG, "Loaded image.");
                            } catch (Exception e) {
                                Log.w(TAG, e.toString());
                                Log.w(TAG, "Could not load image data.");
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
                e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        setContentView(R.layout.activity_main);
    }

    void startAPICall(final String url) {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            Log.d(TAG, "API call successful.");
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError error) {
                            Log.w(TAG, error.toString());
                    }
                });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
