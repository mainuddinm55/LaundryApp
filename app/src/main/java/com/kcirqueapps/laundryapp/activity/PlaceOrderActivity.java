package com.kcirqueapps.laundryapp.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.kcirqueapps.laundryapp.R;
import com.kcirqueapps.laundryapp.database.model.Cart;
import com.kcirqueapps.laundryapp.database.model.User;
import com.kcirqueapps.laundryapp.database.viewmodel.CartViewModel;
import com.kcirqueapps.laundryapp.network.Api.Api;
import com.kcirqueapps.laundryapp.network.Api.ApiClient;
import com.kcirqueapps.laundryapp.network.Model.HttpResponse;

import com.kcirqueapps.laundryapp.network.Model.Item;
import com.kcirqueapps.laundryapp.network.Model.ServiceOrder;
import com.kcirqueapps.laundryapp.network.Model.ServicePackageOrder;
import com.kcirqueapps.laundryapp.utils.BanglaUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class PlaceOrderActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, TextWatcher {
    private static final int SIGN_IN_REQUEST_CODE = 200;
    private static final String TAG = "PlaceOrderActivity";
    private CompositeDisposable disposable = new CompositeDisposable();
    private String[] collectionDays = BanglaUtils.collectionDays();
    private String[] deliveryDates;
    private EditText collectionHomeEditText, collectionRoadEditText, collectionSectorEditText, collectionThanaEditText, collectionUpazilaEditText,
            collectionDistrictEditText, deliveryHomeEditText, deliveryRoadEditText, deliverySectorEditText, deliveryThanaEditText, deliveryUpazilaEditText,
            deliveryDistrictEditText, deliveryNoteEditText;
    private TextView collectionDateTextView, collectionTimeTextView, deliveryDateTextView, deliveryTimeTextView, totalAmountTextView;
    private CheckBox sameAsCollectionAddressCheckbox;
    private Button confirmOrderBtn;

    private String[] times = {"সকাল ০৯ঃ০০ থেকে ১১ঃ০০", "দুপুর ১১ঃ০০ থেকে ০২ঃ০০", "বিকাল ০৩ঃ০০ থেকে ০৫ঃ০০", "সন্ধ্যা ০৫ঃ০০ থেকে ০৭ঃ০০", "রাত ০৭ঃ০০ থেকে ৯ঃ০০"};

    private String collectionDate, collectionTime, deliveryDate, deliveryTime;

    private CartViewModel cartViewModel;
    private Api api;
    private double totalServiceAmount;

    private List<Item> serviceList = new ArrayList<>();
    private List<ServicePackageOrder> packageList = new ArrayList<>();
    private List<Cart> cartList;

    private Account currentUser;
    private android.app.AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.order_text));

        initView();

        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {

            @Override
            public void onSuccess(Account account) {
                currentUser = account;

            }

            @Override
            public void onError(AccountKitError accountKitError) {

            }
        });

        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        api = ApiClient.getInstance().getApi();


        collectionDateTextView.setOnClickListener(this);
        collectionTimeTextView.setOnClickListener(this);
        deliveryDateTextView.setOnClickListener(this);
        deliveryTimeTextView.setOnClickListener(this);
        confirmOrderBtn.setOnClickListener(this);

        sameAsCollectionAddressCheckbox.setOnCheckedChangeListener(this);

        collectionHomeEditText.addTextChangedListener(this);
        collectionRoadEditText.addTextChangedListener(this);
        collectionSectorEditText.addTextChangedListener(this);
        collectionThanaEditText.addTextChangedListener(this);
        collectionUpazilaEditText.addTextChangedListener(this);
        collectionDistrictEditText.addTextChangedListener(this);
        deliveryHomeEditText.addTextChangedListener(this);
        deliveryRoadEditText.addTextChangedListener(this);
        deliverySectorEditText.addTextChangedListener(this);
        deliveryThanaEditText.addTextChangedListener(this);
        deliveryUpazilaEditText.addTextChangedListener(this);
        deliveryDistrictEditText.addTextChangedListener(this);

        cartViewModel.getCarts().observe(this, new Observer<List<Cart>>() {
            @Override
            public void onChanged(List<Cart> carts) {
                updateTotalAmount(carts);
                cartList = carts;
            }
        });


    }

    private void updateTotalAmount(List<Cart> carts) {
        double totalAmount = 0;
        totalServiceAmount = 0;
        for (Cart cart : carts) {
            totalAmount = totalAmount + BanglaUtils.toDouble(cart.getAmount());
        }
        totalAmountTextView.setText(String.format("%s %s", getString(R.string.tk_sign), BanglaUtils.toString(totalAmount)));
        packageList.clear();
        serviceList.clear();
        for (Cart cart : carts) {
            if (!cart.isPackage()) {
                serviceList.add(new Item(cart.getServiceId(), cart.getServiceType(), cart.getUnit(), cart.getUnitPrice(), cart.getAmount()));
                totalServiceAmount = totalServiceAmount + BanglaUtils.toDouble(cart.getAmount());
            }
        }
    }

    private void initView() {
        collectionHomeEditText = findViewById(R.id.collection_home_edit_text);
        collectionRoadEditText = findViewById(R.id.collection_road_edit_text);
        collectionSectorEditText = findViewById(R.id.collection_sector_edit_text);
        collectionThanaEditText = findViewById(R.id.collection_thana_edit_text);
        collectionUpazilaEditText = findViewById(R.id.collection_upazila_edit_text);
        collectionDistrictEditText = findViewById(R.id.collection_district_edit_text);
        deliveryHomeEditText = findViewById(R.id.delivery_home_edit_text);
        deliveryRoadEditText = findViewById(R.id.delivery_road_edit_text);
        deliverySectorEditText = findViewById(R.id.delivery_sector_edit_text);
        deliveryThanaEditText = findViewById(R.id.delivery_thana_edit_text);
        deliveryUpazilaEditText = findViewById(R.id.delivery_upazila_edit_text);
        deliveryDistrictEditText = findViewById(R.id.delivery_district_edit_text);
        deliveryNoteEditText = findViewById(R.id.delivery_note_edit_text);
        collectionDateTextView = findViewById(R.id.collection_date_text_view);
        collectionTimeTextView = findViewById(R.id.collection_time_text_view);
        deliveryDateTextView = findViewById(R.id.delivery_date_text_view);
        deliveryTimeTextView = findViewById(R.id.delivery_time_text_view);
        sameAsCollectionAddressCheckbox = findViewById(R.id.same_as_collection_address_check_box);
        confirmOrderBtn = findViewById(R.id.confirm_order_btn);
        totalAmountTextView = findViewById(R.id.total_amount_text_view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.collection_date_text_view:
                collectionDateDialog();
                break;
            case R.id.collection_time_text_view:
                if (collectionDate == null) {
                    Toasty.error(this, "প্রথমে সংগ্রহের দিন নির্বাচন করুন", Toast.LENGTH_SHORT).show();
                } else {
                    collectionTimeDialog();
                }
                break;
            case R.id.delivery_date_text_view:
                if (deliveryDates == null) {
                    Toasty.error(this, "প্রথমে সংগ্রহের দিন নির্বাচন করুন", Toast.LENGTH_SHORT).show();
                } else {
                    deliveryDateDialog();
                }
                break;
            case R.id.delivery_time_text_view:
                if (deliveryDate == null) {
                    Toasty.error(this, "প্রথমে সরবরাহের দিন নির্বাচন করুন", Toast.LENGTH_SHORT).show();
                } else {
                    deliveryTimeDialog();
                }
                break;
            case R.id.confirm_order_btn:
                if (isValid()) {
                    getDialog().show();
                    placeOrder();
                }
                break;
        }
    }

    private void placeOrder() {
        final String collectionAddress = collectionHomeEditText.getText().toString() + ", " + collectionRoadEditText.getText().toString() + ", " + collectionSectorEditText.getText().toString() +
                ", " + collectionThanaEditText.getText().toString() + ", " + collectionUpazilaEditText.getText().toString() + ", " + collectionDistrictEditText.getText().toString();
        final String deliveryAddress = deliveryHomeEditText.getText().toString() + ", " + deliveryRoadEditText.getText().toString() + ", " + deliverySectorEditText.getText().toString() +
                ", " + deliveryThanaEditText.getText().toString() + ", " + deliveryUpazilaEditText.getText().toString() + ", " + deliveryDistrictEditText.getText().toString();
        for (Cart cart : cartList) {
            if (cart.isPackage()) {
                packageList.add(new ServicePackageOrder(cart.getServiceId(), currentUser.getId(), BanglaUtils.todayDate(), collectionAddress, deliveryAddress,
                        collectionDate, BanglaUtils.packageEndDate(collectionDate), cart.getAmount(), getString(R.string.cash_on_delivery_text)));
            }
        }

        if (currentUser != null) {
            final ServiceOrder serviceOrder = new ServiceOrder(
                    currentUser.getId(), BanglaUtils.todayDate(), collectionAddress, deliveryAddress,
                    deliveryNoteEditText.getText().toString(), collectionDate,
                    collectionTime, deliveryDate, deliveryTime, BanglaUtils.toString(totalServiceAmount),
                    getString(R.string.cash_on_delivery_text), "", "", serviceList
            );

            disposable.add(
                    Observable.create(new ObservableOnSubscribe<Object>() {
                        @Override
                        public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                            api.placeOrder(serviceOrder).execute();
                            api.placePackageOrder(packageList).execute();
                            emitter.onComplete();
                        }
                    }).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<Object>() {
                                @Override
                                public void accept(Object o) throws Exception {
                                    getDialog().dismiss();
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    getDialog().dismiss();
                                }
                            }, new Action() {
                                @Override
                                public void run() throws Exception {
                                    Log.e("", "run: ");
                                    getDialog().dismiss();
                                    cartViewModel.deleteAllCart();
                                    Toasty.success(PlaceOrderActivity.this, "Order placed", Toast.LENGTH_SHORT).show();
                                    Intent cartIntent = new Intent(PlaceOrderActivity.this, MainActivity.class);
                                    cartIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(cartIntent);
                                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                    finish();
                                }
                            })
            );
        }

    }

    private boolean isValid() {
        if (TextUtils.isEmpty(collectionHomeEditText.getText())) {
            collectionHomeEditText.setError(getString(R.string.error_home_required));
            return false;
        }
        if (TextUtils.isEmpty(collectionRoadEditText.getText())) {
            collectionRoadEditText.setError(getString(R.string.error_road_required));
            return false;
        }
        if (TextUtils.isEmpty(collectionSectorEditText.getText())) {
            collectionSectorEditText.setError(getString(R.string.error_sector_required));
            return false;
        }
        if (TextUtils.isEmpty(collectionThanaEditText.getText())) {
            collectionThanaEditText.setError(getString(R.string.error_thana_required));
            return false;
        }
        if (TextUtils.isEmpty(collectionUpazilaEditText.getText())) {
            collectionUpazilaEditText.setError(getString(R.string.error_upazila_required));
            return false;
        }
        if (TextUtils.isEmpty(collectionDistrictEditText.getText())) {
            collectionDistrictEditText.setError(getString(R.string.error_district_required));
            return false;
        }
        if (TextUtils.isEmpty(deliveryHomeEditText.getText())) {
            deliveryHomeEditText.setError(getString(R.string.error_home_required));
            return false;
        }
        if (TextUtils.isEmpty(deliveryRoadEditText.getText())) {
            deliveryRoadEditText.setError(getString(R.string.error_road_required));
            return false;
        }
        if (TextUtils.isEmpty(deliverySectorEditText.getText())) {
            deliverySectorEditText.setError(getString(R.string.error_sector_required));
            return false;
        }
        if (TextUtils.isEmpty(deliveryThanaEditText.getText())) {
            deliveryThanaEditText.setError(getString(R.string.error_thana_required));
            return false;
        }
        if (TextUtils.isEmpty(deliveryUpazilaEditText.getText())) {
            deliveryUpazilaEditText.setError(getString(R.string.error_upazila_required));
            return false;
        }
        if (TextUtils.isEmpty(deliveryDistrictEditText.getText())) {
            deliveryDistrictEditText.setError(getString(R.string.error_district_required));
            return false;
        }
        if (collectionDate == null) {
            Toasty.error(this, getString(R.string.error_collection_date_required), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (collectionTime == null) {
            Toasty.error(this, getString(R.string.error_collection_time_required), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (deliveryDate == null) {
            Toasty.error(this, getString(R.string.error_delivery_date_required), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (deliveryTime == null) {
            Toasty.error(this, getString(R.string.error_delivery_time_required), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void collectionDateDialog() {
        String collectionDateTitle = "সংগ্রহের দিন নির্বাচন করুন";
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle(collectionDateTitle)
                .setCancelable(false)
                .setItems(collectionDays, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        collectionDateTextView.setText(collectionDays[which]);
                        collectionDate = collectionDays[which];
                        if (collectionDate.equals("আজ")) {
                            collectionDate = BanglaUtils.todayDate();
                        } else if (collectionDate.equals("আগামিকাল")) {
                            collectionDate = BanglaUtils.tomorrowDate();
                        }
                        try {
                            deliveryDates = BanglaUtils.deliveryDays(collectionDays[which]);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = dialog.create();
        ListView listView = alertDialog.getListView();
        listView.setDivider(new ColorDrawable(Color.BLACK));
        alertDialog.show();
    }

    private void collectionTimeDialog() {
        String collectionTimeTitle = "সংগ্রহের সময় নির্বাচন করুন";
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(collectionTimeTitle)
                .setItems(times, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        collectionTimeTextView.setText(times[which]);
                        collectionTime = times[which];
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    private void deliveryDateDialog() {
        String deliveryDateTitle = "সরবরাহের দিন নির্বাচন করুন";
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(deliveryDateTitle)
                .setItems(deliveryDates, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deliveryDateTextView.setText(deliveryDates[which]);
                        deliveryDate = deliveryDates[which];
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    private void deliveryTimeDialog() {
        String deliveryTimeTitle = "সরবরাহের সময় নির্বাচন করুন";
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(deliveryTimeTitle)
                .setItems(times, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deliveryTimeTextView.setText(times[which]);
                        deliveryTime = times[which];
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            deliveryHomeEditText.setText(collectionHomeEditText.getText());
            deliveryRoadEditText.setText(collectionRoadEditText.getText());
            deliverySectorEditText.setText(collectionSectorEditText.getText());
            deliveryThanaEditText.setText(collectionThanaEditText.getText());
            deliveryUpazilaEditText.setText(collectionUpazilaEditText.getText());
            deliveryDistrictEditText.setText(collectionDistrictEditText.getText());
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        collectionHomeEditText.setError(null);
        collectionRoadEditText.setError(null);
        collectionSectorEditText.setError(null);
        collectionThanaEditText.setError(null);
        collectionUpazilaEditText.setError(null);
        collectionDistrictEditText.setError(null);
        deliveryHomeEditText.setError(null);
        deliveryRoadEditText.setError(null);
        deliverySectorEditText.setError(null);
        deliveryThanaEditText.setError(null);
        deliveryUpazilaEditText.setError(null);
        deliveryDistrictEditText.setError(null);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private android.app.AlertDialog getDialog() {
        if (dialog == null)
            dialog = new SpotsDialog.Builder()
                    .setContext(this)
                    .setMessage("Loading...")
                    .setCancelable(false)
                    .build();
        return dialog;
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
