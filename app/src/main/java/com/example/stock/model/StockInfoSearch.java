package com.example.stock.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents search results for stock information including symbol and name.
 * Implements Parcelable for object serialization.
 */
public class StockInfoSearch implements Parcelable {
    private String symbol; // Stores the symbol of the stock
    private String name; // Stores the name of the stock

    // Constructor to initialize StockInfoSearch object with symbol and name
    public StockInfoSearch(String symbol, String name) {
        this.symbol = symbol;
        this.name = name;
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
