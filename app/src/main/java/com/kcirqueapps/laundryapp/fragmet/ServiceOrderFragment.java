package com.kcirqueapps.laundryapp.fragmet;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.kcirqueapps.laundryapp.R;
import com.kcirqueapps.laundryapp.activity.SingleOrderDetailsActivity;
import com.kcirqueapps.laundryapp.adapter.OrderAdapter;
import com.kcirqueapps.laundryapp.database.model.User;
import com.kcirqueapps.laundryapp.database.viewmodel.UserViewModel;
import com.kcirqueapps.laundryapp.network.Api.Api;
import com.kcirqueapps.laundryapp.network.Api.ApiClient;
import com.kcirqueapps.laundryapp.network.Model.orderresponse.ServiceOrderResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceOrderFragment extends Fragment {

    private CompositeDisposable disposable = new CompositeDisposable();

    public ServiceOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_service_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        RecyclerView orderRecyclerView = view.findViewById(R.id.order_recycler_view);
        final ProgressBar progressBar = view.findViewById(R.id.progress_bar);
        final TextView noOrderTextView = view.findViewById(R.id.no_order_text_view);

        orderRecyclerView.setHasFixedSize(true);
        final OrderAdapter orderAdapter = new OrderAdapter();
        orderRecyclerView.setAdapter(orderAdapter);

        final Api api = ApiClient.getInstance().getApi();
        progressBar.setVisibility(View.VISIBLE);

        disposable.add(
                userViewModel.getUser()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<User>() {
                            @Override
                            public void onSuccess(User user) {
                                if (user != null) {
                                    disposable.add(
                                            api.getServiceOrders(user.getId())
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribeWith(new DisposableSingleObserver<List<ServiceOrderResponse>>() {
                                                        @Override
                                                        public void onSuccess(List<ServiceOrderResponse> serviceOrderResponses) {
                                                            progressBar.setVisibility(View.GONE);
                                                            List<Object> orderList = new ArrayList<Object>(serviceOrderResponses);
                                                            orderAdapter.setOrderList(orderList);
                                                            if (orderList.size() > 0) {
                                                                noOrderTextView.setVisibility(View.GONE);
                                                            } else {
                                                                noOrderTextView.setVisibility(View.VISIBLE);
                                                            }
                                                        }

                                                        @Override
                                                        public void onError(Throwable e) {
                                                            progressBar.setVisibility(View.GONE);
                                                            e.printStackTrace();
                                                        }
                                                    }));
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }
                        }));


        orderAdapter.setItemClickListener(new OrderAdapter.ItemClickListener() {
            @Override
            public void onItemClicked(Object object) {
                Intent intent = new Intent(getContext(), SingleOrderDetailsActivity.class);
                ServiceOrderResponse orderResponse = (ServiceOrderResponse) object;
                intent.putExtra(SingleOrderDetailsActivity.EXTRA_ORDER, orderResponse);
                startActivity(intent);
            }
        });
    }
}
