package com.kcirqueapps.laundryapp.fragmet;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.kcirqueapps.laundryapp.R;
import com.kcirqueapps.laundryapp.activity.AddCartActivity;
import com.kcirqueapps.laundryapp.activity.CartActivity;
import com.kcirqueapps.laundryapp.activity.PackageDetailsActivity;
import com.kcirqueapps.laundryapp.adapter.PackageAdapter;
import com.kcirqueapps.laundryapp.adapter.ServicePriceAdapter;
import com.kcirqueapps.laundryapp.database.model.Cart;
import com.kcirqueapps.laundryapp.database.viewmodel.CartViewModel;
import com.kcirqueapps.laundryapp.network.Api.Api;
import com.kcirqueapps.laundryapp.network.Api.ApiClient;
import com.kcirqueapps.laundryapp.network.Model.HttpResponse;
import com.kcirqueapps.laundryapp.network.Model.ServicePackage;
import com.kcirqueapps.laundryapp.network.Model.ServicePrice;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class PackageFragment extends Fragment implements PackageAdapter.OnItemClickListener {


    private Context context;

    public PackageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_package, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final TextView noItemTextView = view.findViewById(R.id.no_item_text_view);
        final ProgressBar progressBar = view.findViewById(R.id.progress_bar);

        CartViewModel cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        RelativeLayout rootView = view.findViewById(R.id.root_view);
        recyclerView.setHasFixedSize(true);
        final PackageAdapter packageAdapter = new PackageAdapter();
        recyclerView.setAdapter(packageAdapter);
        packageAdapter.setOnItemClickListener(this);

        Api api = ApiClient.getInstance().getApi();


        progressBar.setVisibility(View.VISIBLE);
        api.getAllPackages().enqueue(new Callback<HttpResponse<List<ServicePackage>>>() {
            @Override
            public void onResponse(@NonNull Call<HttpResponse<List<ServicePackage>>> call, @NonNull Response<HttpResponse<List<ServicePackage>>> response) {
                progressBar.setVisibility(View.GONE);
                assert response.body() != null;
                if (response.isSuccessful() && !response.body().isError()) {
                    packageAdapter.setServicePackageList(response.body().getResponse());
                    if (response.body().getResponse().size() > 0) {
                        noItemTextView.setVisibility(View.GONE);
                    } else {
                        noItemTextView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<HttpResponse<List<ServicePackage>>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onItemClicked(ServicePackage servicePackage) {
        startActivity(new Intent(context, PackageDetailsActivity.class)
                .putExtra(PackageDetailsActivity.EXTRA_PACKAGE, servicePackage));

    }
}
