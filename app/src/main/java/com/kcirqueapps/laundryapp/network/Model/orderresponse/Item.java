package com.kcirqueapps.laundryapp.network.Model.orderresponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item implements Parcelable {

    @SerializedName("order_details_id")
    @Expose
    private int orderDetailsId;
    @SerializedName("order_id")
    @Expose
    private int orderId;
    @SerializedName("service")
    @Expose
    private String service;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("unit_price")
    @Expose
    private String unitPrice;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("service_id")
    @Expose
    private int serviceId;
    @SerializedName("cloth")
    @Expose
    private String cloth;
    @SerializedName("iron_price")
    @Expose
    private String ironPrice;
    @SerializedName("wash_price")
    @Expose
    private String washPrice;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    public final static Creator<Item> CREATOR = new Creator<Item>() {


        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return (new Item[size]);
        }

    };

    Item(Parcel in) {
        this.orderDetailsId = ((int) in.readValue((int.class.getClassLoader())));
        this.orderId = ((int) in.readValue((int.class.getClassLoader())));
        this.service = ((String) in.readValue((String.class.getClassLoader())));
        this.unit = ((String) in.readValue((String.class.getClassLoader())));
        this.unitPrice = ((String) in.readValue((String.class.getClassLoader())));
        this.amount = ((String) in.readValue((String.class.getClassLoader())));
        this.serviceId = ((int) in.readValue((int.class.getClassLoader())));
        this.cloth = ((String) in.readValue((String.class.getClassLoader())));
        this.ironPrice = ((String) in.readValue((String.class.getClassLoader())));
        this.washPrice = ((String) in.readValue((String.class.getClassLoader())));
        this.imageUrl = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Item(int orderDetailsId, int orderId, String service, String unit, String unitPrice, String amount, int serviceId, String cloth, String ironPrice, String washPrice, String imageUrl) {
        this.orderDetailsId = orderDetailsId;
        this.orderId = orderId;
        this.service = service;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.amount = amount;
        this.serviceId = serviceId;
        this.cloth = cloth;
        this.ironPrice = ironPrice;
        this.washPrice = washPrice;
        this.imageUrl = imageUrl;
    }

    public int getOrderDetailsId() {
        return orderDetailsId;
    }

    public void setOrderDetailsId(int orderDetailsId) {
        this.orderDetailsId = orderDetailsId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getCloth() {
        return cloth;
    }

    public void setCloth(String cloth) {
        this.cloth = cloth;
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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(orderDetailsId);
        dest.writeValue(orderId);
        dest.writeValue(service);
        dest.writeValue(unit);
        dest.writeValue(unitPrice);
        dest.writeValue(amount);
        dest.writeValue(serviceId);
        dest.writeValue(cloth);
        dest.writeValue(ironPrice);
        dest.writeValue(washPrice);
        dest.writeValue(imageUrl);
    }

    public int describeContents() {
        return 0;
    }

}
