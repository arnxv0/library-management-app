package com.arnav.library.models;

import android.os.Bundle;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class Student {

    private final String name, libraryID, email, uid, libraryCode, phone;

    public Student(Bundle bundle) {
        this.name = bundle.getString("name");
        this.libraryID = bundle.getString("libraryID");
        this.email = bundle.getString("email");
        this.phone = bundle.getString("phone");
        this.uid = bundle.getString("uid");
        this.libraryCode = bundle.getString("libraryCode");
    }

    public Student(
            String name,
            String libraryID,
            String email,
            String phone,
            String uid,
            String libraryCode
    ) {
        this.name = name;
        this.libraryID = libraryID;
        this.email = email;
        this.phone = phone;
        this.uid = uid;
        this.libraryCode = libraryCode;
    }

    public Student(QueryDocumentSnapshot document) {
        this.name = document.getString("name");
        this.libraryID = document.getString("libraryID");
        this.email = document.getString("email");
        this.phone = document.getString("phone");
        this.uid = document.getString("uid");
        this.libraryCode = document.getString("libraryCode");
    }

    public String getName() {
        return name;
    }

    public String getLibraryID() {
        return libraryID;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    public String getLibraryCode() {
        return libraryCode;
    }

    public String getPhone() {
        return phone;
    }

    public Map<String, Object> getObjectMap() {

        Map<String, Object> map = new HashMap<>();
        map.put("name", this.name);
        map.put("libraryID", this.libraryID);
        map.put("email", this.email);
        map.put("uid", this.uid);
        map.put("libraryCode", this.libraryCode);
        map.put("phone", this.phone);

        return map;

    }

    public Bundle getBundle() {

        Bundle bundle = new Bundle();
        bundle.putString("name", this.name);
        bundle.putString("libraryID", this.libraryID);
        bundle.putString("email", this.email);
        bundle.putString("uid", this.uid);
        bundle.putString("libraryCode", this.libraryCode);
        bundle.putString("phone", this.phone);

        return bundle;
    }
}
