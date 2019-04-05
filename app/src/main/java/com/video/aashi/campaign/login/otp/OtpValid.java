package com.video.aashi.campaign.login.otp;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.video.aashi.campaign.Interface.MainInterface;
import com.video.aashi.campaign.Interface.NetworkClient;
import com.video.aashi.campaign.boothlist.Booths;
import com.video.aashi.campaign.sesssion.Sessions;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class OtpValid {

    Context context;
    otpPresenter otpPresenter;
    Sessions menuStrings;
    ArrayList<Booths> booths;
    String otps,mobiles;
    boolean value;

    public OtpValid(otpPresenter otpPresenter, Context context,boolean value)
    {
        this.otpPresenter = otpPresenter;
        this.context = context;
        this.value = value;
    }

    public  void getOtp(String otp,String mobile,String id) {

        menuStrings = new Sessions(context);
        boolean change = menuStrings.isChange();
        JsonObject validate = new JsonObject();
        validate.addProperty("OTP",otp);
        validate.addProperty("MobileNumber",mobile);
        JsonObject validates = new JsonObject();
        validates.addProperty("CampaignManager_Id",id);
        otps = otp;
        mobiles = mobile;
        if (!menuStrings.isNetworkAvailable(context))

        {
            if (change) {

                otpPresenter.showToast("Check internet connection...!");
                // menuStrings.getToastTamil("Check internet connection...!");
            } else {
                otpPresenter.showToast("இணைய இணைப்பை சரிபார்க்கவும்..!");
                //      menuStrings.getToastTamil("இணைய இணைப்பை சரிபார்க்கவும்..!");
            }
        }
        else
        {
                 otpPresenter.showProgress();
            if (change) {
                otpPresenter.progressMessage("Please wait..!");
            } else {
                otpPresenter.progressMessage("காத்திருக்கவும்..!");
            }
            if (value)
            {
                getObservable(validate).subscribeWith(getMobiles());

            }
            else
            {
                getObservables(validates).subscribeWith(getMobiles());
            }

        }

    }
    public Observable<OtpPojo> getObservable(JsonObject jsonObject) {
        return NetworkClient.getRetrofit().create(MainInterface.class)
                .getBooths(jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public Observable<OtpPojo> getObservables(JsonObject jsonObject) {
        return NetworkClient.getRetrofit().create(MainInterface.class)
                .getmanager(jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
public DisposableObserver<OtpPojo> getMobiles()
{
    return new DisposableObserver<OtpPojo>() {
        @Override
        public void onNext(OtpPojo otpPojo) {
            booths = new ArrayList<>();
            Log.i("TAG","OtpError"+otpPojo.getStatus());
            if (otpPojo.getStatus().equals("true"))
            {
                menuStrings.setLoginKey(true);
                menuStrings.setId(otpPojo.getResponse().get_id());
                if (menuStrings.isChange())
                {
                    if (value)
                    {
                        otpPresenter.showToast("Thank you..!" );
                    }

                }
                else
                {
                    if (value)
                    {
                        otpPresenter.showToast("நன்றி");
                    }

                }


                Response response = otpPojo.getResponse();
                ArrayList<Response.BoothNumber> boothNumbers = response.getBoothNumber();

                for (int i=0;i<boothNumbers.size();i++)
                {
                    Response.BoothNumber b = boothNumbers.get(i);

                    booths.add(new Booths(b.get_id(),b  .getBoothName(),b.getBoothNumber()));

                }

                otpPresenter.hideProgress();
                otpPresenter.shhowLists(booths);
                otpPresenter.showScreen(mobiles,otps);
            }

            else
            {
                if (menuStrings.isChange())
                {
                    otpPresenter.showToast("Please try again..!" );
                }
                else
                {
                    otpPresenter.showToast("மீண்டும் முயற்சிக்கவும்");
                }
            }
        }
        @Override
        public void onError(Throwable e) {
            Log.i("TAG","OtpError"+e.getMessage());
            otpPresenter.hideProgress();
            if (menuStrings.isChange())
            {
                otpPresenter.showToast("Please try again..!" );
            }
            else
            {
                otpPresenter.showToast("மீண்டும் முயற்சிக்கவும்");
            }
        }

        @Override
        public void onComplete() {

        }
    };
}


    public     boolean isSmsPermissionGranted()
    {
        return  Permission.isReceiveSmsPermissionGranted(context);
    }

}
