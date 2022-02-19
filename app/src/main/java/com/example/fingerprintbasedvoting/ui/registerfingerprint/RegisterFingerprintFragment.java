package com.example.fingerprintbasedvoting.ui.registerfingerprint;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fingerprintbasedvoting.databinding.FragmentRegisterfngerprintBinding;

public class RegisterFingerprintFragment extends Fragment {


    private FragmentRegisterfngerprintBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRegisterfngerprintBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}