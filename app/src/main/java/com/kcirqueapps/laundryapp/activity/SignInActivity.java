package com.kcirqueapps.laundryapp.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.kcirqueapps.laundryapp.R;
import com.kcirqueapps.laundryapp.database.model.User;
import com.kcirqueapps.laundryapp.database.viewmodel.UserViewModel;
import com.kcirqueapps.laundryapp.network.Api.Api;
import com.kcirqueapps.laundryapp.network.Api.ApiClient;
import com.kcirqueapps.laundryapp.network.Model.HttpResponse;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "SignInActivity";
    private CompositeDisposable disposable = new CompositeDisposable();
    private static final int SIGN_IN_REQUEST_CODE = 100;
    public static final int INFO_REQUEST_CODE = 200;
    private UserViewModel userViewModel;
    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        api = ApiClient.getInstance().getApi();
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        Button loginBtn = findViewById(R.id.login_btn);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, AccountKitActivity.class);
                AccountKitConfiguration.AccountKitConfigurationBuilder builder =
                        new AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.PHONE,
                                AccountKitActivity.ResponseType.TOKEN);
                intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, builder.build());
                startActivityForResult(intent, SIGN_IN_REQUEST_CODE);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (loginResult.getError() != null) {
                Toasty.error(this, loginResult.getError().getErrorType().getMessage(), Toast.LENGTH_SHORT).show();
            } else if (loginResult.wasCancelled()) {
                Toasty.error(this, "Login Cancel", Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "onActivityResult: " + loginResult.getAccessToken().getAccountId());
                Toasty.success(this, "Login Success", Toast.LENGTH_SHORT).show();
                disposable.add(
                        api.getUser(loginResult.getAccessToken().getAccountId())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(new DisposableSingleObserver<HttpResponse<User>>() {
                                    @Override
                                    public void onSuccess(HttpResponse<User> userHttpResponse) {
                                        userViewModel.insertUser(userHttpResponse.getResponse());
                                        setResult(RESULT_OK);
                                        finish();
                                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        if (e instanceof HttpException && ((HttpException) e).code() == 400) {
                                            Intent intent = new Intent(SignInActivity.this, UserInfoActivity.class);
                                            startActivityForResult(intent, INFO_REQUEST_CODE);
                                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                        }
                                        e.printStackTrace();
                                    }
                                })
                );
            }
        } else if (requestCode == INFO_REQUEST_CODE && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        }
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
