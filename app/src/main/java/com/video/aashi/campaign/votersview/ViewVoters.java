package com.video.aashi.campaign.votersview;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.video.aashi.campaign.R;
import com.video.aashi.campaign.ShowPic;
import com.video.aashi.campaign.Urls.APIUrl;
import com.video.aashi.campaign.affialation.AffiArray;
import com.video.aashi.campaign.affialation.AffiModel;
import com.video.aashi.campaign.affialation.Affialation;
import com.video.aashi.campaign.affialation.affilatiomPresent;
import com.video.aashi.campaign.localdb.AffDB;
import com.video.aashi.campaign.localdb.AffilationDB;
import com.video.aashi.campaign.localdb.ListDB;
import com.video.aashi.campaign.localdb.proLocalArray;
import com.video.aashi.campaign.main.MainScreen;
import com.video.aashi.campaign.view.ImageUtils;
import com.video.aashi.campaign.view.LoadBitmap;
import com.video.aashi.campaign.view.OnSwipeTouchListener;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewVoters extends Fragment implements CityModel,SwipeRefreshLayout.OnRefreshListener  {
    @BindView(R.id.citizenRecycle)
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    View view;
    String position[];
    CityPresent cityPresent;
    affilatiomPresent affilatiomPresent;
    AffilationDB affilationDB;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor;
    AffDB affDB;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;
    LoadBitmap loadBitmap;
    String id;
    ListDB listDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_voters, container, false);
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        sharedPreferences = getActivity().getSharedPreferences("NAMES",Context.MODE_PRIVATE);
        cityPresent = new CityPresent(getActivity(),this);
        affDB = new AffDB(getActivity());
        listDB = new ListDB(getActivity());
        Log.i("TAG","LISTDB"+ listDB.getAllDatas(id).size());
        Bundle bundle = this.getArguments();

        cityPresent = new CityPresent(getActivity(),this);
        if (bundle != null)
        {
          id =   bundle.getString("id");
          Log.i("TAG","BoothIds"+ id);

        }
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

            recyclerView.setAdapter(new Adapter(listDB.getAllDatas(id)));
            recyclerView.setVisibility(View.VISIBLE);



        // affilatiomPresent = new affilatiomPresent(getActivity(),this);
       // cityPresent.getList();
        affilationDB = new AffilationDB(getActivity());
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        getActivity().invalidateOptionsMenu();
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home,menu);
        inflater.inflate(R.menu.referesh,menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.homes:
                 startActivity(new Intent(getActivity(),MainScreen.class));

               // listDB.deleteTables();
                break;
            case R.id.refereshs:
                onRefresh();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void showProgress()
    {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait..!");
     //   progressDialog.show();
    }
    @Override
    public void onResume() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String previouslyStarted = prefs.getString( "Firsts", "");
        Cursor cursor = listDB.getIds(id);
        {
            if (cursor.moveToFirst())
            {

                Log.i("TAG","BothIdss"+ id);
            }
            else
            {
                Log.i("TAG","BothIdsss"+ id);
                onRefresh();
            }
//        }
//        if(previouslyStarted.equals("")) {
//            SharedPreferences.Editor edit = prefs.edit();
//            edit.putString("Firsts", id);
//            edit.commit();
//            Log.i("TAG","BoothIds"+ id);
//            onRefresh();
//        }
        }
        super.onResume();
    }
    @Override
    public void hideProgress() {
    //  progressDialog.dismiss();
    }
    @Override
    public void progressMessage(String message) {
    }
    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void showList(ArrayList<CityList> cityLists) {
        int counter = 0;
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        if (cityLists.size()!= 0)
        {
            recyclerView.setVisibility(View.VISIBLE);


            for (int i=0;i<cityLists.size();i++) {


                CityList ci = cityLists.get(i);
                Cursor cursor = listDB.getId(cityLists.get(i).getIds());
                if (cursor.moveToFirst()) {
                    do {
                        CityList city = new CityList();
                        city.setId(ci.getId());
                        city.setAge(ci.getAge());
                        city.setAlldata(ci.getAlldata());
                        city.setCaste(ci.getCaste());
                        city.setEducaton(ci.getEducaton());
                        city.setFathername(ci.getFathername());
                        city.setFrom(ci.getFrom());
                        city.setIds(ci.getIds());
                        city.setMobilenumber(ci.getMobilenumber());
                        city.setSex(ci.getSex());
                        city.setName(ci.getName());
                        city.setStreet(ci.getStreet());
                        city.setTo(ci.getTo());
                        listDB.updateContact(city, ci.getIds());
                        //                    loadBitmap = new LoadBitmap(getActivity() );
                        //                    loadBitmap.setPath(APIUrl.Api + "Uploads/CitizenRegister/"+cityLists.get(i).getImage(),ci.getIds());
                    }
                    while (cursor.moveToNext());
                } else {
                    CityList city = new CityList();
                    city.setId(ci.getId());
                    city.setAge(ci.getAge());
                    city.setAlldata(ci.getAlldata());
                    city.setCaste(ci.getCaste());
                    city.setEducaton(ci.getEducaton());
                    city.setFathername(ci.getFathername());
                    city.setTo(ci.getTo());
                    city.setFrom(ci.getFrom());
                    city.setIds(ci.getIds());
                    city.setMobilenumber(ci.getMobilenumber());
                    city.setSex(ci.getSex());
                    city.setName(ci.getName());
                    city.setStreet(ci.getStreet());
                    city.setTo(ci.getTo());
                    File path = null;
                    listDB.addItems(city, id);
                    loadBitmap = new LoadBitmap(getActivity());
                    loadBitmap.setPath(APIUrl.Api + "Uploads/CitizenRegister/" + cityLists.get(i).getImage(), ci.getIds());
                }
            }
        }
        else
        {
            showToast("No Data available..!");
        }
        List<CityList> cityLists1 = listDB.getAllDatas(id);
        recyclerView.setAdapter(new Adapter(listDB.getAllDatas(id)));
        mSwipeRefreshLayout.setRefreshing(false);
    }
    public byte[] getBytes(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        return stream.toByteArray();
    }

