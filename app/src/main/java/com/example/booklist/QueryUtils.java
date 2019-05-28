package com.example.booklist;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();



    private QueryUtils() {
    }
    public static List<Books> fetchEarthquakeData(Context context, String searchItem) {


        Log.e(LOG_TAG, "Fetching data...");

        String requestUrl = searchItemUrl(context, searchItem);

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        List<Books> book = extractBooks(context, jsonResponse);

        // Return the {@link Event}
        return book;
    }

    private static String searchItemUrl(Context context, String searchItem){
        String theUrl = null;

        try{
            String searchItemEncoded = URLEncoder.encode(searchItem, "utf-8");
            theUrl = context.getString(R.string.query_url, searchItemEncoded);
        }catch (UnsupportedEncodingException e){
            Log.e(LOG_TAG, "Building search item URL FAILED ", e);
        }

        return theUrl;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }

        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static List<Books> extractBooks(Context context, String BookJson) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(BookJson)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Books> books = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {


            JSONObject data = new JSONObject(BookJson);
            JSONArray dataArray = data.getJSONArray("items");
            for(int i = 0; i < dataArray.length(); i++){
                JSONObject deepData = dataArray.getJSONObject(i).getJSONObject("volumeInfo");

                String title  = deepData.getString("title");

                JSONArray authors = deepData.getJSONArray("authors");
                String author = "";
                for(int j = 0; j < authors.length(); j++){
                    author = authors.getString(j);
                }

                JSONArray categories = deepData.getJSONArray("categories");
                String category = "";
                for(int k = 0; k < categories.length(); k++){
                    category = categories.getString(k);
                }

                String date = deepData.getString("publishedDate");

                String description = null;
                if(deepData.getString("description") != null){
                    description = deepData.getString("description");
                }


                JSONObject imageLinks = deepData.getJSONObject("imageLinks");
                String thumbnail = imageLinks.getString("thumbnail");

                JSONObject accessData = dataArray.getJSONObject(i).getJSONObject("accessInfo");
                String readLink = null;
                if(accessData.getString("webReaderLink") != null){
                    readLink = accessData.getString("webReaderLink");
                }


                books.add(new Books(title, category, author, date, thumbnail, description, readLink));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return books;
    }

}
