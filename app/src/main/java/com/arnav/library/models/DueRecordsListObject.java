package com.arnav.library.models;

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
        return 0;
    }
}