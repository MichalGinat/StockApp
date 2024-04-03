package com.example.stock;

import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * FMPApiServiceSingleton is a singleton class responsible for making API requests
 * to the Financial Modeling Prep (FMP) API. It provides methods to fetch stock data
 * including quote, historical data, and search results by name or symbol.
 */
public class FMPApiServiceSingleton {
    private static final String BASE_URL = "https://financialmodelingprep.com/api/v3";
    private static final String API_KEY = "N1lZL8M2L4rCVzJ8hYApVHGFN7CHL0XS";

    private static FMPApiServiceSingleton instance;

    private final OkHttpClient client;

    // Private constructor to prevent instantiation outside this class
    private FMPApiServiceSingleton() {
        this.client = new OkHttpClient();
    }

    // Static method to get the singleton instance
    public static FMPApiServiceSingleton getInstance() {
        if (instance == null) {
            synchronized (FMPApiServiceSingleton.class) {
                if (instance == null) {
                    instance = new FMPApiServiceSingleton();
                }
            }
        }
        return instance;
    }

    /**
     * Fetches stock data for a given symbol.
     *
     * @param symbol   The stock symbol to fetch data for.
     * @param callback Callback to handle the response asynchronously.
     */
    public void getStockData(String symbol, Callback callback) {
        // Construct URL for the API request
        HttpUrl url = HttpUrl.parse(BASE_URL + "/quote/" + symbol)
                .newBuilder()
                .addQueryParameter("apikey", API_KEY)
                .build();

        // Create HTTP request and enqueue it with the provided callback
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(callback);
    }

    /**
     * Searches for stock data by company name or symbol.
     *
     * @param name     The company name or symbol to search for.
     * @param callback Callback to handle the response asynchronously.
     */
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

    /**
     * Fetches historical stock data for a given symbol within a specified date range.
     *
     * @param symbol   The stock symbol to fetch historical data for.
     * @param fromDate The start date of the historical data range (YYYY-MM-DD).
     * @param toDate   The end date of the historical data range (YYYY-MM-DD).
     * @param callback Callback to handle the response asynchronously.
     */
    public void StockHistoricalData(String symbol, String fromDate, String toDate, Callback callback) {
        HttpUrl url = HttpUrl.parse(BASE_URL + "/historical-price-full/" + symbol)
                .newBuilder()
                .addQueryParameter("from", fromDate)
                .addQueryParameter("to", toDate)
                .addQueryParameter("apikey", API_KEY)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(callback);
    }

    /**
     * Fetches historical stock data for a given symbol for a specific day.
     *
     * @param symbol   The stock symbol to fetch historical data for.
     * @param date     The date for which historical data is to be fetched (YYYY-MM-DD).
     * @param callback Callback to handle the response asynchronously.
     */
    public void StockHistoricalDay(String symbol, String date, Callback callback) {
        HttpUrl url = HttpUrl.parse(BASE_URL + "/historical-chart/5min/" + symbol)
                .newBuilder()
                .addQueryParameter("from", date)
                .addQueryParameter("to", date)
                .addQueryParameter("apikey", API_KEY)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(callback);
    }

}
