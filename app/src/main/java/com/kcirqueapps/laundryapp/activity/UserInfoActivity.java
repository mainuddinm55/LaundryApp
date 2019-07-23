package com.kcirqueapps.laundryapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.kcirqueapps.laundryapp.R;
import com.kcirqueapps.laundryapp.database.model.User;
import com.kcirqueapps.laundryapp.database.viewmodel.UserViewModel;
import com.kcirqueapps.laundryapp.network.Api.Api;
import com.kcirqueapps.laundryapp.network.Api.ApiClient;
import com.kcirqueapps.laundryapp.network.Model.HttpResponse;

import java.util.Calendar;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener, TextWatcher {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Api api;

    private TextInputLayout nameLayout, emailLayout;
    private TextInputEditText nameEditText, emailEditText;
    private RadioGroup genderRadioGroup;
    private TextView dobTextView;
    private Button updateBtn;

    private String gender = "Male";
    private String dob = null;

    private Calendar calendar = Calendar.getInstance();
    private UserViewModel userViewModel;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        init();
        api = ApiClient.getInstance().getApi();
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        dobTextView.setOnClickListener(this);
        updateBtn.setOnClickListener(this);

        genderRadioGroup.setOnCheckedChangeListener(this);

        emailEditText.addTextChangedListener(this);
        nameEditText.addTextChangedListener(this);
    }

    private void init() {
        nameEditText = findViewById(R.id.first_name_edit_text);
        nameLayout = findViewById(R.id.first_name_layout);
        emailLayout = findViewById(R.id.email_layout);
        emailEditText = findViewById(R.id.email_edit_text);
        genderRadioGroup = findViewById(R.id.gender_radio_group);
        dobTextView = findViewById(R.id.birthday_text_view);
        updateBtn = findViewById(R.id.update_btn);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.birthday_text_view:
                showDobDialog();
                break;
            case R.id.update_btn:
                updateUserInfo();
                break;
        }
    }

    private void updateUserInfo() {
        if (isValid()) {
            showDialog();
            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                @Override
                public void onSuccess(final Account account) {
                    User user = new User(account.getId(), nameEditText.getText().toString(),
                            emailEditText.getText().toString(), account.getPhoneNumber().toString(),
                            dob, gender, "");
                    compositeDisposable.add(
                            api.registerUser(user)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeWith(new DisposableSingleObserver<HttpResponse<User>>() {
                                        @Override
                                        public void onSuccess(HttpResponse<User> userHttpResponse) {
                                            hideDialog();
                                            userViewModel.insertUser(userHttpResponse.getResponse());
                                            setResult(RESULT_OK);
                                            finish();
                                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            hideDialog();
                                            e.printStackTrace();
                                        }
                                    })
                    );
                }

                @Override
                public void onError(AccountKitError accountKitError) {
                    hideDialog();
                }
            });

        }
    }

    private void showDialog() {
        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setCancelable(false)
                .setMessage("Loading....")
                .build();
        dialog.show();
    }

    private void hideDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private void showDobDialog() {
        calendar.add(Calendar.YEAR, -10);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (month >= 9) {
                    if (dayOfMonth > 9) {
                        dob = dayOfMonth + "-" + (month + 1) + "-" + year;
                    } else {
                        dob = "0" + dayOfMonth + "-" + (month + 1) + "-" + year;
                    }
                } else {
                    if (dayOfMonth > 9) {
                        dob = dayOfMonth + "-0" + (month + 1) + "-" + year;
                    } else {
                        dob = "0" + dayOfMonth + "-0" + (month + 1) + "-" + year;
                    }
                }
                dobTextView.setText(dob);
            }
        }, year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private boolean isValid() {
        if (TextUtils.isEmpty(nameEditText.getText())) {
            nameLayout.setError("Name required");
            return false;
        }
        if (TextUtils.isEmpty(emailEditText.getText())) {
            emailLayout.setError("Email required");
            return false;
        }
        if (dob == null) {
            Toast.makeText(this, "Select your date of birth", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton radioButton = findViewById(checkedId);
        gender = radioButton.getText().toString();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        emailLayout.setError(null);
        nameLayout.setError(null);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
