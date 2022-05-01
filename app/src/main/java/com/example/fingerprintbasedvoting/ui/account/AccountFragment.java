package com.example.fingerprintbasedvoting.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fingerprintbasedvoting.MainActivity;
import com.example.fingerprintbasedvoting.R;
import com.example.fingerprintbasedvoting.databinding.FragmentAccountBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;
    private UserModel userModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        Button button = view.findViewById(R.id.logoutbtn);
        TextView fname = view.findViewById(R.id.fname);
        TextView aadhar = view.findViewById(R.id.adharnoprofile);
        TextView phoneno = view.findViewById(R.id.phonenoprofile);
        TextView email = view.findViewById(R.id.emailprofile);

        button.setOnClickListener(view1 -> Logout());




        String Uid = FirebaseAuth.getInstance().getUid();
        assert Uid != null;
        DatabaseReference rDatabase = FirebaseDatabase.getInstance().getReference().child("User Data/").child(Uid);

        rDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userModel = snapshot.getValue(UserModel.class);

                if(userModel != null){
                    fname.setText(userModel.getFname());
                    aadhar.setText(userModel.getAdharno());
                    phoneno.setText(userModel.getPhoneno());
                    email.setText(userModel.getEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    void Logout()
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        startActivity(new Intent(getContext(), MainActivity.class));
        getActivity().finishAffinity();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }

}