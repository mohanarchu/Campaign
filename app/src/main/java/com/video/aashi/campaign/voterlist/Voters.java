package com.video.aashi.campaign.voterlist;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.video.aashi.campaign.Interface.MainInterface;
import com.video.aashi.campaign.Interface.NetworkClient;
import com.video.aashi.campaign.sesssion.Sessions;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class Voters {
    Context context;
    VoterPresenter streetPresent;
    Sessions menuStrings;
    ArrayList<Vote> stretLists;
    public Voters(Context context,VoterPresenter steet)
    {
        this.context = context;
        this.streetPresent = steet;
    }


    public void getStreets(String  id)
    {
        menuStrings = new Sessions(context);
        boolean change = menuStrings.isChange();
        JsonObject validate = new JsonObject();
        validate.addProperty("BoothSetting_Id" , id);
        if (!menuStrings.isNetworkAvailable(context))
        {
            if (change) {
                streetPresent.showToast("Check internet connection...!");
                // menuStrings.getToastTamil("Check internet connection...!");
            } else {
                streetPresent.showToast("இணைய இணைப்பை சரிபார்க்கவும்..!");
                //      menuStrings.getToastTamil("இணைய இணைப்பை சரிபார்க்கவும்..!");
            }
        }
        else
        {
          streetPresent.showProgress();
            if (change) {
                streetPresent.setProgressMessage("Please wait..!");
            } else {
                streetPresent.setProgressMessage("காத்திருக்கவும்..!");
            }

            getObservable(validate).subscribeWith(getSteeets());
        }
    }
    public Observable<VoterPojo> getObservable(JsonObject jsonObject) {
        return NetworkClient.getRetrofit().create(MainInterface.class)
                .getVoters(jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
       public DisposableObserver<VoterPojo> getSteeets()
    {
        return new DisposableObserver<VoterPojo>() {
            @Override
            public void onNext(VoterPojo streetPojo) {
                stretLists = new ArrayList<>();
                if (streetPojo.getStatus().equals("false"))
                {
                    Log.i("TAG","VoterError"+streetPojo.getResponse());
                    ArrayList<VoterResponse> streetRes = streetPojo.getResponse();
                    for (int i= 0;i<streetRes.size();i++)
                    {
                        stretLists.add(new Vote(streetRes.get(i).getVotersFile().getFilename()));
                    }
                    streetPresent.showStreetss(stretLists);
                }

        //    streetPresent.hideProgress();
            }
            @Override
            public void onError(Throwable e) {
               streetPresent.hideProgress();
                Log.i("TAG","VoterError"+e.toString());
                if (menuStrings.isChange())
                {
                    streetPresent.showToast("Please try again..!" );
                }
                else
                {
                   streetPresent.showToast("மீண்டும் முயற்சிக்கவும்");
                }
            }
            @Override

            public void onComplete() {
            }
        };
    }

}
