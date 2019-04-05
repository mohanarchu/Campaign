package com.video.aashi.campaign.sesssion;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Sessions {

    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean change;
    String boothname,boothid,boothnuber,name,mobileumber,id;
    boolean loginKey;

    public Sessions(Context context)
    {
        this.context = context;
    }
    public  void  setSharedPreferences()
    {
        sharedPreferences = context.getSharedPreferences("Sessions",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }
    public void apply()
    {
        editor.apply();
    }

    public boolean isLoginKey() {
        setSharedPreferences();
        loginKey = sharedPreferences.getBoolean("Login",false);
        return loginKey;
    }

    public void setLoginKey(boolean loginKey) {
        setSharedPreferences();
        editor.putBoolean("Login",loginKey);
        apply();


        this.loginKey = loginKey;
    }

    public boolean isChange() {
        setSharedPreferences();
        change = sharedPreferences.getBoolean("Language",false);
        return change;
    }
    public void setChange(boolean change) {
         setSharedPreferences();
         editor.putBoolean("Language",change);
         apply();
         this.change = change;
    }
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)context. getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void setName(String name) {
        setSharedPreferences();
        editor.putString("Name",name);
        apply();



        this.name = name;
    }

    public String getName() {
        setSharedPreferences();
        name = sharedPreferences.getString("Name","");
        return name;
    }

    public String getBoothid() {
        setSharedPreferences();
        boothid = sharedPreferences.getString("BoothId","");
        return boothid;
    }

    public void setBoothid(String boothid) {
        setSharedPreferences();
        editor.putString("BoothId",boothid);
        apply();

        this.boothid = boothid;
    }

    public String getBoothname() {
        setSharedPreferences();
        boothname = sharedPreferences.getString("BoothName","");
        return boothname;
    }

    public void setBoothname(String boothname) {
        setSharedPreferences();
        editor.putString("BoothName",boothname);
        apply();

        this.boothname = boothname;
    }

    public String getBoothnuber() {
        setSharedPreferences();
        boothnuber = sharedPreferences.getString("bNumber","");

        return boothnuber;
    }

    public void setBoothnuber(String boothnuber) {
        setSharedPreferences();
        editor.putString("bNumber",boothnuber);
        apply();

        this.boothnuber = boothnuber;
    }

    public String getMobileumber() {
        setSharedPreferences();
        mobileumber = sharedPreferences.getString("Mobile","");
        return mobileumber;
    }

    public void setMobileumber(String mobileumber) {
        setSharedPreferences();
        editor.putString("Mobile",mobileumber);
        apply();
        this.mobileumber = mobileumber;
    }

     public String getId() {
          setSharedPreferences();
          id =sharedPreferences.getString("id","");

        return id;
    }

    public void setId(String id) {
        setSharedPreferences();
        editor.putString("id",id);
        apply();

        this.id = id;
    }

    public  void clearAll()
    {
        sharedPreferences.edit().clear().apply();
    }
}
