package com.example.fingerprintbasedvoting.ui.votingpage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
}