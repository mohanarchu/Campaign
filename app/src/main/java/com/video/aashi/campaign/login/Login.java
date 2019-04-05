package com.video.aashi.campaign.login;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.video.aashi.campaign.Interface.MainInterface;
import com.video.aashi.campaign.Interface.NetworkClient;
import com.video.aashi.campaign.sesssion.Sessions;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class Login implements LoginView{

    public static final String MOBILE_NO_PATTERN = "[6789][0-9]{9}";
    LoginPresenter loginPresenter;
    Context context;
    Sessions menuStrings;
    String number;
    public Login(LoginPresenter presenter, Context context)
    {
        this.loginPresenter = presenter;
        this.context =  context;
    }
    @Override
    public void doLogin(String mobilenumer) {
        menuStrings = new Sessions(context);
        boolean change = menuStrings.isChange() ;
        number = mobilenumer;
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("MobileNumber",mobilenumer);
        if (!menuStrings.isNetworkAvailable(context))
        {
            if (change)
            {
                loginPresenter.showToast("Check internet connection...!");

            }else
            {
                loginPresenter.showToast("இணைய இணைப்பை சரிபார்க்கவும்..!");
            }
        }
        else
        {
            loginPresenter.showProgress();
            if (change)
            {
                loginPresenter.setProgressMessage("Please wait..!");
            }
            else
            {
                loginPresenter.setProgressMessage("காத்திருக்கவும்..!");
            }

            getObservable(jsonObject).subscribeWith(getMobiles());
        }
       }

    @Override
    public boolean validate(String number) {
            Pattern pattern = Pattern.compile(MOBILE_NO_PATTERN);
            return pattern.matcher(number).matches() && number.length() <= 10;

    }

    public Observable<LoginPojo> getObservable(JsonObject number) {


        return NetworkClient.getRetrofit().create(MainInterface.class)
                .getLoginstatus(number)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public DisposableObserver<LoginPojo> getMobiles()
    {
        return new DisposableObserver<LoginPojo>() {
            @Override
            public void onNext(LoginPojo loginPojo) {

                Log.i("TAG","Error"+loginPojo.getStatus());

                if (loginPojo.getStatus().equals("true"))
                {
                    loginPresenter.showToast(loginPojo.getMessage());
                    loginPresenter.verifyOtp(loginPojo.getMessage(),number);
                }
                else
                {
                    if (menuStrings.isChange())
                    {
                        loginPresenter.showToast("Invalid user..!" );
                    }
                    else
                    {
                        loginPresenter.showToast("தவறான பயனர்..!");
                    }
                }
            }
            @Override
            public void onError(Throwable e) {
                Log.i("TAG","Error"+e.toString());
                loginPresenter.hideProgress();
                if (menuStrings.isChange())
                {
                    loginPresenter.showToast("Please try again..!" );
                }
                else
                {
                    loginPresenter.showToast("மீண்டும் முயற்சிக்கவும்");
                }
            }
            @Override
            public void onComplete() {

            }
        };
    }

}
