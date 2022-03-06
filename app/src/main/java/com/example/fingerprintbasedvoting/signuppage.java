package com.example.fingerprintbasedvoting;

import android.app.ProgressDialog;
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

import java.util.HashMap;

public class signuppage extends AppCompatActivity
{

    private EditText eml, pass, pass1, nme, adhr, phon;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    ProgressDialog progressDialog;
    private Button registerButton;

    @Override
    protected void onStart() {

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

//        Button bck = findViewById(R.id.back);

        registerButton = findViewById(R.id.reg);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(this);

        FirebaseApp.initializeApp(this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                SafetyNetAppCheckProviderFactory.getInstance());


        registerButton.setOnClickListener(view -> register());
//        bck.setOnClickListener(view -> openMain());
    }

//    void openMain(){
//        startActivity(new Intent(getApplicationContext(),MainActivity.class));
//        finish();
//    }

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

        String email = eml.getText().toString();
        String password = pass.getText().toString();
        String fname = nme.getText().toString();
        String aadhar = adhr.getText().toString();
        String phone = phon.getText().toString();

        progressDialog.setTitle("Sing in");
        progressDialog.setMessage("Plz wait");
        progressDialog.show();

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

                        firebaseDatabase.getReference("User Data").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(map).addOnCompleteListener(task1 ->
                        {
                            if(task1.isSuccessful())
                            {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(signuppage.this, "Register success", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(signuppage.this, loginpage.class));
                                finish();
                            }
                            progressDialog.dismiss();
                        }).addOnFailureListener(e -> Toast.makeText(signuppage.this, "something went wrong: "+ e.getMessage(), Toast.LENGTH_LONG).show());

                    }
                    else
                    {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(signuppage.this, "Authentication failed."+ task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                    progressDialog.dismiss();
                });
    }
}

