package com.example.fingerprintbasedvoting.ui.registerfingerprint;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fingerprintbasedvoting.R;

public class cam extends AppCompatActivity {


    Button camera_open_id;
    ImageView click_image_id;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_registerfngerprint);

        // By ID we can get each component
        // which id is assigned in XML file
        // get Buttons and imageview.
        camera_open_id = (Button) findViewById(R.id.camera_button);
        click_image_id = (ImageView) findViewById(R.id.click_image);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                // BitMap is data structure of image file
                // which store the image in memory
                Bundle bundle = result.getData().getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                // Set the image in imageview for display
                click_image_id.setImageBitmap(bitmap);
            }
        });

        // Camera_open button is for open the camera
        // and add the setOnClickListener in this button
        camera_open_id.setOnClickListener(v ->
        {
            // Create the camera_intent ACTION_IMAGE_CAPTURE
            // it will open the camera for capture the image
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (camera_intent.resolveActivity(getPackageManager()) != null) {
                activityResultLauncher.launch(camera_intent);
            } else {
                Toast.makeText(this, "No app exist to support this", Toast.LENGTH_SHORT).show();
            }
        });
        // Start the activity with camera_intent,
        // and request pic id
        // startActivityForResult(camera_intent, pic_id);
        // This method will help to retrieve the image

    }
}