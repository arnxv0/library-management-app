package com.arnav.library.activities.LibrarianMain.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.arnav.library.databinding.FragmentLibrarianHomeBinding;


public class LibrarianHomeFragment extends Fragment {

    FragmentLibrarianHomeBinding binding;

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener) {
        this.fragmentActionListener = fragmentActionListener;
    }

    FragmentActionListener fragmentActionListener;

    public LibrarianHomeFragment() {
        // Required empty public constructor
    }


    public static LibrarianHomeFragment newInstance(FragmentActionListener fragmentActionListener) {
        LibrarianHomeFragment fragment = new LibrarianHomeFragment();
        Bundle args = new Bundle();
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

        binding = FragmentLibrarianHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.myStudentsButton.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putInt(
                    FragmentActionListener.ACTION_KEY,
                    FragmentActionListener.SHOW_STUDENTS_LIST_FRAGMENT_ACTION_VALUE
            );
            fragmentActionListener.onActionPerformed(bundle);
        });

        binding.editBooksButton.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putInt(
                    FragmentActionListener.ACTION_KEY,
                    FragmentActionListener.SHOW_BOOK_LIST_FRAGMENT_ACTION_VALUE
            );
            fragmentActionListener.onActionPerformed(bundle);
        });

        binding.duesButton.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putInt(
                    FragmentActionListener.ACTION_KEY,
                    FragmentActionListener.SHOW_DUES_LIST_FRAGMENT_ACTION_VALUE
            );
            fragmentActionListener.onActionPerformed(bundle);
        });

    }
}