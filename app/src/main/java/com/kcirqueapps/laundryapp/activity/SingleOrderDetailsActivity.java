package com.kcirqueapps.laundryapp.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.kcirqueapps.laundryapp.R;
import com.kcirqueapps.laundryapp.adapter.OrderItemAdapter;
import com.kcirqueapps.laundryapp.network.Model.orderresponse.ServiceOrderResponse;

import java.util.ArrayList;
import java.util.List;

public class SingleOrderDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_ORDER = "com.kcirqueapps.laundryapp.activity.SingleOrderDetailsActivity.EXTRA_ORDER";
    private Toolbar toolbar;
    private TextView orderIdTextView, orderDateTextView, totalAmountTextView, collectionAddressTextView, collectionDateTextView, collectionTimeTextView,
            deliveryAddressTextView, deliveryDateTextView, deliveryTimeTextView, deliveryNoteTextView, deliveryStatusTextView, paymentStatusTextView, paymentMethodTextView;
    private OrderItemAdapter orderItemAdapter;
    private ServiceOrderResponse orderResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_order_details);

        init();
        toolbar.setTitle(getString(R.string.order_information));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
        orderDateTextView.setText(orderResponse.getOrder().getCollectionDate());
        totalAmountTextView.setText(String.format("%s %s %s", getString(R.string.total_text), getString(R.string.tk_sign), orderResponse.getOrder().getTotalAmount()));
        collectionAddressTextView.setText(String.format("%s %s", getString(R.string.address_text), orderResponse.getOrder().getAddressLine1()));
        collectionDateTextView.setText(String.format("%s %s", getString(R.string.date_text), orderResponse.getOrder().getCollectionDate()));
        collectionTimeTextView.setText(String.format("%s %s", getString(R.string.time_text), orderResponse.getOrder().getCollectionTime()));
        deliveryAddressTextView.setText(String.format("%s %s", getString(R.string.address_text), orderResponse.getOrder().getAddressLine2()));
        deliveryDateTextView.setText(String.format("%s %s", getString(R.string.date_text), orderResponse.getOrder().getDeliveryDate()));
        deliveryTimeTextView.setText(String.format("%s %s", getString(R.string.time_text), orderResponse.getOrder().getDeliveryTime()));
        deliveryNoteTextView.setText(orderResponse.getOrder().getDeliveryNote());
        deliveryStatusTextView.setText(orderResponse.getOrder().getDeliveryStatus());
        paymentStatusTextView.setText(orderResponse.getOrder().getPaymentStatus());
        paymentMethodTextView.setText(orderResponse.getOrder().getPaymentMethod());
        List<Object> itemList = new ArrayList<Object>(orderResponse.getItems());
        orderItemAdapter.setOrderItemList(itemList);
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        orderIdTextView = findViewById(R.id.order_id_text_view);
        orderDateTextView = findViewById(R.id.order_date_text_view);
        totalAmountTextView = findViewById(R.id.total_amount_text_view);
        collectionAddressTextView = findViewById(R.id.collection_address_text_view);
        collectionDateTextView = findViewById(R.id.collection_date_text_view);
        collectionTimeTextView = findViewById(R.id.collection_time_text_view);
        deliveryAddressTextView = findViewById(R.id.delivery_address_text_view);
        deliveryDateTextView = findViewById(R.id.delivery_date_text_view);
        deliveryTimeTextView = findViewById(R.id.delivery_time_text_view);
        deliveryNoteTextView = findViewById(R.id.delivery_note_text_view);
        deliveryStatusTextView = findViewById(R.id.delivery_status_text_view);
        paymentStatusTextView = findViewById(R.id.payment_status_text_view);
        paymentMethodTextView = findViewById(R.id.payment_method_text_view);
        RecyclerView itemRecyclerView = findViewById(R.id.item_recycler_view);
        orderItemAdapter = new OrderItemAdapter();
        itemRecyclerView.setHasFixedSize(true);
        itemRecyclerView.setAdapter(orderItemAdapter);
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
}
