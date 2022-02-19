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
import com.google.firebase.database.FirebaseDatabase;

public class signuppage extends AppCompatActivity
{

    private EditText eml;
    private EditText pass;
    private EditText pass1;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.signuppage);

        eml = findViewById(R.id.ename);
        pass = findViewById(R.id.pswd);
        pass1 = findViewById(R.id.pswd1);
        EditText nme = findViewById(R.id.sname);
        EditText adhr = findViewById(R.id.aadhar);
        EditText phon = findViewById(R.id.pho);

        Button bck = findViewById(R.id.back);

        Button btn = findViewById(R.id.reg);


        mAuth = FirebaseAuth.getInstance();

        FirebaseApp.initializeApp(this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                SafetyNetAppCheckProviderFactory.getInstance());


        btn.setOnClickListener(view -> register());
        bck.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),MainActivity.class)));
    }

    private void register() {


        String email = eml.getText().toString();
        String password = pass.getText().toString();
        String password1 = pass1.getText().toString();

        if (email.isEmpty())
        {
            Toast.makeText(this, "Email field if empty", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                Toast.makeText(this, "Improper Email Address", Toast.LENGTH_SHORT).show();
            }
            if (!password1.equals(password))
            {
                Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show();

            }
            else
            {
                if (password.length() < 6)
                {
                    Toast.makeText(this, "Password Length is less than 6 char", Toast.LENGTH_SHORT).show();
                }
                authenticate();
            }

        }
    }

    private void authenticate(){

        String email = eml.getText().toString().trim();
        String password = pass.getText().toString().trim();

        if (mAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(getApplicationContext(), homepage.class));
            finish();
        }
        else
        {

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(this, "Register success", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),loginpage.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(signuppage.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });


        }

    }
}

