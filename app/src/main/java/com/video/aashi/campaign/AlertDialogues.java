package com.video.aashi.campaign;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.video.aashi.campaign.sesssion.Sessions;

public class AlertDialogues extends AlertDialog.Builder {
    Sessions menuStrings;
    Context context;
    public AlertDialogues(@NonNull Context context) {
        super(context);
        this.context =context;
    }
    @Override
    public AlertDialog.Builder setTitle(int titleId) {
        return super.setTitle(titleId);
    }

    @Override
    public AlertDialog.Builder setPositiveButton(int textId, DialogInterface.OnClickListener listener) {
        return super.setPositiveButton(textId, listener);
    }

    @Override
    public AlertDialog.Builder setNegativeButton(int textId, DialogInterface.OnClickListener listener) {
        return super.setNegativeButton(textId, listener);
    }

    @Override
    public AlertDialog show() {
        menuStrings = new Sessions(context);
        if (menuStrings.isChange())
        {
            setTitle("Are sure you want to change the language ?");
        }
        else
        {
            setTitle("நீங்கள் மொழியை மாற்ற விரும்புகிறீர்களா?");
        }
        return super.show();
    }
    public  void clicked()
    {
        boolean changes = menuStrings.isChange();
        if (!changes)
        {
           menuStrings.setChange(true);
            Toast.makeText( context,"Language changed successfully..!",Toast.LENGTH_SHORT).show();
        }
        else
        {
            menuStrings.setChange(false);
            Toast.makeText(context,"மொழி மாற்றப்பட்டது..!",Toast.LENGTH_SHORT).show();
        }
    }
}
