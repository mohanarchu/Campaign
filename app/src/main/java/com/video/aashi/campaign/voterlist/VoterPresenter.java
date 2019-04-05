package com.video.aashi.campaign.voterlist;

import java.util.ArrayList;

public interface VoterPresenter {
    void  showProgress();
    void hideProgress();
    void setProgressMessage(String message);
    void showToast(String  string);
    void showStreetss(ArrayList<Vote> stretLists);

}
