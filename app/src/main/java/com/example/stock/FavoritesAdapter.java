package com.example.stock;

import static com.example.stock.ApiServiceUtils.fetchStockQuote;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stock.model.StockInfoDetails;
import com.example.stock.model.StockInfoSearch;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoriteViewHolder> {

    private Context context;
    private static final String TAG = FavoritesAdapter.class.getSimpleName();
    private List<String> symbolList;

    public FavoritesAdapter(Context context, List<String> symbolList) {
        this.context = context;
        this.symbolList = symbolList;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fav_stock_item, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        String symbol = symbolList.get(position);
        BaseActivity activity = (BaseActivity) context;

        fetchStockQuote(symbol, TAG, (StockInfoDetails stockDetails) -> {
            activity.runOnUiThread(() -> {
                holder.itemView.setOnClickListener(v -> {
                    // Handle item click
                    Intent intent = new Intent(context, StockDetailsActivity.class);
                    StockInfoSearch infoSearch = new StockInfoSearch(stockDetails.getSymbol(), stockDetails.getName());
                    intent.putExtra("selectedStockInfo", infoSearch);
                    context.startActivity(intent);
                });

                holder.getStockName().setText(stockDetails.getSymbol().toUpperCase());
                holder.getStockPrice().setText(String.format("%.2f", stockDetails.getPrice()));
                double change = Double.parseDouble(String.format("%.2f", stockDetails.getChange()));
                if (change > 0) {
                    holder.trending.setVisibility(View.VISIBLE);
                    holder.getChange().setTextColor(ContextCompat.getColor(context, R.color.green));
                    holder.trending.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_twotone_trending_up_24));
                } else if (change < 0) {
                    holder.trending.setVisibility(View.VISIBLE);
                    holder.getChange().setTextColor(ContextCompat.getColor(context, R.color.red));
                    holder.trending.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_trending_down_24));
                } else {
                    holder.trending.setVisibility(View.GONE);
                    holder.getChange().setTextColor(ContextCompat.getColor(context, R.color.trendingSearch));
                }

                holder.getChange().setText(String.format("%.2f", change));
                holder.getSubtitle().setText(stockDetails.getName());
            });
        });
    }

    @Override
    public int getItemCount() {
        return symbolList.size();
    }
}
