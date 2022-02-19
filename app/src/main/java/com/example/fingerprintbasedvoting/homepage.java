package com.example.fingerprintbasedvoting;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class homepage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        Button nxtbtn = findViewById(R.id.sbmt);

        nxtbtn.setOnClickListener(view -> openVote());


    }
    void openVote(){
        startActivity(new Intent(getApplicationContext(),votepage.class));
        finish();
    }

}
