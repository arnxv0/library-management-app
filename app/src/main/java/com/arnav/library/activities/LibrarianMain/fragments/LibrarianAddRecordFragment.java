package com.arnav.library.activities.LibrarianMain.fragments;

import android.app.DatePickerDialog;
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
import com.arnav.library.databinding.FragmentLibrarianAddRecordBinding;
import com.arnav.library.models.Book;
import com.arnav.library.models.Record;
import com.arnav.library.models.Student;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class LibrarianAddRecordFragment extends Fragment {

    FragmentLibrarianAddRecordBinding binding;
    private Record record;
    private FirebaseFirestore db;
    private LoadingDialog loadingDialog;
    private FragmentActionListener fragmentActionListener;
    private String action;
    private Student student;
    private Book book;
    private Calendar calendar;
    private String returnRecordDocId;

    public LibrarianAddRecordFragment() {
        // Required empty public constructor
    }


    public static LibrarianAddRecordFragment newInstance(
            Record record,
            FragmentActionListener fragmentActionListener,
            String action
    ) {
        LibrarianAddRecordFragment fragment = new LibrarianAddRecordFragment();
        Bundle args = new Bundle();
        args.putBundle("record", record.getBundle());
        args.putString("action", action);
        fragment.setArguments(args);
        fragment.setFragmentActionListener(fragmentActionListener);
        return fragment;
    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener) {
        this.fragmentActionListener = fragmentActionListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.slide_in_up));
        db = FirebaseFirestore.getInstance();
        calendar = Calendar.getInstance();
        loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.initializeLoadingDialog();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        assert this.getArguments() != null;
        record = new Record(this.getArguments().getBundle("record"));
        action = this.getArguments().getString("action");

        binding = FragmentLibrarianAddRecordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.confirmButton.setOnClickListener(view1 -> {
            if (action.equals("Borrow")) {
                addRecord();
            } else if (action.equals("Return")) {
                updateReturnRecord();
            }
        });

        if (action.equals("Borrow")) {
            binding.editTextDate.setFocusable(false);
            binding.editTextDate.setOnClickListener(view1 -> showDateDialog());
        } else if (action.equals("Return")) {
            binding.editTextDate.setFocusable(false);
        }
        binding.studentActionTextView.setText(action);
        getAndSetDetails();
    }

    private void updateReturnRecord() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        Date today = new Date();
        String returnDate = dateFormat.format(today);

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("returned", "true");
        updateMap.put("returnDate", returnDate);

        loadingDialog.showDialog();
        db.collection("records")
                .document(returnRecordDocId)
                .update(updateMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int newCount = Integer.parseInt(book.getAvailableCount()) + 1;
                        db.collection("books")
                                .document(book.getBookId())
                                .update("availableCount", String.valueOf(newCount))
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(getContext(), "Successfully returned book", Toast.LENGTH_LONG).show();
                                        loadingDialog.hideDialog();
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY, FragmentActionListener.ADD_RECORD_ACTION_VALUE);
                                        fragmentActionListener.onActionPerformed(bundle);
                                    } else {
                                        Toast.makeText(getContext(), "Book count not updated, please update manually", Toast.LENGTH_LONG).show();
                                    }
                                });
                    } else {
                        loadingDialog.hideDialog();
                        Toast.makeText(getContext(), "Try again later!", Toast.LENGTH_LONG).show();
                        Bundle bundle = new Bundle();
                        bundle.putInt(
                                FragmentActionListener.ACTION_KEY,
                                FragmentActionListener.ADD_RECORD_ACTION_VALUE
                        );
                        fragmentActionListener.onActionPerformed(bundle);
                    }
                });
    }

    private void addRecord() {
        if (!record.getDueDate().equals("") &&
                Integer.parseInt(book.getAvailableCount()) > 0
        ) {
            loadingDialog.showDialog();
            record.setReturned("false");
            db.collection("records").add(record.getObjectMap())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            int newCount = Integer.parseInt(book.getAvailableCount()) - 1;
                            db.collection("books")
                                    .document(book.getBookId())
                                    .update("availableCount", String.valueOf(newCount))
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            loadingDialog.hideDialog();
                                        } else {
                                            Toast.makeText(getContext(), "Book count not updated, please update manually", Toast.LENGTH_LONG).show();
                                        }
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY, FragmentActionListener.ADD_RECORD_ACTION_VALUE);
                                        fragmentActionListener.onActionPerformed(bundle);
                                    });
                        } else {
                            loadingDialog.hideDialog();
                            Toast.makeText(getContext(), "Try again!", Toast.LENGTH_LONG).show();
                            Log.i("addRecord", task.getException().toString());
                        }
                    });
        } else {
            Toast.makeText(getContext(), "Book unavailable to borrow", Toast.LENGTH_LONG).show();
            Log.i("addRecord", "Book unavailable to borrow");
        }
    }

    private void showDateDialog() {
        DatePickerDialog.OnDateSetListener listener = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.YEAR, year);
            String myFormat = "dd/MM/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            binding.editTextDate.setText(sdf.format(calendar.getTime()));
            record.setDueDate(sdf.format(calendar.getTime()));
        };
        DatePickerDialog dpDialog = new DatePickerDialog(
                getActivity(),
                listener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dpDialog.show();
    }

    private void getAndSetDetails() {

        if (action.equals("Return")) {
            loadingDialog.showDialog();
            db.collection("records")
                    .whereEqualTo("borrowerID", record.getBorrowerID())
                    .whereEqualTo("bookID", record.getBookID())
                    .whereEqualTo("returned", "false")
                    .get()
                    .addOnCompleteListener(task -> {
                        loadingDialog.hideDialog();
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                record = new Record(document);
                                returnRecordDocId = document.getId();
                            }
                            binding.editTextDate.setText(record.getDueDate());
                        } else {
                            Toast.makeText(getContext(), "Not borrowed this book", Toast.LENGTH_LONG).show();
                            Log.i("getAndSetDetails", "Not borrowed this book");
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, FragmentActionListener.ADD_RECORD_ACTION_VALUE);
                            fragmentActionListener.onActionPerformed(bundle);
                        }

                    });
        } else if (action.equals("Borrow")) {

            loadingDialog.showDialog();
            db.collection("records")
                    .whereEqualTo("borrowerID", record.getBorrowerID())
                    .whereEqualTo("bookID", record.getBookID())
                    .whereEqualTo("returned", "false")
                    .get()
                    .addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful() && !task1.getResult().isEmpty()) {
                            loadingDialog.hideDialog();
                            Toast.makeText(getContext(), "Cannot borrow more than one copy", Toast.LENGTH_LONG).show();
                            Log.i("getAndSetDetails", "Cannot borrow more than one copy");
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, FragmentActionListener.ADD_RECORD_ACTION_VALUE);
                            fragmentActionListener.onActionPerformed(bundle);
                        } else {
                            AtomicBoolean apiCall1 = new AtomicBoolean(false);
                            AtomicBoolean apiCall2 = new AtomicBoolean(false);

                            db.collection("books")
                                    .document(record.getBookID())
                                    .get()
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                book = new Book(
                                                        document.getString("title"),
                                                        document.getString("author"),
                                                        document.getString("libraryCode"),
                                                        document.getString("description"),
                                                        document.getString("librarianID"),
                                                        document.getString("availableCount"),
                                                        document.getId()
                                                );
                                                binding.authorName.setText(book.getAuthor());
                                                binding.bookTitleRecord.setText(book.getTitle());
                                            } else {
                                                Log.i("not found", task.getResult().toString());
                                            }
                                        } else {
                                            Log.e("error", task.getException().toString());
                                        }
                                        apiCall1.set(true);
                                        if (apiCall2.get()) {
                                            loadingDialog.hideDialog();
                                        }
                                    });

                            Log.i("uid", record.getBorrowerID());
                            db.collection("students")
                                    .whereEqualTo("uid", record.getBorrowerID())
                                    .get()
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                student = new Student(
                                                        document.getString("name"),
                                                        document.getString("libraryID"),
                                                        document.getString("email"),
                                                        document.getString("phone"),
                                                        document.getString("uid"),
                                                        document.getString("libraryCode")
                                                );
                                            }
                                            binding.studentName.setText(student.getName());
                                            binding.studentID.setText(student.getLibraryID());
                                        }
                                        apiCall2.set(true);
                                        if (apiCall1.get()) {
                                            loadingDialog.hideDialog();
                                        }
                                    });
                        }

                    });

        }
    }


    @Override
    public void onDestroy() {
        loadingDialog.dismissDialog();
        super.onDestroy();
    }

}