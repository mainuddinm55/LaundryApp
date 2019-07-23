package com.kcirqueapps.laundryapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.kcirqueapps.laundryapp.network.Model.OfferItem;
import com.kcirqueapps.laundryapp.network.Model.ServicePrice;
import com.kcirqueapps.laundryapp.utils.BanglaUtils;

import net.igenius.customcheckbox.CustomCheckBox;

import java.util.ArrayList;
import java.util.List;

public class OfferItemActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_OFFER_ITEM = "com.kcirqueapps.laundryapp.activity.EXTRA_SERVICE";
    private Toolbar toolbar;

    private ImageView serviceImageView;
    private TextView regularPriceTextView, priceTextView, serviceTypeTextView, discountTextView, totalAmountTextView;
    private ElegantNumberButton quantityNumberBtn;
    private Button addToCartBtn;
    private CoordinatorLayout rootView;
    private CounterFab counterFab;

    private OfferItem offerItem;
    private double unitPrice = 0;
    private double totalAmount = 0;
    private int quantity = 1;

    private CartViewModel cartViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_item);

        initView();
        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(getString(R.string.add_to_cart));
        changBackArrow();

        addToCartBtn.setOnClickListener(this);
        counterFab.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            offerItem = bundle.getParcelable(EXTRA_OFFER_ITEM);
            if (offerItem != null) {
                updateUI();
            }
        }
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

        cartViewModel.getCarts().observe(this, new Observer<List<Cart>>() {
            @Override
            public void onChanged(List<Cart> carts) {
                counterFab.setCount(carts.size());
            }
        });
    }

    private void updateUI() {
        unitPrice = BanglaUtils.toDouble(offerItem.getActualPrice());
        Glide.with(this).load(offerItem.getImageUrl()).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).into(serviceImageView);
        toolbar.setTitle(offerItem.getCloth());
        regularPriceTextView.setText(String.format("%s %s", offerItem.getRegularPrice(), getResources().getText(R.string.tk_sign)));
        priceTextView.setText(String.format("%s %s", offerItem.getActualPrice(), getResources().getText(R.string.tk_sign)));
        serviceTypeTextView.setText(String.format("%s %s %s, %s", offerItem.getUnit(), getString(R.string.unit_text), offerItem.getCloth(), offerItem.getServiceType()));
        discountTextView.setText(String.format("%s%s", offerItem.getDiscountPercent(), getString(R.string.discount_text)));
        totalAmount = unitPrice * quantity;
        totalAmountTextView.setText(String.format("%s %s", BanglaUtils.toString(totalAmount), getResources().getString(R.string.tk_sign)));
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        serviceImageView = findViewById(R.id.image_view);
        regularPriceTextView = findViewById(R.id.regular_price_text_view);
        priceTextView = findViewById(R.id.price_text_view);
        discountTextView = findViewById(R.id.discount_text_view);
        serviceTypeTextView = findViewById(R.id.service_type_text_view);
        quantityNumberBtn = findViewById(R.id.quantity_number_btn);
        addToCartBtn = findViewById(R.id.add_to_cart_btn);
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_to_cart_btn) {
            String unit = BanglaUtils.toString(BanglaUtils.toInt(offerItem.getUnit()) * quantity);
            String unitPrice = BanglaUtils.toString(BanglaUtils.toDouble(offerItem.getActualPrice()) / BanglaUtils.toInt(offerItem.getUnit()));
            Cart cart = new Cart(offerItem.getServiceId(), offerItem.getCloth(), offerItem.getServiceType(), unit, unitPrice, BanglaUtils.toString(totalAmount), offerItem.getImageUrl(), false);
            cartViewModel.insertCart(cart);
            Snackbar.make(rootView, "কার্টে যোগ করা হয়েছে!", Snackbar.LENGTH_LONG).setAction("কার্টে যান", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent cartIntent = new Intent(OfferItemActivity    .this, CartActivity.class);
                    cartIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(cartIntent);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    finish();
                }
            }).show();
        }
    }
}
