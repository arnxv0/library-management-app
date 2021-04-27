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
import com.arnav.library.databinding.FragmentLibrarianAddBookBinding;
import com.arnav.library.models.Book;
import com.arnav.library.models.Librarian;
import com.google.firebase.firestore.FirebaseFirestore;

public class LibrarianAddBookFragment extends Fragment {

    private FragmentLibrarianAddBookBinding binding;
    Librarian librarian;

    FirebaseFirestore db;
    LoadingDialog loadingDialog;

    public LibrarianAddBookFragment() {
        // Required empty public constructor
    }

    public static LibrarianAddBookFragment newInstance(Librarian librarian) {
        LibrarianAddBookFragment fragment = new LibrarianAddBookFragment();
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

        db = FirebaseFirestore.getInstance();
        loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.initializeLoadingDialog();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        assert this.getArguments() != null;
        librarian = new Librarian(this.getArguments().getBundle("librarian"));
        binding = FragmentLibrarianAddBookBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.addBookButton.setOnClickListener(view1 -> addNewBook());

    }

    public void addNewBook() {
        loadingDialog.showDialog();
        Log.w("TAG", "Start");
        String title = binding.addBookTitleEditText.getText().toString();
        String author = binding.addBookAuthorEditText.getText().toString();
        String libraryCode = librarian.getLibraryCode();
        String description = binding.addBookDescriptionEditText.getText().toString();
        String librarianID = librarian.getUid();
        String availableCount = binding.addBookCountEditText.getText().toString();
        Book newBook = new Book(
                title,
                author,
                libraryCode,
                description,
                librarianID,
                availableCount,
                "autogenerate"
        );

        db.collection("books")
                .add(newBook.getObjectMap())
                .addOnCompleteListener(task -> {
                    loadingDialog.hideDialog();
                    if (task.isSuccessful()) {
                        clearAddNewBookEntries();
                        String newBookId = task.getResult().getId();

                        Toast.makeText(getContext(), "Success",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Log.w("TAG", "failure", task.getException());
                        Toast.makeText(getContext(), "Failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearAddNewBookEntries() {
        binding.addBookAuthorEditText.getText().clear();
        binding.addBookTitleEditText.getText().clear();
        binding.addBookCountEditText.getText().clear();
        binding.addBookDescriptionEditText.getText().clear();
        binding.uploadBookImageButton.setImageResource(R.drawable.ic_baseline_cloud_upload_24);
    }

    private void uploadImage() {

    }
    

    @Override
    public void onDestroy() {
        super.onDestroy();
        loadingDialog.dismissDialog();
    }
}