package com.example.censusapp;

import static com.example.censusapp.DBHelper.TABLENAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListData extends AppCompatActivity {

    StorageReference storageReference;
    RecyclerView recyclerView;
    SQLiteDatabase sqLiteDatabase;
    DBHelper db;
    MyAdapter adapter;
    Button cloudBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);
        db = new DBHelper(this);
        recyclerView = findViewById(R.id.rv);
        cloudBtn = findViewById(R.id.pushToCloud);
        displayData();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        cloudBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });
    }

    private void uploadData() {
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    private void displayData() {
        sqLiteDatabase = db.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from "+TABLENAME,null);
        ArrayList<Model>models = new ArrayList<>();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            byte[] image = cursor.getBlob(1);
            String name = cursor.getString(2);
            String age = cursor.getString(3);
            String gender = cursor.getString(4);

            models.add(new Model(id,name,age,gender,image));
        }
        cursor.close();
        adapter = new MyAdapter(this, R.layout.userentry,models,sqLiteDatabase);
    }
}
