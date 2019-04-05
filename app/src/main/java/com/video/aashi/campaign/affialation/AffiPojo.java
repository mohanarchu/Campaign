package com.video.aashi.campaign.affialation;

import java.util.ArrayList;

public class AffiPojo {
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

    public ArrayList<AffiPojo.Response> getResponse() {
        return Response;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Status = "+Status+", Response = "+Response+"]";
    }
    public class Response
    {
        private String AffiliationType_Name;

        private String createdAt;

        private String IfDeleted;

        private String CreatedAt;

        private String __v;

        private String _id;

        private String UpdatedAt;

        private String updatedAt;

        public String getAffiliationType_Name ()
        {
            return AffiliationType_Name;
        }

        public void setAffiliationType_Name (String AffiliationType_Name)
        {
            this.AffiliationType_Name = AffiliationType_Name;
        }



        public String getIfDeleted ()
        {
            return IfDeleted;
        }

        public void setIfDeleted (String IfDeleted)
        {
            this.IfDeleted = IfDeleted;
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
