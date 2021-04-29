package com.arnav.library.activities.StudentMain.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.arnav.library.R;
import com.arnav.library.activities.StudentMain.adapters.StudentDuesListAdapter;
import com.arnav.library.activities.StudentMain.adapters.StudentMPBooksListAdapter;
import com.arnav.library.activities.StudentMain.listeners.RecyclerItemClickListener;
import com.arnav.library.databinding.FragmentStudentHomeBinding;
import com.arnav.library.models.Book;
import com.arnav.library.models.DueRecordsListObject;
import com.arnav.library.models.Record;
import com.arnav.library.models.Student;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class StudentHomeFragment extends Fragment {

    FragmentStudentHomeBinding binding;

    FirebaseFirestore db;
    Student student;
    List<Book> mpBookList;
    List<DueRecordsListObject> dueBooksList;
    StudentMPBooksListAdapter studentMPBooksListAdapter;
    StudentDuesListAdapter studentDuesListAdapter;
    FragmentActionListener fragmentActionListener;


    public StudentHomeFragment() {
        // Required empty public constructor
    }

    public static StudentHomeFragment newInstance(
            Student student,
            FragmentActionListener fragmentActionListener
    ) {
        StudentHomeFragment fragment = new StudentHomeFragment();
        Bundle args = new Bundle();
        args.putBundle("student", student.getBundle());
        fragment.setArguments(args);
        fragment.setFragmentActionListener(fragmentActionListener);
        //fragment.setSharedElementEnterTransition();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        assert this.getArguments() != null;
        student = new Student(this.getArguments().getBundle("student"));
        binding = FragmentStudentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mpBookList = new ArrayList<>();
        dueBooksList = new ArrayList<>();
        getMPBooksList();
        setupMPBooksListView();
        getStudentDuesList();
        setupDueBooksListView();

    }

    private void setupDueBooksListView() {
        studentDuesListAdapter = new StudentDuesListAdapter(dueBooksList);
        binding.studentDuesListRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mpListLayoutManager = new LinearLayoutManager(getActivity());
        mpListLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.studentDuesListRecyclerView.setAdapter(studentDuesListAdapter);
        binding.studentDuesListRecyclerView.setLayoutManager(mpListLayoutManager);

        DividerItemDecoration itemDecorator = new DividerItemDecoration(requireContext(), mpListLayoutManager.getOrientation());
        itemDecorator.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(requireContext(), R.drawable.student_dues_list_divider)));
        binding.studentDuesListRecyclerView.addItemDecoration(itemDecorator);

        binding.studentDuesListRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), binding.studentDuesListRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (fragmentActionListener != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(
                            FragmentActionListener.ACTION_KEY,
                            FragmentActionListener.ACTION_VALUE_SELECTED_BOOK
                    );
                    bundle.putBundle(
                            FragmentActionListener.SELECTED_BOOK_KEY,
                            dueBooksList.get(position).getBook().getBundle()
                    );
                    fragmentActionListener.onActionPerformed(bundle, view);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

    }

    private void setupMPBooksListView() {
        studentMPBooksListAdapter = new StudentMPBooksListAdapter(mpBookList);
        binding.studentMPListRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mpListLayoutManager = new LinearLayoutManager(getActivity());
        mpListLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.studentMPListRecyclerView.setAdapter(studentMPBooksListAdapter);
        binding.studentMPListRecyclerView.setLayoutManager(mpListLayoutManager);

        DividerItemDecoration itemDecorator = new DividerItemDecoration(requireContext(), mpListLayoutManager.getOrientation());
        itemDecorator.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(requireContext(), R.drawable.mp_book_list_divider)));
        binding.studentMPListRecyclerView.addItemDecoration(itemDecorator);

        binding.studentMPListRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), binding.studentMPListRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (fragmentActionListener != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(
                            FragmentActionListener.ACTION_KEY,
                            FragmentActionListener.ACTION_VALUE_SELECTED_BOOK
                    );
                    bundle.putBundle(
                            FragmentActionListener.SELECTED_BOOK_KEY,
                            mpBookList.get(position).getBundle()
                    );
                    fragmentActionListener.onActionPerformed(bundle, view);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));
    }

    private void getMPBooksList() {
        binding.studentMPListLoading.setVisibility(View.VISIBLE);
        db.collection("books")
                .whereEqualTo("libraryCode", student.getLibraryCode())
                .limit(6)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Book newBook = new Book(document);
                            mpBookList.add(newBook);
                        }
                        studentMPBooksListAdapter.notifyDataSetChanged();
                        binding.studentMPListLoading.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getContext(), "Try again later", Toast.LENGTH_LONG).show();
                        Log.w("Error", "Error getting documents.", task.getException());
                    }
                });
    }

    private void getStudentDuesList() {
        binding.studentDuesListLoading.setVisibility(View.VISIBLE);
        db.collection("records")
                .whereEqualTo("borrowerID", student.getUid())
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
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot newDocument = task1.getResult();
                                            if (newDocument.exists()) {
                                                Book newBook = new Book(newDocument.getData(), newDocument.getId());
                                                DueRecordsListObject dueRecordsListObject = new DueRecordsListObject();
                                                dueRecordsListObject.setRecord(newRecord);
                                                dueRecordsListObject.setBook(newBook);
                                                dueBooksList.add(dueRecordsListObject);
                                                studentDuesListAdapter.notifyDataSetChanged();
                                                binding.studentDuesListLoading.setVisibility(View.GONE);
                                            } else {
                                                Toast.makeText(getContext(), "Book for record does not exist", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Toast.makeText(getContext(), "Error getting book details for record", Toast.LENGTH_LONG).show();
                                        }
                                    });

                        }
                    } else {
                        Toast.makeText(getContext(), "Error getting records", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener) {
        this.fragmentActionListener = fragmentActionListener;
    }

}