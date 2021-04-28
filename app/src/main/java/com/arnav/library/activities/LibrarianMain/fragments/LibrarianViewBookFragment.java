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
import com.arnav.library.databinding.FragmentLibrarianViewBookBinding;
import com.arnav.library.models.Book;

public class LibrarianViewBookFragment extends Fragment {

    FragmentLibrarianViewBookBinding binding;
    FragmentActionListener fragmentActionListener;
    Book book;

    public LibrarianViewBookFragment() {
        // Required empty public constructor
    }

    public static LibrarianViewBookFragment newInstance(
            Book book,
            FragmentActionListener fragmentActionListener
    ) {
        LibrarianViewBookFragment fragment = new LibrarianViewBookFragment();
        Bundle args = new Bundle();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLibrarianViewBookBinding.inflate(inflater, container, false);
        book = new Book(this.getArguments().getBundle("book"));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String authorText = "- " + book.getAuthor();
        binding.librarianViewBookTitle.setText(book.getTitle());
        binding.librarianViewBookAuthorName.setText(authorText);
        binding.librarianViewBookDescription.setText(book.getDescription());
        binding.librarianViewBookAvailability.setText(book.getAvailability());

        binding.librarianViewBookEditButton.setOnClickListener(view1 -> {
            Bundle bundle = new Bundle();
            bundle.putInt(
                    FragmentActionListener.ACTION_KEY,
                    FragmentActionListener.SHOW_EDIT_BOOK_FRAGMENT_ACTION_VALUE
            );
            bundle.putBundle(
                    FragmentActionListener.SHOW_EDIT_BOOK_FRAGMENT_ACTION_KEY,
                    book.getBundle()
            );
            fragmentActionListener.onActionPerformed(bundle);
        });

    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener) {
        this.fragmentActionListener = fragmentActionListener;
    }
}