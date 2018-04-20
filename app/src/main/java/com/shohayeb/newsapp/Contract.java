package com.shohayeb.newsapp;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

class Contract {
    private static final String ENDPOINT = "http://content.guardianapis.com/search?";
    private static final String QUERY_PARAM = "q";
    private static final String API_PARAM = "api-key";
    private static final String API_KEY = "e70180ca-8692-4f87-b2b2-e11e4fb1eebf";
    private static final String SORT_PARAM = "order-by";
    private static final String ORDER_BY_NEWEST = "newest";
    private static final String PAGE_PARAM = "page";

    static URL getUrl(String search) {
        if (search != null) {
            Uri uri = Uri.parse(ENDPOINT).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, search)
                    .appendQueryParameter(API_PARAM, API_KEY)
                    .appendQueryParameter(SORT_PARAM, ORDER_BY_NEWEST)
                    .appendQueryParameter(PAGE_PARAM, "1").build();
            return createUrl(uri);
        } else {
            Uri uri = Uri.parse(ENDPOINT).buildUpon()
                    .appendQueryParameter(API_PARAM, API_KEY)
                    .appendQueryParameter(SORT_PARAM, ORDER_BY_NEWEST)
                    .appendQueryParameter(PAGE_PARAM, "1").build();
            return createUrl(uri);
        }
    }

    private static URL createUrl(Uri uri) {
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
}
