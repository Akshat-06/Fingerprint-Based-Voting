package com.example.fingerprintbasedvoting.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fingerprintbasedvoting.R;
import com.example.fingerprintbasedvoting.Votepage;

public class Verified extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verified);

        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            // Do something after 5s = 5000ms
            startActivity(new Intent(Verified.this, Votepage.class));
            finish();
        }, 5000);

    }
}
