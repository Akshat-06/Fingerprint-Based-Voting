package com.example.fingerprintbasedvoting;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class votepage extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voterpage);

        Button submtbtn = findViewById(R.id.sbmtvote);

        submtbtn.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), submitresult.class)));



    }
}
