package com.example.stock.model;

import android.os.Parcel;
import android.os.Parcelable;

public class StockInfo implements Parcelable {
    private String symbol;
    private String name;

    public StockInfo(String symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }

    protected StockInfo(Parcel in) {
        symbol = in.readString();
        name = in.readString();
    }

    public static final Creator<StockInfo> CREATOR = new Creator<StockInfo>() {
        @Override
        public StockInfo createFromParcel(Parcel in) {
            return new StockInfo(in);
        }

        @Override
        public StockInfo[] newArray(int size) {
            return new StockInfo[size];
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
