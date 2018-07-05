package ru.startandroid.mvpsample.mymvp;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;

import java.util.LinkedList;
import java.util.List;

import ru.startandroid.mvpsample.common.User;
import ru.startandroid.mvpsample.common.UserTable;
import ru.startandroid.mvpsample.database.DbHelper;

public class MyUsersModel {

    private final DbHelper dbHelper;

    public MyUsersModel(DbHelper helper) {
        dbHelper=helper;
    }


    public void loadUsers(LoadUserCallback callback) {
        LoadUsersTask loadUsersTask = new LoadUsersTask(callback);
        loadUsersTask.execute();
    }


    public void AddUser(ContentValues contentValues, CompleteCallback callback) {
        AddUserTask addUserTask = new AddUserTask(callback);
        addUserTask.execute(contentValues);
    }

    public void clearUsers(CompleteCallback callback) {
        ClearUsersTask clearUsersTask = new ClearUsersTask(callback);
        clearUsersTask.execute();
    }


    public interface LoadUserCallback {
        void onLoad(List<User> list);
    }

    public interface  CompleteCallback{
        void onComplete();
    }



    class LoadUsersTask extends AsyncTask<Void,Void,List<User>> {


        private final LoadUserCallback callback;

        LoadUsersTask(LoadUserCallback callback) {
            this.callback=callback;
        }


        @Override
        protected List<User> doInBackground(Void... voids) {

            LinkedList<User> users = new LinkedList<>();
            Cursor cursor = dbHelper.getReadableDatabase().query(UserTable.TABLE, null, null, null, null,null,null);

            while (cursor.moveToNext()){
                User user = new User();
                user.setEmail(cursor.getString(cursor.getColumnIndex(UserTable.COLUMN.EMAIL)));
                user.setId(cursor.getLong(cursor.getColumnIndex(UserTable.COLUMN.ID)));
                user.setName(cursor.getString(cursor.getColumnIndex(UserTable.COLUMN.NAME)));
                users.add(user);
            }

            cursor.close();
            return users;
        }

        @Override
        protected void onPostExecute(List<User> users) {
            super.onPostExecute(users);
            if (this.callback != null) {
                callback.onLoad(users);
            }

        }
    }


    class AddUserTask extends AsyncTask<ContentValues, Void, Void> {

        private final CompleteCallback callback;

        public AddUserTask(CompleteCallback callback) {
            this.callback = callback;

        }


        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            ContentValues userValue = contentValues[0];

            dbHelper.getWritableDatabase().insert(UserTable.TABLE, null, userValue);
            try{
                this.wait(1000);
            }
            catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (callback != null) {
                callback.onComplete();
            }

        }
    }


    class ClearUsersTask extends AsyncTask<Void, Void, Void> {

        private final CompleteCallback callback;

        public ClearUsersTask(CompleteCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dbHelper.getWritableDatabase().delete(UserTable.TABLE, null, null);
            try {
                this.wait(1000);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (callback != null) {
                callback.onComplete();
            }
        }
    }




}











