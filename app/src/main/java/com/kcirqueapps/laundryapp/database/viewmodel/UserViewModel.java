package com.kcirqueapps.laundryapp.database.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.kcirqueapps.laundryapp.database.model.User;
import com.kcirqueapps.laundryapp.database.repository.UserRepository;

import io.reactivex.Single;

public class UserViewModel extends AndroidViewModel {
    private UserRepository userRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }


    public void insertUser(User user) {
        userRepository.insertUser(user);
    }

    public void deleteUser() {
        userRepository.deleteUser();
    }

    public Single<User> getUser() {
        return userRepository.getUser();
    }

}
