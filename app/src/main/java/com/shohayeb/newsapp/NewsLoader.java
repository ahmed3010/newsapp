package com.shohayeb.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;


class NewsLoader extends AsyncTaskLoader<List<News>> {
    private static final String TAG = "NewsLoader";
    List<News> newsFeed = new ArrayList<>();
    private String searchQuery;
    private int page;


    NewsLoader(Context context, String searchQuery, int page) {
        super(context);
        this.searchQuery = searchQuery;
        this.page = page;
    }


    @Override
    public List<News> loadInBackground() {
        HttpURLConnection urlConnection;
        InputStream inputStream;
        try {
            urlConnection = (HttpURLConnection) Contract.getUrl(searchQuery, page).openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                StringBuilder output = new StringBuilder();
                Log.i(TAG, "loadInBackground: connection successful");
                inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
                JSONObject data = new JSONObject(output.toString());
                JSONObject response = data.getJSONObject("response");
                JSONArray results = response.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject currentNews = results.getJSONObject(i);

                    String section = currentNews.getString("sectionName");
                    String date = currentNews.getString("webPublicationDate");
                    String title = currentNews.getString("webTitle");
                    String webUrl = currentNews.getString("webUrl");
                    JSONArray tags = currentNews.getJSONArray("tags");
                    String authorName = "";
                    if (tags.length() > 0) {
                        JSONObject author = tags.getJSONObject(0);
                        authorName = author.getString("webTitle");
                    }
                    newsFeed.add(new News(title, section, date, webUrl, authorName));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsFeed;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (isStarted())
            forceLoad();
    }
}
