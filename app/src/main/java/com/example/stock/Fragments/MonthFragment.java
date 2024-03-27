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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

// Represents a Fragment displaying monthly stock data using a LineChart,
// handling data fetching, parsing JSON, and chart rendering.
// Encapsulates functionality for displaying stock data in the app's UI.
public class MonthFragment extends Fragment {

    private static final String TAG = DayFragment.class.getSimpleName();

    private LineChart lineChart;

    private FMPApiServiceSingelton apiService;
    private String symbol;

    // Constructor to initialize the fragment with a stock symbol
    public MonthFragment(String symbol) {
        this.symbol=symbol;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_month, container, false);

        // Initialize API service instance
        apiService = FMPApiServiceSingelton.getInstance();

        // Initialize LineChart
        lineChart = rootView.findViewById(R.id.line_chart);
        fetchStockMonthData(symbol);
        return rootView;
    }

    // Method to fetch stock data for the past 30 days
    private void fetchStockMonthData(String symbol) {
        Calendar calendar = Calendar.getInstance();

        // Save the current date
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // Months are 0-based
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        // Move 30 days back
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        int lastMonthYear = calendar.get(Calendar.YEAR);
        int lastMonth = calendar.get(Calendar.MONTH) + 1; // Months are 0-based
        int lastMonthDay = calendar.get(Calendar.DAY_OF_MONTH); // Day 30 days ago

        String startOf30DaysAgo = lastMonthYear + "-" + (lastMonth < 10 ? "0" + lastMonth : lastMonth) + "-" + (lastMonthDay < 10 ? "0" + lastMonthDay : lastMonthDay);

        // Current date (today)
        String today = currentYear + "-" + (currentMonth < 10 ? "0" + currentMonth : currentMonth) + "-" + (currentDay < 10 ? "0" + currentDay : currentDay); // Get current day dynamically

        // Call API to fetch historical stock data for the past 30 days
        apiService.StockHistoricalData(symbol, startOf30DaysAgo, today, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Error fetching stock data from 30 days ago to today: " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    Log.e(TAG, responseData);

                    // Parse the JSON response to a list of daily stock data
                    List<DailyStockData> stockDataList = parseResponseToMonthlyStockDataList(responseData);

                    if (!stockDataList.isEmpty()) {
                        // Update UI with the fetched data
                        getActivity().runOnUiThread(() -> {
                            displayStockDataInGraph(stockDataList);
                        });
                    } else {
                        Log.e(TAG, "No valid data found in response.");
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "No valid data found", Toast.LENGTH_SHORT).show();
                        });
                    }
                } else {
                    Log.e(TAG, "Error fetching stock data: " + response.code() + " - " + response.message());
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }



    // Method to parse JSON response into a list of daily stock data objects
    private List<DailyStockData> parseResponseToMonthlyStockDataList(String responseData) {
        List<DailyStockData> stockDataList = new ArrayList<>();
        try {
            JSONObject jsonResponse = new JSONObject(responseData);
            String symbol = jsonResponse.getString("symbol");
            JSONArray historicalArray = jsonResponse.getJSONArray("historical");

            for (int i = 0; i < historicalArray.length(); i++) {
                JSONObject historicalObj = historicalArray.getJSONObject(i);
                String dateString = historicalObj.getString("date");
                float closePrice = (float) historicalObj.getDouble("close");

                stockDataList.add(new DailyStockData(closePrice, dateString));
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON data: " + e.getMessage());
        }
        return stockDataList;
    }



    // Method to display monthly stock data in a LineChart
    private void displayStockDataInGraph(List<DailyStockData> monthlyStockData) {
        // Sort the monthlyStockData list by date in ascending order
        Collections.sort(monthlyStockData, new Comparator<DailyStockData>() {
            @Override
            public int compare(DailyStockData data1, DailyStockData data2) {
                // Assuming data.getDate() returns a date in "YYYY-MM-DD" format
                return data1.getDate().compareTo(data2.getDate());
            }
        });

        List<Entry> entries = new ArrayList<>();

        // Populate entries for the LineChart
        for (int i = 0; i < monthlyStockData.size(); i++) {
            DailyStockData data = monthlyStockData.get(i);
            entries.add(new Entry(i, data.getPrice()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Monthly Stock Data");

        // Customize the line chart appearance
        dataSet.setColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
        dataSet.setDrawValues(false); // Disable drawing values on data points
        dataSet.setDrawCircles(false); // Hide data points (dots)
        LineData lineData = new LineData(dataSet);
        lineChart.getDescription().setEnabled(false);
        dataSet.setDrawCircles(false); // Hide data points (dots)
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Set x-axis position to bottom
        xAxis.setGranularity(5f); // Set granularity to 5 units

        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                // Convert float value to int for index
                int index = (int) value;
                // Ensure index is within bounds
                if (index >= 0 && index < monthlyStockData.size()) {
                    DailyStockData data = monthlyStockData.get(index);
                    String[] dateParts = data.getDate().split("-");
                    return dateParts[1] + "-" + dateParts[2]; // Return MM-DD
                }
                return "";
            }
        });


        // Configure Y-axis formatting
        YAxis leftYAxis = lineChart.getAxisLeft();
        leftYAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                // Customize left Y-axis labels if needed
                return String.valueOf(value);
            }
        });

        // Disable right Y-axis
        YAxis rightYAxis = lineChart.getAxisRight();
        rightYAxis.setEnabled(false);

        // Set data to the LineChart
        lineChart.setData(lineData);

        // Refresh the chart
        lineChart.invalidate();
    }

}
