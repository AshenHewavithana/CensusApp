package com.example.censusapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class LoginPage extends AppCompatActivity {
    Integer count = 0;
    protected void onCreate(Bundle savedInstanceState) {

        TextInputEditText userPw;
        Button login;
        String password,pw;
        SharedPreferences sp;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userPw = findViewById(R.id.passwordTxt);
        login = findViewById(R.id.loginBtn);

        sp = getSharedPreferences("LoginPassword", Context.MODE_PRIVATE);
        password = sp.getString("password","");

        if(password != null){
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(password.equals(userPw.getText().toString())){
                        Intent intent = new Intent(LoginPage.this, HomePage.class);
                        startActivity(intent);
                    }else{
                        count++;
                        Toast.makeText(LoginPage.this, "Incorrect Password", Toast.LENGTH_LONG).show();
                    }

                    if(count >= 3){
                        LoginPage.this.finish();
                        System.exit(0);
                    }
                }
            });
        }else{
            Intent intent = new Intent(LoginPage.this, MainActivity.class);
        }
    }
}
