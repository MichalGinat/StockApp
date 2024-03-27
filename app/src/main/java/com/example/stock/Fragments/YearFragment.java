package com.example.stock.Fragments;
import android.util.Log;
import android.os.Bundle;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.example.stock.model.DailyStockData;
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

// Represents a Fragment displaying yearly stock data using a LineChart,
// handling data fetching, parsing JSON, and chart rendering.
// Encapsulates functionality for displaying stock data in the app's UI.
public class YearFragment extends Fragment {
    private static final String TAG = YearFragment.class.getSimpleName();

    private String symbol;
    private LineChart lineChart;
    private FMPApiServiceSingelton apiService;

    // Constructor to set the stock symbol
    public YearFragment(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_year, container, false);
        lineChart = view.findViewById(R.id.line_chart);
        apiService = FMPApiServiceSingelton.getInstance();
        // Fetch stock data from one year ago to today
        fetchStockDataYear(symbol);

        return view;
    }

    // Parse JSON response to a list of DailyStockData objects
    private List<DailyStockData> parseResponseToYearlyStockDataList(String responseData) {
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

    // Fetch stock data for the past year
    private void fetchStockDataYear(String symbol) {
        Calendar calendar = Calendar.getInstance();

        // Save the current date
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // Months are 0-based
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        // Move 365 days back (1 year)
        calendar.add(Calendar.DAY_OF_MONTH, -365);
        int lastYear = calendar.get(Calendar.YEAR);
        int lastMonth = calendar.get(Calendar.MONTH) + 1; // Months are 0-based
        int lastDay = calendar.get(Calendar.DAY_OF_MONTH);

        String startOfLastYear = lastYear + "-" + (lastMonth < 10 ? "0" + lastMonth : lastMonth) + "-" + (lastDay < 10 ? "0" + lastDay : lastDay);

        // Current date (today)
        String today = currentYear + "-" + (currentMonth < 10 ? "0" + currentMonth : currentMonth) + "-" + (currentDay < 10 ? "0" + currentDay : currentDay); // Get current day dynamically

        apiService.StockHistoricalData(symbol, startOfLastYear, today, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Error fetching stock data from last year to today: " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    Log.e(TAG, responseData);

                    List<DailyStockData> stockDataList = parseResponseToYearlyStockDataList(responseData);

                    if (!stockDataList.isEmpty()) {
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

    // Display stock data in the LineChart
    private void displayStockDataInGraph(List<DailyStockData> stockDataList) {
        Collections.sort(stockDataList, new Comparator<DailyStockData>() {
            @Override
            public int compare(DailyStockData data1, DailyStockData data2) {
                return data1.getDate().compareTo(data2.getDate());
            }
        });

        List<Entry> entries = new ArrayList<>();

        // Populate entries for the LineChart
        for (int i = 0; i < stockDataList.size(); i++) {
            DailyStockData data = stockDataList.get(i);
            entries.add(new Entry(i, data.getPrice()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Yearly Stock Data");

        // Customize the line chart appearance
        dataSet.setColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(false);
        LineData lineData = new LineData(dataSet);
        lineChart.getDescription().setEnabled(false);
        // Configure X-axis formatting
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setGranularityEnabled(true);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Set x-axis position to bottom
        xAxis.setLabelCount(1);
        xAxis.setGranularity(1f);

        xAxis.setValueFormatter(new ValueFormatter() {
            private String currentYear = ""; // Variable to store the current year

            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                int index = (int) value;
                if (index >= 0 && index < stockDataList.size()) {
                    DailyStockData data = stockDataList.get(index);
                    String[] dateParts = data.getDate().split("-");
                    String year = dateParts[0];
                    if (isCurrentYear(year)) {
                        // Display year label only if it's the current year
                        currentYear = year;
                        return year;
                    } else {
                        return ""; // Empty label for other years
                    }
                }
                return "";
            }

            // Helper method to check if the given year is the current year
            private boolean isCurrentYear(String year) {
                Calendar calendar = Calendar.getInstance();
                int currentYear = calendar.get(Calendar.YEAR);
                return year.equals(String.valueOf(currentYear));
            }
        });

        // Configure Y-axis formatting
        YAxis leftYAxis = lineChart.getAxisLeft();
        leftYAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
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

