package com.video.aashi.campaign.localdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.video.aashi.campaign.votersview.CityList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListDB extends SQLiteOpenHelper {
    public static String DB_NAME = "listss";
    private static int DB_VERSION = 1;
    public static final String TABLE_NAME = "list";

    public  static  final String PRODUCT_ID = "id";
    public static final String SERIAL_NO = "number";
    public static final String P_NAME = "name";
    public static final String F_NAME = "fname";
    public static final String AGE = "age";
    public static final String ALLID = "allss";
    public static final String SEX = "sex";
    public static final String CASTE = "caste";
    public static final String AFFIALATION = "affilation";
    public static final String EDUCATION = "education";
    public static final String PHONE = "phone";
    public static final String IMAGE = "image";
    public static final String ALLDATA = "alls";
    public static final String BOOTH_ID ="booth_id";
    public ListDB(Context context ) {

        super(context, DB_NAME, null, DB_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME
                + "(" + SERIAL_NO +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + P_NAME +
                " VARCHAR, " + PRODUCT_ID +
                " VARCHAR, "+ F_NAME +
                " VARCHAR, " + AGE +
                " VARCHAR, "+SEX +
                " VARCHAR, " + CASTE +
                " VARCHAR, "+AFFIALATION +
                " VARCHAR, " + EDUCATION +
                " VARCHAR, "+PHONE +
                " VARCHAR, " + IMAGE +
                " BLOB, " + ALLDATA +
                " VARCHAR, "+ ALLID +
                " VARCHAR, " + BOOTH_ID +
                " VARCHAR " +
                 ");";
        db.execSQL(sql);
//
//        ci.setIds(cursor.getString(3));
//        ci.setId(cursor.getString(2));
//        ci.setName(cursor.getString(1));
//        ci.setFathername(cursor.getString(4));
//        ci.setAge(cursor.getString(5));
//        ci.setSex(cursor.getString(6));
//        ci.setTo(cursor.getString(7));
//        ci.setEducaton(cursor.getString(8));
//        ci.setMobilenumber(cursor.getString(9));
//        ci.setImages(cursor.getBlob(10));
//        ci.setAlldata(cursor.getString(11));
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void addItems(CityList cityList,String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(P_NAME, cityList.getName());
        contentValues.put(PRODUCT_ID,cityList.getId() );
        contentValues.put(F_NAME,cityList.getFathername() );
        contentValues.put(AGE,cityList.getAge() );
        contentValues.put(SEX,cityList.getSex());
        contentValues.put(CASTE,cityList.getCaste() );
        contentValues.put(AFFIALATION,cityList.getFrom()+"("+cityList.getTo()+")" );
        contentValues.put(EDUCATION,cityList.getEducaton() );
        contentValues.put(PHONE,cityList.getMobilenumber());
        contentValues.put(ALLID,cityList.getIds());
        contentValues.put(BOOTH_ID,id);
      //  contentValues.put(IMAGE,cityList.getImage());
        contentValues.put(ALLDATA,cityList.getAlldata());
        Log.i("TAG","Productss : " +
                " Name " + cityList.getName() +
                "ID : " + cityList.getId()+
                "IDs :" + cityList.getIds()+
                "Father :" + cityList.getFathername()+
                "AGE : " +cityList.getAge() +
                "SEX :" + cityList.getSex()+
                "To :" + cityList.getFrom()+"("+cityList.getTo()+")" +
                "Eduucation : " + cityList.getEducaton() +
                "Mobile :" + cityList.getMobilenumber()+
                "Image :" + cityList.getImages()+
                "All data " + cityList.getAlldata());
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }
    public List<CityList> getAllDatas(String id )
    {
        List<CityList> contactList = new ArrayList<CityList>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                CityList ci = new CityList();
                if (cursor.getString(13) .equals(id))
                {
                    Log.i("TAG","BoothIds"+ id);
                    ci.setIds(cursor.getString(12));
                    ci.setId(cursor.getString(2));
                    ci.setName(cursor.getString(1));
                    ci.setFathername(cursor.getString(3));
                    ci.setAge(cursor.getString(4));
                    ci.setSex(cursor.getString(5));
                    ci.setTo(cursor.getString(7));
                    ci.setEducaton(cursor.getString(8));
                    ci.setMobilenumber(cursor.getString(9));
                    ci.setImages(cursor.getBlob(10));
                    ci.setAlldata(cursor.getString(11));
                    ci.setCaste(cursor.getString(6));
                    Log.i("TAG","Productss :" +
                            "Serial no : " + cursor.getString(0)+
                            " Name " +cursor.getString(1) +
                            "ID : " +cursor.getString(2)+
                            "IDs :" + cursor.getString(12) +
                            "Father " +cursor.getString(3)+
                            "Caste :" +cursor.getString(6)+
                            "AGE : " +cursor.getString(4)+
                            "SEX :" + cursor.getString(5)+
                            "To :" + cursor.getString(7)+
                            "Eduucation : " +cursor.getString(8)+
                            "Mobile :" + cursor.getString(9)+

                            "All data " +cursor.getString(11));
                    contactList.add(ci);
                }
            } while (cursor.moveToNext());
        }
        return contactList;
    }
    public int updateContact(CityList cityList,String id )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(P_NAME, cityList.getName());
        contentValues.put(PRODUCT_ID,cityList.getId() );
        contentValues.put(F_NAME,cityList.getFathername() );
        contentValues.put(AGE,cityList.getAge() );
        contentValues.put(SEX,cityList.getSex());
        contentValues.put(CASTE,cityList.getCaste() );
        contentValues.put(AFFIALATION,cityList.getFrom()+"("+cityList.getTo()+")");
        contentValues.put(EDUCATION,cityList.getEducaton() );
        contentValues.put(PHONE,cityList.getMobilenumber());
        contentValues.put(ALLID,cityList.getIds());
      //   contentValues.put(IMAGE,cityList.getImages());
        contentValues.put(ALLDATA,cityList.getAlldata());
        return db.update(TABLE_NAME, contentValues, ALLID + " = ?",
                new String[]{String.valueOf(id)});
    }
    public Cursor getId(String  id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM list WHERE allss = " + "'"+ id +"'";
        Cursor  cursor = db.rawQuery(query,null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor getIds(String  id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM list WHERE booth_id = " + "'"+ id +"'";
        Cursor  cursor = db.rawQuery(query,null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public  void deleteTables()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql=  "DELETE FROM "+ TABLE_NAME ;
        db.execSQL(sql);
        db.close();
    }
    public int updateImage(CityList contact,String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put( IMAGE , contact.getImages());
    //     Log.i("TAG","Productss" + contact.getImages()+id );
        return db.update(TABLE_NAME, values, ALLID + " = ?",
                new String[] { String.valueOf(id) });
    }
}
