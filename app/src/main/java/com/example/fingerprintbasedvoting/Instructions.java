package com.example.fingerprintbasedvoting;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Instructions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        Button move = findViewById(R.id.movetohome);

        move.setOnClickListener(view -> {
            startActivity(new Intent(Instructions.this, Votingsystem.class));
        });
    }
}