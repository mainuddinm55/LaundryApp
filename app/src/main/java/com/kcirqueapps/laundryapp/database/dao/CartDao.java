package com.kcirqueapps.laundryapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kcirqueapps.laundryapp.database.model.Cart;

import java.util.List;

@Dao
public interface CartDao {
    @Insert
    long insertCart(Cart cart);

    @Update
    int updateCart(Cart cart);

    @Delete
    int deleteCart(Cart cart);

    @Query("SELECT * FROM cart")
    LiveData<List<Cart>> getCarts();

    @Query("DELETE FROM cart")
    void deleteAllCart();
}
