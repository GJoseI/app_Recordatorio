package com.example.apprecordatorio.activities;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.example.apprecordatorio.Adapters.MyViewPageAdapter;
import com.example.apprecordatorio.R;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    MyViewPageAdapter myViewPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.POST_NOTIFICATIONS,Manifest.permission.USE_FULL_SCREEN_INTENT},
                0
        );


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "alarma_channel",
                    "Alarmas",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notificaciones de alarma");
            channel.setSound(null,null);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }


        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager);
        myViewPageAdapter = new MyViewPageAdapter(this);
        viewPager2.setAdapter(myViewPageAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
                esconderFragmento();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });
    }

    public void mostrarFragmento (Fragment fragment){
        View overlay = findViewById(R.id.overlayContainer1);
        overlay.setVisibility(View.VISIBLE);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        overlay.post(() ->{
            ft.setCustomAnimations(
                            android.R.anim.slide_in_left,
                            android.R.anim.slide_out_right,
                            android.R.anim.slide_in_left,
                            android.R.anim.slide_out_right).
                    replace(R.id.overlayContainer1, fragment)
                    .addToBackStack("overlay").commit();
        });
    }

    public void esconderFragmento() {
        FragmentManager fm = getSupportFragmentManager();

        if(fm.getBackStackEntryCount() > 0){
            fm.popBackStack();
        }
        FragmentManager.OnBackStackChangedListener listener = new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
            if(fm.getBackStackEntryCount() == 0){
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    findViewById(R.id.overlayContainer1).setVisibility(View.GONE);
                }, 300);
                fm.removeOnBackStackChangedListener(this);
            }
            }
        };
        fm.addOnBackStackChangedListener(listener);
        //getSupportFragmentManager().popBackStack("overlay", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}