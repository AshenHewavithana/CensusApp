package com.example.censusapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText password, passwordRepeat;
    Button register;
    SharedPreferences sp;
    String pw, pwR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        password = findViewById(R.id.password);
        passwordRepeat = findViewById(R.id.password1);
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
                    editor.commit();
                    Toast.makeText(MainActivity.this, "Password Saved!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, HomePage.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this, "Passwords are not similar!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}