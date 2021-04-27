package com.arnav.library.activities.LibrarianMain.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.arnav.library.databinding.FragmentLibrarianHomeBinding;


public class LibrarianHomeFragment extends Fragment {

    FragmentLibrarianHomeBinding binding;

    public LibrarianHomeFragment() {
        // Required empty public constructor
    }


    public static LibrarianHomeFragment newInstance() {
        LibrarianHomeFragment fragment = new LibrarianHomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLibrarianHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}