package com.arnav.library;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

public class QRCodeDialog {

    private final Activity activity;
    private AlertDialog alertDialog;
    private Bitmap bitmap;
    private View view;

    public QRCodeDialog(Activity myActivity) {
        activity = myActivity;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        ImageView qrImageView = this.view.findViewById(R.id.qrCodeImageView);
        qrImageView.setImageBitmap(this.bitmap);
    }

    public void initializeLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        this.view = inflater.inflate(R.layout.custom_qrcode_dialog, null);

        builder.setView(view);
        builder.setCancelable(true);

        alertDialog = builder.create();
    }

    public void showDialog() {
        alertDialog.show();
    }

    public void hideDialog() {
        alertDialog.hide();
    }

    public void dismissDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

}
