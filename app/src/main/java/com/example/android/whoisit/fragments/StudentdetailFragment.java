package com.example.android.whoisit.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.whoisit.R;
import com.example.android.whoisit.interfaces.StudentInterface;
import com.example.android.whoisit.models.Student;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;


public class StudentdetailFragment extends Fragment {

    private Student student;
    private ImageView studentImage;
    private TextView trait1;
    private TextView trait2;
    private TextView trait3;
    private TextView naam;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_studentdetail, container, false);

        studentImage = (ImageView) v.findViewById(R.id.studentImage);

        trait1 = (TextView) v.findViewById(R.id.trait1);
        trait2 = (TextView) v.findViewById(R.id.trait2);
        trait3 = (TextView) v.findViewById(R.id.trait3);

        naam = (TextView) v.findViewById(R.id.studentName);

        updateView();
        return v;
    }

    public void updateView() {
        student = ((StudentInterface) getActivity()).getSelectedStudent();

        if (student != null) {
            Bitmap image = loadImageBitmap(this.getContext().getApplicationContext(), student.getImage());
            studentImage.setImageBitmap(image);

            naam.setText(student.getName());

            trait1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person, 0, 0, 0);
            trait2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person, 0, 0, 0);
            trait3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person, 0, 0, 0);

            trait1.setText(student.getTraits().get(0));
            trait2.setText(student.getTraits().get(1));
            trait3.setText(student.getTraits().get(2));
        }

    }

    public Bitmap loadImageBitmap(Context context, String imageName) {
        Bitmap bitmap = null;
        FileInputStream fiStream;
        try {
            String path = imageName.replaceAll(".png|.jpg", "");
            File file = context.getApplicationContext().getFileStreamPath(path);
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

