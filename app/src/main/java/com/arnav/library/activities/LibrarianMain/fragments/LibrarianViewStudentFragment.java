package com.arnav.library.activities.LibrarianMain.fragments;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.arnav.library.R;
import com.arnav.library.databinding.FragmentLibrarianViewStudentBinding;
import com.arnav.library.models.Student;

public class LibrarianViewStudentFragment extends Fragment {

    FragmentLibrarianViewStudentBinding binding;
    Student student;

    public LibrarianViewStudentFragment() {
        // Required empty public constructor
    }

    public static LibrarianViewStudentFragment newInstance(Student student) {
        LibrarianViewStudentFragment fragment = new LibrarianViewStudentFragment();
        Bundle args = new Bundle();
        args.putBundle("student", student.getBundle());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.slide_in_up));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        assert this.getArguments() != null;
        student = new Student(this.getArguments().getBundle("student"));
        binding = FragmentLibrarianViewStudentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDetails();
    }

    private void setDetails() {
        binding.librarianViewStudentEmailID.setText(student.getEmail());
        binding.librarianViewStudentLibraryID.setText(student.getLibraryID());
        binding.librarianViewStudentName.setText(student.getName());
        binding.librarianViewStudentPhoneNo.setText(student.getPhone());
    }
}