package com.example.stock.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DailyStockData implements Parcelable {
    private String date;
    private float price;

    public DailyStockData(float price, String time) {
        this.price = price;
        this.date = time;
    }

    protected DailyStockData(Parcel in) {
        price = in.readFloat();
        date = in.readString();
    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(price);
        dest.writeString(date);
    }
}
