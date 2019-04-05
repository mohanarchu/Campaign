package com.video.aashi.campaign.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.video.aashi.campaign.R;
import com.video.aashi.campaign.sesssion.Sessions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MobileNoDialog extends DialogFragment {

    public static final String EXTRA_NUMBER = "EXTRA_NUMBER";
    @BindView(R.id.btnOk)
    LinearLayout btnOk;
    @BindView(R.id.txtNumber)
    TextView txtNumber;
    private ActionCallback callback;
    Unbinder unbinder;

    Sessions menuStrings;
    private String number;
    TextView head,yes;

    public void setCallback(ActionCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        if (args != null) {
            number = args.getString(EXTRA_NUMBER, "");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        menuStrings = new Sessions(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.
                dialog_mobile_check, null);
        head =(TextView)view.findViewById(R.id.correctNumber);
        yes =(TextView) view.findViewById(R.id.yes);
        if (menuStrings.isChange())
        {
            head.setText("Please check the number..!");
            yes.setText("Yes");
        }

        unbinder = ButterKnife.bind(this, view);
        loadData();
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    private void loadData() {
        txtNumber.setText(getString(R.string.number, number));
    }


    @OnClick( R.id.btnOk)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnOk:
                if (callback != null) {
                    dismiss();
                    callback.onOk(number);
                }
                break;

        }
    }

    public interface ActionCallback {
        void onOk(String number);
    }
}
