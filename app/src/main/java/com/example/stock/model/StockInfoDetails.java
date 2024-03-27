package com.example.stock.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents detailed stock information including symbol, name, price, change, percentage change, day low, and day high.
 * Implements Parcelable for object serialization.
 */
public class StockInfoDetails implements Parcelable {
    private String symbol; // Stores the symbol of the stock
    private String name; // Stores the name of the stock
    private double price; // Stores the price of the stock
    private double change; // Stores the change in the stock price
    private double changesPercentage; // Stores the percentage change in the stock price
    private double dayLow; // Stores the lowest price of the stock for the day
    private double dayHigh; // Stores the highest price of the stock for the day

    // Constructor to initialize StockInfoDetails object with all parameters
    public StockInfoDetails(String symbol, String name, double price, double change, double changesPercentage, double dayLow, double dayHigh) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
        this.change = change;
        this.changesPercentage = changesPercentage;
        this.dayLow = dayLow;
        this.dayHigh = dayHigh;
    }

    // Parcelable constructor used to reconstruct the object from a Parcel
    protected StockInfoDetails(Parcel in) {
        symbol = in.readString();
        name = in.readString();
        price = in.readDouble();
        change = in.readDouble();
        changesPercentage = in.readDouble();
        dayLow = in.readDouble();
        dayHigh = in.readDouble();
    }

    // Creator for Parcelable interface, responsible for creating instances of StockInfoDetails from a Parcel
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

    //Getters and setters
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

    // Parcelable method describing the contents of the object
    @Override
    public int describeContents() {
        return 0;
    }

    // Method to write the object's data to a Parcel
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

