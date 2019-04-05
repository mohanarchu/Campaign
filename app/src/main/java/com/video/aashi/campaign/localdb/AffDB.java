package com.video.aashi.campaign.localdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AffDB extends SQLiteOpenHelper {
    public static String DB_NAME = "product";
    private static int DB_VERSION = 1;
    public static final String TABLE_NAME = "produc";
    public  static  final String PRODUCT_ID = "product_id";
    public static final String SERIAL_NO = "serial_no";
    public static final String P_NAME = "product_name";


    public AffDB(Context context ) {

        super(context, DB_NAME, null, DB_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME
                + "(" + SERIAL_NO +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " + P_NAME +
                " VARCHAR, " + PRODUCT_ID +
                " VARCHAR "+
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }


    public void addItems(String productname,String productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(P_NAME, productname);
        contentValues.put(PRODUCT_ID,productId);
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }
    public List<proLocalArray> getAllContacts()
    {
        List<proLocalArray> contactList = new ArrayList<proLocalArray>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                proLocalArray product = new proLocalArray();
                product.setId(cursor.getString(2));
                product.setName(cursor.getString(1));
                contactList.add(product);
            } while (cursor.moveToNext());
        }
        return contactList;
    }
    public int updateContact(String  productname,String serial ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(P_NAME, productname);
        Log.i("TAG","Productss" + serial );
        return db.update(TABLE_NAME, contentValues, PRODUCT_ID + " = ?",
                new String[]{String.valueOf(serial)});
    }
    public Cursor getId(String  id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM produc WHERE product_id = " + "'"+ id +"'";
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
}
