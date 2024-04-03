package com.example.stock;

import static com.example.stock.ApiServiceUtils.fetchStockQuotePeriodic;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stock.model.StockInfoDetails;
import com.example.stock.model.StockInfoSearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * RecyclerView adapter for displaying favorite stock information
 * Handles creation of FavoriteViewHolder and binding data to views
 * Provides item click functionality to launch StockDetailsActivity with selected stock info
 */
public class FavoritesAdapter extends RecyclerView.Adapter<FavoriteViewHolder> {

    private static final String TAG = FavoritesAdapter.class.getSimpleName();
    private static final int INTERVAL_IN_SEC = 30;
    private final Context context;
    private final List<String> symbolList;
    private final List<Handler> handlers = new ArrayList<>();


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
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        stopPeriodicUpdate();
    }

    /**
     * Stopping periodic updates
     */
    private void stopPeriodicUpdate() {
        for (Handler handler : handlers) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        String symbol = symbolList.get(position);
        BaseActivity activity = (BaseActivity) context;

        holder.getLoadingTextView().setVisibility(View.VISIBLE);
        Handler handler = fetchStockQuotePeriodic(symbol, TAG, (StockInfoDetails stockDetails) -> {
            activity.runOnUiThread(() -> {
                holder.itemView.setOnClickListener(v -> {
                    // Handle item click
                    Intent intent = new Intent(context, StockDetailsActivity.class);
                    StockInfoSearch infoSearch = new StockInfoSearch.Builder()
                            .setSymbol(stockDetails.getSymbol())
                            .setName(stockDetails.getName())
                            .build();
                    intent.putExtra("selectedStockInfo", infoSearch);
                    context.startActivity(intent);
                });

                holder.getStockName().setText(stockDetails.getSymbol().toUpperCase());
                holder.getStockPrice().setText(
                        String.format(Locale.getDefault(), "%.2f", stockDetails.getPrice()));
                double change = Double.parseDouble(
                        String.format(Locale.getDefault(), "%.2f", stockDetails.getChange()));
                if (change > 0) {
                    holder.trending.setVisibility(View.VISIBLE);
                    holder.getChange().setTextColor(ContextCompat.getColor(context, R.color.green));
                    holder.trending.setImageDrawable(
                            ContextCompat.getDrawable(context, R.drawable.ic_twotone_trending_up_24));
                } else if (change < 0) {
                    holder.trending.setVisibility(View.VISIBLE);
                    holder.getChange().setTextColor(ContextCompat.getColor(context, R.color.red));
                    holder.trending.setImageDrawable(
                            ContextCompat.getDrawable(context, R.drawable.ic_baseline_trending_down_24));
                } else {
                    holder.trending.setVisibility(View.GONE);
                    holder.getChange().setTextColor(ContextCompat.getColor(context, R.color.trendingSearch));
                }
                holder.getChange().setText(String.format(Locale.getDefault(), "%.2f", change));
                holder.getSubtitle().setText(stockDetails.getName());
                holder.getLoadingTextView().setVisibility(View.GONE);
            });
        }, INTERVAL_IN_SEC);

        handlers.add(handler);
    }

    @Override
    public int getItemCount() {
        return symbolList.size();
    }
}
