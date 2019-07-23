package com.kcirqueapps.laundryapp.network.Model.packageresponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item implements Parcelable {

    @SerializedName("cloth")
    @Expose
    private String cloth;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
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
    @SerializedName("delivery_status")
    @Expose
    private String deliveryStatus;
    public static final Creator<Item> CREATOR = new Creator<Item>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        public Item[] newArray(int size) {
            return (new Item[size]);
        }

    };

    protected Item(Parcel in) {
        this.cloth = ((String) in.readValue((String.class.getClassLoader())));
        this.imageUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((int) in.readValue((int.class.getClassLoader())));
        this.serviceId = ((int) in.readValue((int.class.getClassLoader())));
        this.orderId = ((int) in.readValue((int.class.getClassLoader())));
        this.unit = ((String) in.readValue((String.class.getClassLoader())));
        this.collectionDate = ((String) in.readValue((String.class.getClassLoader())));
        this.collectionTime = ((String) in.readValue((String.class.getClassLoader())));
        this.deliveryDate = ((String) in.readValue((String.class.getClassLoader())));
        this.deliveryTime = ((String) in.readValue((String.class.getClassLoader())));
        this.deliveryStatus = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public Item() {
    }

    /**
     * @param collectionTime
     * @param id
     * @param unit
     * @param deliveryTime
     * @param serviceId
     * @param cloth
     * @param imageUrl
     * @param deliveryDate
     * @param collectionDate
     * @param orderId
     */
    public Item(String cloth, String imageUrl, int id, int serviceId, int orderId, String unit, String collectionDate, String collectionTime, String deliveryDate, String deliveryTime) {
        super();
        this.cloth = cloth;
        this.imageUrl = imageUrl;
        this.id = id;
        this.serviceId = serviceId;
        this.orderId = orderId;
        this.unit = unit;
        this.collectionDate = collectionDate;
        this.collectionTime = collectionTime;
        this.deliveryDate = deliveryDate;
        this.deliveryTime = deliveryTime;
    }

    public String getCloth() {
        return cloth;
    }

    public void setCloth(String cloth) {
        this.cloth = cloth;
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

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(cloth);
        dest.writeValue(imageUrl);
        dest.writeValue(id);
        dest.writeValue(serviceId);
        dest.writeValue(orderId);
        dest.writeValue(unit);
        dest.writeValue(collectionDate);
        dest.writeValue(collectionTime);
        dest.writeValue(deliveryDate);
        dest.writeValue(deliveryTime);
        dest.writeValue(deliveryStatus);
    }

    public int describeContents() {
        return 0;
    }

}
