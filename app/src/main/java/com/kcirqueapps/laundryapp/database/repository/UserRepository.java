package com.kcirqueapps.laundryapp.database.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.kcirqueapps.laundryapp.database.AppDatabase;
import com.kcirqueapps.laundryapp.database.dao.UserDao;
import com.kcirqueapps.laundryapp.database.model.User;

import io.reactivex.Single;

public class UserRepository {
    private static UserDao userDao;

    public UserRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        userDao = database.getUserDao();
    }

    public void insertUser(User user) {
        new InsertTask().execute(user);
    }

    public void deleteUser() {
        new DeleteAllTask().execute();
    }

    public Single<User> getUser() {
        return userDao.getUser();
    }

    private static class InsertTask extends AsyncTask<User, Void, Long> {

        @Override
        protected Long doInBackground(User... users) {
            return userDao.insertUser(users[0]);
        }
    }

    private static class DeleteAllTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            userDao.deleteUser();
            return null;
        }

    }
}
