package com.example.stock;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;
import com.example.stock.model.StockInfoSearch;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * StockDetailsActivity displays detailed information about a specific stock,
 * including its symbol, name, price, change, percentage change, day low, and day high.
 * It utilizes ViewPager2 and TabLayout to present different fragments(graph) for daily,
 * monthly, and yearly stock data.
 */

public class StockDetailsActivity extends BaseActivity {

    private static final String TAG = StockDetailsActivity.class.getSimpleName();
    private TextView symbolTextView;
    private TextView nameTextView;
    private TextView priceTextView;
    private TextView changeTextView;
    private TextView changesPercentageTextView;
    private TextView dayLowTextView;
    private TextView dayHighTextView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details);

        // Find views
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager2 viewPager = findViewById(R.id.view_pager);
        symbolTextView = findViewById(R.id.symbolTextView);
        nameTextView = findViewById(R.id.companyNameTextView);
        priceTextView = findViewById(R.id.priceTextView);
        changeTextView = findViewById(R.id.changeTextView);
        changesPercentageTextView = findViewById(R.id.changesPercentageTextView);
        dayLowTextView = findViewById(R.id.dayLowTextView);
        dayHighTextView = findViewById(R.id.dayHighTextView);

        // Retrieve selected stock information from intent
        StockInfoSearch selectedStock = getIntent().getParcelableExtra("selectedStockInfo");

        if (selectedStock != null) {
            symbolTextView.setText(" " + selectedStock.getSymbol());
            nameTextView.setText(" " + selectedStock.getName());
            PagerAdapter pagerAdapter = new PagerAdapter(this,selectedStock.getSymbol());
            viewPager.setAdapter(pagerAdapter);

            // Setup ViewPager2 and TabLayout
            new TabLayoutMediator(tabLayout, viewPager,
                    (tab, position) -> tab.setText(pagerAdapter.getTabTitle(position))
            ).attach();

            // Fetch price and change using the Quote API
            fetchStockQuote(selectedStock.getSymbol());
        } else {
            Log.e(TAG, "Selected stock is null.");
        }


    }

    /**
     * Fetches the stock quote using the provided stock symbol.
     * @param symbol The symbol of the stock to fetch the quote for.
     */
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
                        final double price = jsonObject.get("price").getAsDouble();
                        final double change = jsonObject.get("change").getAsDouble();
                        final double changesPercentage = jsonObject.get("changesPercentage").getAsDouble();
                        final double dayLow = jsonObject.get("dayLow").getAsDouble();
                        final double dayHigh = jsonObject.get("dayHigh").getAsDouble();

                        // Update UI with stock quote details
                        updateUI(price, change, changesPercentage, dayLow, dayHigh);
                    } else {
                        Log.e(TAG, "Empty JSON array in response.");
                    }
                } else {
                    Log.e(TAG, "Error fetching stock quote: " + response.code() + " - " + response.message());
                }
            }

        });
    }

    /**
     * Updates the UI with the fetched stock quote details.
     * @param price The current price of the stock.
     * @param change The change in the stock price.
     * @param changesPercentage The percentage change in the stock price.
     * @param dayLow The lowest price of the stock for the day.
     * @param dayHigh The highest price of the stock for the day.
     */
    private void updateUI(final double price, final double change, final double changesPercentage, final double dayLow, final double dayHigh) {
        Context context = this;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                priceTextView.setText(String.format(" %.3f USD", price));

                // Set text color for change based on its value
                if (change < 0) {
                    changeTextView.setTextColor(ContextCompat.getColor(context, R.color.red)); // Set text color to red for negative change
                    changeTextView.setText(String.format(" %.3f USD \u2193", change)); // Append down arrow
                    changesPercentageTextView.setTextColor(ContextCompat.getColor(context, R.color.red)); // Set text color to red for negative percentage change
                    changesPercentageTextView.setText(String.format(" %.2f%% \u2193", changesPercentage)); // Append down arrow
                } else {
                    changeTextView.setTextColor(ContextCompat.getColor(context, R.color.green)); // Set text color to green for positive change
                    changeTextView.setText(String.format(" %.3f USD \u2191", change)); // Append up arrow
                    changesPercentageTextView.setTextColor(ContextCompat.getColor(context, R.color.green)); // Set text color to green for positive percentage change
                    changesPercentageTextView.setText(String.format(" %.2f%% \u2191", changesPercentage)); // Append up arrow

                }

                dayLowTextView.setText(String.format(" %.3f USD", dayLow));
                dayHighTextView.setText(String.format(" %.3f USD", dayHigh));
            }
        });
    }
}
