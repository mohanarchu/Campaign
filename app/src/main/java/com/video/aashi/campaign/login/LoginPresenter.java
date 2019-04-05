package com.video.aashi.campaign.login;

public interface LoginPresenter {


    void  showProgress();
    void hideProgress();
    void setProgressMessage(String message);
    void showToast(String Toast_string);
    void verifyOtp(String otp, String number);
    void otpScreen();

    void conFormMobile(String string);
    void canVlidate(boolean b, String number);
    void shoPop();

}
