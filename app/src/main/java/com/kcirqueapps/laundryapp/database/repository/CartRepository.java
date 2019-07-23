package com.kcirqueapps.laundryapp.database.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.kcirqueapps.laundryapp.database.AppDatabase;
import com.kcirqueapps.laundryapp.database.dao.CartDao;
import com.kcirqueapps.laundryapp.database.model.Cart;

import java.util.List;

public class CartRepository {
    private static CartDao cartDao;

    public CartRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        cartDao = database.getCartDao();
    }

    public void insertCart(Cart cart) {
        new InsertTask().execute(cart);
    }


    public void updateCart(Cart cart) {
        new UpdateTask().execute(cart);
    }

    public void deleteCart(Cart cart) {
        new DeleteTask().execute(cart);
    }

    public LiveData<List<Cart>> getCarts() {
        return cartDao.getCarts();
    }

    public void deleteAllCart() {
        new DeleteAllTask().execute();
    }

    private static class InsertTask extends AsyncTask<Cart, Void, Long> {

        @Override
        protected Long doInBackground(Cart... carts) {
            return cartDao.insertCart(carts[0]);
        }
    }

    private static class UpdateTask extends AsyncTask<Cart, Void, Integer> {

        @Override
        protected Integer doInBackground(Cart... carts) {
            return cartDao.updateCart(carts[0]);
        }
    }

    private static class DeleteTask extends AsyncTask<Cart, Void, Integer> {

        @Override
        protected Integer doInBackground(Cart... carts) {
            return cartDao.deleteCart(carts[0]);
        }
    }

    private static class DeleteAllTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            cartDao.deleteAllCart();
            return null;
        }

    }

}
