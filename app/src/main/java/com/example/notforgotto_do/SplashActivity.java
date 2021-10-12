package com.example.notforgotto_do;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity
{



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        final Intent SplashIntent  = new Intent(SplashActivity.this, LogInActivity.class );
        new Handler().postDelayed(() -> {
            startActivity(SplashIntent);
            finish();
        },3000);
    }
}