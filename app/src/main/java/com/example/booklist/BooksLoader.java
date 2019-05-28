package com.example.booklist;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class BooksLoader extends AsyncTaskLoader<List<Books>> {

    private static final String LOG_TAG = BooksLoader.class.getName();

    private String mySearchItem;

    public BooksLoader(Context context, String searchItem) {
        super(context);
         mySearchItem= searchItem;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.e(LOG_TAG, "Start Loading");
    }

    @Override
    public List<Books> loadInBackground() {

        Log.e(LOG_TAG, "Load in background...");

        List<Books> books = null;
        if (mySearchItem != null) {
            books = QueryUtils.fetchEarthquakeData(getContext(), mySearchItem);
        }

        return books;
    }
}
