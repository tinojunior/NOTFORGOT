package com.example.notforgotto_do;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class LogInActivity extends AppCompatActivity implements  View.OnClickListener
{

     EditText mEmail;
     EditText mPassword;
     Button mLoginBtn;
     TextView mCreateBtn;
     ProgressBar progressBar;
     FirebaseAuth fAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mLoginBtn = findViewById(R.id.buttonLog);
        mCreateBtn = findViewById(R.id.navToRegText);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v)
    {

        switch (v.getId())
        {

            case R.id.navToRegText:
                //Navigation back to the Registration Screen
                startActivity(new Intent(LogInActivity.this, RegistrationActivity.class));
                break;
            case R.id.buttonLog:
                loginUser();
                break;

        }
    }

    private void loginUser()

    {

        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        //check if email is filled

        if(email.isEmpty())
        {
            mEmail.setError("Please Enter Your Email");
            mEmail.requestFocus();
            return;
        }

        //Check if password is filled

        if(password.isEmpty())
        {
            mPassword.setError("Please Enter Your Password");
            mEmail.requestFocus();
            return;
        }
        //checking the minimum length the password should be, basic check
        if(password.length()  < 6)
        {
            mPassword.setError("Your Password Is Shorter than 6 characters");
            mEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            mEmail.setError("Please provide valid email");
            mEmail.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        fAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())

                        {
                                User user = new User(email,password);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {

                                        Toast.makeText(LogInActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),MainActivity.class));

                                    }
                                }
                            });

                        }
                    }
                });

    }

}