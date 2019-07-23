package com.kcirqueapps.laundryapp.database.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Cart {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "service_id")
    private int serviceId;
    @ColumnInfo(name = "service_name")
    private String serviceName;
    @ColumnInfo(name = "service_type")
    private String serviceType;
    @ColumnInfo(name = "unit")
    private String unit;
    @ColumnInfo(name = "unit_price")
    private String unitPrice;
    @ColumnInfo(name = "price")
    private String amount;
    @ColumnInfo(name = "image_url")
    private String imageUrl;
    @ColumnInfo(name = "is_package")
    private boolean isPackage;

    public Cart(int serviceId, String serviceName, String serviceType, String unit, String unitPrice, String amount, String imageUrl, boolean isPackage) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceType = serviceType;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.amount = amount;
        this.imageUrl = imageUrl;
        this.isPackage = isPackage;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
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

    public boolean isPackage() {
        return isPackage;
    }

    public void setPackage(boolean aPackage) {
        isPackage = aPackage;
    }

    @NonNull
    @Override
    public String toString() {
        return "Id : "+id+"\nService Id: "+serviceId+"\nService Name: "+serviceName+"\nService Type: "+serviceType+"\nUnit: "+unit+"\nUnit Price: "+unitPrice+"\nAmount: "+amount+"\nImage Url: "+imageUrl+"\nIs Package: "+isPackage;
    }
}
