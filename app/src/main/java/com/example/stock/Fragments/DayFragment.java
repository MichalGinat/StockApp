package com.example.stock.Fragments;

import android.icu.text.SimpleDateFormat;
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
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class DayFragment extends Fragment {

    private static final String TAG = DayFragment.class.getSimpleName();

    private LineChart lineChart;

    private FMPApiServiceSingelton apiService;
    private String symbol;
    public DayFragment(String symbol) {
        this.symbol=symbol;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_day, container, false);

        // Initialize API service instance
        apiService = FMPApiServiceSingelton.getInstance();

        // Initialize LineChart
        lineChart = rootView.findViewById(R.id.line_chart);

        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        fetchStockDataDay(symbol, formattedDate);

        return rootView;
    }

    private void fetchStockDataDay(String symbol, String date) {
        apiService.StockHistoricalDay(symbol, date, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG, "Error fetching stock quote: " + e.getMessage());
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    Log.e(TAG, responseData);

                    List<DailyStockData> dailyStockData = parseResponseToDailyStockDataList(responseData);

                    if (!dailyStockData.isEmpty()) {
                        getActivity().runOnUiThread(() -> {
                            displayStockDataInGraph(dailyStockData);
                        });
                    } else {
                        Log.e(TAG, "No valid data found in response.");
                        LocalDate previousDate = LocalDate.parse(date).minusDays(1);
                        String previousDateString = previousDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        fetchStockDataDay(symbol, previousDateString);
//                        getActivity().runOnUiThread(() -> {
//                            Toast.makeText(getContext(), "No valid data found", Toast.LENGTH_SHORT).show();
//                        });
                    }
                } else {
                    Log.e(TAG, "Error fetching stock quote: " + response.code() + " - " + response.message());
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    private List<DailyStockData> parseResponseToDailyStockDataList(String responseData) {
        List<DailyStockData> stockDataList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(responseData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String dateString = jsonObject.getString("date");
                float closePrice = (float) jsonObject.getDouble("close");

                // Extract hour from the date string
                String[] dateTimeParts = dateString.split(" ");
                String timePart = dateTimeParts[1]; // Format: HH:mm:ss

                stockDataList.add(new DailyStockData(closePrice, timePart));
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON data: " + e.getMessage());
        }
        return stockDataList;
    }



    private void displayStockDataInGraph(List<DailyStockData> dailyStockDataList) {
        if (dailyStockDataList == null || dailyStockDataList.isEmpty()) {
            Log.e(TAG, "Empty or null data list provided.");
            // Handle the empty data case, for example:
            Toast.makeText(getContext(), "No data to display", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Entry> entries = new ArrayList<>();
        List<String> xValues = new ArrayList<>(); // To store x-axis labels

        // Sort the daily stock data list based on time
        Collections.sort(dailyStockDataList, new Comparator<DailyStockData>() {
            @Override
            public int compare(DailyStockData data1, DailyStockData data2) {
                return data1.getDate().compareTo(data2.getDate());
            }
        });

        for (int i = 0; i < dailyStockDataList.size(); i++) {
            DailyStockData data = dailyStockDataList.get(i);
            entries.add(new Entry(i, data.getPrice()));
            xValues.add(data.getDate()); // Assuming date format is "HH:mm:ss"
        }

        LineDataSet dataSet = new LineDataSet(entries, "Daily Stock Data");
        dataSet.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        dataSet.setDrawCircles(false); // Hide data points (dots)
        dataSet.setDrawValues(false); // Hide values (text)
        dataSet.setMode(LineDataSet.Mode.LINEAR); // Set mode to display a smooth line

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.getDescription().setEnabled(false);

        // Customize chart appearance
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);

        // Set custom value formatter for x-axis
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                int index = (int) value;
                if (index >= 0 && index < xValues.size()) {
                    // Use rounded hour labels
                    String time = xValues.get(index);
                    String[] timeParts = time.split(":");
                    if (timeParts.length > 1) {
                        String hour = timeParts[0];
                        String minute = timeParts[1];
                        return hour + ":" + minute; // Format as "HH:mm"
                    }
                }
                return "";
            }
        });

        // Customize the left y-axis
        YAxis yAxisLeft = lineChart.getAxisLeft();
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false); // Disable the right y-axis

        yAxisLeft.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                // Format y-axis values as needed
                return String.format(Locale.US, "%.2f", value); // Example: Format to two decimal places
            }
        });

        // Set minimum and maximum values for the y-axis to avoid compression
        float minYValue = Collections.min(entries, Comparator.comparing(Entry::getY)).getY();
        float maxYValue = Collections.max(entries, Comparator.comparing(Entry::getY)).getY();
        yAxisLeft.setAxisMinimum(minYValue - 1); // Adjust the offset as needed
        yAxisLeft.setAxisMaximum(maxYValue + 1); // Adjust the offset as needed

        // Refresh chart
        lineChart.invalidate();
    }


//    public void setStockSymbol(String symbol) {
//        LocalDate currentDate = LocalDate.now();
//        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        fetchStockDataDay(symbol, formattedDate);
//    }

//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_day, container, false);
//    }
}
