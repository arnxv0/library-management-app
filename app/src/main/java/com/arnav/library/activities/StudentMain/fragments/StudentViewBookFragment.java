package com.arnav.library.activities.StudentMain.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.arnav.library.QRCodeDialog;
import com.arnav.library.R;
import com.arnav.library.databinding.FragmentStudentViewBookBinding;
import com.arnav.library.models.Book;
import com.arnav.library.models.Student;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class StudentViewBookFragment extends Fragment {

    FragmentStudentViewBookBinding binding;
    Book book;
    Student student;
    QRCodeDialog qrCodeDialog;

    public StudentViewBookFragment() {
        // Required empty public constructor
    }

    public static StudentViewBookFragment newInstance(Book book, Student student, Activity activity) {
        StudentViewBookFragment fragment = new StudentViewBookFragment();
        Bundle args = new Bundle();
        args.putBundle("book", book.getBundle());
        args.putBundle("student", student.getBundle());
        fragment.setArguments(args);

//        fragment.setSharedElementEnterTransition(
//                TransitionInflater
//                        .from(activity)
//                        .inflateTransition(R.transition.shared_image)
//        );
//        fragment.setEnterTransition(
//                TransitionInflater
//                        .from(activity)
//                        .inflateTransition(android.R.transition.explode)
//        );

//        TransitionSet transitionSet = new TransitionSet();
//        transitionSet.setOrdering(TransitionSet.ORDERING_TOGETHER);
//        transitionSet.addTransition(new ChangeBounds())
//                .addTransition(new ChangeTransform())
//                .addTransition(new ChangeImageTransform());
//        fragment.setSharedElementEnterTransition(transitionSet);
//        fragment.setSharedElementReturnTransition(transitionSet);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.slide_in_up));

        qrCodeDialog = new QRCodeDialog(getActivity());

//        Transition transition = TransitionInflater
//                .from(requireContext())
//                .inflateTransition(R.transition.shared_image);
//        setSharedElementEnterTransition(transition);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStudentViewBookBinding.inflate(inflater, container, false);
        assert this.getArguments() != null;
        book = new Book(this.getArguments().getBundle("book"));
        student = new Student(this.getArguments().getBundle("student"));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String authorText = "- " + book.getAuthor();

        binding.bookTitle.setText(book.getTitle());
        binding.authorName.setText(authorText);
        binding.bookDescription.setText(book.getDescription());
        binding.bookAvailability.setText(book.getAvailability());

        //ViewCompat.setTransitionName(binding.viewBookCover, book.getBookId());

        String bookId = book.getBookId();
        String studentId = student.getUid();

        binding.buttonBorrow.setOnClickListener(view1 -> {
            String borrowString = "Borrow " + bookId + " " + studentId;
            Bitmap qrCodeBitmap = getQrCodeBitmap(borrowString);
            createQRCodeDialog(qrCodeBitmap);
        });

        binding.buttonReturn.setOnClickListener(view1 -> {
            String returnString = "Return " + bookId + " " + studentId;
            Bitmap qrCodeBitmap = getQrCodeBitmap(returnString);
            createQRCodeDialog(qrCodeBitmap);
        });

    }

    private Bitmap getQrCodeBitmap(String message) {

        Bitmap bitmap = null;

        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();

        Point point = new Point();
        display.getSize(point);

        int width = point.x;
        int height = point.y;

        int dimen = Math.min(width, height);
        dimen = dimen * 3 / 4;

        QRGEncoder qrgEncoder = new QRGEncoder(message, null, QRGContents.Type.TEXT, dimen);
        try {
            bitmap = qrgEncoder.encodeAsBitmap();
        } catch (WriterException e) {
            Log.e("QRGEncoder", e.toString());
        }

        return bitmap;
    }

    private void createQRCodeDialog (Bitmap bitmap) {

        Activity activity = getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_qrcode_dialog, null);

        ImageView qrImageView = view.findViewById(R.id.qrCodeImageView);
        qrImageView.setImageBitmap(bitmap);

        builder.setView(view);
        builder.setCancelable(false);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button doneButton = view.findViewById(R.id.buttonQrCodeDone);
        doneButton.setOnClickListener(view1 -> alertDialog.dismiss());

        //alertDialog.setOnCancelListener(dialogInterface -> alertDialog.dismiss());

    }

}