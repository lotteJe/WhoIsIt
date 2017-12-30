package com.example.android.whoisit.adapters;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.whoisit.R;
import com.example.android.whoisit.models.Student;

import java.util.List;

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
        holder.studentImage.setImageResource(student.getImage());
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public void removeItem(int position) {
        students.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Student item, int position) {
        students.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

}


