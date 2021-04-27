package com.arnav.library.activities.LibrarianMain.fragments;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.arnav.library.LoadingDialog;
import com.arnav.library.R;
import com.arnav.library.databinding.FragmentLibrarianAddStudentBinding;
import com.arnav.library.models.Librarian;
import com.arnav.library.models.Student;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LibrarianAddStudentFragment extends Fragment {

    private FragmentLibrarianAddStudentBinding binding;
    private Librarian librarian;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private LoadingDialog loadingDialog;

    public LibrarianAddStudentFragment() {
        // Required empty public constructor
    }

    public static LibrarianAddStudentFragment newInstance(Librarian librarian) {
        LibrarianAddStudentFragment fragment = new LibrarianAddStudentFragment();
        Bundle args = new Bundle();
        args.putBundle("librarian", librarian.getBundle());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.slide_in_up));
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.initializeLoadingDialog();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        assert this.getArguments() != null;
        librarian = new Librarian(this.getArguments().getBundle("librarian"));
        binding = FragmentLibrarianAddStudentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.adStudentButton.setOnClickListener(view1 -> addStudent());
    }

    public void addStudent() {
        loadingDialog.showDialog();
        String studentName = binding.addStudentName.getText().toString().trim();
        String studentEmail = binding.addStudentEmail.getText().toString().trim();
        String studentPassword = binding.addStudentPassword.getText().toString().trim();
        String studentPhone = binding.addStudentPhone.getText().toString().trim();
        String libraryStudentId = binding.addStudentLibraryID.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(studentEmail, studentPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Student student = new Student(
                                studentName,
                                libraryStudentId,
                                studentEmail,
                                studentPhone,
                                Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).getUid(),
                                librarian.getLibraryCode()
                        );

                        db.collection("students").add(student.getObjectMap())
                                .addOnCompleteListener(task1 -> {
                                    loadingDialog.hideDialog();
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(getContext(), "User Added",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.i("ad", task.toString());
                                        Objects.requireNonNull(task.getResult().getUser()).delete();
                                        Toast.makeText(getContext(), task.getResult().toString(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(getContext(), Objects.requireNonNull(task.getResult()).toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDestroy() {
        loadingDialog.dismissDialog();
        super.onDestroy();
    }
}