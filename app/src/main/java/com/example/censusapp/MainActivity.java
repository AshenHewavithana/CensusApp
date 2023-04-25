package com.example.censusapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    TextInputEditText password, passwordRepeat;
    Button register;
    SharedPreferences sp;
    String pw, pwR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        password = findViewById(R.id.passwordTxt);
        passwordRepeat = findViewById(R.id.repeatPw);
        register = findViewById(R.id.regBtn);

        sp = getSharedPreferences("LoginPassword", Context.MODE_PRIVATE);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw = password.getText().toString();
                pwR = passwordRepeat.getText().toString();

                SharedPreferences.Editor editor = sp.edit();

                if(pw.contentEquals(pwR)){
                    editor.putString("password",pw);
                    editor.putString("passwordRepeat",pwR);
                    editor.apply();
                    Toast.makeText(MainActivity.this, "Password Saved!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, HomePage.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this, "Passwords are not similar!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}