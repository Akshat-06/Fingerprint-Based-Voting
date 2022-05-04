package com.example.fingerprintbasedvoting.ui.registerfingerprint;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.example.fingerprintbasedvoting.CustomProgressBar;
import com.example.fingerprintbasedvoting.R;
import com.example.fingerprintbasedvoting.Votingsystem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class Cam extends AppCompatActivity {
    static final int camera_permission = 1, external_storage = 2, PERMISSION_ALL =  3 ;
    Button camera_open_id, uploadimage;
    ImageView display_image_id;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("Fingerprint Data/User/");
    ActivityResultLauncher<Intent> displayimage;
    Intent camera_intent;
    Uri imageuri, imageuri1;
    File photoFile = null;
    String[] PERMISSION;
    CustomProgressBar prog_bar;
    UCrop.Options options = new UCrop.Options();
    File image = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerfingerprint);

        camera_open_id = findViewById(R.id.camera_button);
        display_image_id = findViewById(R.id.display_image);
        uploadimage = findViewById(R.id.uploadimage);

        PERMISSION = new String[] {
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
                };

        prog_bar = new CustomProgressBar(Cam.this);

        camera_open_id.setOnClickListener(view -> CameraImageCapture());

        uploadimage.setOnClickListener(v -> upload());

        displayimage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->
        {
            Toast.makeText(this, "Activity result", Toast.LENGTH_SHORT).show();
            display_image_id.setImageURI(imageuri1);

            if (result != null)
            {
                UCrop.of(imageuri, imageuri)
                        .withOptions(options)
                        .withAspectRatio(0, 0)
                        .start(Cam.this);
            }
        });
   }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            if (data != null)
            {
                display_image_id.setImageURI(imageuri);
            }
        }
    }


    void CameraImageCapture()
   {
       camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       if (!hasPermission(Cam.this,PERMISSION))
       {
           ActivityCompat.requestPermissions(Cam.this, PERMISSION, PERMISSION_ALL);
       }
      photoFile = createImageFile();

       if (photoFile != null)
       {
           Uri photoURI = FileProvider.getUriForFile(this, "com.example.fingerprintbasedvoting.fileprovider", photoFile);
           camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
           displayimage.launch(camera_intent);

       }

   }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case camera_permission:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(Cam.this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
                }  else {
                    Toast.makeText(Cam.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            case external_storage:
                if (grantResults.length > 1 && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(Cam.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
                }  else {
                    Toast.makeText(Cam.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
                }
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }

    private File createImageFile()
    {
        // Create an image file name
        String imageFileName = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try
        {
            storageDir.mkdirs();
            image = File.createTempFile(imageFileName, ".bmp", storageDir);
            imageuri = Uri.fromFile(image);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return image;
    }

    void upload()
    {
        if (image == null)
        {
            return;
        }

        Uri file = Uri.fromFile(image);

        UploadTask uploadTask = storageRef.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).putFile(file);

        prog_bar.show();
        uploadTask.addOnSuccessListener(taskSnapshot ->
        {
            Toast.makeText(this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
            display_image_id = null;
            startActivity(new Intent(getApplicationContext(), Votingsystem.class));
            prog_bar.cancel();
            finishAffinity();
        });
        uploadTask.addOnFailureListener(exception ->
        {
            Toast.makeText(this, "Upload Failed, Try Again", Toast.LENGTH_SHORT).show();
            prog_bar.cancel();
        });
    }

    private boolean hasPermission(Context context, String... PERMISSION)
    {
        if (context != null)
        {
            for (String permission: PERMISSION){
                if (ActivityCompat.checkSelfPermission(context,permission) != PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        image.delete();
    }
}