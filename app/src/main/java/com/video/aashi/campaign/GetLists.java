package com.video.aashi.campaign;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.pdfviewer.PDFView;
import com.video.aashi.campaign.logout.Expired;
import com.video.aashi.campaign.main.MainScreen;
import com.video.aashi.campaign.pdf.PdfRenderActivity;
import com.video.aashi.campaign.sesssion.Sessions;
import com.video.aashi.campaign.voterlist.Vote;
import com.video.aashi.campaign.voterlist.VotersList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

public class GetLists extends Fragment {


    public GetLists() {
        // Required empty public constructor
    }
    @BindView(R.id.allPdfs)
    RecyclerView recyclerView;
    ArrayList<Vote> votes;
    ProgressDialog progressDialog;
    Sessions sessions;


    BoothHolders boothHolders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_get_lists, container, false);
        ButterKnife.bind(this,view);
        votes = new ArrayList<>();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        getActivity().invalidateOptionsMenu();
        setHasOptionsMenu(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        sessions = new Sessions(getActivity());
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
                        10);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            Search_Dir(Environment.getExternalStorageDirectory());
        }
        return view;
    }
    public void Search_Dir(File dir) {
        String pdfPattern = ".pdf";

        File FileList[] = dir.listFiles();

        if (FileList != null) {
            for (int i = 0; i < FileList.length; i++) {

                if (FileList[i].isDirectory()) {
                    Search_Dir(FileList[i]);
                } else {
                    if (FileList[i].getName().endsWith(pdfPattern) ){
                        Log.d("TAG","AllPdfs"+FileList[i].getName());
                        votes.add(new Vote(FileList[i].getName()));
                    }
                }
            }

            for (int i=0;i<votes.size();i++)
            {
                if  (!votes.get(i).getVoterfile().startsWith("KMDK-CM"))
                {
                    votes.remove(i);
                }
            }
            boothHolders = new BoothHolders(votes);
            recyclerView.setAdapter(boothHolders);
            progressDialog.dismiss();

        }
        else
        {

        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.homes:
                startActivity(new Intent(getActivity(),MainScreen.class));
                break;
        }
        return super.onOptionsItemSelected(item);
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
        public  ViewHolders onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view   = LayoutInflater.from(getActivity()).inflate(R.layout.votedesign, viewGroup, false);
            return new ViewHolders(view);
        }
        @Override
        public void onBindViewHolder(@NonNull ViewHolders viewHolder, int i) {


            if (booths.get(i).getVoterfile().startsWith("KMDK-CM"))
            {
                viewHolder.streetName.setText(booths.get(i).getVoterfile());
                viewHolder.streetName.setVisibility(View.VISIBLE);
            }
            else
            {
                viewHolder.streetName.setVisibility(View.GONE);
            }

            viewHolder.getpdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File newFile = new File(getActivity(). getFilesDir(), booths.get(i).getVoterfile());
                    File fileBrochure = new File(Environment.getExternalStorageDirectory() + "/" +booths.get(i).getVoterfile());
                    if (!fileBrochure.exists())
                    {
                        CopyAssetsbrochure(booths.get(i).getVoterfile());
                    }
                    Log.i("TAG","PdfFile"+ fileBrochure);
                    Uri photoURI = FileProvider.getUriForFile(getActivity(), "com.video.aashi.campaign.fileprovider",
                            fileBrochure);
                    //                    Log.i("TAG","PdfFiles"+ photoURI);

                    Intent intent = new Intent(getActivity(),PdfRenderActivity.class);
                    intent.putExtra("id",booths.get(i).getVoterfile());
                    intent.putExtra("key","1");
                    startActivity(intent);
                }
            });


            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String finalErrorMessage =  null;
                    String passtiveButton = null;
                    String negative = null;
                    if (sessions.isChange())
                    {
                        finalErrorMessage = "Are you sure you want to delete?";
                        passtiveButton = "Yes";
                        negative = "No";
                    }
                    else
                    {
                        finalErrorMessage = "இதை நீக்க வேண்டுமா?";
                        passtiveButton ="ஆம்";
                        negative = "இல்லை";
                    }

                    Expired expired = new Expired(getActivity(), finalErrorMessage);
                    expired.setTitle(finalErrorMessage);
                    expired.setCancelable(false);

                    expired.setPositiveButton(passtiveButton, (dialog1, which) -> {
                        Uri path = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
                                + "/"+booths.get(i).getVoterfile());
                        File fileBrochure = new File(Environment.getExternalStorageDirectory() + "/" +booths.get(i).getVoterfile());
                        File fdelete = new File(path.getPath());
                        if (fdelete.exists()) {
                            if (fdelete.delete()) {
                                Search_Dir(Environment.getExternalStorageDirectory());

                                booths.remove(i);
                                notifyItemRemoved(i);
                                notifyDataSetChanged();

                                if (sessions.isChange())
                                {
                                    Toast.makeText(getActivity(), "Deleted..!", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), "நீக்கப்பட்டது..!", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(getActivity(), "File Not Found..!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    expired.setNegativeButton(negative, (dialog, which) -> {
                        dialog.dismiss();
                    });
                    expired.show();


                }
            });
        }
        @Override
        public int getItemCount() {
            return booths.size();
       }
    }
    private void CopyAssetsbrochure(String name) {
        AssetManager assetManager = getActivity(). getAssets();
        String[] files = null;
        try
        {
            files = assetManager.list("");
        }
        catch (IOException e)
        {
            Log.e("tag", e.getMessage());
        }
        for(int i=0; i<files.length; i++)
        {
            String fStr = files[i];
            if(fStr.equalsIgnoreCase(name))
            {
                InputStream in = null;
                OutputStream out = null;
                try
                {
                    in = assetManager.open(files[i]);
                    out = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + files[i]);
                    copyFile(in, out);
                    in.close();
                    in = null;
                    out.flush();
                    out.close();
                    out = null;
                    break;
                }
                catch(Exception e)
                {
                    Log.e("tag", e.getMessage());
                }
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    public class  ViewHolders extends RecyclerView.ViewHolder {
        @BindView(R.id.streetNames)
        TextView streetName;
        @BindView(R.id.getPdf)
        CardView getpdf;
        @BindView(R.id.removePdf)
        ImageView delete;

        public ViewHolders(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
