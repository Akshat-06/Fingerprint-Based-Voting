package com.example.fingerprintbasedvoting.ui.votingpage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fingerprintbasedvoting.R;
import com.example.fingerprintbasedvoting.votepage;

public class vote extends AppCompatActivity {
    RadioGroup radioGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.votecategory);
        radioGroup = findViewById(R.id.radioGroup);
        Button button = findViewById(R.id.sbmt);
        button.setOnClickListener(view1 -> openVerify());
    }

    void openVerify() {
        int id = radioGroup.getCheckedRadioButtonId();
        if (id == -1) {
            Toast.makeText(this, "Select one Vote Category", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(getApplicationContext(), votepage.class));
        }
    }

}
