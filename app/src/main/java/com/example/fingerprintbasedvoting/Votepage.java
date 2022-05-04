package com.example.fingerprintbasedvoting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Votepage extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton1, radioButton2, radioButton3;
    DatabaseReference rDatabase = FirebaseDatabase.getInstance().getReference("Voting Data");
    Intent intentCategory;
    String radiocategory, Uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.votepage);

        radioGroup = findViewById(R.id.candvote);
        radioButton1 = findViewById(R.id.cand_1);
        radioButton2 = findViewById(R.id.cand_2);
        radioButton3 = findViewById(R.id.cand_3);
        Button submtbtn = findViewById(R.id.sbmtvote);
        intentCategory = getIntent();
        Uid = FirebaseAuth.getInstance().getUid();
        radiocategory = intentCategory.getStringExtra("Candidates");
        submtbtn.setOnClickListener(view -> startsbmitvote());

    }
    void startsbmitvote()
    {
        int id = radioGroup.getCheckedRadioButtonId();
        if (id == -1)
        {
            Toast.makeText(this, "Select one of Candidate", Toast.LENGTH_SHORT).show();
        }
        else
        {
            int radioButtonID = radioGroup.getCheckedRadioButtonId();
            View radioButton = radioGroup.findViewById(radioButtonID);
            int idx = radioGroup.indexOfChild(radioButton);
            RadioButton r = (RadioButton) radioGroup.getChildAt(idx);
            String selectedText = r.getText().toString();
            Toast.makeText(this, "Selected:"+selectedText, Toast.LENGTH_SHORT).show();
            rDatabase.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(selectedText).addOnCompleteListener(task -> {
                Toast.makeText(Votepage.this, "voting Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Votingsystem.class));
                finish();
            }).addOnFailureListener(e -> Toast.makeText(Votepage.this, "Error in  Submitting Vote", Toast.LENGTH_LONG).show());
        }
    }
}
