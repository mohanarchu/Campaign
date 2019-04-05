package com.video.aashi.campaign.affialation;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.video.aashi.campaign.R;
import com.video.aashi.campaign.sesssion.Sessions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class Affialation extends DialogFragment implements AffiModel {



    ProgressDialog progressDialog;
    @BindView(R.id.uAffialtionFrom)
    TextView affialtionfrom;
    @BindView(R.id.uAffialationTo)
    TextView affialtionto;
    @BindView(R.id.uAddress)
    EditText address;
    @BindView(R.id.uEducation)
    Spinner education;
    @BindView(R.id.uCaste)
    Spinner caste;
    @BindView(R.id.uMobile)
    EditText mobile;
    @BindView(R.id.updateAffi)
    CardView update;
    @BindView(R.id.lAffilationfrom)
      LinearLayout laffiFron;
    @BindView(R.id.lAffilationto)
    LinearLayout laffito;
    @BindView(R.id.lMobile)
    LinearLayout lmobile;
    @BindView(R.id.leducation)
    LinearLayout leducation;
    @BindView(R.id.lAddress)
    LinearLayout ladddress;
    @BindView(R.id.lcaste)
    LinearLayout lcaste;
    @BindView(R.id.headers)
            TextView headers;
    PopupWindow popupWindow;
    AlertDialog alertDialog2;
    String affiFrom,affiTo;
    affilatiomPresent affilatiomPresent;
    Sessions sessions;
    String citizenid;
    String[] allCastes = {"Select" ,"கொங்கு/Kongu","BC","SC/ST","FC","Others"};
    String[] edu = {"Select" ,"Not Educated","School level","Graduate","Professional","Post Graduate(PG)","Doctorate","Others"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_affialation, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
        ButterKnife.bind(this,view);
        affilatiomPresent = new affilatiomPresent(getActivity(),this);
        sessions = new Sessions(getActivity());
        Bundle bundle = this.getArguments();
        if (bundle != null)
        {
            String key  =bundle.getString("key");
            citizenid = bundle.getString("id");
            if (key.equals("1"))
            {
                headers.setText("MOBILE");
                lmobile.setVisibility(View.VISIBLE);
            }
            else if (key.equals("2"))
            {
                headers.setText("AFFILATION");
                laffiFron.setVisibility(View.VISIBLE);
                laffito.setVisibility(View.VISIBLE);
            }
            else if (key.equals("3"))
            {
                headers.setText("EDUCATION");
                leducation.setVisibility(View.VISIBLE);
            }
            else if(key.equals("4"))
            {
                headers.setText("CASTE");
                lcaste.setVisibility(View.VISIBLE);
            }
            else
            {
                lcaste.setVisibility(View.VISIBLE);
                lmobile.setVisibility(View.VISIBLE);
                leducation.setVisibility(View.VISIBLE);
                laffiFron.setVisibility(View.VISIBLE);
                laffito.setVisibility(View.VISIBLE);
                ladddress.setVisibility(View.VISIBLE);
            }
        }
        ArrayAdapter<String> qualit = new
                ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, allCastes);
        caste.setAdapter(qualit);
        ArrayAdapter<String> qualits = new
                ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, edu);
        education.setAdapter(qualits);
        affialtionfrom.setOnClickListener(v -> affilatiomPresent.getAffiList());

         affialtionto.setOnClickListener(v -> affilatiomPresent.getPartyList( ));
         update.setOnClickListener(v -> {
             String educations,mobiles,streets,afffi,affto,castes;
                 if (affialtionfrom.getText().toString().isEmpty())
                 {
                     afffi = "";
                 }
                 else
                 {
                    afffi=affiFrom;
                 }
                 if ( affialtionto.getText().toString().isEmpty())
                 {
                 affto="";
                 }
                 else
                 {
                 affto = affiTo;
                 }
                 if (caste.getSelectedItem().toString().equals("Select") )
                 {
                 castes ="";
                 }
                 else
                 {
                   castes = caste.getSelectedItem().toString();
                 }
                 if (education.getSelectedItem().toString().equals("Select") ) {
                     educations = "";
                 } else {
                     educations = education.getSelectedItem().toString();
                 }
                 if (mobile.getText().toString().isEmpty()) {
                     mobiles = "";
                 } else {
                     mobiles = mobile.getText().toString();
                 }
                 if (address.getText().toString().isEmpty()) {
                     streets = "";
                 } else {
                     streets = address.getText().toString();
                 }
                 JsonObject jsonObject = new JsonObject();
                 jsonObject.addProperty("CitizenRegister_Id", citizenid);
                 jsonObject.addProperty("User_Id", sessions.getId());
                 jsonObject.addProperty("Mobile_Number", mobiles);
                 jsonObject.addProperty("Caste", castes);
                 jsonObject.addProperty("Affiliated_Type", affiFrom);
                 jsonObject.addProperty("Affiliated_To", affiTo);
                 jsonObject.addProperty("Educational_Qualification", educations);
                 jsonObject.addProperty("Street", streets);
                 affilatiomPresent.updateAffialation( citizenid,sessions.getId(),mobiles,castes,afffi,affto,
                            educations,streets );
         });
    }
    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void showProgress() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("please wait..!");
        progressDialog.show();
    }
    @Override
    public void hideProgress() {
     progressDialog.dismiss();
    }
    @Override
    public void progressMessage(String message) {

    }
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
    @Override
    public void showAffi(ArrayList<AffiArray> affiArrays) {
        if (affiArrays != null && affiArrays.size() > 0) {
            String[] countryType = new String[affiArrays.size()];
            for (int i = 0; i < affiArrays.size(); i++) {
                countryType[i] = affiArrays.get(i).getName();
            }
            LayoutInflater inflater = getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.countrylist, null);
            popupWindow = new PopupWindow(dialogView, LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            final ListView county;
            county = dialogView.findViewById(R.id.country_list);
            county.setVisibility(View.VISIBLE);
            final TextView country;
            country = dialogView.findViewById(R.id.select_country);
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (getActivity(),
                            R.layout.listview, countryType);
            adapter.notifyDataSetChanged();
            county.setAdapter(adapter);
            county.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    affialtionfrom.setText(affiArrays.get(position).getName());
                    affiFrom = affiArrays.get(position).getId();
                    popupWindow.dismiss();
                }
            });
            popupWindow.showAsDropDown(affialtionfrom);
            dimBehind(popupWindow);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
        }
    }

    @Override
    public void showPartyy(ArrayList<AffiArray> affiArrays ) {
        if (affiArrays != null && affiArrays.size() > 0) {
            String[] countryType = new String[affiArrays.size()];
            for (int i = 0; i < affiArrays.size(); i++) {
                countryType[i] = affiArrays.get(i).getName()+"("+affiArrays.get(i).getNames()+")";
            }
            LayoutInflater inflater = getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.countrylist, null);
            popupWindow = new PopupWindow(dialogView,
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            final ListView county;
            county = dialogView.findViewById(R.id.country_list);
            county.setVisibility(View.VISIBLE);
            final TextView country;
            country = dialogView.findViewById(R.id.select_country);
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (getActivity(),
                            R.layout.listview, countryType);
            adapter.notifyDataSetChanged();
            county.setAdapter(adapter);

            county.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    affialtionto.setText(affiArrays.get(position).getName()+"("+affiArrays.get(position).getNames()+")" );
                    affiTo = affiArrays.get(position).getId();
                    popupWindow.dismiss();
                }
            });
            popupWindow.showAsDropDown(affialtionto);
            dimBehind(popupWindow);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
        }
    }

    @Override
    public void hide() {
        this.dismiss();
    }
    public static void dimBehind(PopupWindow popupWindow) {
        View container;
        if (popupWindow.getBackground() == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                container = (View) popupWindow.getContentView().getParent();
            } else {
                container = popupWindow.getContentView();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                container = (View) popupWindow.getContentView().getParent().getParent();
            } else {
                container = (View) popupWindow.getContentView().getParent();
            }
        }
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.2f;
        wm.updateViewLayout(container, p);
    }
}
