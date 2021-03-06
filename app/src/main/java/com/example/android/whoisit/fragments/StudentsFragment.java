package com.example.android.whoisit.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.whoisit.R;
import com.example.android.whoisit.WhoIsItApplication;
import com.example.android.whoisit.activities.AddStudentActivity;
import com.example.android.whoisit.activities.MainActivity;
import com.example.android.whoisit.adapters.StudentAdapter;
import com.example.android.whoisit.interfaces.OnItemClickListener;
import com.example.android.whoisit.interfaces.StudentInterface;
import com.example.android.whoisit.models.Student;
import com.example.android.whoisit.utils.RecyclerItemClickListener;
import com.example.android.whoisit.utils.RecyclerItemTouchHelper;

import java.util.ArrayList;
import java.util.Arrays;

import io.objectbox.Box;

import static android.content.ContentValues.TAG;

public class StudentsFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private StudentAdapter mAdapter;
    private ArrayList<Student> students;
    private Box<Student> studentBox;
    private ImageView empty;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_students, container, false);
        rootView.setTag(TAG);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        WhoIsItApplication app = (WhoIsItApplication) this.getContext().getApplicationContext();
        studentBox = app.getBoxStore().boxFor(Student.class);

        students = studentBox.getAll().isEmpty() ? new ArrayList<Student>() : (ArrayList<Student>) studentBox.getAll();
        mRecyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), LinearLayoutManager.VERTICAL));

        empty = rootView.findViewById(R.id.empty);

        mAdapter = new StudentAdapter(rootView.getContext(), students);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        //bij swipe naar links
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

        //bij klikt op student
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(rootView.getContext(), mRecyclerView, new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ((StudentInterface) getActivity()).setSelectedStudent(students.get(position));
                        ((StudentInterface) getActivity()).showStudentdetailFragment();
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), AddStudentActivity.class);
                        intent.putExtra("studentId", students.get(position).getId());
                        startActivity(intent);
                    }
                })
        );

        return rootView;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof StudentAdapter.MyViewHolder) {
            // naam van item, om te tonen in snackbar
            String name = students.get(viewHolder.getAdapterPosition()).getName();

            // Student bijhouden in geval van undo
            final Student deletedStudent = students.get(viewHolder.getAdapterPosition());
            final int deletedStudentIndex = viewHolder.getAdapterPosition();

            // Student verwijderen uit recycleview, lijst in adapter aanpassen
            mAdapter.removeItem(viewHolder.getAdapterPosition());
            //studentBox.remove(deletedStudent);

            // update detailfragment, student dat gedelete is moet niet meer getoond worden
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                ((StudentInterface) getActivity()).setSelectedStudent(students.get(0));
                ((StudentInterface) getActivity()).updateDetailFragment();
            }

            // snackbar met naam en optie voor undo tonen
            Snackbar snackbar = Snackbar
                    .make(getActivity().findViewById(android.R.id.content), name + " is het niet!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //wanneer undo werd geklikt, wordt de student terug hersteld
                    mAdapter.restoreItem(deletedStudent, deletedStudentIndex);
                    // studentBox.put(deletedStudent);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    public void updateList() {
        this.students = studentBox.getAll().isEmpty() ? new ArrayList<Student>() : (ArrayList<Student>) studentBox.getAll();
        if (students.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
            empty.setImageResource(R.drawable.empty_01_01);
        } else
            empty.setVisibility(View.GONE);
        mAdapter.updateList(this.students);
    }
}

