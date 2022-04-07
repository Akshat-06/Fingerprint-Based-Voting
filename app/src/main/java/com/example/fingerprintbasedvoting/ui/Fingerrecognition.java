package com.example.fingerprintbasedvoting.ui;

import android.Manifest;
import android.annotation.SuppressLint;
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
import com.example.fingerprintbasedvoting.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class Fingerrecognition extends AppCompatActivity {

    static final int camera_permission = 1, external_storage = 2, PERMISSION_ALL =  3 ;
  //  StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("Fingerprint Data/User/");
    ActivityResultLauncher<Intent> displayimage;
    Intent camera_intent;
    Uri imageuri;
    File photoFile = null;
    String[] PERMISSION;
    //    ProgressBar progressBar;
    File image = null;
    Button camerabutton, verify;
    ImageView imagedisplay;
    TextView textView;
    Bitmap bitmap;
    String imageString, str = "working on verifying";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fingerrecognition);

        imagedisplay = findViewById(R.id.fingerimage);
        camerabutton = findViewById(R.id.cambutton);
        verify = findViewById(R.id.verifyfinger);
        textView = findViewById(R.id.replacetext);

        PERMISSION = new String[] {
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        camerabutton.setOnClickListener(view -> CameraImageCapture());

        verify.setOnClickListener(v -> verify());

        displayimage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->
        {
            Toast.makeText(this, "Activity result", Toast.LENGTH_SHORT).show();
            imagedisplay.setImageURI(imageuri);
            if (result.getResultCode() == RESULT_OK && result.getData() != null)
            {
                if (imageuri == null) {
                    Toast.makeText(this, "Error Retrieving Image", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    imagedisplay.setImageURI(imageuri);
                    Toast.makeText(this, (CharSequence) imageuri, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void verify()
    {
        textView.setText(str);

        if (!Python.isStarted())
        {
            Python.start(new AndroidPlatform(this));
        }
        Python python = Python.getInstance();

        bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
        imageString = getStringImage(bitmap);
        PyObject pyObject =python.getModule("script");

        PyObject obj =  pyObject.callAttr("main", imageString);

        textView.setText(obj.toString());

    }


    private String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,10,baos);
        byte[] imageBytes = baos.toByteArray();
        String encode = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
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
            imageuri = Uri.parse(image.getAbsolutePath());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return image;
    }
}
