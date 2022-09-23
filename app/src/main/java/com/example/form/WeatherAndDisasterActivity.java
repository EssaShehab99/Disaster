package com.example.form;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class WeatherAndDisasterActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;
    ImageView menu;
    AlertDialog.Builder builder;
    private Button logoutYesBtn, logoutCancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_and_disaster);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        menu = findViewById(R.id.ic_menu);
        menu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, menu);
            popup.getMenuInflater().inflate(R.menu.main_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(this::onMenuItemClick);
            popup.show();
                    });
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

    private boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            View view = View.inflate(this, R.layout.dialilog, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(view);
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            logoutYesBtn = view.findViewById(R.id.logoutYesBtn);
            logoutCancelBtn = view.findViewById(R.id.logoutCancelBtn);
            logoutYesBtn.setOnClickListener(v1 -> {
                if (getIntent().getBooleanExtra("isAdmin", false)) {
                    Intent intent = new Intent(this, Activity_login.class);
                    startActivity(intent);
                    finish();}
                else {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("login", false);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
                dialog.dismiss();
            });
            logoutCancelBtn.setOnClickListener(v1 -> dialog.dismiss());


////                openDialog();
//                builder.setMessage("Do you want to logout?")
//                        .setCancelable(false)
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                FirebaseAuth.getInstance().signOut();
//                                startActivity(new Intent(getApplicationContext(), Activity_login.class));
//                                finish();
//
//                            }
//                        })
//                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.cancel();
//                            }
//                        });
//                AlertDialog alert = builder.create();
//                alert.setTitle("Logout");
//                alert.show();

        }
        if (item.getItemId() == R.id.edit) {
//  edit user
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
}
/*menuBTN.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, menuBTN);
            popup.getMenuInflater().inflate(R.menu.main_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.logout) {
                    if (getIntent().getBooleanExtra("isAdmin", false)) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("login", false);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    } else {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
                return true;
            });
            popup.show();
        });
 */