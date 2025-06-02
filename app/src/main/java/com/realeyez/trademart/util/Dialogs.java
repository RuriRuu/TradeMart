package com.realeyez.trademart.util;

import android.app.AlertDialog;
import android.content.Context;

public class Dialogs {

    public static void showDialog(String title, String message, Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showMessageDialog(String message, Context context){
        showDialog("Info", message, context);
    }

    public static void showWarningDialog(String message, Context context){
        showDialog("Warning", message, context);
    }

    public static void showErrorDialog(String message, Context context){
        showDialog("Error", message, context);
    }

}
