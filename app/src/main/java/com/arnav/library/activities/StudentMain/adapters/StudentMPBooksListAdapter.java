package com.arnav.library.activities.StudentMain.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arnav.library.R;
import com.arnav.library.models.Book;

import java.util.List;

public class StudentMPBooksListAdapter extends RecyclerView.Adapter<StudentMPBooksListAdapter.ViewHolder> {

    List<Book> bookList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView bookTitleTextView;
        private final ImageView bookCoverImageView;

        public ViewHolder(View view) {
            super(view);

            bookTitleTextView = view.findViewById(R.id.mpListItemBookTitleTextView);
            bookCoverImageView = view.findViewById(R.id.mpListItemBookImageView);
        }

        public TextView getBookTitleTextView() {
            return bookTitleTextView;
        }

        public ImageView getBookCoverImageView() {
            return bookCoverImageView;
        }
    }

    public StudentMPBooksListAdapter(@NonNull List<Book> mPBookList) {
        this.bookList = mPBookList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_mplist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.getBookTitleTextView().setText(book.getTitle());
        holder.getBookCoverImageView().setClipToOutline(true);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
}
