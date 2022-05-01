package com.example.fingerprintbasedvoting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class signuppage extends AppCompatActivity
{
    private EditText eml, pass, pass1, nme, adhr, phon;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    Button registerButton;
    TextView loginButton;
    CustomProgressBar prog_bar;

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

        registerButton = findViewById(R.id.reg);
        loginButton = findViewById(R.id.logintext);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        prog_bar = new CustomProgressBar(signuppage.this);

        FirebaseApp.initializeApp(this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(SafetyNetAppCheckProviderFactory.getInstance());

        registerButton.setOnClickListener(view -> register());
        loginButton.setOnClickListener(view -> loginintent());
    }

    void loginintent()
    {
        startActivity(new Intent(getApplicationContext(), loginpage.class));
        finish();
    }
    private void register()
    {
        String email = eml.getText().toString();
        String password = pass.getText().toString();
        String password1 = pass1.getText().toString();
        String fname = nme.getText().toString();
        String aadhar = adhr.getText().toString();
        String phone = phon.getText().toString();

        if (fname.isEmpty())
        {
            nme.setError("Name field empty");
            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
        }
        else if(aadhar.isEmpty())
        {
            adhr.setError("Aadhar card field empty");
            Toast.makeText(this, "Enter Aadhar card No", Toast.LENGTH_SHORT).show();
        }
        else if (aadhar.length()!=12)
        {
            adhr.setError("Invalid aadhar card no");
            Toast.makeText(this, "Enter Valid Aadhar card No", Toast.LENGTH_SHORT).show();
        }
        else if (phone.isEmpty())
        {
            phon.setError("Phone no field empty");
            Toast.makeText(this, "Enter Phone No", Toast.LENGTH_SHORT).show();
        }
        else if (phone.length()!=10)
        {
            phon.setError("Invalid phone no");
            Toast.makeText(this, "Improper Phone No", Toast.LENGTH_SHORT).show();
        }
        else if (email.isEmpty())
        {
            eml.setError("Empty Email field");
            Toast.makeText(this, "Email field if empty", Toast.LENGTH_SHORT).show();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            eml.setError("Invalid Email Address");
            Toast.makeText(this, "Improper Email Address", Toast.LENGTH_SHORT).show();
        }
        else if (password.isEmpty())
        {
            pass.setError("Empty Password field");
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
        }
        else if (!password1.equals(password))
        {
            pass1.setError("Password does not match");
            Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show();
        }
        else if (password.length() < 6)
        {
            pass.setError("Invalid Password Length");
            Toast.makeText(this, "Password Length is less than 6 char", Toast.LENGTH_SHORT).show();
        }
        else
        {
            authenticate();
        }
    }

    private void authenticate(){

        String email = eml.getText().toString();
        String password = pass.getText().toString();
        String fname = nme.getText().toString();
        String aadhar = adhr.getText().toString();
        String phone = phon.getText().toString();

        prog_bar.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password).
                addOnCompleteListener(this, task ->
                {
                    if (task.isSuccessful())
                    {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("fname", fname);
                        map.put("adharno", aadhar);
                        map.put("phoneno", phone);
                        map.put("email", email);
                        map.put("password", password);

                        firebaseDatabase.getReference("User Data").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(map).addOnCompleteListener(task1 ->
                        {
                            if (task1.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(signuppage.this, "Register success", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(signuppage.this, loginpage.class));
                                finish();
                            }
//                            progressDialog.dismiss();
                            prog_bar.cancel();
                        }).addOnFailureListener(this, e -> Toast.makeText(signuppage.this, "something went wrong: " + e.getMessage(), Toast.LENGTH_LONG).show());
                    }
                    else
                    {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(signuppage.this, "Authentication failed."+ Objects.requireNonNull(task.getException()).getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
//                    progressDialog.dismiss();
                    prog_bar.cancel();
                });
    }
}

