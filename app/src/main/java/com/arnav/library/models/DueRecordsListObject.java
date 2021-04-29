package com.arnav.library.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DueRecordsListObject {
    Book book;
    Record record;

    public DueRecordsListObject() {
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public Book getBook() {
        return book;
    }

    public Record getRecord() {
        return record;
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
        return dayCount;
    }

}