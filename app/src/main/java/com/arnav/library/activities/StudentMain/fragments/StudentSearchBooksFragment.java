package com.arnav.library.activities.StudentMain.fragments;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.arnav.library.LoadingDialog;
import com.arnav.library.R;
import com.arnav.library.activities.StudentMain.adapters.StudentBooksListAdapter;
import com.arnav.library.databinding.FragmentStudentSearchBooksBinding;
import com.arnav.library.models.Book;
import com.arnav.library.models.Student;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StudentSearchBooksFragment extends Fragment {

    private FragmentStudentSearchBooksBinding binding;
    LoadingDialog loadingDialog;

    FirebaseFirestore db;
    Student student;
    List<Book> bookList;
    ArrayList<Book> filterBookList;
    StudentBooksListAdapter studentBooksListAdapter;
    FragmentActionListener fragmentActionListener;

    public StudentSearchBooksFragment() {
        // Required empty public constructor
    }

    public static StudentSearchBooksFragment newInstance(
            Bundle args,
            FragmentActionListener fragmentActionListener
    ) {
        StudentSearchBooksFragment fragment = new StudentSearchBooksFragment();
        fragment.setArguments(args);
        fragment.setFragmentActionListener(fragmentActionListener);
        //fragment.setSharedElementEnterTransition();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.slide_in_up));

        loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.initializeLoadingDialog();
        db = FirebaseFirestore.getInstance();
        filterBookList = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        assert this.getArguments() != null;
        student = new Student(this.getArguments());
        binding = FragmentStudentSearchBooksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bookList = new ArrayList<>();
        getBookList();
        studentBooksListAdapter = new StudentBooksListAdapter(
                requireContext(),
                R.layout.student_search_books_list_item,
                bookList
        );

        binding.studentSearchBooksListView.setAdapter(studentBooksListAdapter);
        binding.studentSearchBooksListView.setOnItemClickListener((adapterView, view1, i, l) -> {
            if (fragmentActionListener != null) {
                Bundle bundle = new Bundle();
                bundle.putInt(
                        FragmentActionListener.ACTION_KEY,
                        FragmentActionListener.ACTION_VALUE_SELECTED_BOOK
                );
                bundle.putBundle(
                        FragmentActionListener.SELECTED_BOOK_KEY,
                        filterBookList.get(i).getBundle()
                );
                fragmentActionListener.onActionPerformed(bundle, view1);
            }
        });

        binding.studentSearchBooksSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterBookList = studentBooksListAdapter.filter(s);
                return false;
            }
        });
    }

    private void getBookList() {
        //loadingDialog.showDialog();
        binding.studentSearchBooksLoading.setVisibility(View.VISIBLE);
        db.collection("books")
                .whereEqualTo("libraryCode", student.getLibraryCode())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Book newBook = new Book(document);
                            bookList.add(newBook);
                        }
                        filterBookList.addAll(bookList);
                        studentBooksListAdapter.updateSecondList();
                        binding.studentSearchBooksLoading.setVisibility(View.GONE);
                        //loadingDialog.hideDialog();
                    } else {
                        Log.w("Error", "Error getting documents.", task.getException());
                    }
                });

//        db.collection("books").whereEqualTo("libraryId", student.getLibraryID())
//                .addSnapshotListener(new EventListener<Document>);

    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener) {
        this.fragmentActionListener = fragmentActionListener;
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