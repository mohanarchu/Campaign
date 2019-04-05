package com.video.aashi.campaign.voterlist;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;
import com.video.aashi.campaign.PdfReader;

import com.video.aashi.campaign.R;
import com.video.aashi.campaign.pdf.PdfRenderActivity;
import com.video.aashi.campaign.sesssion.Sessions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;
import butterknife.BindView;
import butterknife.ButterKnife;
import static android.content.Context.DOWNLOAD_SERVICE;
public class VotersList extends Fragment implements VoterPresenter,OnLoadCompleteListener,OnPageChangeListener,OnPageErrorListener {


    private DownloadManager mgr=null;
    private long lastDownload=-1L;
    @BindView(R.id.voterRecycle)
    RecyclerView voterRecelr;
    Voters voters;
    ProgressDialog progressDialog;
    @BindView(R.id.webview1)
    WebView wv;
    @BindView(R.id.download)
    FloatingActionButton download;
    @BindView(R.id.pdfView)
    PDFView pdfView;
    String ids = "1";
    Sessions sessions;
    String filenames = "";
    String TAG = "PDF";
    String muUri;
    String key = "2";
    int pagenumber = 0;
    public static int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE= 1;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_voters_list, container, false);
        ButterKnife.bind(this,view);
        voters = new Voters(getActivity(),VotersList.this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        voterRecelr.setLayoutManager(layoutManager);
        sessions = new Sessions(getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mgr=(DownloadManager)getActivity(). getSystemService(DOWNLOAD_SERVICE);
        }
//       getActivity(). registerReceiver(onComplete,
//                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
//       getActivity(). registerReceiver(onNotificationClick,
//                new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String id = bundle.getString("id", "");
            ids = bundle.getString("ids", "");
             key = bundle.getString("key","");
             wv.setVisibility(View.VISIBLE);
            pdfView.setVisibility(View.GONE);

            if (key.equals("1"))
            {
                Uri path = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
                        + "/"+id);//Uri.parse(Environment.getExternalStorageDirectory().toString() + "/data/test.pdf");
                //     wv.loadUrl("file:///android_asset/pdfviewer/index.html?file=/"+uri);
                wv.setWebChromeClient(new WebChromeClient());
                WebSettings settings = wv   .getSettings();
                settings.setJavaScriptEnabled(true);
                    settings.setAllowFileAccessFromFileURLs(true);
                    settings.setAllowUniversalAccessFromFileURLs(true);
                settings.setBuiltInZoomControls(true);
                wv.setWebChromeClient(new WebChromeClient());
               //z   Uri path = Uri.parse(Environment.getExternalStorageDirectory().toString() + "/data/test.pdf");
                wv.getSettings().setJavaScriptEnabled(true);
                wv.getSettings().setAllowFileAccess(true);
                wv.getSettings().setPluginState(WebSettings.PluginState.ON);
                try {
                    InputStream ims = getActivity(). getAssets().open("pdfviewer/index.html");
                    String line = getStringFromInputStream(ims);
                    if(line.contains("THE_FILE")) {
                        line = line.replace("THE_FILE",path.toString());
                        FileOutputStream fileOutputStream = getActivity(). openFileOutput("index.html", Context.MODE_PRIVATE);
                        fileOutputStream.write(line.getBytes());
                        fileOutputStream.close();
                        Log.i("TAG","PdfFiles"+ line);
                    }
                }
                catch (IOException e) {

                     e.printStackTrace();
                }
                wv.loadUrl("file://" + getActivity().getFilesDir() + "/index.html");




            }
            else
            {
                voters.getStreets(id);
            }

        }
        else
        {
            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
            }

        }
        download.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
            else
                {
                new RetrieveFeedTask().execute(filenames);
               // getFromUri(Uri.parse(filenames));
            }

        });


        return view;
    }
    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
    @Override
    public void showProgress() {
        progressDialog = new ProgressDialog(    getActivity());
        progressDialog.show();
    }
    @Override
    public void hideProgress() {
     progressDialog.dismiss();
    }
    @Override
    public void setProgressMessage(String message) {
        progressDialog.setMessage(message);
    }
    @Override
    public void showToast(String string) {
       Toast.makeText(getActivity(),string,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showStreetss(ArrayList<Vote> stretLists) {
        if (stretLists.size() == 0)
        {
            showToast("No pdf available");
        }
        else
        {
            String path ="http://192.168.1.110:5000/API/Uploads/VotersFile/";
//            String doc="<iframe src='http://docs.google.com/viewer?url="+path+stretLists.get(0).getVoterfile()+"&embedded=true' width='100%' height='100%'  style='border: none;'></iframe>";
//            //Log.d("TAG","MyPdfFiles" +getFileFromUrl(path+stretLists.get(0).getVoterfile()));
//            wv.setVisibility(WebView.VISIBLE);
//            wv.getSettings().setJavaScriptEnabled(true);
//            wv.getSettings().setAllowFileAccess(true);
//            wv.getSettings().setPluginState(WebSettings.PluginState.ON);
//            //  wv.loadUrl("http://159.89.163.3:5000/API/Uploads/VotersFile/" + stretLists.get(0).getVoterfile());
//             wv.loadData(doc, "text/html", "UTF-8");
//             filenames = path+stretLists.get(0).getVoterfile();
//             String uriString;
//             Uri uri = Uri.parse(filenames);
//            PdfRendererBasicFragment votersList= new PdfRendererBasicFragment();
//            Bundle bundle = new Bundle();
//            bundle.putString("id",filenames);
//            votersList.setArguments(bundle);
//            getFragmentManager().beginTransaction().replace(R.id.myFrame,votersList).addToBackStack(null).commit();
          new  RetrieveFeedTask().execute(path+stretLists.get(0).getVoterfile());
        }
    }

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        Log.e(TAG, "title = " + meta.getTitle());
        Log.e(TAG, "author = " + meta.getAuthor());
        Log.e(TAG, "subject = " + meta.getSubject());
        Log.e(TAG, "keywords = " + meta.getKeywords());
        Log.e(TAG, "creator = " + meta.getCreator());
        Log.e(TAG, "producer = " + meta.getProducer());
        Log.e(TAG, "creationDate = " + meta.getCreationDate());
        Log.e(TAG, "modDate = " + meta.getModDate());

    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pagenumber = page;
       getActivity().   setTitle(String.format("%s %s / %s", "KMDK CM", page + 1, pageCount));
    }

    @Override
    public void onPageError(int page, Throwable t) {

        showToast(t.toString());

    }


    class RetrieveFeedTask extends AsyncTask<String, Void, String> {
        File file = null;
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);

                URLConnection urlConnection = url.openConnection();
                String contentType = urlConnection.getContentType();

                if (contentType.equalsIgnoreCase("application/pdf")) {
                    InputStream urlStream = url.openStream();
                    String tempFileName = url.getPath().substring(url.getPath().lastIndexOf('/') + 1);
                    file = File.createTempFile(tempFileName, "");
                    // If the temp file needs to be deleted upon
                    // termination of this application
                    file.deleteOnExit();
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] buf = new byte[2048];
                    int numOfBytesRead = 0;
                    while ((numOfBytesRead = urlStream.read(buf)) != -1) {
                        fos.write(buf,0,numOfBytesRead);
                    }

                    fos.flush();
                    fos.close();
                    urlStream.close();
                } else {
                    System.out.println("URL does not resolve to a PDF file");
                }

            } catch (Exception e) {
                if (file != null) {
                    file.delete();
                    file = null;
                }
            }

            return  null;
        }



        //  startDownload(strings[0]);


        @Override
        protected void onPostExecute(String s) {
//            PdfRendererBasicFragment votersList= new PdfRendererBasicFragment();
//            Bundle bundle = new Bundle();
//            bundle.putString("id",file.toString());
//            votersList.setArguments(bundle);
//            getFragmentManager().beginTransaction().replace(R.id.myFrame,votersList).addToBackStack(null).commit();
      //      Intent intent = new Intent(getActivity(), PdfRendererBasicFragment)
            Intent intent = new Intent(getActivity(),PdfRenderActivity.class);
            intent.putExtra("id",file.toString());
            startActivity(intent);
            super.onPostExecute(s);
        }
    }


    void getFromUri(Uri file)
    {


    }
    @Override
    public void onStop() {
        super.onStop();
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onPause() {
      //  getActivity().unregisterReceiver(onComplete);
        super.onPause();
    }
    public class BoothHolders extends RecyclerView.Adapter<ViewHolders>
    {
        ArrayList<Vote> booths;
        public BoothHolders (ArrayList<Vote> booths)
        {
            this.booths = booths;
        }
        @NonNull
        @Override
        public ViewHolders onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view   = LayoutInflater.from(getActivity()).inflate(R.layout.votedesign, viewGroup, false);
            return new ViewHolders(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolders viewHolder, int i) {

            viewHolder.streetName.setText(booths.get(i).getVoterfile());
            viewHolder.getpdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),PdfReader.class);
                    intent.putExtra("pdf",booths.get(i).getVoterfile() );
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return booths.size();
        }
    }
    public class  ViewHolders extends RecyclerView.ViewHolder {
        @BindView(R.id.streetNames)
        TextView streetName;
        @BindView(R.id.getPdf)
        CardView getpdf;

        public ViewHolders(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    public void startDownload(String v) {
        Uri uri=Uri.parse(v);
        Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .mkdirs();
        Random generator = new Random();
        int n = 1000;
        n = generator.nextInt(n);
        String fname = "KMDK-CM-"+ ids +".pdf";
//        lastDownload=
//                mgr.enqueue(new DownloadManager.Request(uri)
//                        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
//                                DownloadManager.Request.NETWORK_MOBILE)
//                        .setAllowedOverRoaming(false)
//                        .setTitle("Demo")
//                        .setDescription("Something useful. No, really.")
//                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
//                                fname));
        String url = v;
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |  DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Example");
        request.setDescription("Downloading a very large zip");
        request.setVisibleInDownloadsUi(false);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        request.setDestinationInExternalFilesDir(getActivity(), null, fname);
        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);

    }
    public void queryStatus() {
      Cursor c=mgr.query(new DownloadManager.Query().setFilterById(lastDownload));
       if (c==null)
       {
          Toast.makeText(getActivity(), "Download not found!", Toast.LENGTH_LONG).show();
       }
       else
       {
            c.moveToFirst();
            Log.d(getClass().getName(), "COLUMN_ID: "+
                    c.getLong(c.getColumnIndex(DownloadManager.COLUMN_ID)));
            Log.d(getClass().getName(), "COLUMN_BYTES_DOWNLOADED_SO_FAR: "+
                    c.getLong(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)));
            Log.d(getClass().getName(), "COLUMN_LAST_MODIFIED_TIMESTAMP: "+
                    c.getLong(c.getColumnIndex(DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP)));
            Log.d(getClass().getName(), "COLUMN_LOCAL_URI: "+
                    c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)));
            Log.d(getClass().getName(), "COLUMN_STATUS: "+
                    c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS)));
            Log.d(getClass().getName(), "COLUMN_REASON: "+
                    c.getInt(c.getColumnIndex(DownloadManager.COLUMN_REASON)));
            Toast.makeText(getActivity(), statusMessage(c), Toast.LENGTH_LONG).show();
       }
    }
    public void viewLog(View v) {
        startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
    }
    private String statusMessage(Cursor c) {
        String msg="???";
        switch(c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
            case DownloadManager.STATUS_FAILED:
                msg="Download failed!";
                break;
            case DownloadManager.STATUS_PAUSED:
                msg="Download paused!";
                break;
            case DownloadManager.STATUS_PENDING:
                msg="Download pending!";
                break;
            case DownloadManager.STATUS_RUNNING:
                msg="Download in progress!";
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                msg="Download complete!";
                break;
            default:
                msg="Download is nowhere in sight";
                break;
        }
        return(msg);
    }
    BroadcastReceiver onComplete=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            if (sessions.isChange())
            {
                Toast.makeText(ctxt, "Download completed..!", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(ctxt, "பதிவிறக்கப்பட்டது ..!", Toast.LENGTH_LONG).show();
            }
            viewLog(getView());
        }
    };
    BroadcastReceiver onNotificationClick=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            Toast.makeText(ctxt, "Ummmm...hi!", Toast.LENGTH_LONG).show();
        }
    };
}
