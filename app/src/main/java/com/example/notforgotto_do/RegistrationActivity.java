package com.example.notforgotto_do;


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

import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity  implements  View.OnClickListener
{

    EditText mFullName, mEmail, mPassword, mPasswordRepeat;
    Button mRegistrationBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //now calling the xml id of all featured usages
        mFullName = findViewById(R.id.fullName);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mPasswordRepeat = findViewById(R.id.repeatPassword);
        mRegistrationBtn = findViewById(R.id.buttonReg);
        mLoginBtn = findViewById(R.id.navToLogText);

        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.navToLogText:
                //Navigation back to the Registration Screen
                startActivity(new Intent(RegistrationActivity.this, LogInActivity.class));
                break;
            case R.id.buttonReg:
                loginUser();
                break;
        }

    }

    private void loginUser()

    {
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String fullname = mFullName.getText().toString().trim();
        String repeatPassword = mPasswordRepeat.getText().toString().trim();

        if(fullname.isEmpty())
        {
            mFullName.setError("Please Enter Your Name");
            mFullName.requestFocus();
            return;
        }

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
        if(!repeatPassword.equals(password))
        {
            mPasswordRepeat.setError("Please Provide Matching Passwords");
            mPasswordRepeat.requestFocus();
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
                                    if(task.isSuccessful())
                                    {

                                        Toast.makeText(RegistrationActivity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),EmptyMainActivity.class));

                                    }

                                    else {
                                        Toast.makeText(RegistrationActivity.this, "Error!,Registration Failed" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    }
                });

    }

}



