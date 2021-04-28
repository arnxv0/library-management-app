package com.arnav.library.activities.StudentMain.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.arnav.library.R;
import com.arnav.library.models.Book;

import java.util.ArrayList;
import java.util.List;

public class StudentBooksListAdapter extends ArrayAdapter {

    Context context;
    int resource;
    List<Book> bookList;
    ArrayList<Book> filterBookList;

    public StudentBooksListAdapter(@NonNull Context context, int resource, @NonNull List<Book> bookList) {
        super(context, resource, bookList);
        this.context = context;
        this.resource = resource;
        this.bookList = bookList;
        this.filterBookList = new ArrayList<>();
        filterBookList.addAll(bookList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(resource, null);
        TextView bookTitleTextView = view.findViewById(R.id.listItemBookTitleTextView);
        TextView bookAuthorTextView = view.findViewById(R.id.listItemBookAuthorTextView);
        TextView bookAvailableTextView = view.findViewById(R.id.listItemBookAvailableTextView);
        ImageView bookCoverImageView = view.findViewById(R.id.listItemBookImageView);

        Book book = filterBookList.get(position);
        String authorString = "- " + book.getAuthor();

        //ViewCompat.setTransitionName(bookCoverImageView , book.getBookId());
        bookTitleTextView.setText(book.getTitle());
        bookAuthorTextView.setText(authorString);
        bookAvailableTextView.setText(book.getAvailability());

        return view;
    }

    @Override
    public int getCount() {
        return filterBookList.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return filterBookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ArrayList<Book> filter(String charText) {
        charText = charText.toLowerCase();
        filterBookList.clear();
        if (charText.length() == 0) {
            filterBookList.addAll(bookList);
        } else {
            for (Book book : bookList) {
                if (book.getTitle().toLowerCase().contains(charText) ||
                        book.getAuthor().toLowerCase().contains(charText)) {
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
