package com.example.android.whoisit.activities;

import android.content.res.Configuration;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.whoisit.R;
import com.example.android.whoisit.fragments.StudentdetailFragment;
import com.example.android.whoisit.fragments.StudentsFragment;
import com.example.android.whoisit.interfaces.StudentInterface;
import com.example.android.whoisit.models.Student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements StudentInterface {

    private StudentsFragment studentsFragment;
    private StudentdetailFragment studentdetailFragment;
    private Student selectedStudent;
    private ArrayList<Student> students;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        studentsFragment = new StudentsFragment();
        studentdetailFragment = new StudentdetailFragment();

        initStudentDataset();

        if (savedInstanceState != null) {
            selectedStudent = (Student) savedInstanceState.getParcelable("selectedStudent");
            students = savedInstanceState.getParcelableArrayList("students");
        }
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
        if (selectedStudent == null)
            outState.putParcelable("selectedStudent", students.get(0));
        else
            outState.putParcelable("selectedStudent", selectedStudent);

        outState.putParcelableArrayList("students", students);
    }

    private void initStudentDataset() {

        students = new ArrayList<>();

        Student ellen = new Student(R.drawable.ellen, "Ellen", Arrays.asList("Bruine krullen", "Leest graag", "Groene tinten"));
        Student fien = new Student(R.drawable.fien, "Fien", Arrays.asList("2 lange vlechten", "Rosse haarkleuren", "Houdt van dieren"));
        Student jana = new Student(R.drawable.jana, "Jana", Arrays.asList("Glimlacht altijd", "Behulpzaam", "Schoenen met hakken"));
        Student marie = new Student(R.drawable.marie, "Marie", Arrays.asList("Lachebekje", "Altijd een praatje maken", "Roze tinten"));
        Student nele = new Student(R.drawable.nele, "Nele", Arrays.asList("Opvallend", "Houdt van schapen", "Zwemt graag"));
        Student luc = new Student(R.drawable.luc, "Luc", Arrays.asList("Oranje das", "Stil", "Houdt van woordzoekers"));
        Student pol = new Student(R.drawable.pol, "Pol", Arrays.asList("Bril", "Houdt van muurklimmen", "Kleurrijke schoenzolen"));
        Student robbe = new Student(R.drawable.robbe, "Robbe", Arrays.asList("Hoed", "Kleurrijke veters", "Houdt van schilderen"));
        Student stijn = new Student(R.drawable.stijn, "Stijn", Arrays.asList("Rosse haren", "Verlegen", "Houdt van karten"));
        Student tijl = new Student(R.drawable.tijl, "Tijl", Arrays.asList("Luidruchtig", "Kleurrijke sokken", "Maakt graag sudoku's"));

        students.add(ellen);
        students.add(luc);
        students.add(fien);
        students.add(pol);
        students.add(jana);
        students.add(robbe);
        students.add(marie);
        students.add(stijn);
        students.add(nele);
        students.add(tijl);
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    //bij landscape detailfragment updaten bij wijzigen van student, of deleten
    public void updateDetailFragment() {
        studentdetailFragment.updateView();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                initStudentDataset();
                studentsFragment.updateList(this.students);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
