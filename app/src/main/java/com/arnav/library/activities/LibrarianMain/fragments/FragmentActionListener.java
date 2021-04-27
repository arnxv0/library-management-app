package com.arnav.library.activities.LibrarianMain.fragments;

import android.os.Bundle;
import android.view.View;

public interface FragmentActionListener {

    String ACTION_KEY = "action_key";

    int SCAN_CODE_ACTION_VALUE = 1;
    String SCAN_CODE_KEY = "scanCode";

    int LOGOUT_ACTION_VALUE = 2;

    int ADD_RECORD_ACTION_VALUE = 3;

    int UPLOAD_BOOK_ACTION_VALUE = 4;
    String UPLOAD_BOOK_URI_ACTION_KEY = "uploadBIU";

    void onActionPerformed(Bundle bundle);
}
