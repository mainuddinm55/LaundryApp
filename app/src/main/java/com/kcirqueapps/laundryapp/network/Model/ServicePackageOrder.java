package com.kcirqueapps.laundryapp.network.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ServicePackageOrder implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("package_id")
    @Expose
    private int packageId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("order_date")
    @Expose
    private String orderDate;
    @SerializedName("collection_address")
    @Expose
    private String collectionAddress;
    @SerializedName("delivery_address")
    @Expose
    private String deliveryAddress;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;

    /**
     * No args constructor for use in serialization
     */
    public ServicePackageOrder() {
    }

    /**
     * @param amount
     * @param collectionAddress
     * @param startDate
     * @param userId
     * @param packageId
     * @param endDate
     * @param paymentMethod
     * @param deliveryAddress
     */
    public ServicePackageOrder( int packageId, String userId, String orderDate,String collectionAddress, String deliveryAddress, String startDate, String endDate, String amount, String paymentMethod) {
        super();
        this.packageId = packageId;
        this.userId = userId;
        this.orderDate = orderDate;
        this.collectionAddress = collectionAddress;
        this.deliveryAddress = deliveryAddress;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getCollectionAddress() {
        return collectionAddress;
    }

    public void setCollectionAddress(String collectionAddress) {
        this.collectionAddress = collectionAddress;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

}