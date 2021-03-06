package com.example.android.whoisit.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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

import io.objectbox.Box;

public class MainActivity extends AppCompatActivity implements StudentInterface {

    private StudentsFragment studentsFragment;
    private StudentdetailFragment studentdetailFragment;
    private Student selectedStudent;
    private ArrayList<Student> students;
    private Box<Student> studentBox;
    private final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        studentsFragment = new StudentsFragment();
        studentdetailFragment = new StudentdetailFragment();

        WhoIsItApplication app = (WhoIsItApplication) getApplication();
        studentBox = app.getBoxStore().boxFor(Student.class);
//        initStudentDataset();

        students = studentBox.getAll().isEmpty() ? new ArrayList<Student>() : (ArrayList<Student>) studentBox.getAll();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, studentsFragment);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ft.add(R.id.fragment_container, studentdetailFragment);
        }
        ft.commit();
        checkPermissions();
    }

    @Override
    protected void onStart() {
        super.onStart();
        studentsFragment.updateList();
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
        if (students.isEmpty()) {
            selectedStudent = null;
        } else if (selectedStudent == null)
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

    private void initStudentDataset() {

        Student ellen = new Student(0, "", "Ellen", Arrays.asList("Bruine krullen", "Leest graag", "Groene tinten"));
        Student fien = new Student(0, "", "Fien", Arrays.asList("2 lange vlechten", "Rosse haarkleuren", "Houdt van dieren"));
        Student jana = new Student(0, "", "Jana", Arrays.asList("Glimlacht altijd", "Behulpzaam", "Schoenen met hakken"));
        Student marie = new Student(0, "", "Marie", Arrays.asList("Lachebekje", "Altijd een praatje maken", "Roze tinten"));
//        Student nele = new Student(0, R.drawable.nele, "Nele", Arrays.asList("Opvallend", "Houdt van schapen", "Zwemt graag"));
//        Student luc = new Student(0, R.drawable.luc, "Luc", Arrays.asList("Oranje das", "Stil", "Houdt van woordzoekers"));
//        Student pol = new Student(0, R.drawable.pol, "Pol", Arrays.asList("Bril", "Houdt van muurklimmen", "Kleurrijke schoenzolen"));
//        Student robbe = new Student(0, R.drawable.robbe, "Robbe", Arrays.asList("Hoed", "Kleurrijke veters", "Houdt van schilderen"));
//        Student stijn = new Student(0, R.drawable.stijn, "Stijn", Arrays.asList("Rosse haren", "Verlegen", "Houdt van karten"));
//        Student tijl = new Student(0, R.drawable.tijl, "Tijl", Arrays.asList("Luidruchtig", "Kleurrijke sokken", "Maakt graag sudoku's"));

        studentBox.put(ellen);
       // studentBox.put(luc);
        studentBox.put(fien);
       // studentBox.put(pol);
        studentBox.put(jana);
        //studentBox.put(robbe);
        studentBox.put(marie);
//        studentBox.put(stijn);
//        studentBox.put(nele);
//        studentBox.put(tijl);

    }

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

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        }
    }

}
