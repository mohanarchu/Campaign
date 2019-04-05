package com.video.aashi.campaign.main;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.video.aashi.campaign.GetLists;
import com.video.aashi.campaign.MainActivity;
import com.video.aashi.campaign.PdfReader;
import com.video.aashi.campaign.R;
import com.video.aashi.campaign.boothlist.Booths;

import com.video.aashi.campaign.login.otp.OtpValid;
import com.video.aashi.campaign.login.otp.OtpVerification;
import com.video.aashi.campaign.login.otp.otpPresenter;
import com.video.aashi.campaign.logout.Expired;
import com.video.aashi.campaign.pdf.PdfRenderActivity;
import com.video.aashi.campaign.sesssion.Sessions;
import com.video.aashi.campaign.voterlist.Vote;
import com.video.aashi.campaign.voterlist.VotersList;
import com.video.aashi.campaign.votersview.ViewVoters;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainScreen extends AppCompatActivity implements otpPresenter {

    @BindView(R.id.mainRecycle)
    RecyclerView mainRecycle;

    ArrayList<Booths> booths;
    OtpValid otpValid;
    String mobile,otp;
    ProgressDialog progressDialog;

    @BindView(R.id.myTool)
    Toolbar toolbar;
    Sessions sessions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        ButterKnife.bind(this);
        booths = new ArrayList<>();
        toolbar.setTitle("KMDK CM");
        sessions = new Sessions(getApplicationContext());
        setSupportActionBar(toolbar);
        otpValid = new OtpValid(MainScreen.this,getApplicationContext(),false);
        Log.i("TAG","mobOtp"+sessions.getId());
        otpValid.getOtp("","",sessions.getId());
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mainRecycle.setLayoutManager(layoutManager);
        if (getIntent().getExtras() != null)
        {
           mobile = getIntent().getStringExtra("mobile");
            otp = getIntent().getStringExtra("otp");
         //   otpValid.getOtp(otp,mobile);
           Log.i("TAG","mobOtp"+mobile+otp);
        }
    }
    @Override
    public void showProgress() {
        progressDialog = new ProgressDialog(MainScreen.this);
        progressDialog.show();
    }
    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }
    @Override
    public void progressMessage(String message) {
        progressDialog.setMessage(message);

    }
    @Override
    public void showScreen(String mobile, String otp) {
    }
    @Override
    public void showToast(String string) {
        Toast.makeText(getApplicationContext(),string,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void seetOtp(String text) {
    }
    @Override
    public void shhowLists(ArrayList<Booths> booths) {
        mainRecycle.setAdapter(new BoothHolder(booths));
    }
    public class BoothHolder extends RecyclerView.Adapter<ViewHolder>
    {
        ArrayList<Booths> booths;
        public BoothHolder (ArrayList<Booths> booths)
        {
            this.booths = booths;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view   = LayoutInflater.from(getApplicationContext()).inflate(R.layout.boothdesign, viewGroup, false);
            return new ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            viewHolder.booothNumber.setText(booths.get(i).getNboothnumber());
            viewHolder.boothname.setText(booths.get(i).getBoothname());
            if (sessions.isChange())
            {
                viewHolder.votersText.setText("Voters");
            }
            else
            {
                viewHolder.votersText.setText("வாக்காளர்கள்");
            }
            viewHolder.getStreets.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });
            viewHolder.showVoters.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ViewVoters viewVoters = new ViewVoters();
                    Bundle bundle = new Bundle();

                    bundle.putString("id",booths.get(i).getId());
                    viewVoters.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.myFrame,viewVoters).addToBackStack(null)
                            .commit();
                }
            });
            viewHolder.showPdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VotersList fragment = new VotersList();
                    Bundle bundle = new Bundle();
                    bundle.putString("id", booths.get(i).getId());
                    bundle.putString("ids",booths.get(i).getBoothname());
                    Intent intent = new Intent(getApplicationContext(),PdfRenderActivity.class);
                    intent.putExtra("id",booths.get(i).getId());
                    intent.putExtra("key","2");
                    intent.putExtra("ids",booths.get(i).getBoothname());
                    startActivity(intent);
                }
            });
        }
        @Override
        public int getItemCount() {
            return booths.size();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId())
        {
            case R.id.exit:
                String finalErrorMessage =  null;
                String passtiveButton = null;
                String negative = null;
                if (sessions.isChange())
                {
                    finalErrorMessage = "Are you sure you want to log out?";
                    passtiveButton = "Yes";
                    negative = "No";
                }
                else
                {
                    finalErrorMessage = "நிச்சயமாக நீங்கள் வெளியேற வேண்டுமா ?";
                    passtiveButton ="ஆம்";
                    negative = "இல்லை";
                }

                Expired expired = new Expired(MainScreen.this, finalErrorMessage);
                expired.setTitle(finalErrorMessage);
                expired.setCancelable(false);

                expired.setPositiveButton(passtiveButton, (dialog1, which) -> {
                    Intent i = new Intent(MainScreen.this, MainActivity.class);
                    sessions.clearAll();
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                });
                expired.setNegativeButton(negative, (dialog, which) -> {
                    dialog.dismiss();
                });
                expired.show();
                break;
            case R.id.savedFiles:
                getSupportFragmentManager().beginTransaction().replace(R.id.myFrame,new GetLists()).addToBackStack(null) .
                  commit();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public class  ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.boothName)
        TextView boothname;
        @BindView(R.id.boothNumber)
        TextView booothNumber;
        @BindView(R.id.votersText)
        TextView votersText;
        @BindView(R.id.shoPdf)
        LinearLayout showPdf;
        @BindView(R.id.showVoters)
        LinearLayout showVoters;
        @BindView(R.id.getStreets)
        CardView getStreets;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
