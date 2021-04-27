package com.arnav.library.activities.LibrarianMain.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.arnav.library.databinding.FragmentLibrarianProfileBinding;
import com.arnav.library.models.Librarian;

public class LibrarianProfileFragment extends Fragment {

    FragmentLibrarianProfileBinding binding;
    Librarian librarian;
    FragmentActionListener fragmentActionListener;

    public LibrarianProfileFragment() {
        // Required empty public constructor
    }

    public static LibrarianProfileFragment newInstance(
            Librarian librarian,
            FragmentActionListener fragmentActionListener
    ) {
        LibrarianProfileFragment fragment = new LibrarianProfileFragment();
        Bundle args = new Bundle();
        args.putBundle("librarian", librarian.getBundle());
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
        librarian = new Librarian(this.getArguments().getBundle("librarian"));
        binding = FragmentLibrarianProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.librarianName.setText(librarian.getName());
        binding.librarianLogoutButton.setOnClickListener(view1 -> logout());
    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener) {
        this.fragmentActionListener = fragmentActionListener;
    }

    private void logout(){
        if (fragmentActionListener != null) {
            Bundle bundle = new Bundle();
            bundle.putInt(
                    FragmentActionListener.ACTION_KEY,
                    FragmentActionListener.LOGOUT_ACTION_VALUE
            );
            fragmentActionListener.onActionPerformed(bundle);
        }
    }

}