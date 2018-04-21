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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<List<News>> {
    private final String IS_ROTATED = "orientation";
    private final String TAG = "MainActivity";
    private final String CURRENT_ITEM = "current";
    private final String PAGE = "page";
    private final String DATA = "Data";
    private final int LOADER_ID = 0;
    private ListView listView;
    private TextView emptyState;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private int page = 1;
    private ArrayList<News> data = new ArrayList<>();
    private Adapter adapter = null;
    private View footer;
    private boolean onOrientation = false;
    private LinearLayout loadingLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            onOrientation = savedInstanceState.getBoolean(IS_ROTATED);
        }
        getLoaderManager().initLoader(LOADER_ID, null, this);
        listView = findViewById(R.id.list_view);
        mySwipeRefreshLayout = findViewById(R.id.swiperefresh);
        emptyState = findViewById(R.id.loading_error);
        loadingLayout = findViewById(R.id.loading_linear_layout);
        footer = getLayoutInflater().inflate(R.layout.base_list_item_loading_footer, null);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh: called from SwipeRefreshLayout");
                        refresh();
                    }
                }
        );
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            public int currentScrollState;
            public int currentVisibleItemCount;
            public int currentFirstVisibleItem;
            int totalItemCount;

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
                this.totalItemCount = totalItemCount;
            }

            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.currentScrollState = scrollState;
                this.isScrollCompleted();
            }

            private void isScrollCompleted() {
                if (this.currentVisibleItemCount + currentFirstVisibleItem == totalItemCount && this.currentScrollState == SCROLL_STATE_IDLE) {
                    if (isConnected()) {
                        page++;
                        listView.addFooterView(footer);
                        listView.setSelection(listView.getCount() - 1);
                        getLoaderManager().restartLoader(0, null, MainActivity.this);
                    }
                }
            }
        });

        if (isConnected()) {
            getLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
            listView.setEmptyView(emptyState);
            emptyState.setText(R.string.loading_error);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(PAGE, page);
        outState.putSerializable(DATA, data);
        outState.putInt(CURRENT_ITEM, listView.getSelectedItemPosition());
        onOrientation = true;
        outState.putBoolean(IS_ROTATED, onOrientation);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        page = savedInstanceState.getInt(PAGE, 1);
        data = (ArrayList<News>) savedInstanceState.getSerializable(DATA);
        adapter = new Adapter(this, data);
        listView.setAdapter(adapter);
        listView.setSelection(savedInstanceState.getInt(CURRENT_ITEM, 1));
        loadingLayout.setVisibility(View.GONE);

        super.onRestoreInstanceState(savedInstanceState);
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
        return new NewsLoader(MainActivity.this, null, page);

    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, final List<News> newsList) {
        if (!onOrientation) {
            listView.removeFooterView(footer);
            loadingLayout.setVisibility(View.GONE);
            if (mySwipeRefreshLayout.isRefreshing()) {
                mySwipeRefreshLayout.setRefreshing(false);
            }
            if (!newsList.isEmpty()) {
                if (data.isEmpty()) {
                    adapter = new Adapter(MainActivity.this, newsList);
                    listView.setAdapter(adapter);
                } else {
                    adapter.addAll(newsList);
                    adapter.notifyDataSetChanged();
                }
                data.addAll(newsList);

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
                emptyState.setText(R.string.data_error);
            }
        } else {
            onOrientation = false;
        }
    }

    private void refresh() {
        if (isConnected()) {
            data.clear();
            page = 1;
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
