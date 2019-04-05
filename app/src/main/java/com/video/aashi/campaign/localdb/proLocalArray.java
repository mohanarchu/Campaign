package com.video.aashi.campaign.localdb;

public class proLocalArray {


    public proLocalArray()
    {

    }

    String id,name;
    public proLocalArray(String id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
}
