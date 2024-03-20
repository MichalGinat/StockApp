package com.example.stock.model;

import android.os.Parcel;
import android.os.Parcelable;

public class StockInfo implements Parcelable {
    private String symbol;
    private String name;
    private double price;
    private double change;

    public StockInfo(String symbol, String name,double price, double change) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
        this.change = change;
    }

    protected StockInfo(Parcel in) {
        symbol = in.readString();
        name = in.readString();
        price = in.readDouble();
        change = in.readDouble();
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
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(symbol);
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeDouble(change);
    }
}
