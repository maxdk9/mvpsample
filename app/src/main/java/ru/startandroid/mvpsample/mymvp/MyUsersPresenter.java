package ru.startandroid.mvpsample.mymvp;

import android.content.ContentValues;
import android.text.TextUtils;

import java.util.List;

import ru.startandroid.mvpsample.R;
import ru.startandroid.mvpsample.common.User;
import ru.startandroid.mvpsample.common.UserTable;
import ru.startandroid.mvpsample.mvp.UserData;

public class MyUsersPresenter {
    private MyUserActivity view;
    private MyUsersModel model;


    public MyUsersPresenter(MyUsersModel model) {
        this.model =model;
    }


    public void AttachView(MyUserActivity activity) {
        view =activity;
    }

    public void DetachView() {
        view =null;
    }

    public void viewIsReady() {
        loadUsers();
    }

    public void loadUsers() {
        model.loadUsers(new MyUsersModel.LoadUserCallback() {
            @Override
            public void onLoad(List<User> list) {
                view.showUsers(list);
            }
        });
    }

    public void add() {
        UserData userData = view.getUserData();
        if (TextUtils.isEmpty(userData.getEmail()) || TextUtils.isEmpty(userData.getName())) {
            view.showToast(R.string.empty_values);
            return;
        }

        String name=userData.getName();

        view.showProgress();
        ContentValues cv = new ContentValues(2);
        cv.put(UserTable.COLUMN.NAME, userData.getName());
        cv.put(UserTable.COLUMN.EMAIL, userData.getEmail());
        model.AddUser(cv, new MyUsersModel.CompleteCallback() {
            @Override
            public void onComplete() {
                view.hideProgress();
                loadUsers();
            }
        });


    }


    public void clear() {
        view.showProgress();
        model.clearUsers(new MyUsersModel.CompleteCallback() {
            @Override
            public void onComplete() {
                view.hideProgress();
                loadUsers();
            }
        });
    }



}
