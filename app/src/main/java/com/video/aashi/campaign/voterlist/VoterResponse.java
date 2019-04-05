package com.video.aashi.campaign.voterlist;

public class VoterResponse { private String UpdatedBy;

    private String IfDeleted;

    private VotersFile VotersFile;

    private String CreatedBy;

    private String BoothNumber;

    private String CreatedAt;

    private String __v;

    private String Street;

    private String _id;

    private String UpdatedAt;

    public String getUpdatedBy ()
    {
        return UpdatedBy;
    }

    public void setUpdatedBy (String UpdatedBy)
    {
        this.UpdatedBy = UpdatedBy;
    }

    public String getIfDeleted ()
    {
        return IfDeleted;
    }

    public void setIfDeleted (String IfDeleted)
    {
        this.IfDeleted = IfDeleted;
    }

    public VotersFile getVotersFile ()
    {
        return VotersFile;
    }

    public void setVotersFile (VotersFile VotersFile)
    {
        this.VotersFile = VotersFile;
    }

    public String getCreatedBy ()
    {
        return CreatedBy;
    }

    public void setCreatedBy (String CreatedBy)
    {
        this.CreatedBy = CreatedBy;
    }

    public String getBoothNumber ()
    {
        return BoothNumber;
    }

    public void setBoothNumber (String BoothNumber)
    {
        this.BoothNumber = BoothNumber;
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

    public String getStreet ()
    {
        return Street;
    }

    public void setStreet (String Street)
    {
        this.Street = Street;
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

    @Override
    public String toString()
    {
        return "ClassPojo [UpdatedBy = "+UpdatedBy+", IfDeleted = "+IfDeleted+", VotersFile = "+VotersFile+", CreatedBy = "+CreatedBy+", BoothNumber = "+BoothNumber+", CreatedAt = "+CreatedAt+", __v = "+__v+", Street = "+Street+", _id = "+_id+", UpdatedAt = "+UpdatedAt+"]";
    }
    public class VotersFile
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
