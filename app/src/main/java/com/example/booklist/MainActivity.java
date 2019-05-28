package com.example.booklist;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.app.LoaderManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity  extends FragmentActivity implements LoaderManager.LoaderCallbacks<List<Books>>{


    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int BOOKS_LOADER_ID = 1;
    private static final String SEARCH_ITEM_KEY = "Search Item";

    private BooksAdapter myAdapter;
    private EditText searchBar;
    private TextView emptyState;
    ImageView searchBtn;
    ProgressBar myProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final ListView bookListView = (ListView) findViewById(R.id.list);

        myProgressBar = (ProgressBar) findViewById(R.id.progress_circular);

        emptyState = (TextView) findViewById(R.id.emptyView_tv);

        searchBar = (EditText) findViewById(R.id.search_et);
        myProgressBar.setVisibility(View.INVISIBLE);
        searchBtn = (ImageView) findViewById(R.id.search_btn);


        bookListView.setEmptyView(emptyState);

        myAdapter = new BooksAdapter(this, new ArrayList<Books>());
        bookListView.setAdapter(myAdapter);

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Books currentBooks = myAdapter.getItem(position);
                String bookTitle = currentBooks.getTitle();
                String bookAuthor = currentBooks.getAuthor();
                String bookCategory = currentBooks.getCategory();
                String bookDescription = currentBooks.getDescription();
                String bookDate = currentBooks.getDate();
                String bookImageUrl = currentBooks.getImageUrl();
                String bookRead = currentBooks.getReadLink();

                Intent descriptionIntent = new Intent(MainActivity.this, BooksDescription.class);
                descriptionIntent.putExtra("bookTitle", bookTitle);
                descriptionIntent.putExtra("bookAuthor", bookAuthor);
                descriptionIntent.putExtra("bookCategory", bookCategory);
                descriptionIntent.putExtra("bookDescription", bookDescription);
                descriptionIntent.putExtra("bookImageUrl", bookImageUrl);
                descriptionIntent.putExtra("bookDate", bookDate);
                descriptionIntent.putExtra("bookRead", bookRead);
                startActivity(descriptionIntent);
            }
        });

    }

    public void onClickSearch(View view) {
        String searchTerm = searchBar.getText().toString().trim();
        boolean searchTermAvailable = null != searchTerm && !searchTerm.isEmpty();
        boolean networkAvailable = NetworkAvailablity();
        emptyState.setVisibility(View.INVISIBLE);

        if (networkAvailable && searchTermAvailable) {
            Bundle bundle = new Bundle();
            bundle.putString(SEARCH_ITEM_KEY, searchTerm);
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.restartLoader(BOOKS_LOADER_ID, bundle, this);
        } else if (!networkAvailable) {
            Bundle bundle = new Bundle();
            getLoaderManager().restartLoader(BOOKS_LOADER_ID, bundle, this); //reload when there is no connection
            emptyState.setText(R.string.connection_error);
        } else {
            myProgressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Please enter the name of the item", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public Loader<List<Books>> onCreateLoader(int id, Bundle args) {
        myProgressBar.setVisibility(View.VISIBLE);
        String searchItem = null;
        if(args != null){
            searchItem = args.getString(SEARCH_ITEM_KEY, null);
        }
        Loader<List<Books>> createLoader =  new BooksLoader(this, searchItem);
        Log.e(LOG_TAG, "Loader created");

        return createLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<Books>> loader, List<Books> data) {


        // Clear the adapter of previous earthquake data
        myAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            myAdapter.addAll(data);
        }

        myProgressBar.setVisibility(View.GONE);
        emptyState.setText(R.string.no_books);

        Log.e(LOG_TAG, "Load Finished");
    }

    @Override
    public void onLoaderReset(Loader<List<Books>> loader) {
        myAdapter.clear();

        Log.e(LOG_TAG, "Loader reset");
    }

    private boolean NetworkAvailablity() {
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }


}
