package com.example.fingerprintbasedvoting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;
import com.google.firebase.auth.FirebaseAuth;

public class signuppage extends AppCompatActivity
{

    private EditText eml, pass, pass1, nme, adhr, phon;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        //FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.signuppage);

        eml = findViewById(R.id.ename);
        pass = findViewById(R.id.pswd);
        pass1 = findViewById(R.id.pswd1);
        nme = findViewById(R.id.sname);
        adhr = findViewById(R.id.aadhar);
        phon = findViewById(R.id.pho);

        Button bck = findViewById(R.id.back);

        Button btn = findViewById(R.id.reg);

        mAuth = FirebaseAuth.getInstance();

        FirebaseApp.initializeApp(this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                SafetyNetAppCheckProviderFactory.getInstance());


        btn.setOnClickListener(view -> register());
        bck.setOnClickListener(view -> openMain());
    }

    void openMain(){
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    private void register() {


        String email = eml.getText().toString();
        String password = pass.getText().toString();
        String password1 = pass1.getText().toString();
        String fname = nme.getText().toString();
        String aadhar = adhr.getText().toString();
        String phone = phon.getText().toString();

        if (fname.isEmpty())
        {
            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
        }
        else if(aadhar.isEmpty())
        {
            Toast.makeText(this, "Enter Aadhar card No", Toast.LENGTH_SHORT).show();
        }
        else if (aadhar.length()!=10)
        {
            Toast.makeText(this, "Enter Valid Aadhar card No", Toast.LENGTH_SHORT).show();
        }
        else if (phone.isEmpty())
        {
            Toast.makeText(this, "Enter Phone No", Toast.LENGTH_SHORT).show();
        }
        else if (phone.length()!=10)
        {
            Toast.makeText(this, "Improper Phone No", Toast.LENGTH_SHORT).show();
        }
        else if (email.isEmpty())
        {
            Toast.makeText(this, "Email field if empty", Toast.LENGTH_SHORT).show();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Toast.makeText(this, "Improper Email Address", Toast.LENGTH_SHORT).show();
        }
        else if (password.isEmpty())
        {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
        }
        else if (!password1.equals(password))
        {
            Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show();
        }
        else if (password.length() < 6)
        {
            Toast.makeText(this, "Password Length is less than 6 char", Toast.LENGTH_SHORT).show();
        }
        else
        {
            authenticate();
        }

        }

    private void authenticate(){

        String email = eml.getText().toString().trim();
        String password = pass.getText().toString().trim();

        if (mAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(getApplicationContext(), votingsystem.class));
            finish();
        }
        else
        {
            mAuth.createUserWithEmailAndPassword(email, password).
                    addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(this, "Register success", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),loginpage.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(signuppage.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }
}

