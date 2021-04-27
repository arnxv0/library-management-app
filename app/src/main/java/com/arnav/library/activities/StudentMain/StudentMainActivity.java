package com.arnav.library.activities.StudentMain;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.arnav.library.R;
import com.arnav.library.activities.Login.LoginActivity;
import com.arnav.library.activities.StudentMain.fragments.FragmentActionListener;
import com.arnav.library.activities.StudentMain.fragments.StudentHomeFragment;
import com.arnav.library.activities.StudentMain.fragments.StudentProfileFragment;
import com.arnav.library.activities.StudentMain.fragments.StudentSearchBooksFragment;
import com.arnav.library.activities.StudentMain.fragments.StudentViewBookFragment;
import com.arnav.library.databinding.ActivityStudentMainBinding;
import com.arnav.library.models.Book;
import com.arnav.library.models.Student;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StudentMainActivity extends AppCompatActivity implements FragmentActionListener {

    Student student;
    int previousItemId;
    ActivityStudentMainBinding binding;
    boolean bookShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityStudentMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.studentBottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        previousItemId = R.id.page_1;

        student = new Student(getIntent().getBundleExtra("student"));
        bookShown = false;

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.student_fragment_container, StudentHomeFragment.newInstance(student, this))
                .commit();
        setFragmentListeners();
        setCurrentDate();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            item -> {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();
                if (previousItemId == itemId) {
                    return true;
                } else if (previousItemId != R.id.page_1) {
                    getSupportFragmentManager().popBackStack();
                }
                previousItemId = itemId;

                if (getSupportFragmentManager().getBackStackEntryCount() >= 2) {
                    Log.i("HomeFragmentin", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));
                    getSupportFragmentManager().popBackStack();
                }
                if (itemId == R.id.page_1) {
                    showStudentHomeFragment();
                } else if (itemId == R.id.page_2) {
                    showStudentSearchBooksFragment();
                } else if (itemId == R.id.page_3) {
                    showStudentSearchBooksFragment();
                } else if (itemId == R.id.page_4) {
                    showStudentSearchBooksFragment();
                }
                return true;
            };

    private void showStudentHomeFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.student_fragment_container, StudentHomeFragment.newInstance(student, this))
                .addToBackStack(null)
                .commit();
        getSupportFragmentManager().popBackStackImmediate();
    }

    public void showStudentSearchBooksFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(
                        R.id.student_fragment_container,
                        StudentSearchBooksFragment.newInstance(
                                student.getBundle(),
                                this
                        )
                )
                .addToBackStack(null)
                .commit();
    }

    public void showStudentProfileFragment(View view) {
        int profileFragmentId = 1;
        if (previousItemId == profileFragmentId) {
            return;
        }
        previousItemId = profileFragmentId;

        if(getSupportFragmentManager().getBackStackEntryCount() >= 2) {
            getSupportFragmentManager().popBackStack();
        }
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager()
                .beginTransaction()
                .add(
                        R.id.student_fragment_container,
                        StudentProfileFragment.newInstance(student, this)
                )
                .addToBackStack(null)
                .commit();
    }

    @SuppressLint("SetTextI18n")
    public void setCurrentDate() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfDate = new SimpleDateFormat("dd");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfDay = new SimpleDateFormat("EEEE");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfMonth = new SimpleDateFormat("MMMM");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
        Date currentDate = new Date();
        binding.dateText.setText(sdfDate.format(currentDate));
        binding.dayText.setText(sdfDay.format(currentDate));
        binding.monthText.setText(sdfMonth.format(currentDate) + " " + sdfYear.format(currentDate));

//        calendar = Calendar.getInstance();
//        dateFormat = new SimpleDateFormat("dd/MM/yyy");
//        date = dateFormat.format(calendar.getTime());
//        curDate.setText(date);
//        ClientInfo.getInstance().setDate(date);
    }

    private void setFragmentListeners() {
    }

    @Override
    public void onActionPerformed(Bundle bundle, View itemView) {

        int selectedAction = bundle.getInt(FragmentActionListener.ACTION_KEY);

        if (selectedAction == FragmentActionListener.ACTION_VALUE_SELECTED_BOOK &&
                itemView != null && !bookShown) {

            View heroBookImageView = itemView.findViewById(R.id.listItemBookImageView);
            Book book = new Book(bundle.getBundle(FragmentActionListener.SELECTED_BOOK_KEY));
            StudentViewBookFragment fragment =
                    StudentViewBookFragment.newInstance(book, student, this);

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.student_fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
//                                .addSharedElement(
//                    heroBookImageView,
//                    book.getBookId()
//            )
        } else if (selectedAction == FragmentActionListener.LOGOUT_ACTION_VALUE) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(StudentMainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (previousItemId == R.id.page_2 ||
                previousItemId == R.id.page_3 ||
                previousItemId == R.id.page_4) {
            previousItemId = 0;
        }
        super.onBackPressed();
    }
}




//Realtime listeners
//https://firebase.google.com/docs/firestore/query-data/listen#java_1

//fragments
//https://developer.android.com/guide/fragments/create

//student@test.com
//lirarian@gmail.com

//bottom navigation
//https://material.io/components/bottom-navigation/android#using-bottom-navigation

//Add data
//https://firebase.google.com/docs/firestore/manage-data/add-data#java_9

//Transition
//https://medium.com/@bherbst/fragment-transitions-with-shared-elements-7c7d71d31cbb