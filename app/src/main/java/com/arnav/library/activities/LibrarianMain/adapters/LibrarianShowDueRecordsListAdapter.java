package com.arnav.library.activities.LibrarianMain.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arnav.library.R;
import com.arnav.library.models.DueRecordsListObject;

import java.util.ArrayList;
import java.util.List;

public class LibrarianShowDueRecordsListAdapter extends RecyclerView.Adapter<LibrarianShowDueRecordsListAdapter.ViewHolder> {

    List<Object> dueRecordsListObjectList;
    ArrayList<Object> filterDueRecordsListObjectList;

    public LibrarianShowDueRecordsListAdapter(List<Object> dueRecordsListObjectList) {
        this.dueRecordsListObjectList = dueRecordsListObjectList;
        filterDueRecordsListObjectList = new ArrayList<>();
        filterDueRecordsListObjectList.addAll(dueRecordsListObjectList);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView bookTitleTextView;
        private final TextView studentNameTextView;
        private final TextView dueDateTextView;

        public ViewHolder(View view) {
            super(view);
            bookTitleTextView = view.findViewById(R.id.librarianDueRecordListItemBookTitle);
            studentNameTextView = view.findViewById(R.id.librarianDueRecordListItemStudentName);
            dueDateTextView = view.findViewById(R.id.librarianDueRecordListItemDueDateField);
        }

        public TextView getBookTitleTextView() {
            return bookTitleTextView;
        }

        public TextView getStudentNameTextView() {
            return studentNameTextView;
        }

        public TextView getDueDateTextView() {
            return dueDateTextView;
        }


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.librarian_view_dues_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DueRecordsListObject dueRecordsListObject = (DueRecordsListObject) filterDueRecordsListObjectList.get(position);

        String dueDateText = "Due by " + dueRecordsListObject.getRecord().getDueDate();

        //ViewCompat.setTransitionName(bookCoverImageView , book.getBookId());
        holder.getBookTitleTextView().setText(dueRecordsListObject.getBook().getTitle());
        holder.getStudentNameTextView().setText(dueRecordsListObject.getStudent().getName());
        holder.getDueDateTextView().setText(dueDateText);
    }

    @Override
    public int getItemCount() {
        return filterDueRecordsListObjectList.size();
    }

    public ArrayList<Object> filter(String charText) {
        charText = charText.toLowerCase();
        filterDueRecordsListObjectList.clear();
        if (charText.length() == 0) {
            filterDueRecordsListObjectList.addAll(dueRecordsListObjectList);
        } else {
            for (Object object : dueRecordsListObjectList) {
                DueRecordsListObject dueRecordsListObject = (DueRecordsListObject) object;
                if (dueRecordsListObject.getBook().getTitle().toLowerCase().contains(charText) ||
                        dueRecordsListObject.getStudent().getName().toLowerCase().contains(charText) ||
                        dueRecordsListObject.getRecord().getDueDate().toLowerCase().contains(charText)
                ) {
                    filterDueRecordsListObjectList.add(dueRecordsListObject);
                }
            }
        }
        notifyDataSetChanged();
        return filterDueRecordsListObjectList;
    }

    public void updateSecondList() {
        filterDueRecordsListObjectList.clear();
        filterDueRecordsListObjectList.addAll(dueRecordsListObjectList);
        notifyDataSetChanged();
    }

}
