package com.kcirqueapps.laundryapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kcirqueapps.laundryapp.R;
import com.kcirqueapps.laundryapp.adapter.AddItemAdapter;
import com.kcirqueapps.laundryapp.database.model.Cart;
import com.kcirqueapps.laundryapp.database.viewmodel.CartViewModel;
import com.kcirqueapps.laundryapp.network.Api.Api;
import com.kcirqueapps.laundryapp.network.Api.ApiClient;
import com.kcirqueapps.laundryapp.network.Model.HttpResponse;
import com.kcirqueapps.laundryapp.network.Model.ServicePrice;

import net.igenius.customcheckbox.CustomCheckBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddItemActivity extends AppCompatActivity implements AddItemAdapter.OnItemClickListener {
    private static final String TAG = "AddItemActivity";
    private List<Cart> cartList = new ArrayList<>();
    private CartViewModel cartViewModel;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.add_item));

        Api api = ApiClient.getInstance().getApi();
        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);

        RecyclerView itemRecyclerView = findViewById(R.id.item_recycler_view);
        final ProgressBar progressBar = findViewById(R.id.progress_bar);
        final TextView noDataTextView = findViewById(R.id.no_item_text_view);

        final AddItemAdapter addItemAdapter = new AddItemAdapter();
        itemRecyclerView.setHasFixedSize(true);
        itemRecyclerView.setAdapter(addItemAdapter);
        itemRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        addItemAdapter.setOnItemClickListener(this);

        progressBar.setVisibility(View.VISIBLE);

        api.getAllServicePrice().enqueue(new Callback<HttpResponse<List<ServicePrice>>>() {
            @Override
            public void onResponse(@NonNull Call<HttpResponse<List<ServicePrice>>> call, @NonNull Response<HttpResponse<List<ServicePrice>>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (!response.body().isError()) {
                        addItemAdapter.setServicePrices(response.body().getResponse());
                        if (response.body().getResponse().size() > 0) {
                            noDataTextView.setVisibility(View.GONE);
                        } else {
                            noDataTextView.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<HttpResponse<List<ServicePrice>>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onItemClicked(ServicePrice servicePackage, Cart cart) {

    }

    @Override
    public void onCheckedChanged(ServicePrice servicePackage, Cart cart, CustomCheckBox customCheckBox, boolean isChecked, int position) {
        Toast.makeText(this, cart.getAmount(), Toast.LENGTH_SHORT).show();
        if (isChecked) {
            cartList.add(cart);
        } else {
            for (Cart c : cartList) {
                if (cart.toString().equals(c.toString())) {
                    cartList.remove(c);
                }
            }

        }

        for (Cart c : cartList) {
            Log.e(TAG, "onCheckedChanged: " + position);
            Log.e(TAG, c.toString());
        }

    }

    @Override
    public void onError(String message) {
        Toasty.error(AddItemActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.action_add) {
            showDialog();
            for (Cart c : cartList) {
                cartViewModel.insertCart(c);
            }
            hideDialog();
            finish();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void showDialog() {
        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Loading...")
                .setCancelable(false)
                .build();
        dialog.show();
    }

    private void hideDialog() {
        if (dialog != null)
            dialog.dismiss();
    }
}
