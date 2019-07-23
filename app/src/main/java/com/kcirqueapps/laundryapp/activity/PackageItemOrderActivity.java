package com.kcirqueapps.laundryapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.kcirqueapps.laundryapp.R;
import com.kcirqueapps.laundryapp.adapter.PackageItemCartAdapter;
import com.kcirqueapps.laundryapp.network.Api.Api;
import com.kcirqueapps.laundryapp.network.Api.ApiClient;
import com.kcirqueapps.laundryapp.network.Model.HttpResponse;
import com.kcirqueapps.laundryapp.network.Model.PackageItem;
import com.kcirqueapps.laundryapp.network.Model.ServicePrice;
import com.kcirqueapps.laundryapp.network.Model.packageresponse.Order;
import com.kcirqueapps.laundryapp.utils.BanglaUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class PackageItemOrderActivity extends AppCompatActivity implements PackageItemCartAdapter.OnItemClickListener, View.OnClickListener {
    private CompositeDisposable disposable = new CompositeDisposable();
    public static final String EXTRA_ORDER = "com.kcirqueapps.laundryapp.activity.EXTRA_ORDER";
    public static final String EXTRA_ORDER_ITEM = "com.kcirqueapps.laundryapp.activity.EXTRA_ORDER_ITEM";
    private Toolbar toolbar;
    private RecyclerView itemRecycleView;
    private TextView collectionDateTextView, collectionTimeTextView, deliveryDateTextView, deliveryTimeTextView, noItemTextView;
    private Button confirmOrderBtn;
    private PackageItemCartAdapter adapter;
    private Order order;
    private ArrayList<ServicePrice> servicePriceList;

    private String[] collectionDays = BanglaUtils.collectionDays();
    private String[] deliveryDates;
    private String[] times = {"সকাল ০৯ঃ০০ থেকে ১১ঃ০০", "দুপুর ১১ঃ০০ থেকে ০২ঃ০০", "বিকাল ০৩ঃ০০ থেকে ০৫ঃ০০", "সন্ধ্যা ০৫ঃ০০ থেকে ০৭ঃ০০", "রাত ০৭ঃ০০ থেকে ৯ঃ০০"};

    private String collectionDate, collectionTime, deliveryDate, deliveryTime;
    private android.app.AlertDialog dialog;

    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_item_order);

        init();

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        itemRecycleView.setHasFixedSize(true);
        itemRecycleView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        collectionDateTextView.setOnClickListener(this);
        collectionTimeTextView.setOnClickListener(this);
        deliveryDateTextView.setOnClickListener(this);
        deliveryTimeTextView.setOnClickListener(this);
        confirmOrderBtn.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            order = bundle.getParcelable(EXTRA_ORDER);
            servicePriceList = bundle.getParcelableArrayList(EXTRA_ORDER_ITEM);
            adapter.setServicePriceList(servicePriceList);
            toggleNoData();
        }
    }

    private void toggleNoData() {
        if (servicePriceList.size() > 0) {
            noItemTextView.setVisibility(View.GONE);
        } else {
            noItemTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.collection_date_text_view:
                collectionDateDialog();
                break;
            case R.id.collection_time_text_view:
                if (collectionDate == null) {
                    Toasty.error(this, "প্রথমে সংগ্রহের দিন নির্বাচন করুন", Toast.LENGTH_SHORT).show();
                } else {
                    collectionTimeDialog();
                }
                break;
            case R.id.delivery_date_text_view:
                if (deliveryDates == null) {
                    Toasty.error(this, "প্রথমে সংগ্রহের দিন নির্বাচন করুন", Toast.LENGTH_SHORT).show();
                } else {
                    deliveryDateDialog();
                }
                break;
            case R.id.delivery_time_text_view:
                if (deliveryDate == null) {
                    Toasty.error(this, "প্রথমে সরবরাহের দিন নির্বাচন করুন", Toast.LENGTH_SHORT).show();
                } else {
                    deliveryTimeDialog();
                }
                break;
            case R.id.confirm_order_btn:
                if (isValid()) {
                    getDialog().show();
                    placeOrder();
                }
                break;
        }
    }

    private void placeOrder() {
        List<PackageItem> packageItems = new ArrayList<>();
        for (ServicePrice servicePrice : servicePriceList) {
            PackageItem item = new PackageItem(servicePrice.getId(), order.getId(), servicePrice.getUnit(), collectionDate, collectionTime, deliveryDate, deliveryTime);
            packageItems.add(item);
        }
        disposable.add(
                api.insertPackageItem(packageItems).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<HttpResponse>() {
                            @Override
                            public void onSuccess(HttpResponse httpResponse) {
                                getDialog().dismiss();
                                if (!httpResponse.isError()) {
                                    Toasty.success(PackageItemOrderActivity.this, httpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                    Intent mainIntent = new Intent(PackageItemOrderActivity.this, ProfileActivity.class);
                                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(mainIntent);
                                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                    finish();
                                } else {
                                    Toasty.error(PackageItemOrderActivity.this, httpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                getDialog().dismiss();
                                Toasty.error(PackageItemOrderActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
        );
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        itemRecycleView = findViewById(R.id.item_recycler_view);
        collectionDateTextView = findViewById(R.id.collection_date_text_view);
        collectionTimeTextView = findViewById(R.id.collection_time_text_view);
        deliveryDateTextView = findViewById(R.id.delivery_date_text_view);
        deliveryTimeTextView = findViewById(R.id.delivery_time_text_view);
        noItemTextView = findViewById(R.id.no_item_text_view);
        confirmOrderBtn = findViewById(R.id.confirm_order_btn);
        adapter = new PackageItemCartAdapter();
        api = ApiClient.getInstance().getApi();
    }

    @Override
    public void onItemClicked(ServicePrice servicePrice) {

    }

    @Override
    public void onRemoveClicked(ServicePrice servicePrice) {
        servicePriceList.remove(servicePrice);
        adapter.notifyDataSetChanged();
        toggleNoData();
    }

    private boolean isValid() {
        if (servicePriceList.size() == 0) {
            Toasty.error(this, getString(R.string.no_items_text), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (collectionDate == null) {
            Toasty.error(this, getString(R.string.error_collection_date_required), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (collectionTime == null) {
            Toasty.error(this, getString(R.string.error_collection_time_required), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (deliveryDate == null) {
            Toasty.error(this, getString(R.string.error_delivery_date_required), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (deliveryTime == null) {
            Toasty.error(this, getString(R.string.error_delivery_time_required), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void collectionDateDialog() {
        String collectionDateTitle = "সংগ্রহের দিন নির্বাচন করুন";
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle(collectionDateTitle)
                .setCancelable(false)
                .setItems(collectionDays, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        collectionDateTextView.setText(collectionDays[which]);
                        collectionDate = collectionDays[which];
                        if (collectionDate.equals("আজ")) {
                            collectionDate = BanglaUtils.todayDate();
                        } else if (collectionDate.equals("আগামিকাল")) {
                            collectionDate = BanglaUtils.tomorrowDate();
                        }
                        try {
                            deliveryDates = BanglaUtils.deliveryDays(collectionDays[which]);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = dialog.create();
        ListView listView = alertDialog.getListView();
        listView.setDivider(new ColorDrawable(Color.BLACK));
        alertDialog.show();
    }

    private void collectionTimeDialog() {
        String collectionTimeTitle = "সংগ্রহের সময় নির্বাচন করুন";
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(collectionTimeTitle)
                .setItems(times, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        collectionTimeTextView.setText(times[which]);
                        collectionTime = times[which];
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    private void deliveryDateDialog() {
        String deliveryDateTitle = "সরবরাহের দিন নির্বাচন করুন";
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(deliveryDateTitle)
                .setItems(deliveryDates, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deliveryDateTextView.setText(deliveryDates[which]);
                        deliveryDate = deliveryDates[which];
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    private void deliveryTimeDialog() {
        String deliveryTimeTitle = "সরবরাহের সময় নির্বাচন করুন";
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(deliveryTimeTitle)
                .setItems(times, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deliveryTimeTextView.setText(times[which]);
                        deliveryTime = times[which];
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private android.app.AlertDialog getDialog() {
        if (dialog == null)
            dialog = new SpotsDialog.Builder()
                    .setContext(this)
                    .setMessage("Loading...")
                    .setCancelable(false)
                    .build();
        return dialog;
    }


}
