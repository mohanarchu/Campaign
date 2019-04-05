package com.video.aashi.campaign.login.otp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * MOHAN on 21-09-2015.
 */
public class SmsReceiver extends BroadcastReceiver {

    public static final String SMS_ORIGIN = "ID-KMDKAP";
    public static final String OTP_DELIMITER = "-";
    public static final String TAG = "SmsReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (Object aPdusObj : pdusObj) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                    String senderAddress = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();

                    Log.e(TAG, "Received SMS: " + message + ", Sender: " + senderAddress);

                    // if the SMS is not from our gateway, ignore the message
                    if (!senderAddress.toLowerCase().contains(SMS_ORIGIN.toLowerCase())) {
                        return;
                    }

                    // verification code from sms
                    int verificationCode = getVerificationCode(message);
                    Log.e(TAG, "OTP received: " + verificationCode);
                    Intent myIntent = new Intent("otp");
                    myIntent.putExtra("message", message);
                    myIntent.putExtra("number", verificationCode);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "MyException:" + e.getMessage());
        }
    }

    /**
     * Getting the OTP from sms message body
     * ':' is the separator of OTP from the message
     *
     * @param message text received from the message
     */
    private int getVerificationCode(String message) {
        String code = null;
        int pin = 0;
        int index = message.indexOf(OTP_DELIMITER);
        Log.e(TAG, "Receiveds: " + message);
        if (index != -1) {
            int start = index + 2;
            int length = 4;

            code = message.substring(start, start + length);
            try {
                pin = Integer.parseInt(code);
            } catch (NumberFormatException e) {
                e.printStackTrace();

            }
            return pin;
        }

        return pin;
    }
}
