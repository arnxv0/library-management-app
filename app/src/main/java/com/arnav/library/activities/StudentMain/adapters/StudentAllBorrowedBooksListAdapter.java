package com.arnav.library.activities.StudentMain.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arnav.library.R;
import com.arnav.library.models.Book;
import com.arnav.library.models.DueRecordsListObject;

import java.util.List;

public class StudentAllBorrowedBooksListAdapter extends RecyclerView.Adapter<StudentAllBorrowedBooksListAdapter.ViewHolder> {

    List<DueRecordsListObject> dueRecordsListObjectList;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView bookTitleTextView;
        private final TextView bookAuthorTextView;
        private final TextView bookReturnedTextView;
        private final ImageView bookCoverImageView;

        public ViewHolder(View view) {
            super(view);

            bookTitleTextView = view.findViewById(R.id.studentAllBorrowedBooksBookTitleTextView);
            bookReturnedTextView = view.findViewById(R.id.studentAllBorrowedBooksBookDueDays);
            bookAuthorTextView = view.findViewById(R.id.studentAllBorrowedBooksBookAuthorTextView);
            bookCoverImageView = view.findViewById(R.id.studentAllBorrowedBooksBookImageView);
        }

        public TextView getBookTitleTextView() {
            return bookTitleTextView;
        }

        public ImageView getBookCoverImageView() {
            return bookCoverImageView;
        }

        public TextView getBookAuthorTextView() {
            return bookAuthorTextView;
        }

        public TextView getBookReturnedTextView() {
            return bookReturnedTextView;
        }
    }

    public StudentAllBorrowedBooksListAdapter(@NonNull List<DueRecordsListObject> allBorrowedBooksList) {
        this.dueRecordsListObjectList = allBorrowedBooksList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_all_borrowed_books_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DueRecordsListObject dueRecordsListObject = dueRecordsListObjectList.get(position);
//        String bookTitle = "";
//        if (book.getTitle().length() > 16) {
//            bookTitle = book.getTitle().substring(0, 16) + "...";
//        } else {
//            bookTitle = book.getTitle();
//        }

        String dueRecordText;

        if (dueRecordsListObject.getRecord().getReturned().equals("false")) {
            if (dueRecordsListObject.daysUntilBookReturn() == 0) {
                dueRecordText = "Due today";
            } else if (dueRecordsListObject.daysUntilBookReturn() == 1) {
                dueRecordText = "Due tomorrow";
            } else {
                dueRecordText = "Due in " + dueRecordsListObject.daysUntilBookReturn() + " days";
            }
            holder.getBookReturnedTextView().setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_baseline_info_24,
                    0
            );
        } else {
            dueRecordText = "Returned";
            holder.getBookReturnedTextView().setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_baseline_check_circle_24,
                    0
            );
            holder.getBookReturnedTextView().setTextColor(Color.parseColor("#00FF00"));
        }


        holder.getBookTitleTextView().setText(dueRecordsListObject.getBook().getTitle());
        holder.getBookAuthorTextView().setText(dueRecordsListObject.getBook().getAuthor());
        holder.getBookReturnedTextView().setText(dueRecordText);
        holder.getBookCoverImageView().setClipToOutline(true);

        Book.getAndSetBookImage(dueRecordsListObject.getBook(), holder.getBookCoverImageView(), context);
    }

    @Override
    public int getItemCount() {
        return dueRecordsListObjectList.size();
    }
}
