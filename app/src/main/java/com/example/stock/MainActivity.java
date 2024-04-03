package com.example.stock;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;


public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupMainButtons(); // Call setupMainButtons after views have been initialized
    }

    protected void setupMainButtons() {
        // Handle Date view
        TextView date = findViewById(R.id.date);
        TextView empty_list_text = findViewById(R.id.empty_fav_list);

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int year = calendar.get(Calendar.YEAR);
        int dateNum = calendar.get(Calendar.DATE);
        String month = new DateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH)];

        date.setText(String.format("%s %s, %s", month, dateNum, year));

        // Retrieving Favorite Stock list
        String serializedFavList = preferences.getString(PREFERENCES_KEY, "");
        if (serializedFavList.isEmpty()) {
            empty_list_text.setVisibility(View.VISIBLE);
        } else {
            empty_list_text.setVisibility(View.GONE);
            List<String> stockInfoList = Arrays.asList(serializedFavList.split(","));
            showWatchlist(stockInfoList);
        }
    }

    private void showWatchlist(List<String> stockInfoList) {
        RecyclerView favoritesView = findViewById(R.id.favoritesView);

        FavoritesAdapter favoritesAdapter = new FavoritesAdapter(this, stockInfoList);
        favoritesView.setLayoutManager(new LinearLayoutManager(this));
        favoritesView.setAdapter(favoritesAdapter);
    }
}
