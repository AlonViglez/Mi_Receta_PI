package com.example.nav_drawer;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AndroidUtil {
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void passUserModelAsIntent(Intent intent, UserModel model){
        intent.putExtra("nombre",model.getNombre());
        intent.putExtra("id",model.getId());

    }

    public static UserModel getUserModelFromIntent(Intent intent) {
        UserModel usermodel= new UserModel();
        usermodel.setNombre(intent.getStringExtra("nombre"));
        usermodel.setId(intent.getStringExtra("id"));
        return usermodel;
    }




}