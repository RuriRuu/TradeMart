package com.realeyez.trademart.util;

import android.app.AlertDialog;
import android.content.Context;

public class Dialogs {

    public static void showMessageDialog(String message, Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
            .setTitle("Message")
            .setMessage(message);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
