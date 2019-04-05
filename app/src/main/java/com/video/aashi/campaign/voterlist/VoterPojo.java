package com.video.aashi.campaign.voterlist;

import java.util.ArrayList;

public class VoterPojo {
    private String Status;

    private ArrayList<VoterResponse> Response;

    public String getStatus ()
    {
        return Status;
    }

    public void setStatus (String Status)
    {
        this.Status = Status;
    }

    public ArrayList<VoterResponse> getResponse() {
        return Response;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Status = "+Status+", Response = "+Response+"]";
    }
}
