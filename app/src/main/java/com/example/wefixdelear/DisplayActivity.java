package com.example.wefixdelear;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.wefixdelear.fragments.AddLogFragment;
import com.example.wefixdelear.fragments.LogFragment;
import com.example.wefixdelear.fragments.PaymentFragment;
import com.example.wefixdelear.fragments.WarrantyFragment;
import com.example.wefixdelear.storage.SharedPrefManager;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class DisplayActivity extends AppCompatActivity {

    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
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
                SharedPrefManager.getInstance(this).clear();
                Intent intent2 = new Intent(this, MainActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                finish();
                return true;

            case R.id.change_password:
                startActivity(new Intent(DisplayActivity.this, ChangePasswordActivity.class));
                return true;
        }

        return false;
    }

}