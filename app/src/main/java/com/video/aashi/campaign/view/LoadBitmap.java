package com.video.aashi.campaign.view;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.video.aashi.campaign.localdb.ListDB;
import com.video.aashi.campaign.votersview.CityList;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Random;
public class LoadBitmap
{



    ListDB listDB;
    Context context;
   String id;
    public LoadBitmap(Context context  )
    {

        this.context = context;

    }
    String path,urls;
    public class Task extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            File file = null;
            try {
                URL url = new URL(strings[0]);
                URLConnection urlConnection = url.openConnection();
                String contentType = urlConnection.getContentType();
                InputStream urlStream = url.openStream();
                Random random = new Random();
                 int x = random.nextInt(10000000) + 1;
                String tempFileName =   "image" + x;
                file = File.createTempFile(tempFileName, "");
                file.deleteOnExit();
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buf = new byte[2048];
                int numOfBytesRead = 0;
                while ((numOfBytesRead = urlStream.read(buf)) != -1) {
                    fos.write(buf, 0, numOfBytesRead);
                }
                fos.flush();
                fos.close();
                urlStream.close();
            } catch (Exception e) {
                if (file != null)
                {
                    file.delete();
                    file = null;
                }
            }
            assert file != null;

            setPaths(file.getPath());
            Bitmap bitmap = BitmapFactory.decodeFile(path);

            listDB = new ListDB(context);
            CityList cityList = new CityList();

            cityList.setImages(getBytes(bitmap));
            listDB.updateImage(cityList,strings[1]);
            Log.i("TAG","Cantread"+ Arrays.toString(getBytes(bitmap)));

            return file.getPath();
        }
        @Override
        protected void onPostExecute(String s)
        {
             setPaths(s);
        //    Log.i("TAG", "Cantread" + s);
            super.onPostExecute(s);
        }
    }
    public byte[] getBytes(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        return stream.toByteArray();
    }
    public String getPath() {
        return path;
    }
    public void setPath(String s,String id) {
        new Task().execute(s,id);
    }
    public void setPaths(String path) {
        this.path = path;
    }

}
