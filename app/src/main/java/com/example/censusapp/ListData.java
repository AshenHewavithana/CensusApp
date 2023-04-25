package com.example.censusapp;

import static com.example.censusapp.DBHelper.TABLENAME;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListData extends AppCompatActivity {

    StorageReference storageReference;
    RecyclerView recyclerView;
    SQLiteDatabase sqLiteDatabase;
    DBHelper db;
    MyAdapter adapter;
    Button cloudBtn;
    ImageView imageView;
    int id;
    byte[] image;
    String name, age, gender, imageStr;
    ArrayList<Model>models = new ArrayList<>();

//    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//    DatabaseReference userRef = firebaseDatabase.getReference("users");
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);
        db = new DBHelper(this);
        recyclerView = findViewById(R.id.rv);
        cloudBtn = findViewById(R.id.pushToCloud);
        imageView = findViewById(R.id.viewUser);
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
        Cursor cursor = db.getAllData();

        if(cursor.getCount()==0){
            Toast.makeText(ListData.this, "No Data to push!", Toast.LENGTH_SHORT).show();
        }else{
            Map<String,Object> data = new HashMap<>();
            while (cursor.moveToNext()){
                String docName = cursor.getString(2);
                data.put("id",cursor.getString(0));
                String imgStr = new String(cursor.getBlob(1));
                data.put("image",imgStr);
                data.put("name",cursor.getString(2));
                data.put("age",cursor.getString(3));
                data.put("gender",cursor.getString(4));

                firestore.collection("users").document(docName).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ListData.this, "Pushed to Cloud", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ListData.this, "Error occured!", Toast.LENGTH_SHORT).show();
                    }
                });  
            }
        }
    }

    private void displayData() {
        sqLiteDatabase = db.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from "+TABLENAME,null);
        while (cursor.moveToNext()){
            id = cursor.getInt(0);
            image = cursor.getBlob(1);
            name = cursor.getString(2);
            age = cursor.getString(3);
            gender = cursor.getString(4);

            models.add(new Model(id,name,age,gender,image));
        }
        cursor.close();
        adapter = new MyAdapter(this, R.layout.userentry,models,sqLiteDatabase);
    }
}
