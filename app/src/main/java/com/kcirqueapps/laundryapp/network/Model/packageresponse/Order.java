package com.kcirqueapps.laundryapp.network.Model.packageresponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order implements Parcelable {

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
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("package_id")
    @Expose
    private int packageId;
    @SerializedName("user_id")
    @Expose
    private String userId;
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
    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;
    public static final Creator<Order> CREATOR = new Creator<Order>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        public Order[] newArray(int size) {
            return (new Order[size]);
        }

    };

    protected Order(Parcel in) {
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.serviceType = ((String) in.readValue((String.class.getClassLoader())));
        this.unit = ((String) in.readValue((String.class.getClassLoader())));
        this.amount = ((String) in.readValue((String.class.getClassLoader())));
        this.imageUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((int) in.readValue((int.class.getClassLoader())));
        this.packageId = ((int) in.readValue((int.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.collectionAddress = ((String) in.readValue((String.class.getClassLoader())));
        this.deliveryAddress = ((String) in.readValue((String.class.getClassLoader())));
        this.startDate = ((String) in.readValue((String.class.getClassLoader())));
        this.endDate = ((String) in.readValue((String.class.getClassLoader())));
        this.paymentMethod = ((String) in.readValue((String.class.getClassLoader())));
        this.paymentStatus = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public Order() {
    }

    /**
     * @param startDate
     * @param imageUrl
     * @param packageId
     * @param paymentStatus
     * @param endDate
     * @param serviceType
     * @param amount
     * @param id
     * @param collectionAddress
     * @param unit
     * @param name
     * @param userId
     * @param paymentMethod
     * @param deliveryAddress
     */
    public Order(String name, String serviceType, String unit, String amount, String imageUrl, int id, int packageId, String userId, String collectionAddress, String deliveryAddress, String startDate, String endDate, String paymentMethod, String paymentStatus) {
        super();
        this.name = name;
        this.serviceType = serviceType;
        this.unit = unit;
        this.amount = amount;
        this.imageUrl = imageUrl;
        this.id = id;
        this.packageId = packageId;
        this.userId = userId;
        this.collectionAddress = collectionAddress;
        this.deliveryAddress = deliveryAddress;
        this.startDate = startDate;
        this.endDate = endDate;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
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

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(name);
        dest.writeValue(serviceType);
        dest.writeValue(unit);
        dest.writeValue(amount);
        dest.writeValue(imageUrl);
        dest.writeValue(id);
        dest.writeValue(packageId);
        dest.writeValue(userId);
        dest.writeValue(collectionAddress);
        dest.writeValue(deliveryAddress);
        dest.writeValue(startDate);
        dest.writeValue(endDate);
        dest.writeValue(paymentMethod);
        dest.writeValue(paymentStatus);
    }

    public int describeContents() {
        return 0;
    }

}
