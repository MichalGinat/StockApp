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

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> {

    private Context context;
    private List<StockInfoSerach> stockInfoList;

    public StockAdapter(Context context, List<StockInfoSerach> stockInfoList) {
        this.context = context;
        this.stockInfoList = stockInfoList;
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_stock, parent, false);
        return new StockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
        StockInfoSerach stockInfo = stockInfoList.get(position);
        holder.symbolTextView.setText(stockInfo.getSymbol());
        holder.nameTextView.setText(stockInfo.getName());

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



    @Override
    public int getItemCount() {
        return stockInfoList.size();
    }

    static class StockViewHolder extends RecyclerView.ViewHolder {
        TextView symbolTextView;
        TextView nameTextView;

        StockViewHolder(@NonNull View itemView) {
            super(itemView);
            symbolTextView = itemView.findViewById(R.id.symbolTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
        }
    }
}
