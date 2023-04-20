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
import android.widget.Toast;

import java.util.ArrayList;

public class ListData extends AppCompatActivity {
    RecyclerView recyclerView;
    SQLiteDatabase sqLiteDatabase;
    DBHelper db;
    MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);
        db = new DBHelper(this);
        recyclerView = findViewById(R.id.rv);
        displayData();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

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
