package com.arnav.library.activities.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.arnav.library.LoadingDialog;
import com.arnav.library.R;
import com.arnav.library.activities.LibrarianMain.LibrarianMainActivity;
import com.arnav.library.activities.StudentMain.StudentMainActivity;
import com.arnav.library.models.Librarian;
import com.arnav.library.models.Student;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

//    security rules
//    https://www.softauthor.com/firebase-cloud-firestore-security-rules/
//
//    Animation
//    https://www.youtube.com/watch?v=eHasNubC4zo

//    webView wView = new webView(this);
//    wView.loadUrl("file:///android_asset/piggy.gif");
//    setContentView(view);
//    https://www.taimoorsikander.com/how-to-add-a-gif-image-in-android-studio-example/

//    Custom dialog
//    https://www.youtube.com/watch?v=tccoRIrMyhU
//    https://www.youtube.com/c/StevdzaSan/videos


    private final String TAG = "LoginActivity";

    private FirebaseAuth mAuth;
    EditText emailEditText, passwordEditText;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loadingDialog = new LoadingDialog(LoginActivity.this);
        loadingDialog.initializeLoadingDialog();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {

        if (currentUser != null) {
            loadingDialog.showDialog();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String currentUserId = currentUser.getUid();

            db.collection("librarians")
                    .whereEqualTo("uid", currentUserId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (Objects.requireNonNull(task.getResult()).isEmpty()) {
                                db.collection("students")
                                        .whereEqualTo("uid", currentUserId)
                                        .get()
                                        .addOnCompleteListener(task1 -> {
                                            loadingDialog.hideDialog();
                                            if (task1.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task1.getResult()) {
                                                    Student student = new Student(
                                                            document.getString("name"),
                                                            document.getString("libraryID"),
                                                            document.getString("email"),
                                                            document.getString("phone"),
                                                            document.getString("uid"),
                                                            document.getString("libraryCode")
                                                    );

                                                    Intent intent = new Intent(LoginActivity.this, StudentMainActivity.class);
                                                    intent.putExtra("student", student.getBundle());
                                                    startActivity(intent);
                                                    finish();
                                                    break;
                                                }
                                            } else {
                                                Log.w(TAG, "Error getting documents.", task.getException());
                                            }
                                        });


                            } else {
                                loadingDialog.hideDialog();
                                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                    if (Objects.equals(document.get("uid"), currentUserId)) {

                                        Intent intent = new Intent(LoginActivity.this, LibrarianMainActivity.class);
                                        Librarian librarian = new Librarian(
                                                document.getString("name"),
                                                document.getString("isLibrarian"),
                                                document.getString("email"),
                                                document.getString("phone"),
                                                document.getString("uid"),
                                                document.getString("libraryCode")
                                        );
                                        intent.putExtra("librarian", librarian.getBundle());
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    });


        }

    }

    public void signInUser(View view) {

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
        } else {
            loadingDialog.showDialog();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        loadingDialog.hideDialog();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    });

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loadingDialog.dismissDialog();
    }

}