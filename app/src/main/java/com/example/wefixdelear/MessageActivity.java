package com.example.wefixdelear;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.example.wefixdelear.fragments.LogFragment;

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
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }, 4000);

    }
}