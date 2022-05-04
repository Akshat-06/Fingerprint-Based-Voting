package com.example.fingerprintbasedvoting;

import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class loginpage extends AppCompatActivity {

    private EditText eml, pass;
    private FirebaseAuth mAuth;
    private BiometricPrompt.AuthenticationCallback authenticationCallback;
    CustomProgressBar prog_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.loginpage);

        eml = findViewById(R.id.uname);
        pass = findViewById(R.id.pwd);

        Button button1 = findViewById(R.id.loginbtn);
        TextView button2 = findViewById(R.id.signuptext);

        button1.setOnClickListener(view -> Login());
        button2.setOnClickListener(view -> openSignup());

        prog_bar = new CustomProgressBar(loginpage.this);

        mAuth = FirebaseAuth.getInstance();


        authenticationCallback = new BiometricPrompt.AuthenticationCallback()
        {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(loginpage.this, "Authentication Error" + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(loginpage.this, "Authentication Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Votingsystem.class));
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        };
    }


    public void Login()
    {

        String email, password;
        email = eml.getText().toString();
        password = pass.getText().toString();

        if (TextUtils.isEmpty(email))
        {
            eml.setError("Empty Email Field");
            return;
        }

        if (password.isEmpty())
        {
            pass.setError("Empty Password Field");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Toast.makeText(this, "Invalid Email Format", Toast.LENGTH_SHORT).show();
        }
        else
        {
            prog_bar.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if (task.isSuccessful())
                {
                    prog_bar.cancel();
                    BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(getApplicationContext()).setTitle("Verify Your Fingerprint").setNegativeButton("cancel", getMainExecutor(), (dialogInterface, i) -> Toast.makeText(loginpage.this, "Authentication Cancelled", Toast.LENGTH_SHORT).show()).build();
                    // start the authenticationCallback in
                    // mainExecutor
                    biometricPrompt.authenticate(
                            getCancellationSignal(),
                            getMainExecutor(),
                            authenticationCallback);
                    Toast.makeText(loginpage.this, "Login Successful", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    prog_bar.cancel();
                    Toast.makeText(loginpage.this, "Login Error"+ Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private CancellationSignal getCancellationSignal()
    {
        CancellationSignal cancellationSignal = new CancellationSignal();
        cancellationSignal.setOnCancelListener( () -> Toast.makeText(this, "Authentication was Cancelled by the user", Toast.LENGTH_SHORT).show());
        return cancellationSignal;
    }

    public void openSignup(){
        startActivity(new Intent(getApplicationContext(), Signuppage.class));
        finish();
    }

}
