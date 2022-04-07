package com.example.fingerprintbasedvoting.ui.registerfingerprint;

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
import com.example.fingerprintbasedvoting.databinding.FragmentRegisterfngerprintBinding;

public class RegisterFingerprintFragment extends Fragment
{
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button button = view.findViewById(R.id.registerfingerbutton);
        button.setOnClickListener(view1 -> openvote());
    }
    void openvote(){
        startActivity(new Intent(getContext(), Cam.class));
    }

}
