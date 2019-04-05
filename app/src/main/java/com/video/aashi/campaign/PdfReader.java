package com.video.aashi.campaign;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;


import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PdfReader extends AppCompatActivity {


    String path = "http://159.89.163.3:5000/API/Uploads/VotersFile/";
    ParcelFileDescriptor mFileDescriptor;
    PdfRenderer mPdfRenderer;
    String pdf;
    @BindView(R.id.myPdfView)
    PDFView mypdf;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_reader);
        ButterKnife.bind(this);
        if (getIntent().getExtras() != null)
        {
            pdf = getIntent().getStringExtra("pdfs");
            Log.i("TAG","PdfFiles"+pdf);
      //    mypdf.fromUri(Uri.fromFile( new File(pdf)) ).load();

          mypdf.fromAsset(pdf).load();
        }
        String doc="<iframe src='http://docs.google.com/viewer?url="+path+pdf+"&embedded=true' width='100%' height='100%'  style='border: none;'></iframe>";
    }
}
