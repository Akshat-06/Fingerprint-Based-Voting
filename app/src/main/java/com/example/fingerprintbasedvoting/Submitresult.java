package com.example.fingerprintbasedvoting;

import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fingerprintbasedvoting.ui.Fingerrecognition;

public class Submitresult extends AppCompatActivity {

    BiometricPrompt.AuthenticationCallback authenticationCallback;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submitresult);

        Button button = findViewById(R.id.rtnhome);

        button.setOnClickListener(view -> openvotingsystem());

        authenticationCallback = new BiometricPrompt.AuthenticationCallback()
        {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(Submitresult.this, "Authentication Error" + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(Submitresult.this, "Authentication Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),Fingerrecognition.class));
                finishAffinity();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        };

    }
    void openvotingsystem()
    {
        BiometricPrompt biometricPrompt = new BiometricPrompt.Builder(getApplicationContext())
                .setTitle("Verify Your Fingerprint")
                .setNegativeButton("cancel", getMainExecutor(), (dialogInterface, i) -> Toast.makeText(Submitresult.this, "Authentication Cancelled", Toast.LENGTH_SHORT)
                        .show()).build();

        biometricPrompt.authenticate(
                getCancellationSignal(),
                getMainExecutor(),
                authenticationCallback);

//        startActivity(new Intent(getApplicationContext(), Fingerrecognition.class));
//        finish();
    }
    private CancellationSignal getCancellationSignal()
    {
        CancellationSignal cancellationSignal = new CancellationSignal();
        cancellationSignal.setOnCancelListener( () -> Toast.makeText(this, "Authentication was Cancelled by the user", Toast.LENGTH_SHORT).show());
        return cancellationSignal;
    }

}
