package com.Aahan.wefix.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.Aahan.wefix.R;
import com.Aahan.wefix.fragments.LogFragment;
import com.Aahan.wefix.fragments.PaymentFragment;
import com.Aahan.wefix.fragments.WarrantyFragment;
import com.Aahan.wefix.storage.SharedPrefManager;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class DisplayActivity extends AppCompatActivity {

    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        name = findViewById(R.id.name);
        String txt_name = "Welcome To " + SharedPrefManager.getInstance(this).getDelear().getDelearName();
        name.setText(txt_name);

        MainActivity.ViewPagerAdapter viewPagerAdapter = new MainActivity.ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new WarrantyFragment(), "Add Call Logs");
        viewPagerAdapter.addFragment(new LogFragment(), "Call Log");
        viewPagerAdapter.addFragment(new PaymentFragment(), "Payment History");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(DisplayActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(DisplayActivity.this));
                builder.setMessage("Are you want Logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", (dialog, id) -> {
                            SharedPrefManager.getInstance(this).clear();
                            Intent intent2 = new Intent(this, LoginActivity.class);
                            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent2);
                            finish();
                        })
                        .setNegativeButton("No", (dialog, id) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.show();
                return true;

            case R.id.change_password:
                startActivity(new Intent(DisplayActivity.this, ChangePasswordActivity.class));
                return true;
        }

        return false;
    }

}