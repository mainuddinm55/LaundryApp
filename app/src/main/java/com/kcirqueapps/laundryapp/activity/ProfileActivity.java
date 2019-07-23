package com.kcirqueapps.laundryapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andremion.counterfab.CounterFab;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.kcirqueapps.laundryapp.R;
import com.kcirqueapps.laundryapp.adapter.ViewPagerAdapter;
import com.kcirqueapps.laundryapp.database.model.Cart;
import com.kcirqueapps.laundryapp.database.model.User;
import com.kcirqueapps.laundryapp.database.viewmodel.CartViewModel;
import com.kcirqueapps.laundryapp.database.viewmodel.UserViewModel;
import com.kcirqueapps.laundryapp.fragmet.PackageOrderFragment;
import com.kcirqueapps.laundryapp.fragmet.ServiceOrderFragment;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ProfileActivity extends AppCompatActivity {
    private CompositeDisposable disposable = new CompositeDisposable();
    private ImageView profileImageView;
    private CircleImageView profileCircleImageView;
    private TextView nameTextView, mobileTextView, emailTextView, dobTextView;
    private UserViewModel userViewModel;
    private Toolbar toolbar;
    private CounterFab counterFab;
    private CartViewModel cartViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = findViewById(R.id.details_toolbar);
        setSupportActionBar(toolbar);
        setTitle(null);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        changBackArrow();

        disposable.add(
                userViewModel.getUser()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<User>() {
                            @Override
                            public void onSuccess(User user) {
                                if (user != null) {
                                    updateUI(user);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }
                        }));

        cartViewModel.getCarts().observe(this, new Observer<List<Cart>>() {
            @Override
            public void onChanged(List<Cart> carts) {
                counterFab.setCount(carts.size());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
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

    private void changBackArrow() {
        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
                Drawable upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_back, null);
                if (offset < -200) {
                    upArrow.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
                    getSupportActionBar().setHomeAsUpIndicator(upArrow);

                    Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_arrow_back);
                    drawable.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);
                    toolbar.setOverflowIcon(drawable);
                } else {

                    upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
                    getSupportActionBar().setHomeAsUpIndicator(upArrow);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                    Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_arrow_back);
                    drawable.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
                    toolbar.setOverflowIcon(drawable);
                }
            }
        });
    }

    private void updateUI(User user) {
        nameTextView.setText(user.getName());
        mobileTextView.setText(user.getMobile());
        emailTextView.setText(user.getEmail());
        dobTextView.setText(user.getDob());
        Glide.with(this).load(user.getImageUrl()).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(profileImageView);
        Glide.with(this).load(user.getImageUrl()).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(profileCircleImageView);
    }

    private void init() {
        profileImageView = findViewById(R.id.profile_image_view);
        profileCircleImageView = findViewById(R.id.user_profile_circle_image_view);
        nameTextView = findViewById(R.id.user_name_text_view);
        mobileTextView = findViewById(R.id.user_mobile_text_view);
        emailTextView = findViewById(R.id.email_text_view);
        dobTextView = findViewById(R.id.dob_text_view);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        counterFab = findViewById(R.id.counter_fab);
        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new ServiceOrderFragment(), getString(R.string.service_order_text));
        viewPagerAdapter.addFragment(new PackageOrderFragment(), getString(R.string.package_order_text));
        viewPager.setAdapter(viewPagerAdapter);
    }
}
