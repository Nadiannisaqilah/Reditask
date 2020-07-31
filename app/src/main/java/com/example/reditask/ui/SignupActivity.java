package com.example.reditask.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.reditask.R;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    private static String TAG = SignupActivity.class.getSimpleName();
    private EditText username, password;
    private Button signUp;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        signUp = findViewById(R.id.bt_signup);
        progressBar = findViewById(R.id.progress);

        listener();
    }

    private void listener() {
        signUp.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            String email = username.getText().toString();
            String pass = password.getText().toString();
            if (pass.length() < 8) {
                password.setError("password harus 8 karakter atau lebih");
            } else {
                mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                Log.d(TAG, "createUserWithEmail:success");
                                Toast.makeText(SignupActivity.this, "Daftar Berhasil",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            } else {
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignupActivity.this, "Gagal Mendaftar",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
