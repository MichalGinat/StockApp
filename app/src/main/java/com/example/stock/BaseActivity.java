package com.example.stock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.stock.model.StockInfoSearch;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * BaseActivity serves as the base class for activities in the Stock app.
 * It contains common functionality shared across multiple activities,
 * such as setting up common UI elements and handling stock data searches.
 */
public class BaseActivity extends AppCompatActivity {
    protected FMPApiServiceSingleton apiService;
    protected SharedPreferences preferences;
    protected final String PREFERENCES_FILE_NAME = "MyPreferences";
    protected final String PREFERENCES_KEY = "fav_stocks";
    private static final String TAG = BaseActivity.class.getSimpleName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiService = FMPApiServiceSingleton.getInstance();
        preferences = getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupCommonButtons(); // Call setupCommonButtons after views have been initialized
    }

    protected void setupCommonButtons() {
        // Handle click for Home icon
        ImageView imageViewHome = findViewById(R.id.imageViewHome);
        imageViewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to MainActivity
                Intent intent = new Intent(BaseActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        EditText editText = findViewById(R.id.editTextCompanyNameOrSymbol);

        // Handle click for Search icon
        ImageView imageViewSearch = findViewById(R.id.imageViewSearch);
        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stockName = editText.getText().toString().trim();

                if (!TextUtils.isEmpty(stockName)) {
                    searchByNameOrSymbol(stockName);
                } else {
                    // Handle case where company name is empty
                    Toast.makeText(BaseActivity.this, "Please enter a company name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Initiates a stock data search by name or symbol.
     * Fetches data from the API and handles the response.
     *
     * @param name The name or symbol of the stock to search for.
     */
    public void searchByNameOrSymbol(String name) {
        apiService.SearchStockDataByName(name, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Error fetching stock data by name: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    JsonArray jsonArray = JsonParser.parseString(responseData).getAsJsonArray();

                    List<StockInfoSearch> stockInfoList = new ArrayList<>();
                    for (JsonElement element : jsonArray) {
                        String symbol = element.getAsJsonObject().get("symbol").getAsString();
                        String name = element.getAsJsonObject().get("name").getAsString();
                        StockInfoSearch infoSearch = new StockInfoSearch.Builder()
                                .setSymbol(symbol)
                                .setName(name)
                                .build();
                        stockInfoList.add(infoSearch);
                    }
                    showSearchResults(stockInfoList); // Start new activity to display search results

                } else {
                    Log.e(TAG, "Error fetching stock data: " + response.code() + " - " + response.message());
                }
            }
        });
    }

    /**
     * Starts the SearchResultsActivity to display search results.
     *
     * @param stockInfoList List of StockInfoSerach objects representing search results.
     */
    private void showSearchResults(List<StockInfoSearch> stockInfoList) {
        Intent intent = new Intent(this, SearchResultsActivity.class);
        intent.putParcelableArrayListExtra("stockInfoList", (ArrayList<StockInfoSearch>) stockInfoList);
        startActivity(intent);
    }
}

