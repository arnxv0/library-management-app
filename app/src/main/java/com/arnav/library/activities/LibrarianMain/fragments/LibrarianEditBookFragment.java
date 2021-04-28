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
import com.arnav.library.databinding.FragmentLibrarianEditBookBinding;
import com.arnav.library.models.Book;
import com.arnav.library.models.Librarian;
import com.google.firebase.firestore.FirebaseFirestore;

public class LibrarianEditBookFragment extends Fragment {

    FragmentLibrarianEditBookBinding binding;
    FragmentActionListener fragmentActionListener;
    Librarian librarian;
    Book book;

    FirebaseFirestore db;
    LoadingDialog loadingDialog;

    public LibrarianEditBookFragment() {
        // Required empty public constructor
    }


    public static LibrarianEditBookFragment newInstance(
            Librarian librarian,
            FragmentActionListener fragmentActionListener,
            Book book
    ) {
        LibrarianEditBookFragment fragment = new LibrarianEditBookFragment();
        Bundle args = new Bundle();
        args.putBundle("librarian", librarian.getBundle());
        args.putBundle("book", book.getBundle());
        fragment.setArguments(args);
        fragment.setFragmentActionListener(fragmentActionListener);
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
        book = new Book(this.getArguments().getBundle("book"));
        binding = FragmentLibrarianEditBookBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setPreviousText();
        binding.editBookButton.setOnClickListener(view1 -> updateBook());
    }

    private void updateBook() {
        loadingDialog.showDialog();
        Log.w("TAG", "Start");
        String title = binding.editBookTitleEditText.getText().toString();
        String author = binding.editBookAuthorEditText.getText().toString();
        String libraryCode = librarian.getLibraryCode();
        String description = binding.editBookDescriptionEditText.getText().toString();
        String librarianID = librarian.getUid();
        String availableCount = binding.editBookCountEditText.getText().toString();
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
                .document(book.getBookId())
                .update(newBook.getObjectMap())
                .addOnCompleteListener(task -> {
                    loadingDialog.showDialog();
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Update Successful", Toast.LENGTH_LONG).show();
                        Bundle bundle = new Bundle();
                        bundle.putInt(
                                FragmentActionListener.ACTION_KEY,
                                FragmentActionListener.EDIT_BOOK_DONE_ACTION_VALUE
                        );
                        fragmentActionListener.onActionPerformed(bundle);
                    } else {
                        Toast.makeText(getContext(), "Try again later", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener) {
        this.fragmentActionListener = fragmentActionListener;
    }

    private void setPreviousText() {
        binding.editBookTitleEditText.setText(book.getTitle());
        binding.editBookAuthorEditText.setText(book.getAuthor());
        binding.editBookDescriptionEditText.setText(book.getDescription());
        binding.editBookCountEditText.setText(book.getAvailableCount());
    }

    @Override
    public void onPause() {
        super.onPause();
        loadingDialog.dismissDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loadingDialog.dismissDialog();
    }
}