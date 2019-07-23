package com.kcirqueapps.laundryapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.kcirqueapps.laundryapp.R;
import com.kcirqueapps.laundryapp.database.model.Cart;
import com.kcirqueapps.laundryapp.database.viewmodel.CartViewModel;
import com.kcirqueapps.laundryapp.network.Model.ServicePackage;
import com.kcirqueapps.laundryapp.utils.BanglaUtils;

import es.dmoral.toasty.Toasty;

public class PackageDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_PACKAGE = "com.kcirqueapps.laundryapp.activity.EXTRA_PACKAGE";
    private ImageView serviceImageView, monthOneImageView, monthTwoImageView, monthThreeImageView;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private CardView monthOneCardView, monthTwoCardView, monthThreeCardView;
    private TextView monthOneQuantityTextView, monthOnePriceTextView, monthOneDurationTextView, monthTwoQuantityTextView, monthTwoPriceTextView, monthTwoDurationTextView,
            monthThreeQuantityTextView, monthThreePriceTextView, monthThreeDurationTextView, serviceTypeTextView;
    private Button subscribePackageBtn;

    private int selectPackage = 0;
    private ServicePackage servicePackage = null;

    private CartViewModel cartViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_details);

        init();

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        changBackArrow();

        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);

        monthTwoCardView.setOnClickListener(this);
        monthThreeCardView.setOnClickListener(this);
        monthOneCardView.setOnClickListener(this);
        subscribePackageBtn.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            servicePackage = bundle.getParcelable(EXTRA_PACKAGE);
            updateUI();
        }

    }


    private void init() {
        toolbar = findViewById(R.id.toolbar);
        appBarLayout = findViewById(R.id.app_bar);
        serviceImageView = findViewById(R.id.image_view);
        serviceTypeTextView = findViewById(R.id.service_type_text_view);
        monthOneCardView = findViewById(R.id.month_one_card_view);
        monthTwoCardView = findViewById(R.id.month_two_card_view);
        monthThreeCardView = findViewById(R.id.month_three_card_view);
        monthOneQuantityTextView = findViewById(R.id.month_one_quantity_text_view);
        monthOnePriceTextView = findViewById(R.id.month_one_price_text_view);
        monthOneDurationTextView = findViewById(R.id.month_one_duration_text_view);
        monthTwoQuantityTextView = findViewById(R.id.month_two_quantity_text_view);
        monthTwoPriceTextView = findViewById(R.id.month_two_price_text_view);
        monthTwoDurationTextView = findViewById(R.id.month_two_duration_text_view);
        monthThreeQuantityTextView = findViewById(R.id.month_three_quantity_text_view);
        monthThreePriceTextView = findViewById(R.id.month_three_price_text_view);
        monthThreeDurationTextView = findViewById(R.id.month_three_duration_text_view);
        monthOneImageView = findViewById(R.id.month_one_iron_image_view);
        monthTwoImageView = findViewById(R.id.month_two_iron_image_view);
        monthThreeImageView = findViewById(R.id.month_three_iron_image_view);
        subscribePackageBtn = findViewById(R.id.subscribe_package_btn);
    }

    private void updateUI() {
        if (servicePackage != null) {
            Glide.with(this).load(servicePackage.getImageUrl()).placeholder(R.drawable.placeholder).error(R.drawable.placeholder)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).into(serviceImageView);
            toolbar.setTitle(servicePackage.getName());
            toolbar.setTitleTextColor(Color.WHITE);
            serviceTypeTextView.setText(servicePackage.getServiceType());
            double price = BanglaUtils.toDouble(servicePackage.getAmount());
            int quantity = BanglaUtils.toInt(servicePackage.getUnit());

            monthOneQuantityTextView.setText(String.format("%s %s", servicePackage.getUnit(), getString(R.string.unit_text)));
            monthOnePriceTextView.setText(String.format("%s %s %s", getString(R.string.price_text), servicePackage.getAmount(), getString(R.string.tk_sign)));
            monthOneDurationTextView.setText(String.format("%s %s", getString(R.string.duration_text), servicePackage.getDuration()));

            monthTwoQuantityTextView.setText(String.format("%s %s", BanglaUtils.toString(quantity * 2), getString(R.string.unit_text)));
            monthTwoPriceTextView.setText(String.format("%s %s %s", getString(R.string.price_text), BanglaUtils.toString(price * 2), getString(R.string.tk_sign)));
            monthTwoDurationTextView.setText(String.format("%s %s %s", getString(R.string.duration_text), BanglaUtils.toString(2), getString(R.string.month_text)));

            monthThreeQuantityTextView.setText(String.format("%s %s", BanglaUtils.toString(quantity * 3), getString(R.string.unit_text)));
            monthThreePriceTextView.setText(String.format("%s %s %s", getString(R.string.price_text), BanglaUtils.toString(price * 3), getString(R.string.tk_sign)));
            monthThreeDurationTextView.setText(String.format("%s %s %s", getString(R.string.duration_text), BanglaUtils.toString(3), getString(R.string.month_text)));

            if (servicePackage.getServiceType().equals(getString(R.string.iron_price_text))) {
                monthOneImageView.setImageResource(R.drawable.ic_iron);
                monthTwoImageView.setImageResource(R.drawable.ic_iron);
                monthThreeImageView.setImageResource(R.drawable.ic_iron);
            } else if (servicePackage.getServiceType().equals(getString(R.string.wash_price_text))) {
                monthOneImageView.setImageResource(R.drawable.ic_washing);
                monthTwoImageView.setImageResource(R.drawable.ic_washing);
                monthThreeImageView.setImageResource(R.drawable.ic_washing);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.month_one_card_view:
                selectPackage = 1;
                selectedPackage(monthOneCardView, monthOneQuantityTextView, monthOnePriceTextView, monthOneDurationTextView);
                unselectedPackage(monthTwoCardView, monthTwoQuantityTextView, monthTwoPriceTextView, monthTwoDurationTextView);
                unselectedPackage(monthThreeCardView, monthThreeQuantityTextView, monthThreePriceTextView, monthThreeDurationTextView);
                break;
            case R.id.month_two_card_view:
                selectPackage = 2;
                unselectedPackage(monthOneCardView, monthOneQuantityTextView, monthOnePriceTextView, monthOneDurationTextView);
                selectedPackage(monthTwoCardView, monthTwoQuantityTextView, monthTwoPriceTextView, monthTwoDurationTextView);
                unselectedPackage(monthThreeCardView, monthThreeQuantityTextView, monthThreePriceTextView, monthThreeDurationTextView);
                break;
            case R.id.month_three_card_view:
                selectPackage = 3;
                unselectedPackage(monthOneCardView, monthOneQuantityTextView, monthOnePriceTextView, monthOneDurationTextView);
                unselectedPackage(monthTwoCardView, monthTwoQuantityTextView, monthTwoPriceTextView, monthTwoDurationTextView);
                selectedPackage(monthThreeCardView, monthThreeQuantityTextView, monthThreePriceTextView, monthThreeDurationTextView);
                break;
            case R.id.subscribe_package_btn:
                subscribePackage();
                break;
        }
    }

    private void subscribePackage() {
        if (selectPackage == 0) {
            Toasty.error(this, "Select Package First", Toast.LENGTH_SHORT).show();
            return;
        }
        double totalAmount = selectPackage * (BanglaUtils.toDouble(servicePackage.getAmount()));
        Cart cart = new Cart(servicePackage.getId(), servicePackage.getName(), servicePackage.getServiceType(), BanglaUtils.toString(selectPackage),
                servicePackage.getAmount(), BanglaUtils.toString(totalAmount), servicePackage.getImageUrl(), true);
        cartViewModel.insertCart(cart);
        Intent intent = new Intent(this, CartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void selectedPackage(CardView cardView, TextView quantityTextView, TextView priceTextView, TextView durationTextView) {
        cardView.setCardBackgroundColor(getResources().getColor(R.color.colorAccent));
        quantityTextView.setTextColor(Color.WHITE);
        priceTextView.setTextColor(Color.WHITE);
        durationTextView.setTextColor(Color.WHITE);
    }

    private void unselectedPackage(CardView cardView, TextView quantityTextView, TextView priceTextView, TextView durationTextView) {
        cardView.setCardBackgroundColor(Color.WHITE);
        quantityTextView.setTextColor(Color.DKGRAY);
        priceTextView.setTextColor(Color.DKGRAY);
        durationTextView.setTextColor(Color.DKGRAY);
    }

    private void changBackArrow() {
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

}
