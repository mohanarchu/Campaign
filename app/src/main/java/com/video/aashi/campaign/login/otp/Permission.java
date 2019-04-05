package com.video.aashi.campaign.login.otp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.video.aashi.campaign.R;

import java.util.ArrayList;

public class Permission {
    public static final int PERMISSION_REQUEST = 2332;
    public static final String CALL_PHONE = Manifest.permission.CALL_PHONE;
    public static final String RECEIVE_SMS = Manifest.permission.RECEIVE_SMS;
    public static final String STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    /**
     * @param activity    Activity Object
     * @param permissions permissions to check whether granted or not
     * @return true - if Version is greater than Lollipop and all permissions are already enabled.
     * true - if Version is less than MarshMallow.
     * false - if Version is greater than Lollipop and all/atlease one permission(s) are not enabled.
     * false - if Version is not greater than Lollipop
     */
    public static boolean checkPermission(Activity activity, @NonNull String... permissions) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            ArrayList<String> permissionList = new ArrayList<>();
            for (String permission : permissions)
                if (ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED) {
                    permissionList.add(permission);
                }
            if (permissionList.size() > 0) {
                String[] permissionArray = new String[permissionList.size()];
                permissionList.toArray(permissionArray);
                ActivityCompat.requestPermissions(activity, permissionArray, PERMISSION_REQUEST);
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    public static boolean isReceiveSmsPermissionGranted(Context context) {
        return ActivityCompat.checkSelfPermission(context, RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isStoragePermissionGranted(Context context) {
        return ActivityCompat.checkSelfPermission(context, STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Displays Permission dialog when the permission is denied
     *
     * @param context Application context
     * @param message message to display
     */
    public static void showPermissionDialog(final Context context, String message) {
        AlertDialog.Builder pDialog = new AlertDialog.Builder(context);
        pDialog.setTitle("Alert");
        pDialog.setMessage("Enable permisiion in settings" +  message);
        pDialog.setCancelable(false);
        pDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package",
                        context.getPackageName(), null));
                context.startActivity(intent);
            }
        });
        pDialog.show();
    }
}