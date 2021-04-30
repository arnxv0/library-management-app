package com.arnav.library.activities.LibrarianMain.adapters;

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

import java.util.ArrayList;
import java.util.List;

public class LibrarianShowAllBooksListAdapter extends RecyclerView.Adapter<LibrarianShowAllBooksListAdapter.ViewHolder> {

    List<Object> bookList;
    ArrayList<Object> filterBookList;
    Context context;

    public LibrarianShowAllBooksListAdapter(List<Object> bookList) {
        this.bookList = bookList;
        filterBookList = new ArrayList<>();
        filterBookList.addAll(filterBookList);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView bookTitleTextView;
        private final TextView bookAuthorTextView;
        private final TextView bookAvailableTextView;
        private final ImageView bookCoverImageView;

        public ViewHolder(View view) {
            super(view);
            bookTitleTextView = view.findViewById(R.id.listItemBookTitleTextView);
            bookAuthorTextView = view.findViewById(R.id.listItemBookAuthorTextView);
            bookAvailableTextView = view.findViewById(R.id.listItemBookAvailableTextView);
            bookCoverImageView = view.findViewById(R.id.listItemBookImageView);
        }

        public TextView getBookTitleTextView() {
            return bookTitleTextView;
        }

        public TextView getBookAuthorTextView() {
            return bookAuthorTextView;
        }

        public TextView getBookAvailableTextView() {
            return bookAvailableTextView;
        }

        public ImageView getBookCoverImageView() {
            return bookCoverImageView;
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        this.context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.librarian_search_books_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = (Book) filterBookList.get(position);
        String authorString = "- " + book.getAuthor();

        //ViewCompat.setTransitionName(bookCoverImageView , book.getBookId());
        holder.getBookTitleTextView().setText(book.getTitle());
        holder.getBookAuthorTextView().setText(authorString);
        holder.getBookAvailableTextView().setText(book.getAvailability());

        Book.getAndSetBookImage(book, holder.getBookCoverImageView(), context);
    }

    @Override
    public int getItemCount() {
        return filterBookList.size();
    }

    public ArrayList<Object> filter(String charText) {
        charText = charText.toLowerCase();
        filterBookList.clear();
        if (charText.length() == 0) {
            filterBookList.addAll(bookList);
        } else {
            for (Object book : bookList) {
                Book thisBook = (Book) book;
                if (thisBook.getTitle().toLowerCase().contains(charText) ||
                        thisBook.getAuthor().toLowerCase().contains(charText)) {
                    filterBookList.add(book);
                }
            }
        }
        notifyDataSetChanged();
        return filterBookList;
    }

    public void updateSecondList() {
        filterBookList.addAll(bookList);
        notifyDataSetChanged();
    }

}
