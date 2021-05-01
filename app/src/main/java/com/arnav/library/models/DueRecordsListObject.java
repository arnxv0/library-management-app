package com.arnav.library.models;

import android.os.Bundle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DueRecordsListObject {
    Book book;
    Record record;
    Student student;

    public DueRecordsListObject() {
    }

    public DueRecordsListObject(Bundle bundle) {
        this.book = new Book(bundle.getBundle("book"));
        this.record = new Record(bundle.getBundle("record"));
    }

    public Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putBundle("book", this.book.getBundle());
        bundle.putBundle("record", this.record.getBundle());
        return bundle;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Book getBook() {
        return book;
    }

    public Record getRecord() {
        return record;
    }

    public Student getStudent() {
        return student;
    }

    public boolean bookIsReturned() {
        return false;
    }

    public int daysUntilBookReturn() {

        String dueDate = this.record.getDueDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        Date expireConvertedDate = null;
        try {
            expireConvertedDate = dateFormat.parse(dueDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar todayCal = Calendar.getInstance();
        Calendar dueDateCal = Calendar.getInstance();
        dueDateCal.setTime(expireConvertedDate);
        long diff = dueDateCal.getTimeInMillis() - todayCal.getTimeInMillis();
        int dayCount = (int) diff / (24 * 60 * 60 * 1000);
        return dayCount + 1;
    }

}