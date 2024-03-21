package com.example.stock.model;

import android.os.Parcel;
import android.os.Parcelable;

public class StockInfoSerach implements Parcelable {
    private String symbol;
    private String name;

    public StockInfoSerach(String symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }

    protected StockInfoSerach(Parcel in) {
        symbol = in.readString();
        name = in.readString();
    }

    public static final Creator<StockInfoSerach> CREATOR = new Creator<StockInfoSerach>() {
        @Override
        public StockInfoSerach createFromParcel(Parcel in) {
            return new StockInfoSerach(in);
        }

        @Override
        public StockInfoSerach[] newArray(int size) {
            return new StockInfoSerach[size];
        }
    };

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(symbol);
        dest.writeString(name);
    }
}
