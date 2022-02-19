package com.example.fingerprintbasedvoting;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button log= findViewById(R.id.loginpgbtn);
        Button sig= findViewById(R.id.signuppgbtn);
        Button vot= findViewById(R.id.vote);

        log.setOnClickListener(view -> openLogin());
        sig.setOnClickListener(view -> openSignup());
        vot.setOnClickListener(view -> openVote());


        }
        void openLogin(){
            startActivity(new Intent(getApplicationContext(),loginpage.class));
            finish();
        }
        void openSignup(){
            startActivity(new Intent(getApplicationContext(),signuppage.class));
            finish();
        }
        void openVote(){
            startActivity(new Intent(getApplicationContext(),signuppage.class));
            finish();
        }


}