package com.video.aashi.campaign.affialation;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.video.aashi.campaign.Interface.MainInterface;
import com.video.aashi.campaign.Interface.NetworkClient;
import com.video.aashi.campaign.login.LoginPojo;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class affilatiomPresent {

    Context context;
    AffiModel affiModel;
    String id;
    int poss;

    public affilatiomPresent(Context context,AffiModel affiModel)
    {
        this.context = context;
        this.affiModel = affiModel;
    }

    public void getAffiList()
    {

        affiModel.showProgress();

        getAffi().subscribeWith(getAffilation());
    }
    public Observable<AffiPojo> getAffi() {
        return NetworkClient.getRetrofit().create(MainInterface.class)
                .AffiList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    DisposableObserver<AffiPojo> getAffilation()
    {
        return new DisposableObserver<AffiPojo>() {
            @Override
            public void onNext(AffiPojo affiPojo) {
                ArrayList<AffiArray> affiArrays = new ArrayList<>();


                if (affiPojo.getStatus().equals("true"))
                {
                    ArrayList<AffiPojo.Response> responses = affiPojo.getResponse();


                    for (int i=0;i<responses.size();i++)
                    {
                        affiArrays.add(new AffiArray(
                                responses.get(i).getAffiliationType_Name(),"",
                                responses.get(i).get_id()
                        ));
                    }
                    affiModel.showAffi(affiArrays);
                }
               affiModel.hideProgress();
            }
            @Override
            public void onError(Throwable e) {
                affiModel.hideProgress();
            }
            @Override
            public void onComplete() {

            }
        };
    }
   public void getPartyList( )
    {
        affiModel.showProgress();
        getAffis().subscribeWith(getAffilations());
    }
    public Observable<PartyPojo> getAffis() {
        return NetworkClient.getRetrofit().create(MainInterface.class)
                .PartyList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    DisposableObserver<PartyPojo> getAffilations()
    {
        return new DisposableObserver<PartyPojo>() {
            @Override
            public void onNext(PartyPojo affiPojo) {
                ArrayList<AffiArray> affiArrays = new ArrayList<>();
                if (affiPojo.getStatus().equals("true"))
                {
                    ArrayList<PartyPojo.Response> responses = affiPojo.getResponse();

                        for (int i=0;i<responses.size();i++)
                        {
                            affiArrays.add(new AffiArray(
                                    responses.get(i).getPartyType_Code() ,responses.get(i).getPartyType_Name()  ,
                                    responses.get(i).get_id()
                            ));
                        }
                        affiModel.showPartyy(affiArrays);

               }
                 affiModel.hideProgress();

            }

            @Override
            public void onError(Throwable e) {
                affiModel.hideProgress();

            }

            @Override
            public void onComplete() {

            }
        };
    }

    void updateAffialation(String citizenid,String userId,String mobiles,String caste,String affiFrom,String affiTo,
                                    String educations,String streets  )
    {
        affiModel.showProgress();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("CitizenRegister_Id", citizenid);
        jsonObject.addProperty("User_Id",userId);
        jsonObject.addProperty("Mobile_Number", mobiles);
        jsonObject.addProperty("Caste", caste);
        jsonObject.addProperty("Affiliated_Type", affiFrom);
        jsonObject.addProperty("Affiliated_To", affiTo);
        jsonObject.addProperty("Educational_Qualification", educations);
        jsonObject.addProperty("Street", streets);
        Log.i("TAG","RretrofitException"+ jsonObject);
        getAffiUpdate(jsonObject).subscribeWith(getAffilationsUpdate());
    }


    Observable<SuccessPojo> getAffiUpdate(JsonObject jsonObject) {
        Log.i("TAG","RretrofitExceptions"+ jsonObject);
        return NetworkClient.getRetrofit().create(MainInterface.class)
                .UpdateCitizen(jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    DisposableObserver<SuccessPojo> getAffilationsUpdate()
    {
        return new DisposableObserver<SuccessPojo>() {
            @Override
            public void onNext(SuccessPojo loginPojo) {
                Log.i("TAG","RretrofitExceptionss"+ loginPojo.getMessage());
                ArrayList<AffiArray> affiArrays = new ArrayList<>();
                if (loginPojo.getStatus().equals("true"))
                {
                    affiModel.hide();
                    affiModel.hideProgress();
                    affiModel.showToast("Updated successfully...!");
                }
                else
                {
                    affiModel.showToast("Try again..!");
                }
                affiModel.hideProgress();
            }
            @Override
            public void onError(Throwable e) {
                affiModel.hideProgress();
                affiModel.showToast("Try again..!");
                Log.i("TAG","UploadException"+ e.toString());
            }
            @Override
            public void onComplete() {
            }
        };
    }
}
