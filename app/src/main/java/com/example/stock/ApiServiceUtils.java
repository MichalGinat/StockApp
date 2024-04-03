package com.example.stock;

import android.os.Handler;
import android.util.Log;

import com.example.stock.model.StockInfoDetails;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.function.Consumer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Utility class for making API calls to fetch stock quotes.
 */
public final class ApiServiceUtils {

    /**
     * Fetches stock quote for the given symbol.
     *
     * @param symbol The symbol of the stock to fetch quote for.
     * @param tag    A string tag for logging purposes.
     * @param action A consumer function to handle the fetched stock info.
     */
    public static void fetchStockQuote(String symbol, String tag, Consumer<StockInfoDetails> action) {
        FMPApiServiceSingleton apiService = FMPApiServiceSingleton.getInstance();
        apiService.getStockData(symbol, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(tag, "Error fetching stock quote: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    JsonArray jsonArray = JsonParser.parseString(responseData).getAsJsonArray();

                    if (!jsonArray.isEmpty()) {
                        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
                        // Extract price and change from the JSON object
                        final String name = jsonObject.get("name").getAsString();
                        final double price = jsonObject.get("price").getAsDouble();
                        final double change = jsonObject.get("change").getAsDouble();
                        final double changesPercentage = jsonObject.get("changesPercentage").getAsDouble();
                        final double dayLow = jsonObject.get("dayLow").getAsDouble();
                        final double dayHigh = jsonObject.get("dayHigh").getAsDouble();
                        StockInfoDetails stockInfoDetails = new StockInfoDetails.Builder()
                                .setSymbol(symbol)
                                .setName(name)
                                .setPrice(price)
                                .setChange(change)
                                .setChangesPercentage(changesPercentage)
                                .setDayLow(dayLow)
                                .setDayHigh(dayHigh)
                                .build();
                        action.accept(stockInfoDetails);
                    } else {
                        Log.e(tag, "Empty JSON array in response.");
                    }
                } else {
                    Log.e(tag, "Error fetching stock quote: " + response.code() + " - " + response.message());
                }
            }
        });
    }

    /**
     * Fetches stock quote periodically for the given symbol at specified interval.
     *
     * @param symbol         The symbol of the stock to fetch quote for.
     * @param tag            A string tag for logging purposes.
     * @param action         A consumer function to handle the fetched stock info.
     * @param intervalInSec  Interval in seconds for periodic updates.
     * @return A handler to control the periodic updates.
     */
    public static Handler fetchStockQuotePeriodic(String symbol,
                                                  String tag,
                                                  Consumer<StockInfoDetails> action,
                                                  int intervalInSec) {
        Handler handler = new Handler();
        Runnable fetchStockQuoteRunnable = new Runnable() {
            @Override
            public void run() {
                // Call your data fetching method here
                fetchStockQuote(symbol, tag, action);

                // Schedule the next execution after intervalInSec
                handler.postDelayed(this, intervalInSec * 1000L);
            }
        };

        // Remove any existing callbacks to avoid duplicate execution
        handler.removeCallbacks(fetchStockQuoteRunnable);
        // Schedule the first execution immediately, and then every intervalInSec
        handler.post(fetchStockQuoteRunnable);
        return handler;
    }

}
