package ru.startandroid.mvpsample.mymvp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import ru.startandroid.mvpsample.R;
import ru.startandroid.mvpsample.common.User;
import ru.startandroid.mvpsample.common.UserAdapter;
import ru.startandroid.mvpsample.database.DbHelper;
import ru.startandroid.mvpsample.mvp.UserData;
import ru.startandroid.mvpsample.mvp.UsersModel;

public class MyUserActivity extends AppCompatActivity {

    private UserAdapter userAdapter;
    private EditText editTextName;
    private EditText editTextEmail;
    private ProgressDialog progressDialog;

    private MyUsersPresenter myUsersPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        init();
    }


    private void init() {

        editTextName = (EditText) findViewById(R.id.name);
        editTextEmail = (EditText) findViewById(R.id.email);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        userAdapter = new UserAdapter();

        RecyclerView userList = (RecyclerView) findViewById(R.id.list);
        userList.setLayoutManager(layoutManager);
        userList.setAdapter(userAdapter);

        DbHelper dbHelper = new DbHelper(this);
        MyUsersModel usersModel = new MyUsersModel(dbHelper);

        myUsersPresenter = new MyUsersPresenter(usersModel);
        myUsersPresenter.AttachView(this);
        myUsersPresenter.viewIsReady();


    }

    public void showProgress() {
        progressDialog = ProgressDialog.show(this, "", getString(R.string.please_wait));
    }


    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }


    public void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }



    public void showUsers(List<User> list) {
        userAdapter.setData(list);
    }

    public UserData getUserData() {
        UserData userData = new UserData();
        userData.setName(editTextName.getText().toString());
        userData.setEmail(editTextEmail.getText().toString());
        return userData;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myUsersPresenter.DetachView();
    }
}
