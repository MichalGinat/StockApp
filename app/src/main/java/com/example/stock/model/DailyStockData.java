package com.example.stock.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DailyStockData implements Parcelable {
    private float time;
    private float price;

    public DailyStockData(float price, float time) {
        this.price = price;
        this.time = time;
    }

    protected DailyStockData(Parcel in) {
        price = in.readFloat();
        time = in.readFloat();
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

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
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
        dest.writeFloat(time);
    }
}
