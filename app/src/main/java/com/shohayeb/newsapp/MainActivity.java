package com.shohayeb.newsapp;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<List<News>> {
    private static final String TAG = "MainActivity";
    private ListView listView;
    private TextView emptyState;
    private SwipeRefreshLayout mySwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLoaderManager().initLoader(0, null, this);
        listView = findViewById(R.id.list_view);
        mySwipeRefreshLayout = findViewById(R.id.swiperefresh);
        emptyState = findViewById(R.id.loading_error);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh: called from SwipeRefreshLayout");
                        refresh();
                    }
                }
        );

        if (isConnected()) {
            getLoaderManager().initLoader(0, null, this);
        } else {
            listView.setEmptyView(emptyState);
            emptyState.setText(R.string.loading_error);
        }

    }

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(MainActivity.this, null);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, final List<News> data) {
        LinearLayout loadingLayout = findViewById(R.id.loading_linear_layout);
        loadingLayout.setVisibility(View.GONE);
        if (mySwipeRefreshLayout.isRefreshing()) {
            mySwipeRefreshLayout.setRefreshing(false);
        }
        if (!data.isEmpty()) {
            Adapter adapter = new Adapter(MainActivity.this, data);
            listView.setAdapter(adapter);
            Log.i(TAG, "onLoadFinished: " + data.size());
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(data.get(position).getWebUrl()));
                    startActivity(i);
                }
            });
        } else {
            listView.setEmptyView(emptyState);
            emptyState.setText(R.string.loading_error);
        }
    }

    private void refresh() {
        if (isConnected()) {
            getLoaderManager().restartLoader(0, null, this);
        } else {
            Toast.makeText(this, "No internet connection found", Toast.LENGTH_SHORT).show();
            if (mySwipeRefreshLayout.isRefreshing()) {
                mySwipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        loader.abandon();
    }
}
