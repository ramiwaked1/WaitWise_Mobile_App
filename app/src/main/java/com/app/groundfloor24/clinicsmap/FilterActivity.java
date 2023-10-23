package com.app.groundfloor24.clinicsmap;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.makeramen.roundedimageview.RoundedImageView;

public class FilterActivity extends AppCompatActivity {

    AHBottomNavigation bottomNavigation;
    FilterClinicsAdapter adapter;
    //Toolbar toolbar;
    RecyclerView recyclerView2,recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);


        //init views
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bnve);
//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);




        double lat = getIntent().getDoubleExtra("lat",0.0);
        double lng = getIntent().getDoubleExtra("lng",0.0);

        lat = 43.599695;
        lng = -79.645314;
        //bottom bar
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Home", R.drawable.home, R.color.teal_200);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Clinics", R.drawable.clinic, R.color.teal_200);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Map", R.drawable.pin, R.color.teal_200);


        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);



        bottomNavigation.setVisibility(View.VISIBLE);

        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#050506"));

        bottomNavigation.setAccentColor(Color.parseColor("#FF9800"));
        bottomNavigation.setInactiveColor(Color.parseColor("#8B8D92"));
        bottomNavigation.enableItemAtPosition(1);
        bottomNavigation.setCurrentItem(1);
        bottomNavigation.setForceTint(true);

        bottomNavigation.setNotificationBackgroundColor(getResources().getColor(R.color.teal_200));
        bottomNavigation.setNotificationBackgroundColorResource(R.color.teal_200);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setOnTabSelectedListener(ob);






        // set up the RecyclerView
        recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        adapter = new FilterClinicsAdapter(this, Maps2Activity.stationList2,lat,lng);
        //adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        ScrollView ll = findViewById(R.id.ll);
        RoundedImageView clinic1 = findViewById(R.id.clinic1);
        RoundedImageView clinic2 = findViewById(R.id.clinic2);
        RoundedImageView clinic3 = findViewById(R.id.clinic3);

        clinic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(FilterActivity.this,ProfileActivity.class);
                intent.putExtra("name","Glen Gate Medical Clinic");
                intent.putExtra("price","Glen Gate Medical Clinic");
                intent.putExtra("lat","43.599695");
                intent.putExtra("lng","-79.645314");
                intent.putExtra("random",1);

                startActivity(intent);


            }
        });
        clinic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(FilterActivity.this,ProfileActivity.class);
                intent.putExtra("name","The Queen Clinic");
                intent.putExtra("price","The Queen Clinic");
                intent.putExtra("lat","43.599695");
                intent.putExtra("lng","-79.645314");
                intent.putExtra("random",2);

                startActivity(intent);


            }
        });
        clinic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(FilterActivity.this,ProfileActivity.class);
                intent.putExtra("name","Britannia Health Clinic");
                intent.putExtra("price","Britannia Health Clinic");
                intent.putExtra("lat","43.599695");
                intent.putExtra("lng","-79.645314");
                intent.putExtra("random",3);

                startActivity(intent);


            }
        });

        if(Maps2Activity.stationList2.size()==0){
            ll.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            ll.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }







    }




    private class StationViewHolder extends RecyclerView.ViewHolder {

        private View view;
        TextView tvName;
        ImageView img;
        LinearLayout layout;

        StationViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            tvName = view.findViewById(R.id.tvName);
            img = view.findViewById(R.id.img);
            layout = view.findViewById(R.id.layout);

        }


    }


    AHBottomNavigation.OnTabSelectedListener ob = new AHBottomNavigation.OnTabSelectedListener() {
        @Override
        public boolean onTabSelected(int position, boolean wasSelected) {
            switch(position){

                case 0:


                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
//                    bottomNavigation.enableItemAtPosition(0);
//                    bottomNavigation.setCurrentItem(0);
                    break;

                case 1:

//                    bottomNavigation.enableItemAtPosition(1);
//                    bottomNavigation.setCurrentItem(1);
                    Intent intent2 = new Intent(getApplicationContext(),FilterActivity.class);
                    startActivity(intent2);
                    overridePendingTransition(0, 0);


                    break;
                case 2:
                    Intent intent3 = new Intent(getApplicationContext(),Maps2Activity.class);
                    startActivity(intent3);
                    overridePendingTransition(0, 0);
//                    bottomNavigation.enableItemAtPosition(1);
//                    bottomNavigation.setCurrentItem(1);


                    break;
            }

            return true;
        }
    };



}