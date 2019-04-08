package com.video.aashi.campaign.votersview;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.video.aashi.campaign.Interface.MainInterface;
import com.video.aashi.campaign.Interface.NetworkClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class CityPresent {

    Context context;
    CityModel cityModel;

    public CityPresent(Context context,CityModel cityModel)
    {
        this.cityModel = cityModel;
        this.context = context;
    }
    void getList(String id)
    {
        cityModel.showProgress();
        JsonObject jsonObject = new JsonObject()    ;
        jsonObject.addProperty("Booth_Id",id);
        getObservable(jsonObject).subscribeWith(getCoomplaint());
    }
    public Observable<CitizenPojo> getObservable(JsonObject jsonObject ) {
        return NetworkClient.getRetrofit().create(MainInterface.class)
                .CitizenList(jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
    public DisposableObserver<CitizenPojo> getCoomplaint()
    {
        return new DisposableObserver<CitizenPojo>() {
            @Override
            public void onNext(CitizenPojo responseBody) {
                ArrayList<CityList> cityLists = new ArrayList<>();
                if (responseBody.getStatus().equals("true"))
                {
                    ArrayList<CitizenResponse> citizenResponses = responseBody.getResponse();
                    String from ="",to ="";
                    for (int i=0;i<citizenResponses.size();i++)
                    {
                        CitizenResponse res= citizenResponses.get(i);
                        if (res.getAffiliated_To() != null)
                        {
                            to = res.getAffiliated_To().getPartyType_Name();
                        }
                        else
                        {
                            to ="";
                        }
                        if (res.getAffiliated_Type() != null)
                        {
                            from = res.getAffiliated_Type().getAffiliationType_Name();
                        }
                        else
                        {
                            from     ="";
                        }
                        if (res.getImage() != null)
                        {
                            cityLists.add(new CityList(res.getVoterIdNumber(),res.getCandidateName(),null, res.getImage().getFilename()
                                    ,res.getCandidateAge(),res.getALL_DATA(),res.getFatherName(),res.get_id(),
                                    res.getStreet(),res.getCaste(),res.getMobile_Number(),from,
                                    to,res.getEducational_Qualification(),res.getCandidateSex()
                            ));
                        }
                    }
                    cityModel.showList(cityLists);
                }
                cityModel.hideProgress();
            }
            @Override
            public void onError(Throwable e)
            {
                cityModel.hideProgress();
                Log.i("Tag","ComplaintsError"+ e.toString());
                 cityModel.showToast("Something went wrong..!");
            }
            @Override
            public void onComplete() {
            }
        };
    }


}
