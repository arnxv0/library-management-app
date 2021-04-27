package com.arnav.library.activities.LibrarianMain;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.arnav.library.R;
import com.arnav.library.activities.LibrarianMain.fragments.FragmentActionListener;
import com.arnav.library.activities.LibrarianMain.fragments.LibrarianAddBookFragment;
import com.arnav.library.activities.LibrarianMain.fragments.LibrarianAddRecordFragment;
import com.arnav.library.activities.LibrarianMain.fragments.LibrarianAddStudentFragment;
import com.arnav.library.activities.LibrarianMain.fragments.LibrarianHomeFragment;
import com.arnav.library.activities.LibrarianMain.fragments.LibrarianProfileFragment;
import com.arnav.library.activities.LibrarianMain.fragments.LibrarianScanCodeFragment;
import com.arnav.library.activities.Login.LoginActivity;
import com.arnav.library.databinding.ActivityLibrarianMainBinding;
import com.arnav.library.models.Librarian;
import com.arnav.library.models.Record;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LibrarianMainActivity extends AppCompatActivity implements FragmentActionListener {

    Librarian librarian;
    ActivityLibrarianMainBinding binding;
    int previousItemId;
    private static final int PERMISSION_REQUEST_CAMERA = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLibrarianMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.librarianBottomNavigation
                .setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        librarian = new Librarian(getIntent().getBundleExtra("librarian"));

        getSupportFragmentManager()
                .beginTransaction()
                .add(
                        R.id.librarian_fragment_container,
                        LibrarianHomeFragment.newInstance()
                )
                .commit();

        setCurrentDate();

    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            item -> {

                int itemId = item.getItemId();
                if (previousItemId == itemId) {
                    return true;
                }
                previousItemId = itemId;

                Log.i("stn", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));

                getSupportFragmentManager().popBackStack();
                if (itemId == R.id.page_1) {
                    showHomeFragment();
                } else if (itemId == R.id.page_2) {
                    showAddBookFragment();
                } else if (itemId == R.id.page_3) {
                    showAddStudentFragment();
                } else if (itemId == R.id.page_4) {
                    showScanCodeFragment();
                }
                return true;

            };

    private void showHomeFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.librarian_fragment_container, LibrarianHomeFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    private void showAddBookFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(
                        R.id.librarian_fragment_container,
                        LibrarianAddBookFragment.newInstance(librarian)
                )
                .addToBackStack(null)
                .commit();
    }

    private void showAddStudentFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(
                        R.id.librarian_fragment_container,
                        LibrarianAddStudentFragment.newInstance(librarian)
                )
                .addToBackStack(null)
                .commit();
    }

    private void showScanCodeFragment() {
        requestCamera();
    }

    public void showProfileFragment(View view) {
        int profileFragmentId = 1;
        if(previousItemId == profileFragmentId) {
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
                        R.id.librarian_fragment_container,
                        LibrarianProfileFragment.newInstance(librarian, this)
                )
                .addToBackStack(null)
                .commit();
    }

    public void showAddRecordFragment(Record record, String action){
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager()
                .beginTransaction()
                .add(
                        R.id.librarian_fragment_container,
                        LibrarianAddRecordFragment.newInstance(record, this, action)
                )
                .addToBackStack(null)
                .commit();
    }

    private void startCamera(){
        getSupportFragmentManager()
                .beginTransaction()
                .add(
                        R.id.librarian_fragment_container,
                        LibrarianScanCodeFragment.newInstance(librarian, this)
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
    }

    private void requestCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (previousItemId == R.id.page_2 ||
            previousItemId == R.id.page_3 ||
            previousItemId == R.id.page_4 ) {
            previousItemId = 0;
        }
        super.onBackPressed();
    }

    @Override
    public void onActionPerformed(Bundle bundle) {
        int selectedAction = bundle.getInt(FragmentActionListener.ACTION_KEY);

        if (selectedAction == FragmentActionListener.SCAN_CODE_ACTION_VALUE) {

            String[] info = bundle.getString(FragmentActionListener.SCAN_CODE_KEY).split("\\s+");

            Record record = new Record(
                    info[2],
                    info[1],
                    "",
                    librarian.getLibraryCode(),
                    librarian.getUid(),
                    "",
                    "false"
            );
            showAddRecordFragment(record, info[0]);
            //Toast.makeText(this, info, Toast.LENGTH_LONG).show();

        } else if (selectedAction == FragmentActionListener.LOGOUT_ACTION_VALUE) {

            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(LibrarianMainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        } else if (selectedAction == FragmentActionListener.ADD_RECORD_ACTION_VALUE) {
            previousItemId = R.id.page_4;
            getSupportFragmentManager().popBackStack();
            showScanCodeFragment();
        }

    }
}
