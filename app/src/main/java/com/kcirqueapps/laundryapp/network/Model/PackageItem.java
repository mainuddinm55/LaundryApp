package com.kcirqueapps.laundryapp.network.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PackageItem implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("service_id")
    @Expose
    private int serviceId;
    @SerializedName("order_id")
    @Expose
    private int orderId;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("collection_date")
    @Expose
    private String collectionDate;
    @SerializedName("collection_time")
    @Expose
    private String collectionTime;
    @SerializedName("delivery_date")
    @Expose
    private String deliveryDate;
    @SerializedName("delivery_time")
    @Expose
    private String deliveryTime;

    /**
     * No args constructor for use in serialization
     */
    public PackageItem() {
    }

    /**
     * @param collectionTime
     * @param unit
     * @param deliveryTime
     * @param serviceId
     * @param deliveryDate
     * @param collectionDate
     * @param orderId
     */
    public PackageItem(int serviceId, int orderId, String unit, String collectionDate, String collectionTime, String deliveryDate, String deliveryTime) {
        super();
        this.serviceId = serviceId;
        this.orderId = orderId;
        this.unit = unit;
        this.collectionDate = collectionDate;
        this.collectionTime = collectionTime;
        this.deliveryDate = deliveryDate;
        this.deliveryTime = deliveryTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(String collectionDate) {
        this.collectionDate = collectionDate;
    }

    public String getCollectionTime() {
        return collectionTime;
    }

    public void setCollectionTime(String collectionTime) {
        this.collectionTime = collectionTime;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

}