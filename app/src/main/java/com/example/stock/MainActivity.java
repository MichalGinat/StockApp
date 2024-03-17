package com.example.stock;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;

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

//        // Example usage: Fetch stock data for AAPL
//        apiService.getStockData("AAPL", new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e(TAG, "Error fetching stock data: " + e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String responseData = response.body().string();
//                    Log.d(TAG, "Stock data response: " + responseData);
//                    // Parse and handle the JSON response here
//                } else {
//                    Log.e(TAG, "Error fetching stock data: " + response.code() + " - " + response.message());
//                }
//            }
//        });
        fetchStockDataForSymbol("IBM");
    }


    public void fetchStockDataForSymbol(String symbol) {
        apiService.getStockData(symbol, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Error fetching stock data: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    
                    String responseData = response.body().string();
                    Log.d(TAG, "Stock data response: " + responseData);
                    // Parse and handle the JSON response here
                } else {
                    Log.e(TAG, "Error fetching stock data: " + response.code() + " - " + response.message());
                }
            }
        });
    }
}
