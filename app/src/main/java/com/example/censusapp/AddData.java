package com.example.censusapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ResourceBundle;
import java.util.Vector;

public class AddData extends AppCompatActivity {

    private static int REQUEST_CODE = 100;
    TextInputEditText name, age;
    RadioButton male, female;
    ImageView imageView;
    FloatingActionButton openCamera;
    Button submit;
    String gender, entryTime;
    DBHelper db;
    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        imageView = findViewById(R.id.image_view_person);
        openCamera = findViewById(R.id.cameraBtn);
        name = findViewById(R.id.nameTxt);
        age = findViewById(R.id.ageTxt);
        male = findViewById(R.id.maleRbtn);
        female = findViewById(R.id.femaleRbtn);
        submit = findViewById(R.id.submitBtn);
        db = new DBHelper(this);

        if(ContextCompat.checkSelfPermission(AddData.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AddData.this, new String[]{android.Manifest.permission.CAMERA}, 101);
        }

        entryTime = String.valueOf(System.currentTimeMillis());

        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri imagePath = saveImage();
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imagePath);
                startActivityForResult(intent, 101);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String nameText = name.getText().toString();
                String ageNum = age.getText().toString();

                if(male.isChecked()){
                    gender = "Male";
                }
                else {
                    gender = "Female";
                }

                Boolean saveUserData = db.saveData(nameText, ageNum, gender, entryTime);

                if(TextUtils.isEmpty(nameText) || TextUtils.isEmpty(ageNum) || TextUtils.isEmpty(gender)){
                    Toast.makeText(AddData.this, "Enter all Data!", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    if(saveUserData == true){
                        Toast.makeText(AddData.this, "Data Saved Successfully", Toast.LENGTH_SHORT).show();
                        name.setText("");
                        age.setText("");
                        male.setChecked(false);
                        female.setChecked(false);
                        imageView.setImageResource(R.drawable.baseline_person_128);
                    }
                    else{
                        Toast.makeText(AddData.this, "User Data Exists", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    private Uri saveImage() {
        Uri uri = null;

        ContentResolver resolver = getContentResolver();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY); //DCIM or Pictures
        }else {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        String imgName = entryTime;
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, imgName + ".jpg");
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/"+"User Images/");
        Uri finalUri = resolver.insert(uri, contentValues);
        imageUri = finalUri;
        return finalUri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if(resultCode == Activity.RESULT_OK){
                imageView.setImageURI(imageUri);
            }
        }
    }
}