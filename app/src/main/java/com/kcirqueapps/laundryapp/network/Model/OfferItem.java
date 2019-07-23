package com.kcirqueapps.laundryapp.network.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OfferItem implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("service_id")
    @Expose
    private int serviceId;
    @SerializedName("service_type")
    @Expose
    private String serviceType;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("regular_price")
    @Expose
    private String regularPrice;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("discount_percent")
    @Expose
    private String discountPercent;
    @SerializedName("actual_price")
    @Expose
    private String actualPrice;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("offer_id")
    @Expose
    private int offerId;
    @SerializedName("cloth")
    @Expose
    private String cloth;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    protected OfferItem(Parcel in) {
        id = in.readInt();
        serviceId = in.readInt();
        serviceType = in.readString();
        description = in.readString();
        regularPrice = in.readString();
        discount = in.readString();
        discountPercent = in.readString();
        actualPrice = in.readString();
        unit = in.readString();
        offerId = in.readInt();
        cloth = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<OfferItem> CREATOR = new Creator<OfferItem>() {
        @Override
        public OfferItem createFromParcel(Parcel in) {
            return new OfferItem(in);
        }

        @Override
        public OfferItem[] newArray(int size) {
            return new OfferItem[size];
        }
    };

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

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRegularPrice() {
        return regularPrice;
    }

    public void setRegularPrice(String regularPrice) {
        this.regularPrice = regularPrice;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(String discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(serviceId);
        dest.writeString(serviceType);
        dest.writeString(description);
        dest.writeString(regularPrice);
        dest.writeString(discount);
        dest.writeString(discountPercent);
        dest.writeString(actualPrice);
        dest.writeString(unit);
        dest.writeInt(offerId);
        dest.writeString(cloth);
        dest.writeString(imageUrl);
    }
}
