package com.video.aashi.campaign.affialation;

public class AffiArray {

    String name,id,names;
    public AffiArray(String name,String names,String id)
    {
        this.name = name;
        this.id = id;
        this.names = names;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNames() {
        return names;
    }
}
