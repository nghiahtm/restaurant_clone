package com.example.restaurantmanager.utils;

import android.app.AlertDialog;
import android.content.Context;

import com.example.restaurantmanager.R;

public class ProgressDialog {
    private AlertDialog dialog;
    public ProgressDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        dialog = builder.create();
    }
    public void show(){
        dialog.show();
    }
    public void dismiss(){
        dialog.dismiss();
    }

}
