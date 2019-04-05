package com.video.aashi.campaign.votersview;

import com.video.aashi.campaign.view.ViewIntract;

import java.util.ArrayList;

public interface CityModel extends ViewIntract {


    @Override
    void showProgress();

    @Override
    void hideProgress();

    @Override
    void progressMessage(String message);

   void showToast(String message);

   void showList(ArrayList<CityList> cityLists );



}
