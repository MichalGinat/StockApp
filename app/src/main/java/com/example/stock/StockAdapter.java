package com.example.stock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stock.model.StockInfoSerach;
import java.util.List;
import android.content.Intent;

// RecyclerView adapter for displaying stock information
// Handles creation of StockViewHolder and binding data to views
// Provides item click functionality to launch StockDetailsActivity with selected stock info
public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> {

    // Context to be used for creating views
    private Context context;

    // List of stock information items
    private List<StockInfoSerach> stockInfoList;

    // Constructor to initialize the adapter with context and stock information list
    public StockAdapter(Context context, List<StockInfoSerach> stockInfoList) {
        this.context = context;
        this.stockInfoList = stockInfoList;
    }


    // Create a new ViewHolder by inflating the item_stock layout
    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_stock, parent, false);
        return new StockViewHolder(view);
    }

    // Bind data to views in the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
        StockInfoSerach stockInfo = stockInfoList.get(position);
        holder.symbolTextView.setText(stockInfo.getSymbol());
        holder.nameTextView.setText(stockInfo.getName());

        // Set item click listener to handle clicks on stock items
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle item click
                Intent intent = new Intent(context, StockDetailsActivity.class);
                intent.putExtra("selectedStockInfo", stockInfo);
                context.startActivity(intent);
            }
        });
    }


    // Get the total number of items in the adapter
    @Override
    public int getItemCount() {
        return stockInfoList.size();
    }

    // ViewHolder class to hold views for each item in the RecyclerView
    static class StockViewHolder extends RecyclerView.ViewHolder {
        TextView symbolTextView;
        TextView nameTextView;

        // Constructor to initialize views in the ViewHolder
        StockViewHolder(@NonNull View itemView) {
            super(itemView);
            symbolTextView = itemView.findViewById(R.id.symbolTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
        }
    }
}
