package com.app.groundfloor24.clinicsmap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.makeramen.roundedimageview.RoundedImageView;

public class MainActivity extends AppCompatActivity {

    AHBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //==========Bottom Navigation=============

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bnve);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Home", R.drawable.home, R.color.teal_200);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Clinics", R.drawable.clinic, R.color.teal_200);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Map", R.drawable.pin, R.color.teal_200);

//        int qq = getResources().getDimensionPixelSize(R.dimen._9sdp);
//        int qqa = getResources().getDimensionPixelSize(R.dimen._10sdp);

      //  bottomNavigation.setTitleTextSize(qqa,qq);

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);


        bottomNavigation.setVisibility(View.VISIBLE);

        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#050506"));

        bottomNavigation.setAccentColor(Color.parseColor("#FF9800"));
        bottomNavigation.setInactiveColor(Color.parseColor("#8B8D92"));
        bottomNavigation.enableItemAtPosition(0);
        bottomNavigation.setCurrentItem(0);
        bottomNavigation.setForceTint(true);

        bottomNavigation.setNotificationBackgroundColor(getResources().getColor(R.color.teal_200));
        bottomNavigation.setNotificationBackgroundColorResource(R.color.teal_200);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setOnTabSelectedListener(ob);




        RoundedImageView clinic1 = findViewById(R.id.im1);
        RoundedImageView clinic2 = findViewById(R.id.im2);
        RoundedImageView clinic3 = findViewById(R.id.im3);

        clinic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,CapCheckActivity.class);
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

                Intent intent = new Intent(MainActivity.this,DetailsRealTimeActivity.class);
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

                Intent intent = new Intent(MainActivity.this,StayTunedActivity.class);
                intent.putExtra("name","Britannia Health Clinic");
                intent.putExtra("price","Britannia Health Clinic");
                intent.putExtra("lat","43.599695");
                intent.putExtra("lng","-79.645314");
                intent.putExtra("random",3);

                startActivity(intent);


            }
        });
    }


    AHBottomNavigation.OnTabSelectedListener ob = new AHBottomNavigation.OnTabSelectedListener() {
        @Override
        public boolean onTabSelected(int position, boolean wasSelected) {
            switch(position){

                case 0:



//                    bottomNavigation.enableItemAtPosition(0);
//                    bottomNavigation.setCurrentItem(0);
                    break;

                case 1:

//                    bottomNavigation.enableItemAtPosition(1);
//                    bottomNavigation.setCurrentItem(1);
                    Intent intent = new Intent(getApplicationContext(),FilterActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);


                    break;
                case 2:
                    Intent intent2 = new Intent(getApplicationContext(),Maps2Activity.class);
                    startActivity(intent2);
                    overridePendingTransition(0, 0);
//                    bottomNavigation.enableItemAtPosition(1);
//                    bottomNavigation.setCurrentItem(1);


                    break;
            }

            return true;
        }
    };
}