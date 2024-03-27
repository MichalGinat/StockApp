package com.example.stock;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.stock.model.StockInfoSearch;

import java.util.List;

/**
 * SearchResultsActivity is responsible for displaying the search results
 * obtained from the user's query. It extends BaseActivity to inherit common
 * functionalities like navigation and UI setup.
 */
public class SearchResultsActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private StockAdapter stockAdapter;
    private List<StockInfoSearch> stockInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        recyclerView = findViewById(R.id.recyclerView);
        stockInfoList = getIntent().getParcelableArrayListExtra("stockInfoList");
        setupRecyclerView();
    }

    /**
     * Initializes the RecyclerView to display the search results.
     */
    private void setupRecyclerView() {
        stockAdapter = new StockAdapter(this, stockInfoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(stockAdapter);
    }
}

