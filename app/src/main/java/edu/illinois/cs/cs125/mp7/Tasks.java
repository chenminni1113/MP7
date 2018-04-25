package edu.illinois.cs.cs125.mp7;

import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class Tasks {
    private static final String TAG = "MP7:Tasks";

    // Helper function to parse response strings to JSON
    public static JsonObject getJsonObject(final String json) {
        JsonParser parser = new JsonParser();
        try {
            return parser.parse(json).getAsJsonObject();
        } catch (Exception e) {
            return null;
        }
    }
}
