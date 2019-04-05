package com.video.aashi.campaign.affialation;

public class SuccessPojo {

    private String Status;

    private String Message;

    public String getStatus ()
    {
        return Status;
    }

    public void setStatus (String Status)
    {
        this.Status = Status;
    }

    public String getMessage ()
    {
        return Message;
    }
    public void setMessage (String Message)
    {
        this.Message = Message;
    }
    @Override
    public String toString()
    {
        return "ClassPojo [Status = "+Status+", Message = "+Message+"]";
    }
}
