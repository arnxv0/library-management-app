package com.arnav.library.activities.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.arnav.library.activities.LibrarianMain.LibrarianMainActivity;
import com.arnav.library.activities.Login.LoginActivity;
import com.arnav.library.activities.StudentMain.StudentMainActivity;
import com.arnav.library.databinding.ActivitySplashScreenBinding;
import com.arnav.library.models.Librarian;
import com.arnav.library.models.Student;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Objects;

public class SplashScreenActivity extends AppCompatActivity {

    ActivitySplashScreenBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser == null) {
            new CountDownTimer(2000, 1000) {
                @Override
                public void onTick(long l) {
                }

                public void onFinish() {
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }.start();

        } else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String currentUserId = currentUser.getUid();

            db.collection("librarians")
                    .whereEqualTo("uid", currentUserId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (!Objects.requireNonNull(task.getResult()).isEmpty()) {
                                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                    Librarian librarian = null;
                                    if (Objects.equals(document.get("uid"), currentUserId)) {
                                        librarian = new Librarian(document);
                                    }
                                    Intent intent = new Intent(SplashScreenActivity.this, LibrarianMainActivity.class);
                                    assert librarian != null;
                                    intent.putExtra("librarian", librarian.getBundle());
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            } else {
                                Log.w("Splash Screen", "Error getting documents.", task.getException());
                            }
                        }
                    });

            db.collection("students")
                    .whereEqualTo("uid", currentUserId)
                    .get()
                    .addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Student student = null;
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task1.getResult())) {
                                student = new Student(document);
                            }

                            Intent intent = new Intent(SplashScreenActivity.this, StudentMainActivity.class);
                            assert student != null;
                            intent.putExtra("student", student.getBundle());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Log.w("Splash Screen", "Error getting documents.", task1.getException());
                        }
                    });

        }
    }
}