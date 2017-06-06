package com.epicodus.myrestaurants.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.epicodus.myrestaurants.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = CreateAccountActivity.class.getSimpleName();
    @Bind(R.id.createAccountButt)
    Button createButt;
    @Bind(R.id.createAccountEmail)
    EditText createEmail;
    @Bind(R.id.createAccountName)
    EditText createName;
    @Bind(R.id.createAccountPassword)
    EditText createPass;
    @Bind(R.id.createAccountPasswordConf)
    EditText createPassConf;
    @Bind(R.id.createAccountLoginAlt)
    TextView createLoginAlt;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        ButterKnife.bind(this);

        createLoginAlt.setOnClickListener(this);
        createButt.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
        createAuthStateListener();
    }

    private void createAuthStateListener() {
        authListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(CreateAccountActivity.this, user.getEmail(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    @Override
    public void onClick(View v) {

        if (v == createLoginAlt) {
            Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        if (v == createButt) {
            createNewUser();
        }
    }

    private void createNewUser() {
        final String name = createName.getText().toString().trim();
        final String email = createEmail.getText().toString().trim();
        final String password = createPass.getText().toString().trim();
        final String confirmPassword = createPassConf.getText().toString().trim();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Success!!");
                        } else {
                            Toast.makeText(CreateAccountActivity.this, "FAILURE", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
