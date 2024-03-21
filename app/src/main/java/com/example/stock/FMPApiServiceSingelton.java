package com.example.stock;

import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class FMPApiServiceSingelton {
    private static final String BASE_URL = "https://financialmodelingprep.com/api/v3";
    private static final String API_KEY = "yjNSBqquKsLTabngBsZpsHsKjYdhSnPJ"; // Replace with your actual API key

    private static FMPApiServiceSingelton instance;

    private final OkHttpClient client;

    // Private constructor to prevent instantiation outside this class
    private FMPApiServiceSingelton() {
        this.client = new OkHttpClient();
    }

    // Static method to get the singleton instance
    public static FMPApiServiceSingelton getInstance() {
        if (instance == null) {
            synchronized (FMPApiServiceSingelton.class) {
                if (instance == null) {
                    instance = new FMPApiServiceSingelton();
                }
            }
        }
        return instance;
    }

    public void getStockData(String symbol, Callback callback) {
        HttpUrl url = HttpUrl.parse(BASE_URL + "/quote/" + symbol)
                .newBuilder()
                .addQueryParameter("apikey", API_KEY)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(callback);
    }

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