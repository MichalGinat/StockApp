package com.example.stock;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.stock.model.StockInfoSerach;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class StockDetailsActivity extends BaseActivity {

    private static final String TAG = StockDetailsActivity.class.getSimpleName();
    private TextView symbolTextView;
    private TextView nameTextView;
    private TextView priceTextView;
    private TextView changeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details);

        symbolTextView = findViewById(R.id.symbolTextView);
        nameTextView = findViewById(R.id.nameTextView);
        priceTextView = findViewById(R.id.priceTextView);
        changeTextView = findViewById(R.id.changeTextView);

        StockInfoSerach selectedStock = getIntent().getParcelableExtra("selectedStockInfo");
        if (selectedStock != null) {
            symbolTextView.setText(" " + selectedStock.getSymbol());
            nameTextView.setText(" " + selectedStock.getName());

            // Fetch price and change using the Quote API
            fetchStockQuote(selectedStock.getSymbol());
        } else {
            Log.e(TAG, "Selected stock is null.");
        }
    }

    private void fetchStockQuote(String symbol) {
        apiService.getStockData(symbol, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Error fetching stock quote: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    JsonArray jsonArray = JsonParser.parseString(responseData).getAsJsonArray();

                    if (jsonArray.size() > 0) {
                        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
                        // Extract price and change from the JSON object
                        final double price = jsonObject.get("price").getAsDouble();
                        final double change = jsonObject.get("change").getAsDouble();

                        // Update UI with price and change information
                        updateUI(price, change);
                    } else {
                        Log.e(TAG, "Empty JSON array in response.");
                    }
                } else {
                    Log.e(TAG, "Error fetching stock quote: " + response.code() + " - " + response.message());
                }
            }

        });
    }

    private void updateUI(final double price, final double change) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                priceTextView.setText(String.format(" %.3f USD", price));
                changeTextView.setText(String.format(" %.3f USD", change));
            }
        });
    }
}
