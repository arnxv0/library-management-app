package com.arnav.library.activities.LibrarianMain.fragments;

import android.os.Bundle;

public interface FragmentActionListener {

    String ACTION_KEY = "action_key";

    int SCAN_CODE_ACTION_VALUE = 1;
    String SCAN_CODE_KEY = "scanCode";

    int LOGOUT_ACTION_VALUE = 2;

    int ADD_RECORD_ACTION_VALUE = 3;

    int UPLOAD_BOOK_ACTION_VALUE = 4;
    String UPLOAD_BOOK_URI_ACTION_KEY = "uploadBIU";

    int SHOW_STUDENTS_LIST_FRAGMENT_ACTION_VALUE = 5;

    int SHOW_BOOK_LIST_FRAGMENT_ACTION_VALUE = 6;

    int SHOW_EDIT_BOOK_FRAGMENT_ACTION_VALUE = 10;
    String SHOW_EDIT_BOOK_FRAGMENT_ACTION_KEY = "book";

    int SHOW_VIEW_BOOK_FRAGMENT_ACTION_VALUE = 11;
    String SHOW_VIEW_BOOK_FRAGMENT_ACTION_KEY = "book";

    int EDIT_BOOK_DONE_ACTION_VALUE = 12;

    void onActionPerformed(Bundle bundle);
}
