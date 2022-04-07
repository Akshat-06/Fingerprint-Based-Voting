package com.example.fingerprintbasedvoting;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fingerprintbasedvoting.ui.Fingerrecognition;

public class submitresult extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submitresult);

        Button button = findViewById(R.id.rtnhome);

        button.setOnClickListener(view -> openvotingsystem());
    }
    void openvotingsystem()
    {
        startActivity(new Intent(getApplicationContext(), Fingerrecognition.class));
        finish();
    }
}
