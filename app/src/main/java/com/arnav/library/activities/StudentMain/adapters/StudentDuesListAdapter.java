package com.arnav.library.activities.StudentMain.adapters;

import android.content.Context;
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

public class StudentDuesListAdapter extends RecyclerView.Adapter<StudentDuesListAdapter.ViewHolder> {

    List<DueRecordsListObject> dueRecordsListObjectList;

    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView bookTitleTextView;
        private final TextView bookAuthorTextView;
        private final TextView bookDueDaysTextView;
        private final ImageView bookCoverImageView;

        public ViewHolder(View view) {
            super(view);

            bookTitleTextView = view.findViewById(R.id.studentDueListBookTitle);
            bookAuthorTextView = view.findViewById(R.id.studentDueListBookAuthor);
            bookDueDaysTextView = view.findViewById(R.id.studentDueListDueDays);
            bookCoverImageView = view.findViewById(R.id.studentDueListBookImg);
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

        public TextView getBookDueDaysTextView() {
            return bookDueDaysTextView;
        }
    }

    public StudentDuesListAdapter(@NonNull List<DueRecordsListObject> dueRecordsListObjectList) {
        this.dueRecordsListObjectList = dueRecordsListObjectList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_due_records_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DueRecordsListObject dueRecordsListObject = dueRecordsListObjectList.get(position);
        String dueDays = "";
        String authorName = "- " + dueRecordsListObject.getBook().getAuthor();
        if (dueRecordsListObject.daysUntilBookReturn() == 0) {
            dueDays = "Due today";
        } else if (dueRecordsListObject.daysUntilBookReturn() == 1) {
            dueDays = "Due tomorrow";
        } else {
            dueDays = "Due in " + dueRecordsListObject.daysUntilBookReturn() + " days";
        }

        holder.getBookTitleTextView().setText(dueRecordsListObject.getBook().getTitle());
        holder.getBookAuthorTextView().setText(authorName);
        holder.getBookDueDaysTextView().setText(dueDays);
        holder.getBookCoverImageView().setClipToOutline(true);

        Book.getAndSetBookImage(dueRecordsListObject.getBook(), holder.getBookCoverImageView(), context);
    }

    @Override
    public int getItemCount() {
        return dueRecordsListObjectList.size();
    }
}
