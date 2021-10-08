package com.example.notforgotto_do;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LogInActivity extends AppCompatActivity {

    EditText mEmail, mPassword;
    Button mLoginBtn;
    Button mCreateBtn;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



//Here below we are checking if the application is opened for the first time .
        SharedPreferences preferences = getSharedPreferences("PREFERENCES", MODE_PRIVATE);
        String FirstTime = preferences.getString("FirstTimeInstall", " ");

        if(FirstTime.equals("Yes")) // this is if its opened first time
        {
            Intent intent = new Intent(LogInActivity.this, LogInActivity.class); // or , EmptyActivity
            startActivity(intent);
        }
        else // if not otherwise.
        {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("FirstTimeInstall", "Yes");
            editor.apply();
        }



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mEmail = findViewById(R.id.emailLog);
        mPassword = findViewById(R.id.passwordLog);
        fAuth = FirebaseAuth.getInstance();
        mLoginBtn  = findViewById(R.id.buttonLog);
        mCreateBtn = findViewById(R.id.navToRegText);

        //Navigation back to the LogIn Screen
        mCreateBtn.setOnClickListener(v -> startActivity(new Intent(LogInActivity.this, RegistrationActivity.class)));


        //The LogIn Button Functionality
        mLoginBtn.setOnClickListener(v -> {
            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();

            if(TextUtils.isEmpty(email)){
                mEmail.setError("Email is required");
                return;
            }

            if(TextUtils.isEmpty(password))
            {
                //fields have to be filled
                mPassword.setError("Password cannot be empty");
                return;
            }
            //checking the minimum length the password should be, basic check
            if(password.length() < 6)
            {
                mPassword.setError("Password must contain at least 6 characters");

            }

            //trying to fix navigation to the register

            //auth the user now.
            fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

                if(task.isSuccessful())
                {
                    Toast.makeText(LogInActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),EmptyMainActivity.class));
                }

                else{
                    Toast.makeText(LogInActivity.this, "Error!, LogIn Failed " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }

            });

        });
    }
}