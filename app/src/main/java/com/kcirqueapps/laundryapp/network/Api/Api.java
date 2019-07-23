package com.kcirqueapps.laundryapp.network.Api;

import com.kcirqueapps.laundryapp.database.model.User;
import com.kcirqueapps.laundryapp.network.Model.HttpResponse;
import com.kcirqueapps.laundryapp.network.Model.Offer;
import com.kcirqueapps.laundryapp.network.Model.PackageItem;
import com.kcirqueapps.laundryapp.network.Model.ServiceOrder;
import com.kcirqueapps.laundryapp.network.Model.ServicePackage;
import com.kcirqueapps.laundryapp.network.Model.ServicePackageOrder;
import com.kcirqueapps.laundryapp.network.Model.ServicePrice;
import com.kcirqueapps.laundryapp.network.Model.orderresponse.ServiceOrderResponse;
import com.kcirqueapps.laundryapp.network.Model.packageresponse.PackageOrderResponse;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Api {
    @GET("service-price")
    Call<HttpResponse<List<ServicePrice>>> getAllServicePrice();

    @GET("service-package")
    Call<HttpResponse<List<ServicePackage>>> getAllPackages();

    @GET("service-package/package/items")
    Single<HttpResponse<List<ServicePrice>>> getPackageItems();

    @GET("user/{id}")
    Single<HttpResponse<User>> getUser(@Path("id") String id);

    @POST("user")
    Single<HttpResponse<User>> registerUser(
            @Body User user
    );

    @POST("service-order")
    Call<HttpResponse<ServiceOrder>> placeOrder(
            @Body ServiceOrder serviceOrder
    );

    @POST("service-package-order")
    Call<HttpResponse> placePackageOrder(
            @Body List<ServicePackageOrder> servicePackageOrders
    );

    @POST("service-package-order-details")
    Single<HttpResponse> insertPackageItem(@Body List<PackageItem> packageItems);

    @GET("order/{id}")
    Single<List<ServiceOrderResponse>> getServiceOrders(
            @Path("id") String userId
    );

    @GET("order/package/{id}")
    Single<List<PackageOrderResponse>> getPackageOrders(
            @Path("id") String userId
    );

    @GET("offer-item/current-offer")
    Single<HttpResponse<List<Offer>>> getCurrentOffer();

    @GET("offer-item/upcoming-offer")
    Single<HttpResponse<List<Offer>>> getUpcomingOffer();
}
