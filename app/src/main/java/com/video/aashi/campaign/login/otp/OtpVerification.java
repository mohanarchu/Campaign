package com.video.aashi.campaign.login.otp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.video.aashi.campaign.R;
import com.video.aashi.campaign.boothlist.Booths;
import com.video.aashi.campaign.main.MainScreen;
import com.video.aashi.campaign.sesssion.Sessions;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OtpVerification extends AppCompatActivity implements otpPresenter {

    @BindView(R.id.otpCode)
    Pinview otpCode;
    @BindView(R.id.submitOtp)
    CardView submitotp;
    ProgressDialog progressDialog;
    SmsReceiver smsReceiver;
    private  static  int REQUEST_ID_MULTIPLE_PERMISSIONS;

    OtpValid otpValid;
    String otps;

    Sessions menuStrings;
    String mobileNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        ButterKnife.bind(this);
        otpValid = new OtpValid(OtpVerification.this,getApplicationContext(),true);
        smsReceiver = new SmsReceiver();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        otpCode.requestFocus();
        otpCode.setFocusable(false);
        menuStrings = new Sessions(getApplicationContext());
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(otpCode, InputMethodManager.SHOW_IMPLICIT);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        otpCode.setFocusable(false);
        progressDialog = new ProgressDialog(OtpVerification.this);
        if (getIntent().getExtras() != null)
        {
            mobileNumber = getIntent().getStringExtra("mobileNumber");

        }
        otpCode.setFocusable(false);
      //  sharedPreference = new SharedPreference(getApplicationContext());
        submitotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otpCode.getValue().equals(""))
                {
                    if (menuStrings.isChange())
                    {
                        showToast("Enter OTP");
                    }
                    else
                    {
                        showToast("OTP-ஐ உள்ளிடவும்");
                    }

                }
                else
                {
                    otpValid.getOtp(otpCode.getValue(),mobileNumber,"");
                }
            }
        });
        Permission.checkPermission(this, Permission.RECEIVE_SMS);
        if (checkAndRequestPermissions()) {
            // carry on the normal flow, as the case of  permissions  granted.
        }
    }
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                Log.e("TAG", "OTP receiveds: " + message);

                Pattern pattern = Pattern.compile("(\\d{4})");
                Matcher matcher = pattern.matcher(message);
                String val = "";
                if (matcher.find()) {
                    System.out.println(matcher.group(1));

                    val = matcher.group(1);
                    seetOtp(val);
                }

            }
        }
    };
    @Override
    public void showProgress() {
        progressDialog.show();
    }
    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));


        super.onResume();
        if (otpValid.isSmsPermissionGranted()) {
            IntentFilter smsFilter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
            smsReceiver = new SmsReceiver();
            registerReceiver(smsReceiver, smsFilter);
        }
    }
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onPause();
        if (smsReceiver != null && otpValid.isSmsPermissionGranted())
            unregisterReceiver(smsReceiver);
    }
    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }
    @Override
    public void progressMessage(String message) {
        progressDialog.setMessage(message);
    }
    @Override
    public void showScreen(String mobile,String otp)
    {
        Intent intent = new Intent(getApplicationContext(),MainScreen.class);
       intent.putExtra("mobile",mobile);
       intent.putExtra("otp",otp);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    @Override
    public void showToast(String string) {
        Toast.makeText(getApplicationContext(),string,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void seetOtp(String text) {
        otpCode.setValue(text);
        otpValid.getOtp(text,mobileNumber,"");
    }

    @Override
    public void shhowLists(ArrayList<Booths> booths) {

    }

    @Override
    protected void onStart() {
        super.onStart();
      //  EventBus.getDefault().register(this);
    }
    @Override
    protected void onStop() {
        super.onStop();
        //EventBus.getDefault().unregister(this);
    }
    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int receiveSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        int readSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (otpValid.isSmsPermissionGranted())
        {
            IntentFilter smsFilter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
            smsReceiver = new SmsReceiver();
            registerReceiver(smsReceiver, smsFilter);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
