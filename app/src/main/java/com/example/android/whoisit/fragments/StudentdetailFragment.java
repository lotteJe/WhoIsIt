package com.example.android.whoisit.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.whoisit.R;
import com.example.android.whoisit.interfaces.StudentInterface;
import com.example.android.whoisit.models.Student;

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

        studentImage.setImageResource(student.getImage());

        naam.setText(student.getName());

        trait1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person, 0, 0, 0);
        trait2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person, 0, 0, 0);
        trait3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person, 0, 0, 0);

        trait1.setText(student.getTraits().get(0));
        trait2.setText(student.getTraits().get(1));
        trait3.setText(student.getTraits().get(2));
    }
}

