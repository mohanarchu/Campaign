package com.video.aashi.campaign.votersview;

import java.util.ArrayList;

public class CitizenPojo {
    private String Status;

    private ArrayList<CitizenResponse> Response;

    public String getStatus ()
    {
        return Status;
    }

    public void setStatus (String Status)
    {
        this.Status = Status;
    }

    public ArrayList<CitizenResponse> getResponse() {
        return Response;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Status = "+Status+", Response = "+Response+"]";
    }
}
