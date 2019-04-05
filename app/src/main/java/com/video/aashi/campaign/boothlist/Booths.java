package com.video.aashi.campaign.boothlist;

public class Booths {

    String id,boothname,nboothnumber;
    public Booths(String id,String boothname,String nboothnumber)
    {
        this.id  =id;
        this.boothname = boothname;
        this.nboothnumber = nboothnumber;
    }

    public String getBoothname() {
        return boothname;
    }

    public String getId() {
        return id;
    }

    public String getNboothnumber() {
        return nboothnumber;
    }
}
