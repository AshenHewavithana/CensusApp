package com.example.censusapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private Context context;
    int userentry;
    ArrayList<Model>modelArrayList;
    SQLiteDatabase sqLiteDatabase;

    public MyAdapter(Context context, int userentry, ArrayList<Model> modelArrayList, SQLiteDatabase sqLiteDatabase) {
        this.context = context;
        this.userentry = userentry;
        this.modelArrayList = modelArrayList;
        this.sqLiteDatabase = sqLiteDatabase;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.userentry,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Model model = modelArrayList.get(position);
        byte[]image = model.getImg();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image,0,image.length);
        holder.img_id.setImageBitmap(bitmap);
        holder.name_id.setText(model.getName());
        holder.age_id.setText(model.getAge());
        holder.gender_id.setText(model.getGender());

    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name_id, gender_id, age_id;
        ImageView img_id;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name_id = itemView.findViewById(R.id.editname);
            age_id = itemView.findViewById(R.id.editage);
            gender_id = itemView.findViewById(R.id.editgender);
            img_id = itemView.findViewById(R.id.viewUser);
        }
    }
}
