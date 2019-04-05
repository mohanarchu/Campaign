package com.video.aashi.campaign.votersview;

public class CitizenResponse {
    private String IfDeleted;

    private String DoorNumber;


    private String CreatedAt;

    private Image Image;

    private String VoterIdNumber;



    private String UpdatedAt;

    private String CandidateAge;

    private String ActiveStatus;

    private String CandidateSex;


    private String CandidateName;

    private String __v;

    private String Street;

    private  String Mobile_Number;

    private String Educational_Qualification;

    private  String Caste;

    private Affiliated_Type Affiliated_Type;

    private Affiliated_To Affiliated_To;

    private String ALL_DATA;
    private String _id;
    private String FatherName;
    public String getStreet() {
        return Street;
    }
    public String getCaste() {
        return Caste;
    }
    public CitizenResponse.Affiliated_To getAffiliated_To() {
        return Affiliated_To;
    }
    public CitizenResponse.Affiliated_Type getAffiliated_Type() {
        return Affiliated_Type;
    }
    public String getEducational_Qualification() {
        return Educational_Qualification;
    }
    public String getMobile_Number() {
        return Mobile_Number;
    }
    public String getIfDeleted ()
    {
        return IfDeleted;
    }
    public void setIfDeleted (String IfDeleted)
    {
        this.IfDeleted = IfDeleted;
    }
    public String getDoorNumber ()
    {
        return DoorNumber;
    }
    public void setDoorNumber (String DoorNumber)
    {
        this.DoorNumber = DoorNumber;
    }
    public String getCreatedAt ()
    {
        return CreatedAt;
    }
    public void setCreatedAt (String CreatedAt)
    {
        this.CreatedAt = CreatedAt;
    }
    public Image getImage ()
    {
        return Image;
    }
    public void setImage (Image Image)
    {
        this.Image = Image;
    }
    public String getVoterIdNumber ()
    {
        return VoterIdNumber;
    }
    public void setVoterIdNumber (String VoterIdNumber)
    {
        this.VoterIdNumber = VoterIdNumber;
    }
    public String getUpdatedAt ()
    {
        return UpdatedAt;
    }
    public void setUpdatedAt (String UpdatedAt)
    {
        this.UpdatedAt = UpdatedAt;
    }
    public String getCandidateAge ()
    {
        return CandidateAge;
    }
    public void setCandidateAge (String CandidateAge)
    {
        this.CandidateAge = CandidateAge;
    }
    public String getActiveStatus ()
    {
        return ActiveStatus;
    }
    public void setActiveStatus (String ActiveStatus)
    {
        this.ActiveStatus = ActiveStatus;
    }
    public String getCandidateSex ()
    {
        return CandidateSex;
    }
    public void setCandidateSex (String CandidateSex)
    {
        this.CandidateSex = CandidateSex;
    }
    public String getCandidateName ()
    {
        return CandidateName;
    }
    public void setCandidateName (String CandidateName)
    {
        this.CandidateName = CandidateName;
    }
    public String get__v ()
    {
        return __v;
    }
    public void set__v (String __v)
    {
        this.__v = __v;
    }
    public String getALL_DATA ()
    {
        return ALL_DATA;
    }
    public void setALL_DATA (String ALL_DATA)
    {
        this.ALL_DATA = ALL_DATA;
    }
    public String get_id ()
    {
        return _id;
    }
    public void set_id (String _id)
    {
        this._id = _id;
    }
    public String getFatherName ()
    {
        return FatherName;
    }
    public void setFatherName (String FatherName)
    {
        this.FatherName = FatherName;
    }
    public class Affiliated_To
    {
        private String PartyType_Name;
        private String _id;
        public String getPartyType_Name ()
        {
            return PartyType_Name;
        }
        public void setPartyType_Name (String PartyType_Name)
        {
            this.PartyType_Name = PartyType_Name;
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
            return "ClassPojo [PartyType_Name = "+PartyType_Name+", _id = "+_id+"]";
        }
    }
    public class Affiliated_Type
    {
        private String AffiliationType_Name;

        private String _id;

        public String getAffiliationType_Name ()
        {
            return AffiliationType_Name;
        }

        public void setAffiliationType_Name (String AffiliationType_Name)
        {
            this.AffiliationType_Name = AffiliationType_Name;
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
            return "ClassPojo [AffiliationType_Name = "+AffiliationType_Name+", _id = "+_id+"]";
        }
    }


    public class Image
    {
        private String filename;

        private String size;

        private String mimetype;

        public String getFilename ()
        {
            return filename;
        }

        public void setFilename (String filename)
        {
            this.filename = filename;
        }

        public String getSize ()
        {
            return size;
        }

        public void setSize (String size)
        {
            this.size = size;
        }

        public String getMimetype ()
        {
            return mimetype;
        }

        public void setMimetype (String mimetype)
        {
            this.mimetype = mimetype;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [filename = "+filename+", size = "+size+", mimetype = "+mimetype+"]";
        }
    }


}