//     public static   Bitmap getBitmap(String url)
//    {
//        byte[] img = Base64.decode(url, Base64.DEFAULT);
//        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
//
//
//        return bitmap;
//
//
//    }
    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        cityPresent.getList(id);
    }
    public class Adapter extends RecyclerView.Adapter<ViewHoler>
    {
        ViewHoler viewHolderl;
        List<CityList> cityLists;
        public Adapter( List<CityList> cityLists)
        {
            this.cityLists = cityLists;
        }
        @NonNull
        @Override
        public ViewHoler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.citizenview, viewGroup, false);
            return new ViewHoler(view);
        }
        @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
        @Override
        public void onBindViewHolder(@NonNull ViewHoler viewHoler, int i) {
            position = new String[cityLists.size()];
            viewHolderl =  viewHoler;

            CityList ci= cityLists.get(i);
            viewHoler.name.setText(ci.getName().trim());
            viewHoler.husband.setText(ci.getFathername().trim());
            Glide.with(getActivity()).load(APIUrl.Api + "Uploads/CitizenRegister/"+
            ci.getImage()).into(viewHoler.imageView);
            viewHoler.vid.setText(ci.getId());
            viewHoler.toVisible.setVisibility(View.GONE);
            viewHoler.fromVisivle.setVisibility(View.GONE);
            Log.i("TAG","ImageFiles"+ci.getIds()+ci.getId());
            if (  ci.getTo().equals("()"))
            {
                viewHoler.affito.setText( "Affilation");
                  viewHoler.fromVisivle.setVisibility(View.VISIBLE);
                  viewHoler.affito .setTextColor(getResources().getColor(R.color.normal_red));
            }
            else
            {
                 viewHoler.fromVisivle.setVisibility(View.VISIBLE);
                 viewHoler.affito.setText( ci.getTo());
                 viewHoler.affito.setTextColor(getResources().getColor(R.color.greys));
            }
            if ( ci.getAge() == null)
            {
                viewHoler.ageVisible.setVisibility(View.GONE);
            }
            else
            {
                viewHoler.ageVisible.setVisibility(View.VISIBLE);
                viewHoler.age.setText(ci.getAge(). trim().replace("வயது",""));
            }
            if ( ci.getSex() == null )
            {
                viewHoler.sexVisible.setVisibility(View.GONE);
            }
            else
            {
                viewHoler.sexVisible.setVisibility(View.VISIBLE);
                viewHoler.sex.setText(ci.getSex().trim());
            }
            if (  ci.getStreet() == null)
            {
                viewHoler.streetVisible.setVisibility(View.GONE);
            }
            else
            {
                viewHoler.streetVisible.setVisibility(View.GONE);
                viewHoler.street.setText(ci.getStreet().trim());
            }
            if (  ci.getCaste() == null )
            {
                viewHoler.casteVisible.setVisibility(View.VISIBLE);
                viewHoler.caste.setText("Caste");
                viewHoler.caste.setTextColor(getResources().getColor(R.color.normal_red));
            }
            else{
                viewHoler.casteVisible.setVisibility(View.VISIBLE);
                viewHoler.caste.setText(ci.getCaste().trim());
                viewHoler.caste.setTextColor(getResources().getColor(R.color.greys));
            }
            if (  ci.getEducaton() == null)
            {
                viewHoler.educationVisible.setVisibility(View.VISIBLE);
                viewHoler.education.setText("Education");
                viewHoler.education.setTextColor(getResources().getColor(R.color.normal_red));
            }
            else
            {
                viewHoler.educationVisible.setVisibility(View.VISIBLE);
                viewHoler.education.setText( ci.getEducaton().trim());
                viewHoler.education.setTextColor(getResources().getColor(R.color.greys));
            }
            if (ci.getMobilenumber()== null)
            {
                viewHoler.mobilevisioble.setVisibility(View.VISIBLE);
                viewHoler.mobile.setText("Mobile Number");
                viewHoler.mobile.setTextColor(getResources().getColor(R.color.normal_red));
            }
            else
            {

                viewHoler.mobilevisioble.setVisibility(View.VISIBLE);
                viewHoler.mobile.setText(ci.getMobilenumber());
                viewHoler.mobile.setTextColor(getResources().getColor(R.color.greys));
            }
            viewHoler.affialation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Affialation affialation = new Affialation();
                    Bundle bundle = new Bundle();
                    bundle.putString("key","0");
                    bundle.putString("id",cityLists.get(i).getIds());
                    affialation.setArguments(bundle);
                    affialation.show(getChildFragmentManager(),"TAG");
                }
            });
            viewHoler.mobilevisioble.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Affialation affialation = new Affialation();
                    Bundle bundle = new Bundle();
                    bundle.putString("key","1");
                    bundle.putString("id",cityLists.get(i).getIds());
                    affialation.setArguments(bundle);
                    affialation.show(getChildFragmentManager(),"TAG");
                }
            });
            viewHoler.fromVisivle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Affialation affialation = new Affialation();
                    Bundle bundle = new Bundle();
                    bundle.putString("key","2");
                    bundle.putString("id",cityLists.get(i).getIds());
                    affialation.setArguments(bundle);
                    affialation.show(getChildFragmentManager(),"TAG");
                }
            });
            viewHoler.educationVisible.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Affialation affialation = new Affialation();
                    Bundle bundle = new Bundle();
                    bundle.putString("key","3");
                    bundle.putString("id",cityLists.get(i).getIds());
                    affialation.setArguments(bundle);
                    affialation.show(getChildFragmentManager(),"TAG");
                }
            });
            viewHoler.casteVisible.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Affialation affialation = new Affialation();
                    Bundle bundle = new Bundle();
                    bundle.putString("key","4");
                    bundle.putString("id",cityLists.get(i).getIds());
                    affialation.setArguments(bundle);
                    affialation.show(getChildFragmentManager(),"TAG");
                }
            });
            if (ci.getImages() != null)
            {
                byte[] outImage = ci.getImages();
                if (outImage.length != 0 )
                {
                    ByteArrayInputStream imageStream = new ByteArrayInputStream(outImage);
                    Bitmap theImage = BitmapFactory.decodeStream(imageStream);
                    //     viewHoler.imageView.setImageBitmap(theImage);
                    Glide.with(getActivity()).load(theImage).into(viewHoler.imageView);
                }

            }


            viewHoler.imageView.setOnClickListener(v -> {
                ShowPic showPic = new ShowPic(getActivity(),ci.getImages(),view);
                showPic.initiateLayout();
            });
        }

        @Override
        public int getItemCount()
        {
            return cityLists.size();
        }
    }
    class ViewHoler extends RecyclerView.ViewHolder {
        @BindView(R.id.vName)
        TextView name;
        @BindView(R.id.vHusbend)
        TextView husband;
        @BindView(R.id.vAge)
        TextView age;
        @BindView(R.id.vImage)
        ImageView imageView;
        @BindView(R.id.vId)
        TextView vid;
        @BindView(R.id.vAffialation)
        TextView affialation;
        @BindView(R.id.updateCaste)
        CardView updateCaste;
        @BindView(R.id.updateEducation)
        CardView updateEducation;
        @BindView(R.id.updateAffilation)
        CardView updateAffilation;
        @BindView(R.id.vEducation)
        TextView education;
        @BindView(R.id.vAfialationfrom)
        TextView affiFrom;
        @BindView(R.id.vAfialationTo)
        TextView affito;
        @BindView(R.id.vCaste)
        TextView caste;
        @BindView(R.id.vStreet)
        TextView street;
        @BindView(R.id.casteVisible)
        LinearLayout casteVisible;
        @BindView(R.id.educationVisible)
        LinearLayout educationVisible;
        @BindView(R.id.streetVisible)
        LinearLayout streetVisible;
        @BindView(R.id.fromVisible)
        LinearLayout fromVisivle;
        @BindView(R.id.toVisibile)
        LinearLayout toVisible;
        @BindView(R.id.ageVisible)
        LinearLayout ageVisible;
        @BindView(R.id.sexVisible)
        LinearLayout sexVisible;
        @BindView(R.id.vSex)
        TextView sex;
        @BindView(R.id.vMobile)
        TextView mobile;
        @BindView(R.id.mobileVisible)
        LinearLayout mobilevisioble;
        public ViewHoler(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            affito =(TextView)itemView.findViewById(R.id.vAfialationTo);
        }
    }


}
