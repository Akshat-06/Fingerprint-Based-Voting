package com.example.fingerprintbasedvoting;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button log= findViewById(R.id.loginpgbtn);
        Button sig= findViewById(R.id.signuppgbtn);

        log.setOnClickListener(view -> openLogin());
        sig.setOnClickListener(view -> openSignup());

        }
    void openLogin(){
        startActivity(new Intent(getApplicationContext(),loginpage.class));
        finish();
    }
    void openSignup(){
        startActivity(new Intent(getApplicationContext(),signuppage.class));
        finish();
    }
}