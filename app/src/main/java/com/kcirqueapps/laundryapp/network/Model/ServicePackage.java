package com.kcirqueapps.laundryapp.network.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ServicePackage implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("service_type")
    @Expose
    private String serviceType;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    /**
     * No args constructor for use in serialization
     */
    public ServicePackage() {
    }

    public ServicePackage(String name, String serviceType, String  unit, String amount, String duration) {
        super();
        this.name = name;
        this.serviceType = serviceType;
        this.unit = unit;
        this.amount = amount;
        this.duration = duration;
    }

    protected ServicePackage(Parcel in) {
        id = in.readInt();
        name = in.readString();
        serviceType = in.readString();
        unit = in.readString();
        amount = in.readString();
        duration = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<ServicePackage> CREATOR = new Creator<ServicePackage>() {
        @Override
        public ServicePackage createFromParcel(Parcel in) {
            return new ServicePackage(in);
        }

        @Override
        public ServicePackage[] newArray(int size) {
            return new ServicePackage[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String  unit) {
        this.unit = unit;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(serviceType);
        dest.writeString(unit);
        dest.writeString(amount);
        dest.writeString(duration);
        dest.writeString(imageUrl);
    }
}