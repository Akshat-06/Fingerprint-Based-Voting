package com.example.fingerprintbasedvoting.ui.votingpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fingerprintbasedvoting.R;
import com.example.fingerprintbasedvoting.Submitresult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VoteCategory extends AppCompatActivity {
    RadioGroup radioGroup;
    DatabaseReference dReference = FirebaseDatabase.getInstance().getReference();

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
            View radioView = radioGroup.findViewById(id);
            int rid = radioGroup.indexOfChild(radioView);
            RadioButton rb = (RadioButton) radioGroup.getChildAt(rid);
            String radiotext = rb.getText().toString();
//            Toast.makeText(this, "Value" +radiotext, Toast.LENGTH_SHORT).show();

            startActivity(new Intent(VoteCategory.this, Submitresult.class));
//            Intent intent = new Intent(this, Votepage.class);
//            startActivity(intent);
        }
    }

}
