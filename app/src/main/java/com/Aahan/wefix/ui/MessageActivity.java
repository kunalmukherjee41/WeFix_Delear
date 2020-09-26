package com.Aahan.wefix.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.Aahan.wefix.R;
import com.Aahan.wefix.storage.SharedPrefManager;
import com.Aahan.wefix.ui.DisplayActivity;
import com.Aahan.wefix.ui.MainActivity;

import java.util.Objects;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Message");

        TextView name = findViewById(R.id.log_submission);
        Intent intent = getIntent();

        String s = intent.getStringExtra("string");

        name.setText(s);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (SharedPrefManager.getInstance(this).getDelear().getPlusMunber().equals("NO")) {
                startActivity(new Intent(this, MainActivity.class));
            } else {
                startActivity(new Intent(this, DisplayActivity.class));
            }
            finish();
        }, 4000);

    }
}