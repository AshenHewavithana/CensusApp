package com.example.censusapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
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
    String gender;
    DBHelper db;
    Bitmap userImg;
    OutputStream outputStream;

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

        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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

                Boolean saveUserData = db.saveData(nameText, ageNum, gender);

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

                if(ContextCompat.checkSelfPermission(AddData.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    saveImage();
                }else{
                    askPermission();
                }
            }
        });
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(AddData.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveImage();
            } else {
                Toast.makeText(AddData.this, "Please grant permission!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveImage() {
        File dir = new File(Environment.getExternalStorageDirectory(),"SaveImage");
        if(!dir.exists()){
            dir.mkdir();
        }

        File file = new File(dir, name.getText().toString() + ".jpg");
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        userImg.compress(Bitmap.CompressFormat.JPEG,100, outputStream);
        try {
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
            userImg = bitmap;
        }
    }
}