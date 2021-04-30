package com.arnav.library.models;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class Book {

    private final String title;
    private final String author;
    private final String libraryCode;
    private final String description;
    private final String librarianID;
    private final String availableCount;
    private final String bookId;
    private String imageURl;

    public Book(Bundle bundle) {
        this.title = bundle.getString("title");
        this.author = bundle.getString("author");
        this.libraryCode = bundle.getString("libraryCode");
        this.description = bundle.getString("description");
        this.librarianID = bundle.getString("librarianID");
        this.availableCount = bundle.getString("availableCount");
        this.bookId = bundle.getString("bookId", "");
    }

    public Book(
            String title,
            String author,
            String libraryCode,
            String description,
            String librarianID,
            String availableCount,
            String bookId
    ) {
        this.title = title;
        this.author = author;
        this.libraryCode = libraryCode;
        this.description = description;
        this.librarianID = librarianID;
        this.availableCount = availableCount;
        this.bookId = bookId;
    }

    public Book(QueryDocumentSnapshot document) {
        this.title = document.getString("title");
        this.author = document.getString("author");
        this.libraryCode = document.getString("libraryCode");
        this.description = document.getString("description");
        this.librarianID = document.getString("librarianID");
        this.availableCount = document.getString("availableCount");
        this.bookId = document.getId();
    }

    public Book(Map<String, Object> document, String documentId) {
        this.title = (String) document.get("title");
        this.author = (String) document.get("author");
        this.libraryCode = (String) document.get("libraryCode");
        this.description = (String) document.get("description");
        this.librarianID = (String) document.get("librarianID");
        this.availableCount = (String) document.get("availableCount");
        this.bookId = documentId;
    }

    public static void getAndSetBookImage(Book book, ImageView imageView, Context context) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        String bookFilePath = "bookCoverImages/" + book.getBookId() + ".jpg";

        StorageReference storageRef = storage.getReference();
        try {
            StorageReference mountainImagesRef = storageRef.child(bookFilePath);
            mountainImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    if (context != null) {
                        Glide.with(context)
                                .load(uri.toString())
                                .into(imageView);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("Failed to get img url", exception.toString());
                }
            });
        } catch (Exception e) {
            Log.e("Image doesn't exist", e.toString());
        }

    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getLibraryCode() {
        return libraryCode;
    }

    public String getDescription() {
        return description;
    }

    public String getLibrarianID() {
        return librarianID;
    }

    public String getAvailableCount() {
        return availableCount;
    }

    public String getBookId() {
        return bookId;
    }

    public String getImageURl() {
        return imageURl;
    }

    public void setImageURl(String imageURl) {
        this.imageURl = imageURl;
    }

    public Map<String, Object> getObjectMap() {

        Map<String, Object> map = new HashMap<>();
        map.put("title", this.title);
        map.put("author", this.author);
        map.put("libraryCode", this.libraryCode);
        map.put("description", this.description);
        map.put("librarianID", this.librarianID);
        map.put("availableCount", this.availableCount);

        return map;

    }

    public Bundle getBundle() {

        Bundle bundle = new Bundle();
        bundle.putString("title", this.title);
        bundle.putString("author", this.author);
        bundle.putString("libraryCode", this.libraryCode);
        bundle.putString("description", this.description);
        bundle.putString("librarianID", this.librarianID);
        bundle.putString("availableCount", this.availableCount);
        bundle.putString("bookId", this.bookId);

        return bundle;
    }

    public String getAvailability() {
        String availableString = "Available";
        if (this.availableCount.equals("")) {
            return "Not Available";
        }
        if (Integer.parseInt(this.getAvailableCount()) == 0) {
            availableString = "Not Available";
        }
        return availableString;
    }
}
