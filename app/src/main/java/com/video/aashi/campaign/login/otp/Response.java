package com.video.aashi.campaign.login.otp;

import java.util.ArrayList;

public class Response
{


    private String MobileNumber;

    private String IfDeleted;



    private ArrayList<BoothNumber> BoothNumber;

    private String CreatedAt;

    private String __v;

    private String OTP;

    private String _id;

    private String UpdatedAt;

    private String Name;



    public String getMobileNumber ()
    {
        return MobileNumber;
    }

    public void setMobileNumber (String MobileNumber)
    {
        this.MobileNumber = MobileNumber;
    }

    public String getIfDeleted ()
    {
        return IfDeleted;
    }

    public void setIfDeleted (String IfDeleted)
    {
        this.IfDeleted = IfDeleted;
    }


    public ArrayList<Response.BoothNumber> getBoothNumber() {
        return BoothNumber;
    }

    public String getCreatedAt ()
    {
        return CreatedAt;
    }

    public void setCreatedAt (String CreatedAt)
    {
        this.CreatedAt = CreatedAt;
    }

    public String get__v ()
    {
        return __v;
    }

    public void set__v (String __v)
    {
        this.__v = __v;
    }

    public String getOTP ()
    {
        return OTP;
    }

    public void setOTP (String OTP)
    {
        this.OTP = OTP;
    }

    public String get_id ()
    {
        return _id;
    }

    public void set_id (String _id)
    {
        this._id = _id;
    }

    public String getUpdatedAt ()
    {
        return UpdatedAt;
    }

    public void setUpdatedAt (String UpdatedAt)
    {
        this.UpdatedAt = UpdatedAt;
    }

    public String getName ()
    {
        return Name;
    }

    public void setName (String Name)
    {
        this.Name = Name;
    }


    public class BoothNumber
    {
        private String BoothNumber;

        private String BoothName;

        private String _id;

        public String getBoothNumber ()
        {
            return BoothNumber;
        }

        public void setBoothNumber (String BoothNumber)
        {
            this.BoothNumber = BoothNumber;
        }

        public String getBoothName ()
        {
            return BoothName;
        }

        public void setBoothName (String BoothName)
        {
            this.BoothName = BoothName;
        }

        public String get_id ()
        {
            return _id;
        }

        public void set_id (String _id)
        {
            this._id = _id;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [BoothNumber = "+BoothNumber+", BoothName = "+BoothName+", _id = "+_id+"]";
        }
    }


}
			
			