package com.video.aashi.campaign.affialation;

import java.util.ArrayList;

public class PartyPojo {

    private String Status;

    private ArrayList<Response> Response;

    public String getStatus ()
    {
        return Status;
    }

    public void setStatus (String Status)
    {
        this.Status = Status;
    }

    public ArrayList<PartyPojo.Response> getResponse() {
        return Response;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Status = "+Status+", Response = "+Response+"]";
    }
    public class Response
    {
        private String createdAt;

        private String IfDeleted;

        private String PartyType_Code;

        private String PartyType_Name;

        private String CreatedAt;

        private String __v;

        private String _id;

        private String UpdatedAt;

        private String updatedAt;

        public String getCreatedAt ()
        {
            return createdAt;
        }

        public void setCreatedAt (String createdAt)
        {
            this.createdAt = createdAt;
        }

        public String getIfDeleted ()
        {
            return IfDeleted;
        }

        public void setIfDeleted (String IfDeleted)
        {
            this.IfDeleted = IfDeleted;
        }

        public String getPartyType_Code ()
        {
            return PartyType_Code;
        }

        public void setPartyType_Code (String PartyType_Code)
        {
            this.PartyType_Code = PartyType_Code;
        }

        public String getPartyType_Name ()
        {
            return PartyType_Name;
        }

        public void setPartyType_Name (String PartyType_Name)
        {
            this.PartyType_Name = PartyType_Name;
        }



        public String get__v ()
        {
            return __v;
        }

        public void set__v (String __v)
        {
            this.__v = __v;
        }

        public String get_id ()
        {
            return _id;
        }

        public void set_id (String _id)
        {
            this._id = _id;
        }




    }


}
