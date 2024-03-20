package com.example.stock;

import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class FMPApiService {
    private static final String BASE_URL = "https://financialmodelingprep.com/api/v3";
    private static final String API_KEY = "yjNSBqquKsLTabngBsZpsHsKjYdhSnPJ"; // Replace with your actual API key

    private final OkHttpClient client;

    public FMPApiService() {
        this.client = new OkHttpClient();
    }

//    public void getStockData(String symbol, Callback callback) {
//        HttpUrl url = HttpUrl.parse(BASE_URL + "/quote/" + symbol)
//                .newBuilder()
//                .addQueryParameter("apikey", API_KEY)
//                .build();
//
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        client.newCall(request).enqueue(callback);
//    }

    public void SearchStockDataByName(String name, Callback callback) {
        HttpUrl url = HttpUrl.parse(BASE_URL + "/search")
                .newBuilder()
                .addQueryParameter("query", name)
                .addQueryParameter("limit", "30")
                .addQueryParameter("apikey", API_KEY)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(callback);
    }

}
