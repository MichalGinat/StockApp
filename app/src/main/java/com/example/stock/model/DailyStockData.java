package com.example.stock.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents daily stock data including date and price.
 * Implements Parcelable for object serialization.
 */
public class DailyStockData implements Parcelable {
    private final String date; // Stores the date of the daily stock data
    private final float price; // Stores the price of the stock on the given date

    // Constructor to initialize the DailyStockData object with price and date
    private DailyStockData(float price, String time) {
        this.price = price;
        this.date = time;
    }

    // Builder class for constructing DailyStockData objects
    public static class Builder {
        private String date;
        private float price;

        public Builder setDate(String date) {
            this.date = date;
            return this;
        }

        public Builder setPrice(float price) {
            this.price = price;
            return this;
        }

        public DailyStockData build() {
            return new DailyStockData(price, date);
        }
    }

    // Parcelable constructor used to reconstruct the object from a Parcel
    protected DailyStockData(Parcel in) {
        price = in.readFloat();
        date = in.readString();
    }

    // Creator for Parcelable interface, responsible for creating instances of DailyStockData from a Parcel
    public static final Creator<DailyStockData> CREATOR = new Creator<DailyStockData>() {
        @Override
        public DailyStockData createFromParcel(Parcel in) {
            return new DailyStockData(in);
        }

        @Override
        public DailyStockData[] newArray(int size) {
            return new DailyStockData[size];
        }
    };

    //Getters and setters
    public String getDate() {
        return date;
    }

    public float getPrice() {
        return price;
    }

    // Parcelable method describing the contents of the object
    @Override
    public int describeContents() {
        return 0;
    }

    // Method to write the object's data to a Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(price);
        dest.writeString(date);
    }
}
