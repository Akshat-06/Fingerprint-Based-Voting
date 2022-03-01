package com.example.fingerprintbasedvoting.ui.votingpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.fingerprintbasedvoting.R;
import com.example.fingerprintbasedvoting.databinding.FragmentVotingpageBinding;
import com.example.fingerprintbasedvoting.votepage;

public class VotingPageFragment extends Fragment {

    private FragmentVotingpageBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentVotingpageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final View view = binding.viewhomepage;
        view.setActivated(true);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

class votingsystem extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button btn = findViewById(R.id.sbmt);
        btn.setOnClickListener(view -> openVerify()) ;
    }
    void openVerify(){
        Intent intent = new Intent(votingsystem.this, votepage.class);
        startActivity(intent);
    }
}