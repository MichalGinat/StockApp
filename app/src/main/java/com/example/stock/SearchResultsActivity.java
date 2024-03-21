package com.example.stock;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.stock.model.StockInfoSerach;

import java.util.List;

public class SearchResultsActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private StockAdapter stockAdapter;
    private List<StockInfoSerach> stockInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        recyclerView = findViewById(R.id.recyclerView);
        stockInfoList = getIntent().getParcelableArrayListExtra("stockInfoList");
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        stockAdapter = new StockAdapter(this, stockInfoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(stockAdapter);
    }
}

