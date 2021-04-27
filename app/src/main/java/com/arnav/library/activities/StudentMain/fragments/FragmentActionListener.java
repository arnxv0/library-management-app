package com.arnav.library.activities.StudentMain.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.arnav.library.models.Book;

public interface FragmentActionListener {

    String ACTION_KEY = "action_key";

    int ACTION_VALUE_SELECTED_BOOK = 1;
    String SELECTED_BOOK_KEY = "selectedBook";

    int LOGOUT_ACTION_VALUE = 2;
    String LOGOUT_ACTION_KEY = "logout";

    void onActionPerformed(Bundle bundle, View view);
}
