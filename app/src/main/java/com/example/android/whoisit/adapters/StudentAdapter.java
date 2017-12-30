package com.example.android.whoisit.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.whoisit.R;
import com.example.android.whoisit.WhoIsItApplication;
import com.example.android.whoisit.models.Student;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import io.objectbox.Box;

/**
 * Created by lottejespers on 29/12/17.
 */

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder> {


    private List<Student> students;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView trait1;
        public TextView trait2;
        public TextView trait3;
        public ImageView studentImage;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            trait1 = (TextView) view.findViewById(R.id.trait1);
            trait2 = (TextView) view.findViewById(R.id.trait2);
            trait3 = (TextView) view.findViewById(R.id.trait3);
            studentImage = (ImageView) view.findViewById(R.id.studentImage);

            // achtergrond met delete en vuilbak voor swipe actie naar links
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }

    public StudentAdapter(Context context, List<Student> students) {
        this.context = context;
        this.students = students;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Student student = students.get(position);
        holder.trait1.setText(student.getTraits().get(0));
        holder.trait2.setText(student.getTraits().get(1));
        holder.trait3.setText(student.getTraits().get(2));
        Bitmap image = loadImageBitmap(context.getApplicationContext(), student.getImage());
        holder.studentImage.setImageBitmap(image);
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public void removeItem(int position) {
        students.remove(position);
        // laten weten dat een item werd verwijderd
        notifyItemRemoved(position);
    }

    public void restoreItem(Student item, int position) {
        students.add(position, item);
// laten weten dat een item werd toegevoegd
        notifyItemInserted(position);
    }

    public Bitmap loadImageBitmap(Context context, String imageName) {
        Bitmap bitmap = null;
        FileInputStream fiStream;
        try {
            String path = imageName.replaceAll(".png|.jpg", "");
            File file            = context.getApplicationContext().getFileStreamPath(path);
            if (file.exists()) Log.d("file", imageName);
            fiStream = context.openFileInput(path);
            bitmap = BitmapFactory.decodeStream(fiStream);
            fiStream.close();
        } catch (Exception e) {
            Log.d("saveImage", "Exception 3, Something went wrong!");
            e.printStackTrace();
        }
        return bitmap;
    }
}


