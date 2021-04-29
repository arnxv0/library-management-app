package com.arnav.library.activities.LibrarianMain.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.arnav.library.databinding.FragmentLibrarianViewRecordBinding;
import com.arnav.library.models.DueRecordsListObject;

public class LibrarianViewRecordFragment extends Fragment {

    FragmentLibrarianViewRecordBinding binding;
    DueRecordsListObject dueRecordsListObject;

    public LibrarianViewRecordFragment() {
        // Required empty public constructor
    }

    public static LibrarianViewRecordFragment newInstance(
            DueRecordsListObject dueRecordsListObject,
            FragmentActionListener fragmentActionListener
    ) {
        LibrarianViewRecordFragment fragment = new LibrarianViewRecordFragment();
        Bundle args = new Bundle();
        args.putBundle("dueRecordsListObject", dueRecordsListObject.getBundle());
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
        dueRecordsListObject = new DueRecordsListObject(this.getArguments().getBundle("dueRecordsListObject"));
        binding = FragmentLibrarianViewRecordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}