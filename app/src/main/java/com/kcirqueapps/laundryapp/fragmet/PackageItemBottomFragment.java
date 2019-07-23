package com.kcirqueapps.laundryapp.fragmet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.kcirqueapps.laundryapp.R;
import com.kcirqueapps.laundryapp.adapter.PackageItemOrderAdapter;
import com.kcirqueapps.laundryapp.network.Api.Api;
import com.kcirqueapps.laundryapp.network.Api.ApiClient;
import com.kcirqueapps.laundryapp.network.Model.HttpResponse;
import com.kcirqueapps.laundryapp.network.Model.ServicePrice;

import net.igenius.customcheckbox.CustomCheckBox;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class PackageItemBottomFragment extends BottomSheetDialogFragment implements PackageItemOrderAdapter.OnItemClickedListener, View.OnClickListener {
    public static final String EXTRA_REMAINING_ITEM = "com.kcirqueapps.laundryapp.fragmet.EXTRA_REMAINING_ITEM";
    private CompositeDisposable disposable = new CompositeDisposable();

    private List<ServicePrice> servicePriceList = new ArrayList<>();
    private Context context;
    private BottomSheetListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }

    public PackageItemBottomFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_package_item_bottom, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView packageItemRecyclerView = view.findViewById(R.id.package_item_recycler_view);
        Button requestForPickupBtn = view.findViewById(R.id.request_for_pickup_btn);
        requestForPickupBtn.setOnClickListener(this);
        Api api = ApiClient.getInstance().getApi();
        Bundle bundle = getArguments();
        int remainingItem = 0;
        if (bundle != null) {
            remainingItem = bundle.getInt(EXTRA_REMAINING_ITEM);
        }
        final PackageItemOrderAdapter adapter = new PackageItemOrderAdapter(remainingItem);
        packageItemRecyclerView.setHasFixedSize(true);
        packageItemRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickedListener(this);
        disposable.add(
                api.getPackageItems()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<HttpResponse<List<ServicePrice>>>() {
                            @Override
                            public void onSuccess(HttpResponse<List<ServicePrice>> servicePrices) {
                                if (!servicePrices.isError()) {
                                    adapter.setServicePriceList(servicePrices.getResponse());
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }
                        })
        );
    }

    @Override
    public void onItemClicked(ServicePrice servicePrice) {

    }

    @Override
    public void onCheckedChanged(ServicePrice servicePrice, CustomCheckBox customCheckBox, boolean isChecked) {
        if (isChecked) {
            servicePriceList.add(servicePrice);
        } else {
            servicePriceList.remove(servicePrice);
        }
    }

    @Override
    public void onClick(View v) {
        if (servicePriceList.size() > 0) {
            mListener.onButtonClicked(servicePriceList);
            dismiss();
        } else {
            Toasty.error(context, "Please select item", Toast.LENGTH_SHORT).show();
        }
    }

    public interface BottomSheetListener {
        void onButtonClicked(List<ServicePrice> servicePriceList);
    }
}
