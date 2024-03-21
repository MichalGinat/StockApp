package com.example.stock;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.stock.model.StockInfoSerach;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class BaseActivity extends AppCompatActivity {
    protected FMPApiServiceSingelton apiService;
    private static final String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiService = FMPApiServiceSingelton.getInstance();
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
                    searchByName(stockName);
                } else {
                    // Handle case where company name is empty
                    Toast.makeText(BaseActivity.this, "Please enter a company name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void searchByName(String name) {
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

                    List<StockInfoSerach> stockInfoList = new ArrayList<>();
                    for (JsonElement element : jsonArray) {
                        String symbol = element.getAsJsonObject().get("symbol").getAsString();
                        String name = element.getAsJsonObject().get("name").getAsString();
                        stockInfoList.add(new StockInfoSerach(symbol, name));
                    }

                    // Handle the list of StockInfo objects here, e.g., update UI or store in data structure
                    for (StockInfoSerach stockInfo : stockInfoList) {
                        Log.d(TAG, "Symbol: " + stockInfo.getSymbol() + ", Name: " + stockInfo.getName());
                    }

                    showSearchResults(stockInfoList); // Start new activity to display search results

                } else {
                    Log.e(TAG, "Error fetching stock data: " + response.code() + " - " + response.message());
                }
            }
        });
    }

    private void showSearchResults(List<StockInfoSerach> stockInfoList) {
        Intent intent = new Intent(this, SearchResultsActivity.class);
        intent.putParcelableArrayListExtra("stockInfoList", (ArrayList<StockInfoSerach>) stockInfoList);
        startActivity(intent);
    }
}

