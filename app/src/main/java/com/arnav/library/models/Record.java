package com.arnav.library.models;

import android.os.Bundle;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class Record {

    private final String borrowerID, bookID, libraryCode, librarianID;
    private String returnDate, dueDate, returned;

    public Record(QueryDocumentSnapshot documentSnapshot) {
        this.borrowerID = documentSnapshot.getString("borrowerID");
        this.bookID = documentSnapshot.getString("bookID");
        this.returnDate = documentSnapshot.getString("returnDate");
        this.libraryCode = documentSnapshot.getString("libraryCode");
        this.librarianID = documentSnapshot.getString("librarianID");
        this.dueDate = documentSnapshot.getString("dueDate");
        this.returned = documentSnapshot.getString("returned");
    }

    public Record(Bundle bundle) {
        this.borrowerID = bundle.getString("borrowerID");
        this.bookID = bundle.getString("bookID");
        this.returnDate = bundle.getString("returnDate");
        this.libraryCode = bundle.getString("libraryCode");
        this.librarianID = bundle.getString("librarianID");
        this.dueDate = bundle.getString("dueDate");
        this.returned = bundle.getString("returned");
    }

    public Record(
            String borrowerID,
            String bookID,
            String returnDate,
            String libraryCode,
            String librarianID,
            String dueDate,
            String returned
    ) {
        this.borrowerID = borrowerID;
        this.bookID = bookID;
        this.returnDate = returnDate;
        this.libraryCode = libraryCode;
        this.librarianID = librarianID;
        this.dueDate = dueDate;
        this.returned = returned;

    }

    public String getBorrowerID() {
        return borrowerID;
    }

    public String getBookID() {
        return bookID;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public String getLibraryCode() {
        return libraryCode;
    }

    public String getLibrarianID() {
        return librarianID;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getReturned() {
        return returned;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setReturned(String returned) {
        this.returned = returned;
    }

    public Map<String, Object> getObjectMap() {

        Map<String, Object> map = new HashMap<>();
        map.put("borrowerID", this.borrowerID);
        map.put("bookID", this.bookID);
        map.put("returnDate", this.returnDate);
        map.put("libraryCode", this.libraryCode);
        map.put("librarianID", this.librarianID);
        map.put("dueDate", this.dueDate);
        map.put("returned", this.returned);

        return map;

    }

    public Bundle getBundle() {

        Bundle bundle = new Bundle();
        bundle.putString("borrowerID", this.borrowerID);
        bundle.putString("bookID", this.bookID);
        bundle.putString("returnDate", this.returnDate);
        bundle.putString("libraryCode", this.libraryCode);
        bundle.putString("librarianID", this.librarianID);
        bundle.putString("dueDate", this.dueDate);
        bundle.putString("returned", this.returned);

        return bundle;
    }
}
