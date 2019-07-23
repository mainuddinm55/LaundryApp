package com.kcirqueapps.laundryapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kcirqueapps.laundryapp.R;
import com.kcirqueapps.laundryapp.adapter.PackageItemAdapter;
import com.kcirqueapps.laundryapp.fragmet.PackageItemBottomFragment;
import com.kcirqueapps.laundryapp.network.Model.ServicePrice;
import com.kcirqueapps.laundryapp.network.Model.packageresponse.Item;
import com.kcirqueapps.laundryapp.network.Model.packageresponse.PackageOrderResponse;
import com.kcirqueapps.laundryapp.utils.BanglaUtils;

import java.util.ArrayList;
import java.util.List;

public class PackageOrderDetailsActivity extends AppCompatActivity implements PackageItemBottomFragment.BottomSheetListener {

    public static final String EXTRA_ORDER = "com.kcirqueapps.laundryapp.activity.PackageOrderDetailsActivity.EXTRA_ORDER";
    private TextView orderIdTextView, orderDateTextView, serviceTypeTextView, amountTextView, collectionAddressTextView,
            deliveryAddressTextView, dayRemainingTextView, itemRemainingTextView, paymentMethodTextView, paymentStatusTextView,
            startDateTextView, endDateTextView;
    private Toolbar toolbar;
    RecyclerView itemRecyclerView;
    private PackageItemAdapter adapter;
    private PackageOrderResponse orderResponse;
    private FloatingActionButton addFab;
    private int remainingItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_order_details);
        init();
        toolbar.setTitle(getString(R.string.order_information));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageItemBottomFragment bottomFragment = new PackageItemBottomFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(PackageItemBottomFragment.EXTRA_REMAINING_ITEM, remainingItem);
                bottomFragment.setArguments(bundle);
                bottomFragment.show(getSupportFragmentManager(), "bottom");
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            orderResponse = bundle.getParcelable(EXTRA_ORDER);
            if (orderResponse != null) {
                updateUI();
            }
        }
    }

    private void updateUI() {
        orderIdTextView.setText(String.format("%s%s", getString(R.string.order_text), orderResponse.getOrder().getId()));
        orderDateTextView.setText(orderResponse.getOrder().getStartDate());
        startDateTextView.setText(orderResponse.getOrder().getStartDate());
        endDateTextView.setText(orderResponse.getOrder().getEndDate());
        amountTextView.setText(String.format("%s %s %s", getString(R.string.total_text), getString(R.string.tk_sign), orderResponse.getOrder().getAmount()));
        collectionAddressTextView.setText(String.format("%s %s", getString(R.string.address_text), orderResponse.getOrder().getCollectionAddress()));
        deliveryAddressTextView.setText(String.format("%s %s", getString(R.string.address_text), orderResponse.getOrder().getDeliveryAddress()));
        paymentStatusTextView.setText(orderResponse.getOrder().getPaymentStatus());
        paymentMethodTextView.setText(orderResponse.getOrder().getPaymentMethod());
        serviceTypeTextView.setText(orderResponse.getOrder().getServiceType());
        itemRemainingTextView.setText(String.format("%s %s", itemRemaining(), getString(R.string.unit_text)));
        dayRemainingTextView.setText(String.format("%s %s", BanglaUtils.dayRemaining(orderResponse.getOrder().getEndDate()), getString(R.string.day_remaining_text)));
        itemRecyclerView.setAdapter(adapter);
        adapter.setItemList(orderResponse.getItems());
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        orderIdTextView = findViewById(R.id.order_id_text_view);
        startDateTextView = findViewById(R.id.start_date_text_view);
        endDateTextView = findViewById(R.id.end_date_text_view);
        orderDateTextView = findViewById(R.id.order_date_text_view);
        serviceTypeTextView = findViewById(R.id.service_type_text_view);
        amountTextView = findViewById(R.id.total_amount_text_view);
        collectionAddressTextView = findViewById(R.id.collection_address_text_view);
        deliveryAddressTextView = findViewById(R.id.delivery_address_text_view);
        dayRemainingTextView = findViewById(R.id.day_remaining_text_view);
        itemRemainingTextView = findViewById(R.id.item_remaining_text_view);
        paymentMethodTextView = findViewById(R.id.payment_method_text_view);
        paymentStatusTextView = findViewById(R.id.payment_status_text_view);
        itemRecyclerView = findViewById(R.id.item_recycler_view);
        addFab = findViewById(R.id.add_btn);
        adapter = new PackageItemAdapter();
        itemRecyclerView.setHasFixedSize(true);

    }

    private String itemRemaining() {
        if (orderResponse != null) {
            double totalItem = 0;
            for (Item item : orderResponse.getItems()) {
                totalItem = totalItem + BanglaUtils.toDouble(item.getUnit());
            }
            double remainingItem = BanglaUtils.toDouble(orderResponse.getOrder().getUnit()) - totalItem;
            this.remainingItem = (int) remainingItem;
            return BanglaUtils.toString(remainingItem);
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onButtonClicked(List<ServicePrice> servicePriceList) {
        Intent intent = new Intent(this, PackageItemOrderActivity.class);
        intent.putExtra(PackageItemOrderActivity.EXTRA_ORDER, orderResponse.getOrder());
        intent.putParcelableArrayListExtra(PackageItemOrderActivity.EXTRA_ORDER_ITEM, (ArrayList<ServicePrice>) servicePriceList);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }
}
