package com.example.stock;

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

public final class ApiServiceUtils {
    public static void fetchStockQuote(String symbol, String tag, Consumer<StockInfoDetails> action) {
        FMPApiServiceSingelton apiService = FMPApiServiceSingelton.getInstance();
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

                        StockInfoDetails stockInfoDetails = new StockInfoDetails(symbol, name,
                                price, change, changesPercentage, dayLow, dayHigh);
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

}
