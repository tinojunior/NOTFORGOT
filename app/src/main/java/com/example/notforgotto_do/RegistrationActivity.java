package com.example.notforgotto_do;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity  {

    EditText mFullName,mEmail,mPassword,mPasswordRepeat;
    Button mRegistrationBtn;
    Button mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Here below we are checking if the application is opened for the first time .
        SharedPreferences preferences = getSharedPreferences("PREFERENCES", MODE_PRIVATE);
        String FirstTime = preferences.getString("FirstTimeInstall", "");

        if (FirstTime.equals("Yes")) // this is if its opened first time stay on the reg
        {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("FirstTimeInstall", "Yes");
            editor.apply();


        } else // if not otherwise. it means user already logged in
        {
            Intent intent = new Intent(RegistrationActivity.this, EmptyMainActivity.class);
            startActivity(intent);
        }

        //now calling the xml id of all featured usages
        mFullName = findViewById(R.id.fullName);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mPasswordRepeat = findViewById(R.id.repeatPassword);
        mRegistrationBtn = findViewById(R.id.buttonReg);
        mLoginBtn = findViewById(R.id.navToLogText);

        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);


        //Opening the LogIn Screen if this button is clicked navigated at the last part of code
        mLoginBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), LogInActivity.class)));


        //Already registered user already go straight to home activity
        if (fAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(getApplicationContext(), EmptyMainActivity.class));
            finish();
        }


        mRegistrationBtn.setOnClickListener(v -> {

            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();
            String passwordRepeat = mPasswordRepeat.getText().toString().trim();

            if (TextUtils.isEmpty(email))
            {
                mEmail.setError("Email is required");
                return;
            }

            if (TextUtils.isEmpty(password))
            {

                mPassword.setError("Password cannot be empty");
                return;
            }
            if (password.length() < 6)
            {
                mPassword.setError("Password must contain at least 6 characters");

            }
            if (passwordRepeat.equals(password))
            {
                mPasswordRepeat.setError("The password does not match!");
            }



            progressBar.setVisibility(View.VISIBLE);

            //Register the user now in firebase.

            fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful())
                {
                    Toast.makeText(RegistrationActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), EmptyMainActivity.class));

                }
                else
                {
                    Toast.makeText(RegistrationActivity.this, "Error!,Registration Failed" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }

            });


        });


    }
    //Navigation to open LogIn Activity
    public  void openLogInActivity()
    {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

}



