package com.arnav.library.activities.LibrarianMain.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arnav.library.R;
import com.arnav.library.models.Student;

import java.util.ArrayList;
import java.util.List;

public class LibrarianShowAllStudentsListAdapter extends RecyclerView.Adapter<LibrarianShowAllStudentsListAdapter.ViewHolder> {

    List<Object> studentList;
    ArrayList<Object> filterStudentList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView studentNameTextView;
        private final TextView studentIdTextView;

        public ViewHolder(View view) {
            super(view);
            studentNameTextView = view.findViewById(R.id.librarianShowStudentNameTextView);
            studentIdTextView = view.findViewById(R.id.librarianShowStudentIdTextView);
        }

        public TextView getStudentIdTextView() {
            return studentIdTextView;
        }

        public TextView getStudentNameTextView() {
            return studentNameTextView;
        }
    }

    public LibrarianShowAllStudentsListAdapter(List<Object> studentList) {
        this.studentList = studentList;
        filterStudentList = new ArrayList<>();
        filterStudentList.addAll(studentList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.librarian_show_all_student_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Student student = (Student) filterStudentList.get(position);
        holder.getStudentNameTextView().setText(student.getName());
        holder.getStudentIdTextView().setText(student.getLibraryID());
    }

    @Override
    public int getItemCount() {
        return filterStudentList.size();
    }


    public ArrayList<Object> filter(String charText) {
        charText = charText.toLowerCase();
        filterStudentList.clear();
        if (charText.length() == 0) {
            filterStudentList.addAll(studentList);
        } else {
            for (Object student : studentList) {
                Student thisStudent = (Student) student;
                if (thisStudent.getName().toLowerCase().contains(charText) ||
                        thisStudent.getLibraryID().toLowerCase().contains(charText)) {
                    filterStudentList.add(student);
                }
            }
        }
        notifyDataSetChanged();
        return filterStudentList;
    }

    public void updateSecondList() {
        filterStudentList.addAll(studentList);
        notifyDataSetChanged();
    }


}
