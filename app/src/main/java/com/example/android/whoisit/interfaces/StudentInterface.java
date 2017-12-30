package com.example.android.whoisit.interfaces;

import com.example.android.whoisit.models.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lottejespers on 29/12/17.
 */

public interface StudentInterface {

    void showStudentsFragment();

    void showStudentdetailFragment();

    Student getSelectedStudent();

    void setSelectedStudent(Student selectedStudent);

    void updateDetailFragment();
}
