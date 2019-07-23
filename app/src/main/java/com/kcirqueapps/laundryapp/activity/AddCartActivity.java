package com.kcirqueapps.laundryapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.andremion.counterfab.CounterFab;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.kcirqueapps.laundryapp.R;
import com.kcirqueapps.laundryapp.database.model.Cart;
import com.kcirqueapps.laundryapp.database.viewmodel.CartViewModel;
import com.kcirqueapps.laundryapp.network.Model.ServicePrice;
import com.kcirqueapps.laundryapp.utils.BanglaUtils;

import net.igenius.customcheckbox.CustomCheckBox;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class AddCartActivity extends AppCompatActivity implements View.OnClickListener, CustomCheckBox.OnCheckedChangeListener {

    public static final String EXTRA_SERVICE = "com.kcirqueapps.laundryapp.activity.EXTRA_SERVICE";
    private Toolbar toolbar;

    private ImageView serviceImageView;
    private CardView ironCardView, washCardView;
    private TextView ironPriceTextView, washPriceTextView, offerTextView, totalAmountTextView;
    private ElegantNumberButton quantityNumberBtn;
    private Button addToCartBtn;
    private CustomCheckBox ironCheckBox, washCheckBox;
    private CoordinatorLayout rootView;
    private CounterFab counterFab;

    private ServicePrice servicePrice;
    private double unitPrice = 0;
    private double totalAmount = 0;
    private int quantity;
    private List<String> services = new ArrayList<>();

    private CartViewModel cartViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cart);

        initView();
        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(getString(R.string.add_to_cart));
        changBackArrow();


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            servicePrice = bundle.getParcelable(EXTRA_SERVICE);
            if (servicePrice != null) {
                updateUI();
            }
        }

        ironCardView.setOnClickListener(this);
        washCardView.setOnClickListener(this);
        addToCartBtn.setOnClickListener(this);

        quantity = Integer.parseInt(quantityNumberBtn.getNumber());
        totalAmountTextView.setText(String.format("%s %s", BanglaUtils.toString(totalAmount), getResources().getString(R.string.tk_sign)));

        quantityNumberBtn.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                quantity = Integer.parseInt(view.getNumber());
                totalAmount = unitPrice * quantity;
                totalAmountTextView.setText(String.format("%s %s", BanglaUtils.toString(totalAmount), getResources().getString(R.string.tk_sign)));
            }
        });


        washCheckBox.setOnCheckedChangeListener(this);
        ironCheckBox.setOnCheckedChangeListener(this);

        cartViewModel.getCarts().observe(this, new Observer<List<Cart>>() {
            @Override
            public void onChanged(List<Cart> carts) {
                counterFab.setCount(carts.size());
            }
        });
    }

    private void updateUI() {
        Glide.with(this).load(servicePrice.getImageUrl()).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).into(serviceImageView);
        toolbar.setTitle(servicePrice.getCloth());
        washPriceTextView.setText(String.format("%s %s %s", getResources().getText(R.string.wash_price_text), servicePrice.getWashPrice(), getResources().getText(R.string.tk_sign)));
        ironPriceTextView.setText(String.format("%s %s %s", getResources().getText(R.string.iron_price_text), servicePrice.getIronPrice(), getResources().getText(R.string.tk_sign)));
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        serviceImageView = findViewById(R.id.image_view);
        ironCardView = findViewById(R.id.iron_card_view);
        washCardView = findViewById(R.id.wash_card_view);
        ironPriceTextView = findViewById(R.id.iron_price_text_view);
        washPriceTextView = findViewById(R.id.wash_price_text_view);
        offerTextView = findViewById(R.id.offer_text_view);
        quantityNumberBtn = findViewById(R.id.quantity_number_btn);
        addToCartBtn = findViewById(R.id.add_to_cart_btn);
        ironCheckBox = findViewById(R.id.iron_check_box);
        washCheckBox = findViewById(R.id.wash_check_box);
        totalAmountTextView = findViewById(R.id.total_amount_text_view);
        rootView = findViewById(R.id.root_view);
        counterFab = findViewById(R.id.counter_fab);
    }

    private void changBackArrow() {
        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
                Drawable upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_back, null);
                if (offset < -200) {
                    upArrow.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
                    getSupportActionBar().setHomeAsUpIndicator(upArrow);

                    Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_arrow_back);
                    drawable.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
                    toolbar.setOverflowIcon(drawable);
                } else {

                    upArrow.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
                    getSupportActionBar().setHomeAsUpIndicator(upArrow);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                    Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_arrow_back);
                    drawable.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
                    toolbar.setOverflowIcon(drawable);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wash_card_view:
                washCheckBox.setChecked(!washCheckBox.isChecked());
                break;
            case R.id.iron_card_view:
                ironCheckBox.setChecked(!ironCheckBox.isChecked());
                break;
            case R.id.add_to_cart_btn:
                String service = TextUtils.join(", ", services);
                if (service == null || service.isEmpty()) {
                    Toasty.error(this, "আপনার সার্ভিসটি নির্বাচন করুন", Toast.LENGTH_SHORT).show();
                    return;
                }
                Cart cart = new Cart(servicePrice.getId(), servicePrice.getCloth(), service, BanglaUtils.toString(quantity), BanglaUtils.toString(unitPrice), BanglaUtils.toString(totalAmount), servicePrice.getImageUrl(), false);
                cartViewModel.insertCart(cart);
                washCheckBox.setChecked(false, true);
                ironCheckBox.setChecked(false, true);
                Snackbar.make(rootView, "কার্টে যোগ করা হয়েছে!", Snackbar.LENGTH_LONG).setAction("কার্টে যান", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent cartIntent = new Intent(AddCartActivity.this, CartActivity.class);
                        cartIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(cartIntent);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        finish();
                    }
                }).show();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CustomCheckBox checkBox, boolean isChecked) {
        double price;
        switch (checkBox.getId()) {
            case R.id.wash_check_box:
                price = BanglaUtils.toDouble(servicePrice.getWashPrice());
                if (isChecked) {
                    unitPrice = unitPrice + price;
                    totalAmount = unitPrice * quantity;
                    totalAmountTextView.setText(String.format("%s %s", BanglaUtils.toString(totalAmount), getResources().getString(R.string.tk_sign)));
                    services.add(getString(R.string.wash_price_text));
                } else {
                    services.remove(getString(R.string.wash_price_text));
                    unitPrice = unitPrice - price;
                    totalAmount = unitPrice * quantity;
                    totalAmountTextView.setText(String.format("%s %s", BanglaUtils.toString(totalAmount), getResources().getString(R.string.tk_sign)));

                }
                break;
            case R.id.iron_check_box:
                price = BanglaUtils.toDouble(servicePrice.getIronPrice());
                if (isChecked) {
                    unitPrice = unitPrice + price;
                    totalAmount = unitPrice * quantity;
                    totalAmountTextView.setText(String.format("%s %s", BanglaUtils.toString(totalAmount), getResources().getString(R.string.tk_sign)));
                    services.add(getString(R.string.iron_price_text));
                } else {
                    unitPrice = unitPrice - price;
                    totalAmount = unitPrice * quantity;
                    totalAmountTextView.setText(String.format("%s %s", BanglaUtils.toString(totalAmount), getResources().getString(R.string.tk_sign)));
                    services.remove(getString(R.string.iron_price_text));
                }
                break;
        }
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
}
