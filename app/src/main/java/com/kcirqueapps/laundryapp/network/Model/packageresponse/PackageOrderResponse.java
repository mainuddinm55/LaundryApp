package com.kcirqueapps.laundryapp.network.Model.packageresponse;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PackageOrderResponse implements Parcelable {

    @SerializedName("order")
    @Expose
    private Order order;
    @SerializedName("items")
    @Expose
    private List<Item> items = new ArrayList<>();
    public final static Parcelable.Creator<PackageOrderResponse> CREATOR = new Creator<PackageOrderResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public PackageOrderResponse createFromParcel(Parcel in) {
            return new PackageOrderResponse(in);
        }

        public PackageOrderResponse[] newArray(int size) {
            return (new PackageOrderResponse[size]);
        }

    };

    protected PackageOrderResponse(Parcel in) {
        this.order = ((Order) in.readValue((Order.class.getClassLoader())));
        in.readList(this.items, (com.kcirqueapps.laundryapp.network.Model.orderresponse.Item.class.getClassLoader()));
    }

    /**
     * No args constructor for use in serialization
     */
    public PackageOrderResponse() {
    }

    /**
     * @param order
     * @param items
     */
    public PackageOrderResponse(Order order, List<Item> items) {
        super();
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

