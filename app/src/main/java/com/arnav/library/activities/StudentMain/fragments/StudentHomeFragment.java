package com.arnav.library.activities.StudentMain.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.arnav.library.R;
import com.arnav.library.activities.StudentMain.adapters.StudentMPBooksListAdapter;
import com.arnav.library.activities.StudentMain.listeners.RecyclerItemClickListener;
import com.arnav.library.databinding.FragmentStudentHomeBinding;
import com.arnav.library.models.Book;
import com.arnav.library.models.Student;
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
    StudentMPBooksListAdapter studentMPBooksListAdapter;
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
        getMPBooksList();
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
                        Log.w("Error", "Error getting documents.", task.getException());
                    }
                });
    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener) {
        this.fragmentActionListener = fragmentActionListener;
    }

}