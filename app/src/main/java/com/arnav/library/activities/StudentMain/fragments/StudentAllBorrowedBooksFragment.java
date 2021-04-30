package com.arnav.library.activities.StudentMain.fragments;

import android.os.Bundle;
import android.transition.TransitionInflater;
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
import com.arnav.library.activities.StudentMain.adapters.StudentAllBorrowedBooksListAdapter;
import com.arnav.library.databinding.FragmentStudentAllBorrowedBooksBinding;
import com.arnav.library.models.Book;
import com.arnav.library.models.DueRecordsListObject;
import com.arnav.library.models.Record;
import com.arnav.library.models.Student;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class StudentAllBorrowedBooksFragment extends Fragment {

    FragmentStudentAllBorrowedBooksBinding binding;
    FragmentActionListener fragmentActionListener;
    Student student;

    FirebaseFirestore db;

    List<DueRecordsListObject> recordsListObjectList;
    StudentAllBorrowedBooksListAdapter studentAllBorrowedBooksListAdapter;

    public StudentAllBorrowedBooksFragment() {
        // Required empty public constructor
    }

    public static StudentAllBorrowedBooksFragment newInstance(
            Student student,
            FragmentActionListener fragmentActionListener
    ) {
        StudentAllBorrowedBooksFragment fragment = new StudentAllBorrowedBooksFragment();
        Bundle args = new Bundle();
        args.putBundle("student", student.getBundle());
        fragment.setArguments(args);
        fragment.setFragmentActionListener(fragmentActionListener);
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
        binding = FragmentStudentAllBorrowedBooksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.slide_in_up));

        recordsListObjectList = new ArrayList<>();
        getAllBorrowedBooksList();
        setupAllBorrowedBooksRecyclerView();

    }

    private void setupAllBorrowedBooksRecyclerView() {
        studentAllBorrowedBooksListAdapter = new StudentAllBorrowedBooksListAdapter(recordsListObjectList);
        binding.studentAllBorrowedBooksRecyclerView.setHasFixedSize(true);
        LinearLayoutManager allBorrowedBooksListLayoutManager = new LinearLayoutManager(getActivity());
        allBorrowedBooksListLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        binding.studentAllBorrowedBooksRecyclerView.setAdapter(studentAllBorrowedBooksListAdapter);
        binding.studentAllBorrowedBooksRecyclerView.setLayoutManager(allBorrowedBooksListLayoutManager);

        DividerItemDecoration itemDecorator = new DividerItemDecoration(requireContext(), allBorrowedBooksListLayoutManager.getOrientation());
        itemDecorator.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(requireContext(), R.drawable.student_all_borrowed_books_list_divider)));
        binding.studentAllBorrowedBooksRecyclerView.addItemDecoration(itemDecorator);
    }

    private void getAllBorrowedBooksList() {
        binding.studentAllBorrowedBooksLoading.setVisibility(View.VISIBLE);
        db.collection("records")
                .whereEqualTo("borrowerID", student.getUid())
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
                                            assert newDocument != null;
                                            if (newDocument.exists()) {
                                                Book newBook = new Book(Objects.requireNonNull(newDocument.getData()), newDocument.getId());
                                                DueRecordsListObject dueRecordsListObject = new DueRecordsListObject();
                                                dueRecordsListObject.setRecord(newRecord);
                                                dueRecordsListObject.setBook(newBook);
                                                recordsListObjectList.add(dueRecordsListObject);
                                                Collections.sort(recordsListObjectList, (o1, o2) -> {
                                                    if (dueRecordsListObject.getRecord().getReturned().equals("false")) {
                                                        return Integer.compare(o1.daysUntilBookReturn(), o2.daysUntilBookReturn());
                                                    } else {
                                                        return -1;
                                                    }

                                                });
                                                studentAllBorrowedBooksListAdapter.notifyDataSetChanged();
                                                binding.studentAllBorrowedBooksLoading.setVisibility(View.GONE);
                                            } else {
                                                Toast.makeText(getContext(), "Book for record does not exist", Toast.LENGTH_LONG).show();
                                            }

                                        } else {
                                            Toast.makeText(getContext(), "Error getting book details for record", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(getContext(), "Cant get records.", Toast.LENGTH_LONG).show();
                    }
                });

    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener) {
        this.fragmentActionListener = fragmentActionListener;
    }

}