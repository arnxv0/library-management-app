package com.arnav.library.activities.StudentMain.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arnav.library.R;


public class StudentHomeFragment extends Fragment {


    public StudentHomeFragment() {
        // Required empty public constructor
    }

    public static StudentHomeFragment newInstance() {
        StudentHomeFragment fragment = new StudentHomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_student_home, container, false);
    }
}