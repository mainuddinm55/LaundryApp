package com.kcirqueapps.laundryapp.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.kcirqueapps.laundryapp.database.model.User;

import io.reactivex.Single;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertUser(User user);

    @Query("SELECT * FROM user")
    Single<User> getUser();

    @Query("DELETE FROM user")
    void deleteUser();
}
