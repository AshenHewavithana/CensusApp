package com.example.censusapp;

import static com.example.censusapp.DBHelper.TABLENAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListData extends AppCompatActivity {

    StorageReference storageReference;
    FirebaseStorage storage;
    RecyclerView recyclerView;
    SQLiteDatabase sqLiteDatabase;
    DBHelper db;
    MyAdapter adapter;
    Button cloudBtn;
    ImageView imageView;
    int id;
    byte[] image;
    String name, age, gender, imageURL, docName;
    ArrayList<Model>models = new ArrayList<>();
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
                db.clear();
                recreate();
            }
        });
    }

    private void uploadData() {
        Cursor cursor = db.getAllData();

        CollectionReference collectionRef = firestore.collection("users");

        Map<String,Object> data = new HashMap<>();

        if(cursor.getCount()==0){
            Toast.makeText(ListData.this, "No Data to push!", Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){
                String imgName = cursor.getString(0) + "_" + cursor.getString(2);

                storage = FirebaseStorage.getInstance();
                storageReference = storage.getReference();

                StorageReference imageRef = storageReference.child("images/"+ imgName);

                image = cursor.getBlob(1);

                UploadTask uploadTask = imageRef.putBytes(image);

                docName = cursor.getString(2);
                data.put("id",cursor.getString(0));
                image = cursor.getBlob(1);
                data.put("image",String.valueOf(image));
                data.put("name",cursor.getString(2));
                data.put("age",cursor.getString(3));
                data.put("gender",cursor.getString(4));

                firestore.collection("users").document(docName).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ListData.this, "Data pushed to cloud!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ListData.this, "Failed to pushed to cloud!", Toast.LENGTH_SHORT).show();
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
