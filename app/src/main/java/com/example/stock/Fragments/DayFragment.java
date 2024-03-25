package com.example.stock.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.stock.FMPApiServiceSingelton;
import com.example.stock.R;
import com.example.stock.model.DailyStockData;
import com.example.stock.model.StockInfoSerach;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class DayFragment extends Fragment {

//    private static final String TAG = DayFragment.class.getSimpleName();
//
//    private LineChart lineChart;
//
//    private FMPApiServiceSingelton apiService;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.fragment_day, container, false);
//
//        // Initialize API service instance
//        apiService = FMPApiServiceSingelton.getInstance();
//
//        // Initialize LineChart
//        lineChart = rootView.findViewById(R.id.line_chart);
//
//        return rootView;
//    }
//
//    private void fetchStockDataDay(String symbol, String date) {
//        apiService.StockHistoricalDay(symbol, date, new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                Log.e(TAG, "Error fetching stock quote: " + e.getMessage());
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String responseData = response.body().string();
//                    Log.e(TAG, responseData);
//
//                    List<DailyStockData> dailyStockData;
//                    dailyStockData = parseResponseToDailyStockDataList(responseData);
//
//                    if (!dailyStockData.isEmpty()) {
//                        // Update UI with fetched data
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                displayStockDataInGraph(dailyStockData);
//                            }
//                        });
//                    } else {
//                        Log.e(TAG, "No data found in response.");
//                    }
//                } else {
//                    Log.e(TAG, "Error fetching stock quote: " + response.code() + " - " + response.message());
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            }
//        });
//
//    }
//
//    private List<DailyStockData> parseResponseToDailyStockDataList(String responseData) {
//        List<DailyStockData> stockDataList = new ArrayList<>();
//        try {
//            JSONArray jsonArray = new JSONArray(responseData);
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                float price = (float) jsonObject.getDouble("close");
//                String dateString = jsonObject.getString("date");
//                float time = parseDateStringToTime(dateString);
//                stockDataList.add(new DailyStockData(price, time));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return stockDataList;
//    }
//
//    private float parseDateStringToTime(String dateString) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
//        // Extract time in hours from LocalDateTime
//        int hour = dateTime.getHour();
//        int minute = dateTime.getMinute();
//        float time = hour + (minute / 60.0f); // Convert minutes to fraction of hours
//        return time;
//    }
//
//
//    private void displayStockDataInGraph(List<DailyStockData> dailyStockDataList) {
//        List<Entry> entries = new ArrayList<>();
//        for (DailyStockData data : dailyStockDataList) {
//            // Assuming the time field in DailyStockData represents the x-axis value
//            // and the price field represents the y-axis value
//            entries.add(new Entry(data.getTime(), data.getPrice()));
//        }
//
//        LineDataSet dataSet = new LineDataSet(entries, "Stock Data");
//        dataSet.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
//        dataSet.setValueTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
//
//        LineData lineData = new LineData(dataSet);
//        lineChart.setData(lineData);
//
//        // Customize chart appearance
//        XAxis xAxis = lineChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setGranularity(1f);
//        xAxis.setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getAxisLabel(float value, AxisBase axis) {
//                // Format x-axis labels as needed
//                return String.valueOf(value);
//            }
//        });
//
//        YAxis yAxisRight = lineChart.getAxisRight();
//        yAxisRight.setEnabled(false);
//
//        // Refresh chart
//        lineChart.invalidate();
//    }
//
//
//    public void setStockSymbol(String symbol) {
//        LocalDate currentDate = LocalDate.now();
//        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        fetchStockDataDay(symbol, formattedDate);
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_day, container, false);
    }
}
