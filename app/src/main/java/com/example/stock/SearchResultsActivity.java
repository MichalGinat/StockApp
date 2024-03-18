package com.example.stock;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.stock.model.StockInfo;
import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StockAdapter stockAdapter;
    private List<StockInfo> stockInfoList;

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

