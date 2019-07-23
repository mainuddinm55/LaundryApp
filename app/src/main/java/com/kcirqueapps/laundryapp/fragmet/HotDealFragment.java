package com.kcirqueapps.laundryapp.fragmet;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.TimeUtils;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.kcirqueapps.laundryapp.R;
import com.kcirqueapps.laundryapp.adapter.OfferAdapter;
import com.kcirqueapps.laundryapp.network.Api.Api;
import com.kcirqueapps.laundryapp.network.Api.ApiClient;
import com.kcirqueapps.laundryapp.network.Model.HttpResponse;
import com.kcirqueapps.laundryapp.network.Model.Offer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class HotDealFragment extends Fragment {
    private static final String TAG = "HotDealFragment";
    private CompositeDisposable disposable = new CompositeDisposable();
    private TextView counterTextView;
    private TextView upcomingCounterTextView;
    private ImageView offerImageView;
    private ImageView upcomingOfferImageView;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public HotDealFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hot_deal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView itemRecyclerView = view.findViewById(R.id.item_recycler_view);
        counterTextView = view.findViewById(R.id.counter_text_view);
        offerImageView = view.findViewById(R.id.offer_image_view);
        upcomingCounterTextView = view.findViewById(R.id.upcoming_text_view);
        upcomingOfferImageView = view.findViewById(R.id.upcoming_offer_image_view);
        itemRecyclerView.setHasFixedSize(true);
        final OfferAdapter offerAdapter = new OfferAdapter();
        itemRecyclerView.setAdapter(offerAdapter);

        Api api = ApiClient.getInstance().getApi();

        disposable.add(
                api.getCurrentOffer().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<HttpResponse<List<Offer>>>() {
                            @Override
                            public void onSuccess(HttpResponse<List<Offer>> listHttpResponse) {
                                if (listHttpResponse.getResponse().size() > 0) {
                                    Offer offer = listHttpResponse.getResponse().get(0);
                                    offerAdapter.setOfferItemList(offer.getItems(),offer.getItems().size());
                                    try {
                                        setTimer(offer.getEndDate(), offer.getEndTime(), counterTextView);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    Glide.with(context).load(offer.getImageUrl() == null ? "" : offer.getImageUrl()).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                                            .placeholder(R.drawable.offer).error(R.drawable.offer).into(offerImageView);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }
                        }));
        disposable.add(
                api.getUpcomingOffer().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<HttpResponse<List<Offer>>>() {
                            @Override
                            public void onSuccess(HttpResponse<List<Offer>> listHttpResponse) {
                                if (listHttpResponse.getResponse().size() > 0) {
                                    Offer offer = listHttpResponse.getResponse().get(0);

                                    try {
                                        setTimer(offer.getStartDate(), offer.getStartTime(), upcomingCounterTextView);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    Glide.with(context).load(offer.getImageUrl() == null ? "" : offer.getImageUrl()).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                                            .placeholder(R.drawable.offer).error(R.drawable.offer).into(upcomingOfferImageView);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }
                        })
        );
    }

    private void setTimer(String endDateStr, String endTime, final TextView counterTextView) throws ParseException {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);

        String date = endDateStr.substring(0, 10);
        String s = date + " " + endTime;
        Date endDate = dateFormat.parse(s);
        Date currentDate = new Date();
        long diff = endDate.getTime() - currentDate.getTime();
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
}
