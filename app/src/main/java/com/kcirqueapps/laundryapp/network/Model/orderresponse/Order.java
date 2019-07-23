package com.kcirqueapps.laundryapp.network.Model.orderresponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("address_line1")
    @Expose
    private String addressLine1;
    @SerializedName("address_line2")
    @Expose
    private String addressLine2;
    @SerializedName("delivery_note")
    @Expose
    private String deliveryNote;
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
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;
    @SerializedName("delivery_status")
    @Expose
    private String deliveryStatus;
    public final static Creator<Order> CREATOR = new Creator<Order>() {


        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        public Order[] newArray(int size) {
            return (new Order[size]);
        }

    };

    Order(Parcel in) {
        this.id = ((int) in.readValue((int.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.addressLine1 = ((String) in.readValue((String.class.getClassLoader())));
        this.addressLine2 = ((String) in.readValue((String.class.getClassLoader())));
        this.deliveryNote = ((String) in.readValue((String.class.getClassLoader())));
        this.collectionDate = ((String) in.readValue((String.class.getClassLoader())));
        this.collectionTime = ((String) in.readValue((String.class.getClassLoader())));
        this.deliveryDate = ((String) in.readValue((String.class.getClassLoader())));
        this.deliveryTime = ((String) in.readValue((String.class.getClassLoader())));
        this.totalAmount = ((String) in.readValue((String.class.getClassLoader())));
        this.paymentMethod = ((String) in.readValue((String.class.getClassLoader())));
        this.paymentStatus = ((String) in.readValue((String.class.getClassLoader())));
        this.deliveryStatus = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Order() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getDeliveryNote() {
        return deliveryNote;
    }

    public void setDeliveryNote(String deliveryNote) {
        this.deliveryNote = deliveryNote;
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

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
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

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(userId);
        dest.writeValue(addressLine1);
        dest.writeValue(addressLine2);
        dest.writeValue(deliveryNote);
        dest.writeValue(collectionDate);
        dest.writeValue(collectionTime);
        dest.writeValue(deliveryDate);
        dest.writeValue(deliveryTime);
        dest.writeValue(totalAmount);
        dest.writeValue(paymentMethod);
        dest.writeValue(paymentStatus);
        dest.writeValue(deliveryStatus);
    }

    public int describeContents() {
        return 0;
    }

}
