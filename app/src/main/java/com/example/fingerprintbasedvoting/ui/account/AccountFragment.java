package com.example.fingerprintbasedvoting.ui.account;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;
    private UserModel userModel;
    ImageView fingerimage;
    File localFile = null;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("Fingerprint Data/User/"+ FirebaseAuth.getInstance().getUid());
    Uri uri;

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
        fingerimage = view.findViewById(R.id.accountfingerprintimage);

        retriveimage();
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

    private void retriveimage()
    {
        try {
            localFile = File.createTempFile("images", ".bmp");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        File finalLocalFile = localFile;
        storageRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
            uri = Uri.fromFile(finalLocalFile);
            fingerimage.setImageURI(uri);
        }).addOnFailureListener(e -> Toast.makeText(getContext(), "Error Downloading File", Toast.LENGTH_SHORT).show());
    }
}