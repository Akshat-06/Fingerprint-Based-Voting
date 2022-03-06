package com.example.fingerprintbasedvoting.ui.votingpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fingerprintbasedvoting.R;
import com.example.fingerprintbasedvoting.databinding.FragmentVotingpageBinding;

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



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button button = view.findViewById(R.id.nextbutton);
        button.setOnClickListener(view1 -> openvote());
    }
    void openvote(){
        startActivity(new Intent(getContext(),vote.class));
    }
}