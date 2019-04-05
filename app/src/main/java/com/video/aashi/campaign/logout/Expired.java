package com.video.aashi.campaign.logout;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

public class Expired extends AlertDialog.Builder {

    Context context;
    String message;

    public Expired(@NonNull Context context, String message) {

        super(context);
        this.context = context;
        this.message= message;
    }
    @Override
    public AlertDialog.Builder setTitle(@Nullable CharSequence title) {
        return super.setTitle(title);
        }
    @Override
    public AlertDialog show() {
        return super.show();
    }
    @Override
    public AlertDialog.Builder setCancelable(boolean cancelable) {
        return super.setCancelable(cancelable);
    }
    @Override
    public AlertDialog.Builder setPositiveButton(int textId, DialogInterface.OnClickListener listener) {
        return super.setPositiveButton(textId, listener);
    }
    @Override
    public AlertDialog.Builder setNegativeButton(int textId, DialogInterface.OnClickListener listener) {
        return super.setNegativeButton(textId, listener);
    }
}
