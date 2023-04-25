package com.example.censusapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import yuku.ambilwarna.AmbilWarnaDialog;

public class Preferences extends AppCompatActivity {

    Button changeColor;
    int defaultColor;

    ConstraintLayout constraintLayout;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        changeColor = findViewById(R.id.changeColorBtn);
        constraintLayout = (ConstraintLayout) findViewById(R.id.layout);

        sp = getSharedPreferences("sharePrefColo",MODE_PRIVATE);
        defaultColor = sp.getInt("color",0);

        if (defaultColor == 0){
            defaultColor = ContextCompat.getColor(Preferences.this, R.color.white);
        }
        constraintLayout.setBackgroundColor(defaultColor);
        changeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });

    }

    private void openColorPicker() {
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultColor = color;
                constraintLayout.setBackgroundColor(defaultColor);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("color", defaultColor);
                editor.apply();
            }
        });
        ambilWarnaDialog.show();
    }
}