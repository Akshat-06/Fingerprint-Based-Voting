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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}