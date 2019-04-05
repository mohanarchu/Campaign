package com.video.aashi.campaign.pdf;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.video.aashi.campaign.R;
import com.video.aashi.campaign.Urls.APIUrl;
import com.video.aashi.campaign.sesssion.Sessions;
import com.video.aashi.campaign.voterlist.Vote;
import com.video.aashi.campaign.voterlist.VoterPresenter;
import com.video.aashi.campaign.voterlist.Voters;
import com.video.aashi.campaign.voterlist.VotersList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PdfRenderActivity extends AppCompatActivity implements VoterPresenter,View.OnTouchListener {
    @BindView(R.id.pdf_image) ImageView imageViewPdf;
    @BindView(R.id.button_pre_doc) FloatingActionButton prePageButton;
    @BindView(R.id.button_next_doc) FloatingActionButton nextPageButton;
    @BindView(R.id.downDocumet)FloatingActionButton download;
    private static String FILENAME ="sample.pdf";
    private int pageIndex;
    private PdfRenderer pdfRenderer;
    private PdfRenderer.Page currentPage;
    private ParcelFileDescriptor parcelFileDescriptor;
    ProgressDialog progressDialog;
    Voters voters;
    String id;
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    private static final float MIN_ZOOM = 1f,MAX_ZOOM = 1f;
    String TAG = "ZOOM";
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    String ids;
    Sessions sessions;
    String key = "2";
    String filenames;
    private DownloadManager mgr=null;
    private long lastDownload=-1L;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pdf_renderer_basic);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        voters = new Voters(getApplicationContext(),this);
        FILENAME = intent.getStringExtra("id");
        sessions = new Sessions(this);
        key = intent.getStringExtra("key");
        id = intent.getStringExtra("id");
        if (key.equals("1"))
        {
            download.setVisibility(View.GONE);
            Uri path = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
                    + "/"+id);
            Log.i("TAG","Pdfsss"+path.getPath());
            try {
                openRenderer(getApplicationContext(),new File(path.getPath()));
                showPage(pageIndex);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            mgr=(DownloadManager)getSystemService(DOWNLOAD_SERVICE);
            registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            registerReceiver(onNotificationClick,
                    new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
            download.setVisibility(View.VISIBLE);
            ids = intent.getStringExtra("ids");
            voters.getStreets(id);
        }
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                      startDownload(filenames);
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStart() {
        super.onStart();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onStop() {
        //      closeRenderer();
        super.onStop();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

         if (key.equals("2"))
         {
             unregisterReceiver(onComplete);
             unregisterReceiver(onNotificationClick);
         }

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.button_pre_doc)
    public void onPreviousDocClick(){
        showPage(currentPage.getIndex() - 1);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick(R.id.button_next_doc)
    public void onNextDocClick(){
        showPage(currentPage.getIndex() + 1);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void openRenderer(Context context,File file) throws IOException {
        // In this sample, we read a PDF from the assets directory.
       // File file = new File(context.getCacheDir(), FILENAME);
        Log.i("TAG","MyPdfs"+file);
        if (!file.exists()) {
            // Since PdfRenderer cannot handle the compressed asset file directly, we copy it into
            // the cache directory.
            InputStream asset = context.getAssets().open(FILENAME);
            FileOutputStream output = new FileOutputStream(file);
            final byte[] buffer = new byte[1024];
            int size;
            while ((size = asset.read(buffer)) != -1) {
                output.write(buffer, 0, size);
            }
            asset.close();
            output.close();
        }
        parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        // This is the PdfRenderer we use to render the PDF.
        if (parcelFileDescriptor != null) {
            pdfRenderer = new PdfRenderer(parcelFileDescriptor);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void closeRenderer() throws IOException {
        if (null != currentPage) {
            currentPage.close();
        }
//        pdfRenderer.close();
  //      parcelFileDescriptor.close();
    }
    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showPage(int index) {
        if (pdfRenderer.getPageCount() <= index) {
            return;
        }
        // Make sure to close the current page before opening another one.
        if (null != currentPage) {
            currentPage.close();
        }
        // Use `openPage` to open a specific page in PDF.
        currentPage = pdfRenderer.openPage(index);
        // Important: the destination bitmap must be ARGB (not RGB).
   //     Bitmap bitmap = Bitmap.createBitmap(currentPage.getWidth(), currentPage.getHeight(),
 //              Bitmap.Config.ARGB_8888);

        Bitmap bitmap = Bitmap.createBitmap(
                getResources().getDisplayMetrics().densityDpi * currentPage.getWidth() / 100,
                getResources().getDisplayMetrics().densityDpi * currentPage.getHeight() / 100,
                Bitmap.Config.ARGB_8888
        );
        // Here, we render the page onto the Bitmap.
        // To render a portion of the page, use the second and third parameter. Pass nulls to get
        // the default result.
        // Pass either RENDER_MODE_FOR_DISPLAY or RENDER_MODE_FOR_PRINT for the last parameter.
        currentPage.render( bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        // We are ready to show the Bitmap to user.
        imageViewPdf.setImageBitmap(bitmap);
        imageViewPdf.setScaleType(ImageView.ScaleType.FIT_XY);
       imageViewPdf.setOnTouchListener(this);
        updateUi();
    }
    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();
        float pivotX = 0;
        float pivotY = 0;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));
        return scaledBitmap;
    }
    /**
     * Updates the state of 2 control buttons in response to the current page index.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void updateUi() {
        int index = currentPage.getIndex();
        int pageCount = pdfRenderer.getPageCount();
        prePageButton.setEnabled(0 != index);
        nextPageButton.setEnabled(index + 1 < pageCount);
        setTitle(getString(R.string.app_name_with_index, index + 1, pageCount));
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public int getPageCount() {
        return pdfRenderer.getPageCount();
    }
    @Override
    public void showProgress() {
       progressDialog = new ProgressDialog(PdfRenderActivity.this);
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
        Toast.makeText(this,string,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void showStreetss(ArrayList<Vote> stretLists) {
        if (stretLists.size() == 0)
        {
            showToast("No pdf available");
        }
        else {
            String path =APIUrl.Api+"Uploads/VotersFile/";
            new  RetrieveFeedTask().execute(path+stretLists.get(0).getVoterfile());
            filenames = path+stretLists.get(0).getVoterfile();
            pageIndex = 0;
        }
    }
    /**
     * Called when a touch event is dispatched to a view. This allows listeners to
     * get a chance to respond before the target view.
     *
     * @param v     The view the touch event has been dispatched to.
     * @param event The MotionEvent object containing full information about
     *              the event.
     * @return True if the listener has consumed the event, false otherwise.
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;
        view.setScaleType(ImageView.ScaleType.MATRIX);
        float scale;
        dumpEvent(event);
        // Handle touch events here...
        switch (event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:   // first finger down only
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                Log.d(TAG, "mode=DRAG"); // write to LogCat
                mode = DRAG;
                break;

            case MotionEvent.ACTION_UP: // first finger lifted

            case MotionEvent.ACTION_POINTER_UP: // second finger lifted

                mode = NONE;
                Log.d(TAG, "mode=NONE");
                break;

            case MotionEvent.ACTION_POINTER_DOWN: // first and second finger down

                oldDist = spacing(event);
                Log.d(TAG, "oldDist=" + oldDist);
                if (oldDist > 5f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                    Log.d(TAG, "mode=ZOOM");
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG)
                {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y); // create the transformation in the matrix  of points
                }
                else if (mode == ZOOM)
                {
                    // pinch zooming
                    float newDist = spacing(event);
                    Log.d(TAG, "newDist=" + newDist);
                    if (newDist > 3f)
                    {
                        matrix.set(savedMatrix);
                        scale = newDist / oldDist; // setting the scaling of the
                        // matrix...if scale > 1 means
                        // zoom in...if scale < 1 means
                        // zoom out
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }

        view.setImageMatrix(matrix); // display the transformation on screen

        return true; // indicate event was handled
    }
    private float spacing(MotionEvent event)
    {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }
    private void midPoint(PointF point, MotionEvent event)
    {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
    private void dumpEvent(MotionEvent event)
    {
        String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE","POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);

        if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP)
        {
            sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }

        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++)
        {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }

        sb.append("]");
        Log.d("Touch Events ---------", sb.toString());
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
            try {
                openRenderer(getApplicationContext(), file);
                showPage(pageIndex);
                hideProgress();
            } catch (IOException e) {
                e.printStackTrace();
            }

            super.onPostExecute(s);
        }

    }
    public void startDownload(String f_url) {

        Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .mkdirs();
        Random generator = new Random();
        int n = 1000;
        n = generator.nextInt(n);

        Log.i("TAG","FileDown"+ f_url);
        String fname = "KMDK-CM-" + ids + ".pdf";
        lastDownload=
                mgr.enqueue(new DownloadManager.Request(Uri.parse(f_url))
                        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                                DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false)
                        .setTitle("Demo")
                        .setDescription("Something useful. No, really.")
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                                fname));

    }
    public void viewLog() {
        startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
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
            viewLog();
        }
    };
    BroadcastReceiver onNotificationClick=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            Toast.makeText(ctxt, "Ummmm...hi!", Toast.LENGTH_LONG).show();
        }
    };
}