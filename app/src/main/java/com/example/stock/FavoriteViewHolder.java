package com.example.stock;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

/**
 * ViewHolder class for displaying favorite stock items in a RecyclerView.
 */
public class FavoriteViewHolder extends RecyclerView.ViewHolder {
    ImageView details;
    ImageView trending;
    private final TextView loadingTextView;
    private final TextView stockName;
    private final TextView stockPrice;
    private final TextView change;
    private final TextView subtitle;
    private final ConstraintLayout stockListingLayout;

    public FavoriteViewHolder(View stockListingView) {
        super(stockListingView);
        stockName = stockListingView.findViewById(R.id.portfolioHeading);
        stockPrice = stockListingView.findViewById(R.id.stockPrice);
        change = stockListingView.findViewById(R.id.change);
        subtitle = stockListingView.findViewById(R.id.sharesOwned);
        details = stockListingView.findViewById(R.id.detailsButton);
        trending = stockListingView.findViewById(R.id.trendingImage);
        stockListingLayout = stockListingView.findViewById(R.id.stock_listing_layout);
        loadingTextView = stockListingView.findViewById(R.id.portfolioLoading);
    }

    public TextView getStockName() {
        return this.stockName;
    }

    public TextView getStockPrice() {
        return this.stockPrice;
    }

    public TextView getChange() {
        return this.change;
    }

    public TextView getSubtitle() {
        return this.subtitle;
    }

    public ConstraintLayout getStockListingLayout() {
        return stockListingLayout;
    }

    public TextView getLoadingTextView() {
        return loadingTextView;
    }
}
