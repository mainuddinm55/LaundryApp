package com.kcirqueapps.laundryapp.fragmet;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.internal.bind.util.ISO8601Utils;
import com.kcirqueapps.laundryapp.R;
import com.kcirqueapps.laundryapp.activity.AddCartActivity;
import com.kcirqueapps.laundryapp.activity.MainActivity;
import com.kcirqueapps.laundryapp.activity.OfferItemActivity;
import com.kcirqueapps.laundryapp.adapter.OfferAdapter;
import com.kcirqueapps.laundryapp.adapter.PackageAdapter;
import com.kcirqueapps.laundryapp.adapter.ServicePriceAdapter;
import com.kcirqueapps.laundryapp.network.Api.Api;
import com.kcirqueapps.laundryapp.network.Api.ApiClient;
import com.kcirqueapps.laundryapp.network.Model.HttpResponse;
import com.kcirqueapps.laundryapp.network.Model.Offer;
import com.kcirqueapps.laundryapp.network.Model.OfferItem;
import com.kcirqueapps.laundryapp.network.Model.ServicePrice;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceFragment extends Fragment implements ServicePriceAdapter.OnItemClickListener, View.OnClickListener, OfferAdapter.OnItemClickListener {

    private CompositeDisposable disposable = new CompositeDisposable();
    private Context context;
    private TextView counterTextView;
    private MainActivity activity;

    public ServiceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        activity = (MainActivity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_service, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        RecyclerView offerRecyclerVew = view.findViewById(R.id.offer_recyclerView);
        final TextView noItemTextView = view.findViewById(R.id.no_item_text_view);
        final ProgressBar progressBar = view.findViewById(R.id.progress_bar);
        counterTextView = view.findViewById(R.id.counter_text_view);
        TextView seeMoreTextView = view.findViewById(R.id.see_more_text_view);
        seeMoreTextView.setOnClickListener(this);

        recyclerView.setHasFixedSize(true);
        offerRecyclerVew.setHasFixedSize(true);
        final OfferAdapter offerAdapter = new OfferAdapter();
        offerAdapter.setOnItemClickListener(this);
        offerRecyclerVew.setAdapter(offerAdapter);
        final ServicePriceAdapter adapter = new ServicePriceAdapter();
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);
        Api api = ApiClient.getInstance().getApi();
        api.getAllServicePrice().enqueue(new Callback<HttpResponse<List<ServicePrice>>>() {
            @Override
            public void onResponse(@NonNull Call<HttpResponse<List<ServicePrice>>> call, @NonNull Response<HttpResponse<List<ServicePrice>>> response) {
                progressBar.setVisibility(View.GONE);
                assert response.body() != null;
                if (response.isSuccessful() && !response.body().isError()) {
                    adapter.setServicePriceList(response.body().getResponse());
                    if (response.body().getResponse().size() > 0) {
                        noItemTextView.setVisibility(View.GONE);
                    } else {
                        noItemTextView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<HttpResponse<List<ServicePrice>>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });

        disposable.add(
                api.getCurrentOffer().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<HttpResponse<List<Offer>>>() {
                            @Override
                            public void onSuccess(HttpResponse<List<Offer>> listHttpResponse) {
                                if (listHttpResponse.getResponse().size() > 0) {
                                    Offer offer = listHttpResponse.getResponse().get(0);
                                    offerAdapter.setOfferItemList(offer.getItems(), offer.getItems().size() > 4 ? 4 : offer.getItems().size());
                                    try {
                                        setTimer(offer.getEndDate(), offer.getEndTime(), counterTextView);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
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
        Intent serviceIntent = new Intent(context, AddCartActivity.class);
        serviceIntent.putExtra(AddCartActivity.EXTRA_SERVICE, servicePrice);
        startActivity(serviceIntent);
        getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private void setTimer(String endDateStr, String endTime, final TextView counterTextView) throws ParseException {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
        Date endDate = ISO8601Utils.parse(endDateStr, new ParsePosition(0));
        String date = endDateStr.substring(0, 10);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.set(Calendar.HOUR, Integer.parseInt(endTime.substring(0, 2)));
        calendar.set(Calendar.MINUTE, Integer.parseInt(endTime.substring(3, 4)));
        Date currentDate = new Date();
        long diff = calendar.getTimeInMillis() - currentDate.getTime();
        CountDownTimer countDownTimer = new CountDownTimer(diff, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateCountDownText(millisUntilFinished, counterTextView);
            }

            @Override
            public void onFinish() {

            }
        };
        countDownTimer.start();
    }

    private void updateCountDownText(long diff, TextView counterTextView) {
        int hours = (int) ((diff / 1000) / 60) / 60;
        int minutes = (int) ((diff / 1000) / 60) % 60;
        int seconds = (int) (diff / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);

        counterTextView.setText(timeLeftFormatted);
    }

    @Override
    public void onClick(View v) {
        activity.viewPager.setCurrentItem(2);
    }

    @Override
    public void onItemClicked(OfferItem offerItem) {
        Intent intent = new Intent(context, OfferItemActivity.class);
        intent.putExtra(OfferItemActivity.EXTRA_OFFER_ITEM, offerItem);
        startActivity(intent);
    }
}
