package com.example.fingerprintbasedvoting.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.fingerprintbasedvoting.CustomProgressBar;
import com.example.fingerprintbasedvoting.R;
import com.example.fingerprintbasedvoting.Votepage;
import com.example.fingerprintbasedvoting.Votingsystem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class Fingerrecognition extends AppCompatActivity {

    static final int camera_permission = 1, external_storage = 2, PERMISSION_ALL =  3 ;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("Fingerprint Data/User/"+ FirebaseAuth.getInstance().getUid());
    ActivityResultLauncher<Intent> displayimage;
    Intent camera_intent;
    Uri imageuri;
    int flag =0;
    File photoFile = null;
    String[] PERMISSION;
    CustomProgressBar prog_bar;
    File image = null;
    Button camerabutton, verify, confirm_voting;
    String encode;
    ImageView imagedisplay, firebasedisplay;
    TextView textView;
    Bitmap bitmap, bitmap1;
    Uri uri;
    String imageString, imageString1, str = "working on verifying";
    UCrop.Options options = new UCrop.Options();
    File localFile = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fingerrecognition);

        imagedisplay = findViewById(R.id.fingerimage);
        camerabutton = findViewById(R.id.cambutton);
        verify = findViewById(R.id.verifyfinger);
        textView = findViewById(R.id.replacetext);
        firebasedisplay = findViewById(R.id.firebaseimage);
        confirm_voting = findViewById(R.id.verifyvoting);

        prog_bar = new CustomProgressBar(Fingerrecognition.this);

        PERMISSION = new String[] {
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        confirm_voting.setOnClickListener(view ->
        {
            Toast.makeText(this, "Verified Value" +flag, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Fingerrecognition.this, Verified.class));
            finishAffinity();
        });
        retriveimage();

        firebasedisplay.setImageURI(uri);

        camerabutton.setOnClickListener(view -> CameraImageCapture());

        verify.setOnClickListener(v -> verify());

        displayimage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->
        {
            Toast.makeText(this, "Activity result", Toast.LENGTH_SHORT).show();
//            imagedisplay.setImageURI(imageuri);
            if (result != null)
            {
                UCrop.of(imageuri, imageuri)
                        .withOptions(options)
                        .withAspectRatio(0, 0)
                        .start(Fingerrecognition.this);

//                imagedisplay.setImageURI(imageuri);
            }
            else
            {
                Toast.makeText(this, "Error Retrieving Image", Toast.LENGTH_SHORT).show();
            }
            });

        if (flag == 1)
        {
//            startActivity(new Intent(Fingerrecognition.this,Verified.class));
            startActivity(new Intent(Fingerrecognition.this, Votepage.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP)
        {
            if (data != null)
            {
                Uri resulturi = UCrop.getOutput(data);
                imagedisplay.setImageURI(resulturi);
            }
        }
    }



    private void verify()
    {

        if (imageuri == null)
        {
            Toast.makeText(this, "Capture Image From Camera", Toast.LENGTH_SHORT).show();
            return;
        }

        Runnable runnable = () ->
        {
            if (!Python.isStarted())
            {
                textView.setText(str);

                Python.start(new AndroidPlatform(Fingerrecognition.this));
            }
            Python python = Python.getInstance();

            if (image != null && localFile != null)
            {

                bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
                imageString = getStringImage(bitmap);
                bitmap1 = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                imageString1 = getStringImage(bitmap1);

//                PyObject pyObject =python.getModule("FingerRecognitionScript");
                PyObject pyObject =python.getModule("script");

                PyObject obj =  pyObject.callAttr("main", imageString, imageString1);

        //                    String str = obj.toString();
        //                    byte[] data = android.util.Base64.decode(str,Base64.DEFAULT);
        //                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);

        //                    resultimage.setImageBitmap(bmp);
                if (obj.toString().equals("verified"))
                {
                    flag = 1;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(obj.toString());
                    }
                });
            }

        };


        if (image == null && localFile == null)
        {
            Toast.makeText(Fingerrecognition.this, "Empty Image File", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Thread thread = new Thread(runnable);
            thread.start();
        }

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
            firebasedisplay.setImageURI(uri);
        }).addOnFailureListener(e -> Toast.makeText(Fingerrecognition.this, "Error Downloading File", Toast.LENGTH_SHORT).show());
    }


    private String getStringImage(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,10,baos);
        byte[] imageBytes = baos.toByteArray();
        encode = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encode;
    }


    void CameraImageCapture()
    {
        camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (!hasPermission(Fingerrecognition.this,PERMISSION))
        {
            ActivityCompat.requestPermissions(Fingerrecognition.this, PERMISSION, PERMISSION_ALL);
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
                    Toast.makeText(Fingerrecognition.this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
                }  else {
                    Toast.makeText(Fingerrecognition.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            case external_storage:
                if (grantResults.length > 1 && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(Fingerrecognition.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
                }  else {
                    Toast.makeText(Fingerrecognition.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
                }
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
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

    private File createImageFile()
    {
        // Create an image file name
        String imageFileName = "Image";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            image = File.createTempFile(imageFileName, ".bmp", storageDir);
            imageuri = Uri.fromFile(image);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Fingerrecognition.this, Votingsystem.class));
        finish();
    }

}
