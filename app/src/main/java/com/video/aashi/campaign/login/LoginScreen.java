package com.video.aashi.campaign.login;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.video.aashi.campaign.AlertDialogues;
import com.video.aashi.campaign.R;
import com.video.aashi.campaign.login.otp.OtpVerification;
import com.video.aashi.campaign.sesssion.Sessions;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginScreen extends Fragment implements MobileNoDialog.ActionCallback ,LoginPresenter{

    @BindView(R.id.logSubmitText)
    TextView submitText;
    @BindView(R.id.submitLogin)
    CardView login;
    @BindView(R.id.logMobile)
    EditText loginMobile;
    ProgressDialog progressDialog;
    Sessions sessions;
    @BindView(R.id.changeLan)
    TextView changeLang;
    Login logins ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_screen, container, false);
        ButterKnife.bind(this,view);
        logins = new Login(this,getActivity());
        sessions = new Sessions(getActivity());
        getActivity(). setTitle("KMDK CM");
        if (sessions.isChange())
        {
            changeLang.setText("Change language..");
            submitText.setText("Submit");
            loginMobile.setHint("Mobile Number");
        }
        else
        {
            changeLang.setText("மொழி மாற்ற..");
        }
        changeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogues alertDialogues = new AlertDialogues(getActivity());
                String neg,pas;
                if (sessions.isChange())
                {
                    neg = "No";
                    pas ="yes";
                }
                else
                {
                    neg = "இல்லை";
                    pas ="ஆம்";
                }
                alertDialogues.setNegativeButton(neg, (dialog, which) -> dialog.dismiss());
                alertDialogues.setPositiveButton(pas, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialogues.clicked();
                        getActivity(). finish();
                        startActivity(getActivity(). getIntent());
                    }
                });
                alertDialogues.show();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (loginMobile.getText().toString().isEmpty() ||loginMobile.getText().toString().length() != 10 )
                    {
                        if (sessions.isChange())
                        {
                            showToast("Enter valid mobile number..!");
                        }
                        else
                        {
                            showToast("சரியான கைபேசி எண்ணை உள்ளிடவும்..!");
                        }
                    }
                    else
                    {
                        conFormMobile(loginMobile.getText().toString());;
                    }

            }
        });
        return  view;
    }
    @Override
    public void onOk(String number) {
        logins.doLogin(number);
    }
    @Override
    public void showProgress() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
    }
    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }
    @Override
    public void setProgressMessage(String message) {
        progressDialog.setMessage(message);
    }
    @Override
    public void showToast(String Toast_string) {
        Toast.makeText(getActivity(),Toast_string,  Toast.LENGTH_SHORT).show();
    }
    @Override
    public void verifyOtp(String otp, String number) {
        Intent intent = new Intent(getActivity(),OtpVerification.class);
        intent.putExtra("OTP",otp);
        intent.putExtra("mobileNumber",number);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    @Override
    public void otpScreen() {
    }
    @Override
    public void conFormMobile(String string) {
        if (logins. validate(string)) {
            MobileNoDialog mobileNoDialog = new MobileNoDialog();
            mobileNoDialog.setCallback(this);
            mobileNoDialog.setCancelable(true);
            Bundle args = new Bundle();
            args.putString(MobileNoDialog.EXTRA_NUMBER, string);
            mobileNoDialog.setArguments(args);
            mobileNoDialog.show(getChildFragmentManager() , "mobileNoDialog");
        }
    }
    @Override
    public void canVlidate(boolean b, String number) {
        if (b)
        {
        }
        else
        {
            logins.doLogin(number);
        }
    }
    @Override
    public void shoPop() {
    }
}
