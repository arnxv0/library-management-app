package com.arnav.library.activities.StudentMain.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.arnav.library.databinding.FragmentStudentProfileBinding;
import com.arnav.library.models.Student;

public class StudentProfileFragment extends Fragment {

    FragmentStudentProfileBinding binding;
    Student student;
    FragmentActionListener fragmentActionListener;

    public StudentProfileFragment() {
        // Required empty public constructor
    }

    public static StudentProfileFragment newInstance(
            Student student,
            FragmentActionListener fragmentActionListener
    ) {
        StudentProfileFragment fragment = new StudentProfileFragment();
        Bundle args = new Bundle();
        args.putBundle("student", student.getBundle());
        fragment.setArguments(args);
        fragment.setFragmentActionListener(fragmentActionListener);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        assert this.getArguments() != null;
        student = new Student(this.getArguments().getBundle("student"));
        binding = FragmentStudentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.myName.setText(student.getName());
        binding.email.setText(student.getEmail());
        binding.PhoneNo.setText(student.getPhone());
        binding.myIdTextView.setText(student.getLibraryID());
        binding.studentLogoutButton.setOnClickListener(view1 -> logout());
    }

    private void logout() {
        if (fragmentActionListener != null) {
            Bundle bundle = new Bundle();
            bundle.putInt(
                    FragmentActionListener.ACTION_KEY,
                    FragmentActionListener.LOGOUT_ACTION_VALUE
            );
            fragmentActionListener.onActionPerformed(bundle, null);
        }
    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener) {
        this.fragmentActionListener = fragmentActionListener;
    }
}