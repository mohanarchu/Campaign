package com.video.aashi.campaign.votersview;

public class CityList {

    public CityList()
    {

    }
        String name,id,image,age,alldata,fathername,ids,street,caste,mobilenumber,from,to,educaton,sex ;
       byte[] images;

   public CityList( String id,String name,byte[] images,String image, String age,String alldata,
                   String fathername,String ids,String street,String caste,String mobilenumber,String from,String to,
                   String education,String sex)
   {
       this.id = id;
       this.name = name;
       this.images = images;
       this.age = age;
       this.alldata = alldata;
       this.fathername = fathername;
       this.ids = ids;
       this.street = street;
       this.caste = caste;
       this.image = image;
       this.mobilenumber = mobilenumber;
       this.from = from;
       this.to = to;
       this.educaton = education;
       this.sex = sex;
   }

    public String getTo() {
        return to;
    }

    public String getIds() {
        return ids;
    }

    public String getName() {
        return name;
    }

    public String getFathername() {
        return fathername;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public String getAge() {
        return age;
    }

    public String getAlldata() {
        return alldata;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public String getCaste() {
        return caste;
    }

    public String getEducaton() {
        return educaton;
    }

    public String getFrom() {
        return from;
    }

    public String getSex() {
        return sex;
    }

    public String getStreet() {
        return street;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getImages() {
        return images;
    }

    public void setImages(byte[] images) {
        this.images = images;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setAlldata(String alldata) {
        this.alldata = alldata;
    }

    public void setFathername(String fathername) {
        this.fathername = fathername;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setEducaton(String educaton) {
        this.educaton = educaton;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
