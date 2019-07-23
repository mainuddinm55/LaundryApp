package com.kcirqueapps.laundryapp.network.Model.orderresponse;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceOrderResponse implements Parcelable {

    @SerializedName("order")
    @Expose
    private Order order;
    @SerializedName("items")
    @Expose
    private List<Item> items = new ArrayList<>();
    public final static Parcelable.Creator<ServiceOrderResponse> CREATOR = new Creator<ServiceOrderResponse>() {


        public ServiceOrderResponse createFromParcel(Parcel in) {
            return new ServiceOrderResponse(in);
        }

        public ServiceOrderResponse[] newArray(int size) {
            return (new ServiceOrderResponse[size]);
        }

    };

    private ServiceOrderResponse(Parcel in) {
        this.order = ((Order) in.readValue((Order.class.getClassLoader())));
        in.readList(this.items, (Item.class.getClassLoader()));
    }

    public ServiceOrderResponse(Order order, List<Item> items) {
        this.order = order;
        this.items = items;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(order);
        dest.writeList(items);
    }

    public int describeContents() {
        return 0;
    }

}

