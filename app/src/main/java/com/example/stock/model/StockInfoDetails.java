package com.example.stock.model;

import android.os.Parcel;
import android.os.Parcelable;

public class StockInfoDetails implements Parcelable {
    private String symbol;
    private String name;
    private double price;
    private double change;
    private double changesPercentage;
    private double dayLow;
    private double dayHigh;

    public StockInfoDetails(String symbol, String name, double price, double change, double changesPercentage, double dayLow, double dayHigh) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
        this.change = change;
        this.changesPercentage = changesPercentage;
        this.dayLow = dayLow;
        this.dayHigh = dayHigh;
    }

    protected StockInfoDetails(Parcel in) {
        symbol = in.readString();
        name = in.readString();
        price = in.readDouble();
        change = in.readDouble();
        changesPercentage = in.readDouble();
        dayLow = in.readDouble();
        dayHigh = in.readDouble();
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

    public double getChangesPercentage() {
        return changesPercentage;
    }

    public void setChangesPercentage(double changesPercentage) {
        this.changesPercentage = changesPercentage;
    }

    public double getDayLow() {
        return dayLow;
    }

    public void setDayLow(double dayLow) {
        this.dayLow = dayLow;
    }

    public double getDayHigh() {
        return dayHigh;
    }

    public void setDayHigh(double dayHigh) {
        this.dayHigh = dayHigh;
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
        dest.writeDouble(changesPercentage);
        dest.writeDouble(dayLow);
        dest.writeDouble(dayHigh);
    }
}

