package com.video.aashi.campaign.affialation;

import com.video.aashi.campaign.view.ViewIntract;

import java.util.ArrayList;

public interface AffiModel extends ViewIntract {
    @Override
    void showProgress();

    @Override
    void hideProgress();

    @Override
    void progressMessage(String message);

    void showAffi(ArrayList<AffiArray> affiArrays );

    void showPartyy(ArrayList<AffiArray> affiArrays );

    void hide();

    void showToast(String message);

}
