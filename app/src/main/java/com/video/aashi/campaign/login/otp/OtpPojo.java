package com.video.aashi.campaign.login.otp;

import java.util.ArrayList;

public class OtpPojo {
    private String Status;

    private Response Response;

    public String getStatus ()
    {
        return Status;
    }

    public void setStatus (String Status)
    {
        this.Status = Status;
    }

    public com.video.aashi.campaign.login.otp.Response getResponse() {
        return Response;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Status = "+Status+", Response = "+Response+"]";
    }
}
