package com.kcirqueapps.laundryapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.kcirqueapps.laundryapp.database.dao.CartDao;
import com.kcirqueapps.laundryapp.database.dao.UserDao;
import com.kcirqueapps.laundryapp.database.model.Cart;
import com.kcirqueapps.laundryapp.database.model.User;

@Database(entities = {Cart.class, User.class}, version = 7, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CartDao getCartDao();

    public abstract UserDao getUserDao();

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null)
            instance = Room.databaseBuilder(context, AppDatabase.class, "app_db")
                    .fallbackToDestructiveMigration()
                    .build();
        return instance;
    }
}
