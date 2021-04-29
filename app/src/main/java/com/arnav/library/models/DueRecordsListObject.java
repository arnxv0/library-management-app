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

//    public int getCountOfDays(String createdDateString, String expireDateString) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
//
//        Date createdConvertedDate = null, expireConvertedDate = null, todayWithZeroTime = null;
//        try {
//            createdConvertedDate = dateFormat.parse(createdDateString);
//            expireConvertedDate = dateFormat.parse(expireDateString);
//
//            Date today = new Date();
//            todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        int cYear = 0, cMonth = 0, cDay = 0;
//
//        if (createdConvertedDate.after(todayWithZeroTime)) {
//            Calendar cCal = Calendar.getInstance();
//            cCal.setTime(createdConvertedDate);
//            cYear = cCal.get(Calendar.YEAR);
//            cMonth = cCal.get(Calendar.MONTH);
//            cDay = cCal.get(Calendar.DAY_OF_MONTH);
//
//        } else {
//            Calendar cCal = Calendar.getInstance();
//            cCal.setTime(todayWithZeroTime);
//            cYear = cCal.get(Calendar.YEAR);
//            cMonth = cCal.get(Calendar.MONTH);
//            cDay = cCal.get(Calendar.DAY_OF_MONTH);
//        }
//
//
//    /*Calendar todayCal = Calendar.getInstance();
//    int todayYear = todayCal.get(Calendar.YEAR);
//    int today = todayCal.get(Calendar.MONTH);
//    int todayDay = todayCal.get(Calendar.DAY_OF_MONTH);
//    */
//
//        Calendar eCal = Calendar.getInstance();
//        eCal.setTime(expireConvertedDate);
//
//        int eYear = eCal.get(Calendar.YEAR);
//        int eMonth = eCal.get(Calendar.MONTH);
//        int eDay = eCal.get(Calendar.DAY_OF_MONTH);
//
//        Calendar date1 = Calendar.getInstance();
//        Calendar date2 = Calendar.getInstance();
//
//        date1.clear();
//        date1.set(cYear, cMonth, cDay);
//        date2.clear();
//        date2.set(eYear, eMonth, eDay);
//
//        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();
//
//        float dayCount = (float) diff / (24 * 60 * 60 * 1000);
//
//        return (int) dayCount;
//    }
}