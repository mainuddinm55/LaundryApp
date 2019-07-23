package com.kcirqueapps.laundryapp.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.EmptyResultSetException;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.kcirqueapps.laundryapp.R;
import com.kcirqueapps.laundryapp.adapter.CartAdapter;
import com.kcirqueapps.laundryapp.database.model.Cart;
import com.kcirqueapps.laundryapp.database.model.User;
import com.kcirqueapps.laundryapp.database.viewmodel.CartViewModel;
import com.kcirqueapps.laundryapp.database.viewmodel.UserViewModel;
import com.kcirqueapps.laundryapp.network.Api.Api;
import com.kcirqueapps.laundryapp.network.Api.ApiClient;
import com.kcirqueapps.laundryapp.network.Model.HttpResponse;
import com.kcirqueapps.laundryapp.utils.BanglaUtils;

import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class CartActivity extends AppCompatActivity implements View.OnClickListener, CartAdapter.OnItemClickListener {
    private static final int SIGN_IN_REQUEST_CODE = 200;
    private CompositeDisposable disposable = new CompositeDisposable();
    private Button placeOrderButton;
    private TextView addItemTextView, totalAmountTextView, noItemTextView;
    private RecyclerView itemsRecyclerView;
    private CartAdapter cartAdapter;
    private CartViewModel cartViewModel;
    private UserViewModel userViewModel;

    private double totalAmount;

    private List<Cart> cartList;
    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.your_items_text));

        initView();

        addItemTextView.setOnClickListener(this);
        placeOrderButton.setOnClickListener(this);

        itemsRecyclerView.setHasFixedSize(true);
        itemsRecyclerView.setAdapter(cartAdapter);
        cartAdapter.setOnItemClickListener(this);

        cartViewModel.getCarts().observe(this, new Observer<List<Cart>>() {
            @Override
            public void onChanged(List<Cart> carts) {
                cartAdapter.setCartList(carts);
                updateTotalAmount(carts);
                cartList = carts;
            }
        });

    }

    private void updateTotalAmount(List<Cart> carts) {
        totalAmount = 0;
        for (Cart cart : carts) {
            totalAmount = totalAmount + BanglaUtils.toDouble(cart.getAmount());
        }
        totalAmountTextView.setText(String.format("%s %s", getString(R.string.tk_sign), BanglaUtils.toString(totalAmount)));
        if (carts.size() > 0) {
            noItemTextView.setVisibility(View.GONE);
        } else {
            noItemTextView.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        addItemTextView = findViewById(R.id.add_item_text_view);
        totalAmountTextView = findViewById(R.id.total_amount_text_view);
        itemsRecyclerView = findViewById(R.id.items_recycler_view);
        noItemTextView = findViewById(R.id.no_item_text_view);
        placeOrderButton = findViewById(R.id.place_order_btn);
        cartAdapter = new CartAdapter();
        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        api = ApiClient.getInstance().getApi();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_item_text_view:
                Intent mainIntent = new Intent(this, AddItemActivity.class);
                startActivity(mainIntent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.place_order_btn:
                placeOrder();
                break;
        }
    }

    private void placeOrder() {
        disposable.add(userViewModel.getUser().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<User>() {
                    @Override
                    public void onSuccess(User user) {
                        if (user != null) {
                            if (cartList.size() > 0) {
                                Intent orderIntent = new Intent(CartActivity.this, PlaceOrderActivity.class);
                                startActivity(orderIntent);
                                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            } else {
                                Toast.makeText(CartActivity.this, getString(R.string.no_items_text), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Intent intent = new Intent(CartActivity.this, SignInActivity.class);
                            startActivityForResult(intent, SIGN_IN_REQUEST_CODE);
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof EmptyResultSetException) {
                            Intent intent = new Intent(CartActivity.this, SignInActivity.class);
                            startActivityForResult(intent, SIGN_IN_REQUEST_CODE);
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        }
                    }
                }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST_CODE && resultCode == RESULT_OK) {
            placeOrder();
        }
    }

    @Override
    public void onItemClicked(Cart cart) {
        cartViewModel.deleteCart(cart);
        Toasty.success(this, "আইটেমটি কার্ট থেকে মুছে ফেলা হয়েছে", Toast.LENGTH_SHORT).show();
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
