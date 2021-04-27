package com.arnav.library.managers;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.arnav.library.LoadingDialog;
import com.arnav.library.activities.Login.LoginActivity;
import com.arnav.library.models.Book;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class BookManager {

    List<Book> booksList;

    public BookManager(){
    }

//    public int getBookCount() {
//
//    }
//
//    public List<Book> getBooksList() {
//
//    }
//
//    public void updateBooksList() {
//
//    }
//
//    public boolean isOwner() {
//
//    }

    public void addBook(FirebaseFirestore db, Book book, OnCompleteListener<DocumentReference> onCompleteListener) {
        db.collection("books")
                .add(book.getObjectMap())
                .addOnCompleteListener(onCompleteListener);
    }

//    public boolean updateBook() {
//
//    }
//
//    public List<Book> getSuggestedBooksList() {
//
//    }
//
//    public boolean returnBook() {
//
//    }
//
//    public boolean borrowBook() {
//
//    }
//
//    public Book getBookById() {
//
//    }
//
//    public List<Book> getBooksByIds() {
//
//    }
//
//    public List<Book> getAllBorrowedBooks() {
//
//    }

}
