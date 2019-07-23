package com.kcirqueapps.laundryapp.network.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ServicePrice implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("cloth")
    @Expose
    private String cloth;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("iron_price")
    @Expose
    private String ironPrice;
    @SerializedName("wash_price")
    @Expose
    private String washPrice;

    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    protected ServicePrice(Parcel in) {
        id = in.readInt();
        cloth = in.readString();
        unit = in.readString();
        ironPrice = in.readString();
        washPrice = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<ServicePrice> CREATOR = new Creator<ServicePrice>() {
        @Override
        public ServicePrice createFromParcel(Parcel in) {
            return new ServicePrice(in);
        }

        @Override
        public ServicePrice[] newArray(int size) {
            return new ServicePrice[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCloth() {
        return cloth;
    }

    public void setCloth(String cloth) {
        this.cloth = cloth;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getIronPrice() {
        return ironPrice;
    }

    public void setIronPrice(String ironPrice) {
        this.ironPrice = ironPrice;
    }

    public String getWashPrice() {
        return washPrice;
    }

    public void setWashPrice(String washPrice) {
        this.washPrice = washPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(cloth);
        dest.writeString(unit);
        dest.writeString(ironPrice);
        dest.writeString(washPrice);
        dest.writeString(imageUrl);
    }
}