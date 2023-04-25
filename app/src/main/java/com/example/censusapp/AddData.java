package com.example.censusapp;

import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;

public class AddData extends AppCompatActivity {

    private static final int requestCamera = 100;
    TextInputEditText name, age;
    RadioGroup genderGroup;
    RadioButton radioButton;
    ImageView userImage;
    FloatingActionButton openCamera;
    Button submit;
    DBHelper db;
    byte[] byteArray;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        name = findViewById(R.id.nameTxt);
        age = findViewById(R.id.ageTxt);
        genderGroup = findViewById(R.id.radioBtnGroup);
        userImage = findViewById(R.id.image_view_person);
        openCamera = findViewById(R.id.cameraBtn);
        submit = findViewById(R.id.submitBtn);
        db = new DBHelper(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id++;
                int g_id = genderGroup.getCheckedRadioButtonId();
                radioButton = findViewById(g_id);
                String gender = radioButton.getText().toString();

                String nameText = name.getText().toString();
                String ageText = age.getText().toString();

                Boolean savedata = db.saveUserData(id, nameText, ageText, gender,byteArray);

                if(TextUtils.isEmpty(nameText) || TextUtils.isEmpty(ageText) || TextUtils.isEmpty(gender)){
                    Toast.makeText(AddData.this, "Enter all Data!", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    if(savedata == true){
                        Toast.makeText(AddData.this, "Data saved successfully!", Toast.LENGTH_SHORT).show();
                        name.setText("");
                        age.setText("");
                        radioButton.setChecked(false);
                        userImage.setImageResource(R.drawable.baseline_person_128);
                        name.requestFocus();
                    }
                    else{
                        Toast.makeText(AddData.this, "Data exists", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera,requestCamera);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == requestCamera){
            Bitmap bitmapImage = (Bitmap)data.getExtras().get("data");
            userImage.setImageBitmap(bitmapImage);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmapImage.compress(Bitmap.CompressFormat.PNG,100,stream);
            byteArray = stream.toByteArray();
        }
    }
}

