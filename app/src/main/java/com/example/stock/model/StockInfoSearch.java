package com.example.stock.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents search results for stock information including symbol and name.
 * Implements Parcelable for object serialization.
 */
public class StockInfoSearch implements Parcelable {
    private final String symbol; // Stores the symbol of the stock
    private final String name; // Stores the name of the stock

    // Constructor to initialize StockInfoSearch object with symbol and name
    private StockInfoSearch(String symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }

    // Builder class for constructing StockInfoSearch objects
    public static class Builder {
        private String symbol;
        private String name;

        public Builder setSymbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public StockInfoSearch build() {
            return new StockInfoSearch(symbol, name);
        }
    }

    // Parcelable constructor used to reconstruct the object from a Parcel
    protected StockInfoSearch(Parcel in) {
        symbol = in.readString();
        name = in.readString();
    }

    // Creator for Parcelable interface, responsible for creating instances of StockInfoSearch from a Parcel
    public static final Creator<StockInfoSearch> CREATOR = new Creator<StockInfoSearch>() {
        @Override
        public StockInfoSearch createFromParcel(Parcel in) {
            return new StockInfoSearch(in);
        }

        @Override
        public StockInfoSearch[] newArray(int size) {
            return new StockInfoSearch[size];
        }
    };

    //Getters and setters
    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
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
    }
}
