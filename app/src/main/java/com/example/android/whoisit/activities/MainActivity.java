package com.example.android.whoisit.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.os.WorkSource;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.whoisit.R;
import com.example.android.whoisit.WhoIsItApplication;
import com.example.android.whoisit.fragments.StudentdetailFragment;
import com.example.android.whoisit.fragments.StudentsFragment;
import com.example.android.whoisit.interfaces.StudentInterface;
import com.example.android.whoisit.models.Student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.objectbox.Box;

public class MainActivity extends AppCompatActivity implements StudentInterface {

    private StudentsFragment studentsFragment;
    private StudentdetailFragment studentdetailFragment;
    private Student selectedStudent;
    private ArrayList<Student> students;
    private Box<Student> studentBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        studentsFragment = new StudentsFragment();
        studentdetailFragment = new StudentdetailFragment();

        WhoIsItApplication app = (WhoIsItApplication) getApplication();
        studentBox = app.getBoxStore().boxFor(Student.class);

        students = studentBox.getAll().isEmpty() ? new ArrayList<Student>() : (ArrayList<Student>) studentBox.getAll();

        //studentBox.removeAll();
//        initStudentDataset();

//        if (savedInstanceState != null) {
//            selectedStudent = (Student) studentBox.get(savedInstanceState.getLong("selectedStudent"));
//        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, studentsFragment);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ft.add(R.id.fragment_container, studentdetailFragment);
        }
        ft.commit();
    }

    @Override
    public void showStudentsFragment() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, studentsFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    @Override
    public void showStudentdetailFragment() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, studentdetailFragment);
            ft.addToBackStack(null);
            ft.commit();
        } else
            studentdetailFragment.updateView();
    }

    public Student getSelectedStudent() {
        if (selectedStudent == null)
            selectedStudent = students.get(0);
        return selectedStudent;
    }

    public void setSelectedStudent(Student selectedStudent) {
        this.selectedStudent = selectedStudent;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        if (selectedStudent == null)
//            outState.putLong("selectedStudent", students.get(0).getId());
//        else
//            outState.putLong("selectedStudent", selectedStudent.getId());


    }

//    private void initStudentDataset() {
//
//        Student ellen = new Student(0, R.drawable.ellen, "Ellen", Arrays.asList("Bruine krullen", "Leest graag", "Groene tinten"));
//        Student fien = new Student(0, R.drawable.fien, "Fien", Arrays.asList("2 lange vlechten", "Rosse haarkleuren", "Houdt van dieren"));
//        Student jana = new Student(0, R.drawable.jana, "Jana", Arrays.asList("Glimlacht altijd", "Behulpzaam", "Schoenen met hakken"));
//        Student marie = new Student(0, R.drawable.marie, "Marie", Arrays.asList("Lachebekje", "Altijd een praatje maken", "Roze tinten"));
//        Student nele = new Student(0, R.drawable.nele, "Nele", Arrays.asList("Opvallend", "Houdt van schapen", "Zwemt graag"));
//        Student luc = new Student(0, R.drawable.luc, "Luc", Arrays.asList("Oranje das", "Stil", "Houdt van woordzoekers"));
//        Student pol = new Student(0, R.drawable.pol, "Pol", Arrays.asList("Bril", "Houdt van muurklimmen", "Kleurrijke schoenzolen"));
//        Student robbe = new Student(0, R.drawable.robbe, "Robbe", Arrays.asList("Hoed", "Kleurrijke veters", "Houdt van schilderen"));
//        Student stijn = new Student(0, R.drawable.stijn, "Stijn", Arrays.asList("Rosse haren", "Verlegen", "Houdt van karten"));
//        Student tijl = new Student(0, R.drawable.tijl, "Tijl", Arrays.asList("Luidruchtig", "Kleurrijke sokken", "Maakt graag sudoku's"));
//
//        studentBox.put(ellen);
//        studentBox.put(luc);
//        studentBox.put(fien);
//        studentBox.put(pol);
//        studentBox.put(jana);
//        studentBox.put(robbe);
//        studentBox.put(marie);
//        studentBox.put(stijn);
//        studentBox.put(nele);
//        studentBox.put(tijl);
//
//    }

    //bij landscape detailfragment updaten bij wijzigen van student, of deleten
    public void updateDetailFragment() {
        studentdetailFragment.updateView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.voegStudentToe:
                Intent intent = new Intent(MainActivity.this, AddStudentActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_refresh:
                studentsFragment.updateList();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
