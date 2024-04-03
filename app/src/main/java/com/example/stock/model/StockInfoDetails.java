package com.example.stock.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents detailed stock information including symbol, name, price, change, percentage change, day low, and day high.
 * Implements Parcelable for object serialization.
 */
public class StockInfoDetails implements Parcelable {
    private final String symbol; // Stores the symbol of the stock
    private final String name; // Stores the name of the stock
    private final double price; // Stores the price of the stock
    private final double change; // Stores the change in the stock price
    private final double changesPercentage; // Stores the percentage change in the stock price
    private final double dayLow; // Stores the lowest price of the stock for the day
    private final double dayHigh; // Stores the highest price of the stock for the day

    // Private constructor used by the builder
    private StockInfoDetails(String symbol, String name, double price, double change,
                             double changesPercentage, double dayLow, double dayHigh) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
        this.change = change;
        this.changesPercentage = changesPercentage;
        this.dayLow = dayLow;
        this.dayHigh = dayHigh;
    }

    // Builder class for constructing StockInfoDetails objects
    public static class Builder {
        private String symbol;
        private String name;
        private double price;
        private double change;
        private double changesPercentage;
        private double dayLow;
        private double dayHigh;

        public Builder setSymbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setPrice(double price) {
            this.price = price;
            return this;
        }

        public Builder setChange(double change) {
            this.change = change;
            return this;
        }

        public Builder setChangesPercentage(double changesPercentage) {
            this.changesPercentage = changesPercentage;
            return this;
        }

        public Builder setDayLow(double dayLow) {
            this.dayLow = dayLow;
            return this;
        }

        public Builder setDayHigh(double dayHigh) {
            this.dayHigh = dayHigh;
            return this;
        }

        public StockInfoDetails build() {
            return new StockInfoDetails(symbol, name, price, change, changesPercentage, dayLow, dayHigh);
        }
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

    public double getChange() {
        return change;
    }

    public double getChangesPercentage() {
        return changesPercentage;
    }

    public double getDayLow() {
        return dayLow;
    }

    public double getDayHigh() {
        return dayHigh;
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
