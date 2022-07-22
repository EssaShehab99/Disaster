package com.example.disaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.res.ColorStateList;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WeatherAndDisasterActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_and_disaster);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new WeatherFragment()).commit();
        bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.whether:
                    openFragment(new WeatherFragment());
                    return true;
                case R.id.disaster:
                    openFragment(new DisasterFragment());
                    return true;
            }
            return false;
        });
    }
    public void openFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commit();

    }

}