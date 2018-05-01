package edu.illinois.cs.cs125.mp7;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class Tasks {
    private static final String TAG = "MP7:Tasks";

    /**
     * Helper function to parse strings to JSON Objects.
     *
     * @param json string returned by volley request
     * @return string formatted as JsonObject
     */
    public static JsonObject getJsonObject(final String json) {
        JsonParser parser = new JsonParser();
        try {
            return parser.parse(json).getAsJsonObject();
        } catch (Exception e) {
            return null;
        }
    }


}
