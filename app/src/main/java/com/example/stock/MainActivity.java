package com.example.stock;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.stock.model.StockInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private FMPApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiService = new FMPApiService();

        EditText editTextCompanyName = findViewById(R.id.editTextCompanyName);
        Button buttonSearch = findViewById(R.id.buttonSearch);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String companyName = editTextCompanyName.getText().toString().trim();

                if (!TextUtils.isEmpty(companyName)) {
                    searchByName(companyName);
                } else {
                    // Handle case where company name is empty
                    Toast.makeText(MainActivity.this, "Please enter a company name", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


//
//    public void fetchStockDataForSymbol(String symbol) {
//        apiService.getStockData(symbol, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e(TAG, "Error fetching stock data: " + e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//
//                    String responseData = response.body().string();
//                    Log.d(TAG, "Stock data response: " + responseData);
//                    // Parse and handle the JSON response here
//                } else {
//                    Log.e(TAG, "Error fetching stock data: " + response.code() + " - " + response.message());
//                }
//            }
//        });
//    }

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

                    List<StockInfo> stockInfoList = new ArrayList<>();
                    for (JsonElement element : jsonArray) {
                        String symbol = element.getAsJsonObject().get("symbol").getAsString();
                        String name = element.getAsJsonObject().get("name").getAsString();
                        stockInfoList.add(new StockInfo(symbol, name));
                    }

                    // Handle the list of StockInfo objects here, e.g., update UI or store in data structure
                    for (StockInfo stockInfo : stockInfoList) {
                        Log.d(TAG, "Symbol: " + stockInfo.getSymbol() + ", Name: " + stockInfo.getName());
                    }

                    showSearchResults(stockInfoList); // Start new activity to display search results

                } else {
                    Log.e(TAG, "Error fetching stock data: " + response.code() + " - " + response.message());
                }
            }
        });
    }

    private void showSearchResults(List<StockInfo> stockInfoList) {
        Intent intent = new Intent(this, SearchResultsActivity.class);
        intent.putParcelableArrayListExtra("stockInfoList", (ArrayList<StockInfo>) stockInfoList);
        startActivity(intent);
    }
}
