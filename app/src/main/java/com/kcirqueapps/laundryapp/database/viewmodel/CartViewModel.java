package com.kcirqueapps.laundryapp.database.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.kcirqueapps.laundryapp.database.model.Cart;
import com.kcirqueapps.laundryapp.database.repository.CartRepository;

import java.util.List;

public class CartViewModel extends AndroidViewModel {
    private CartRepository cartRepository;

    public CartViewModel(@NonNull Application application) {
        super(application);
        cartRepository = new CartRepository(application);
    }


    public void insertCart(Cart cart) {
         cartRepository.insertCart(cart);
    }


    public void updateCart(Cart cart) {
         cartRepository.updateCart(cart);
    }

    public  void  deleteCart(Cart cart) {
         cartRepository.deleteCart(cart);
    }

    public LiveData<List<Cart>> getCarts() {
        return cartRepository.getCarts();
    }

    public void deleteAllCart() {
         cartRepository.deleteAllCart();
    }
}
