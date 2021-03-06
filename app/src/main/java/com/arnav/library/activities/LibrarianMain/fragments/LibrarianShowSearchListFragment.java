package com.arnav.library.activities.LibrarianMain.fragments;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.arnav.library.R;
import com.arnav.library.activities.LibrarianMain.adapters.LibrarianShowAllBooksListAdapter;
import com.arnav.library.activities.LibrarianMain.adapters.LibrarianShowAllStudentsListAdapter;
import com.arnav.library.activities.LibrarianMain.adapters.LibrarianShowDueRecordsListAdapter;
import com.arnav.library.activities.StudentMain.listeners.RecyclerItemClickListener;
import com.arnav.library.databinding.FragmentLibrarianShowSearchListBinding;
import com.arnav.library.models.Book;
import com.arnav.library.models.DueRecordsListObject;
import com.arnav.library.models.Librarian;
import com.arnav.library.models.Record;
import com.arnav.library.models.Student;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LibrarianShowSearchListFragment extends Fragment {

    public static int DUES_LIST = 1;
    public static int RECORDS_LIST = 2;
    public static int STUDENTS_LIST = 3;
    public static int EDIT_BOOK_LIST = 4;

    LibrarianShowAllStudentsListAdapter librarianShowAllStudentsListAdapter;
    LibrarianShowAllBooksListAdapter librarianShowAllBooksListAdapter;
    LibrarianShowDueRecordsListAdapter librarianShowDueRecordsListAdapter;

    int chosenList;

    FragmentLibrarianShowSearchListBinding binding;

    FirebaseFirestore db;
    Librarian librarian;
    List<Object> objectList;
    ArrayList<Object> filterObjectList;
    FragmentActionListener fragmentActionListener;

    public LibrarianShowSearchListFragment() {
        // Required empty public constructor
    }

    public static LibrarianShowSearchListFragment newInstance(
            Librarian librarian,
            FragmentActionListener fragmentActionListener,
            int listType
    ) {
        LibrarianShowSearchListFragment fragment = new LibrarianShowSearchListFragment();
        Bundle args = new Bundle();
        args.putBundle("librarian", librarian.getBundle());
        args.putInt("selectedList", listType);
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
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        assert this.getArguments() != null;
        librarian = new Librarian(this.getArguments().getBundle("librarian"));
        chosenList = this.getArguments().getInt("selectedList");
        binding = FragmentLibrarianShowSearchListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.librarianShowSearchListRecyclerView.setHasFixedSize(true);
        LinearLayoutManager searchListLayoutManager = new LinearLayoutManager(getActivity());
        searchListLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.librarianShowSearchListRecyclerView.setLayoutManager(searchListLayoutManager);

        objectList = new ArrayList<>();
        filterObjectList = new ArrayList<>();
        if (chosenList == STUDENTS_LIST) {
            getStudentsList();
            studentsListSetup();
        } else if (chosenList == EDIT_BOOK_LIST) {
            getBookList();
            bookListSetup();
        } else if (chosenList == DUES_LIST) {
            getDuesList();
            duesListSetup();
        }
    }

    private void duesListSetup() {
        librarianShowDueRecordsListAdapter = new LibrarianShowDueRecordsListAdapter(filterObjectList);
        binding.librarianShowSearchListRecyclerView.setAdapter(librarianShowDueRecordsListAdapter);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL);
        itemDecorator.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(requireContext(), R.drawable.librarian_dues_list_divider)));
        binding.librarianShowSearchListRecyclerView.addItemDecoration(itemDecorator);
        binding.librarianShowSearchListSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterObjectList = librarianShowDueRecordsListAdapter.filter(s);
                return false;
            }
        });

    }

    private void studentsListSetup() {
        librarianShowAllStudentsListAdapter = new LibrarianShowAllStudentsListAdapter(filterObjectList);
        binding.librarianShowSearchListRecyclerView.setAdapter(librarianShowAllStudentsListAdapter);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL);
        itemDecorator.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(requireContext(), R.drawable.all_students_list_divider)));
        binding.librarianShowSearchListRecyclerView.addItemDecoration(itemDecorator);
        binding.librarianShowSearchListRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(), binding.librarianShowSearchListRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (fragmentActionListener != null) {
                    Student student = (Student) filterObjectList.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putInt(
                            FragmentActionListener.ACTION_KEY,
                            FragmentActionListener.SHOW_STUDENT_PROFILE_FRAGMENT_ACTION_VALUE
                    );
                    bundle.putBundle(
                            FragmentActionListener.SHOW_STUDENT_PROFILE_FRAGMENT_ACTION_KEY,
                            student.getBundle()
                    );
                    fragmentActionListener.onActionPerformed(bundle);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }
        ));

        binding.librarianShowSearchListSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterObjectList = librarianShowAllStudentsListAdapter.filter(s);
                return false;
            }
        });
    }

    private void bookListSetup() {
        librarianShowAllBooksListAdapter = new LibrarianShowAllBooksListAdapter(filterObjectList);
        binding.librarianShowSearchListRecyclerView.setAdapter(librarianShowAllBooksListAdapter);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL);
        itemDecorator.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(requireContext(), R.drawable.all_books_list_divider)));
        binding.librarianShowSearchListRecyclerView.addItemDecoration(itemDecorator);
        binding.librarianShowSearchListRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                getActivity(),
                binding.librarianShowSearchListRecyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        if (fragmentActionListener != null) {
                            Book book = (Book) filterObjectList.get(position);
                            Bundle bundle = new Bundle();
                            bundle.putInt(
                                    FragmentActionListener.ACTION_KEY,
                                    FragmentActionListener.SHOW_VIEW_BOOK_FRAGMENT_ACTION_VALUE
                            );
                            bundle.putBundle(
                                    FragmentActionListener.SHOW_VIEW_BOOK_FRAGMENT_ACTION_KEY,
                                    book.getBundle()
                            );
                            fragmentActionListener.onActionPerformed(bundle);
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                    }
                }
        ));

        binding.librarianShowSearchListSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterObjectList = librarianShowAllBooksListAdapter.filter(s);
                return false;
            }
        });
    }

    private void getStudentsList() {
        binding.librarianShowSearchListLoading.setVisibility(View.VISIBLE);
        db.collection("students")
                .whereEqualTo("libraryCode", librarian.getLibraryCode())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Student student = new Student(document);
                            objectList.add(student);
                        }
                        filterObjectList.addAll(objectList);
                        librarianShowAllStudentsListAdapter.updateSecondList();
                        binding.librarianShowSearchListLoading.setVisibility(View.GONE);

                    } else {
                        Log.w("Error", "Error getting documents.", task.getException());
                    }
                });
    }

    private void getBookList() {
        binding.librarianShowSearchListLoading.setVisibility(View.VISIBLE);
        db.collection("books")
                .whereEqualTo("libraryCode", librarian.getLibraryCode())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Book newBook = new Book(document);
                            objectList.add(newBook);
                        }
                        filterObjectList.addAll(objectList);
                        librarianShowAllBooksListAdapter.updateSecondList();
                        binding.librarianShowSearchListLoading.setVisibility(View.GONE);
                    } else {
                        Log.w("Error", "Error getting documents.", task.getException());
                    }
                });
    }

    private void getDuesList() {
        binding.librarianShowSearchListLoading.setVisibility(View.VISIBLE);
        db.collection("records")
                .whereEqualTo("returned", "false")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Record newRecord = new Record(document);
                            db.collection("books")
                                    .document(newRecord.getBookID())
                                    .get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            DocumentSnapshot newDocument = task1.getResult();
                                            if (newDocument.exists()) {
                                                Book newBook = new Book(newDocument.getData(), newDocument.getId());
                                                db.collection("students")
                                                        .whereEqualTo("uid", newRecord.getBorrowerID())
                                                        .get()
                                                        .addOnCompleteListener(task2 -> {
                                                            if (task2.isSuccessful() && !task2.getResult().isEmpty()) {
                                                                Student newStudent = null;
                                                                for (QueryDocumentSnapshot document2 : Objects.requireNonNull(task2.getResult())) {
                                                                    newStudent = new Student(document2);
                                                                }
                                                                DueRecordsListObject dueRecordsListObject = new DueRecordsListObject();
                                                                dueRecordsListObject.setBook(newBook);
                                                                dueRecordsListObject.setRecord(newRecord);
                                                                dueRecordsListObject.setStudent(newStudent);
                                                                objectList.add(dueRecordsListObject);
                                                                filterObjectList.add(dueRecordsListObject);
                                                                librarianShowDueRecordsListAdapter.updateSecondList();
                                                                binding.librarianShowSearchListLoading.setVisibility(View.GONE);
                                                            } else {
                                                                Toast.makeText(getContext(), "Error getting documents.", Toast.LENGTH_LONG).show();
                                                            }
                                                        });
                                            } else {
                                                Toast.makeText(getContext(), "Book not found. Record invalid.", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Log.w("Error", "Error getting document.", task1.getException());
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(getContext(), "getDueLit:" + task.getException(), Toast.LENGTH_LONG).show();
                        Log.w("Error", "Error getting documents.", task.getException());
                    }
                });

    }

    private void getAllRecordsList() {
        binding.librarianShowSearchListLoading.setVisibility(View.VISIBLE);
        db.collection("records")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Record newRecord = new Record(document);
                            db.collection("books")
                                    .document(newRecord.getBookID())
                                    .get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            DocumentSnapshot newDocument = task1.getResult();
                                            if (newDocument.exists()) {
                                                Book newBook = new Book(newDocument.getData(), newDocument.getId());
                                                DueRecordsListObject dueRecordsListObject = new DueRecordsListObject();
                                                dueRecordsListObject.setBook(newBook);
                                                dueRecordsListObject.setRecord(newRecord);
                                                objectList.add(dueRecordsListObject);
                                                filterObjectList.add(dueRecordsListObject);
//                                                librarianShowAllBooksListAdapter.updateSecondList();
//                                                binding.librarianShowSearchListLoading.setVisibility(View.GONE);
                                            } else {
                                                Toast.makeText(getContext(), "Book not found. Record invalid.", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Log.w("Error", "Error getting document.", task1.getException());
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(getContext(), "getDueLit:" + task.getException(), Toast.LENGTH_LONG).show();
                        Log.w("Error", "Error getting documents.", task.getException());
                    }
                });

    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener) {
        this.fragmentActionListener = fragmentActionListener;
    }
}