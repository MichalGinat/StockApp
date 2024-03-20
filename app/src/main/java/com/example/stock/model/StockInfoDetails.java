package com.example.stock.model;

import android.os.Parcel;
import android.os.Parcelable;

public class StockInfoDetails implements Parcelable {
    private String symbol;
    private String name;
    private double price;
    private double change;

    public StockInfoDetails(String symbol, String name, double price, double change) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
        this.change = change;
    }

    protected StockInfoDetails(Parcel in) {
        symbol = in.readString();
        name = in.readString();
        price = in.readDouble();
        change = in.readDouble();
    }

    public static final Creator<StockInfoDetails> CREATOR = new Creator<StockInfoDetails>() {
        @Override
        public StockInfoDetails createFromParcel(Parcel in) {
            return new StockInfoDetails(in);
        }

        @Override
        public StockInfoDetails[] newArray(int size) {
            return new StockInfoDetails[size];
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
