package com.realeyez.trademart.gui.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface.OnCancelListener;
import android.view.LayoutInflater;
import com.realeyez.trademart.R;

public class LoadingDialog {

    private Activity activity;
    private AlertDialog dialog;

    public LoadingDialog(Activity activity){
        this.activity = activity;
    }

    public void show(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    public void close(){
        dialog.dismiss();
    }
    
    public void setOnCancelListener(OnCancelListener listener){
        dialog.setOnCancelListener(listener);
    }

}
