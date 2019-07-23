package com.kcirqueapps.laundryapp.network.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Item implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("service_id")
    @Expose
    private int serviceId;
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
    @SerializedName("order_id")
    @Expose
    private int orderId;


    /**
     * No args constructor for use in serialization
     */
    public Item() {
    }

    /**
     * @param amount
     * @param unit
     * @param serviceId
     * @param unitPrice
     */
    public Item(int serviceId,String service, String unit, String unitPrice, String amount) {
        super();
        this.serviceId = serviceId;
        this.service = service;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.amount = amount;
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

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}