package com.example.notforgotto_do;

import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EmptyMainActivity extends AppCompatActivity
{

    public FloatingActionButton button;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emptymain);

        button =  findViewById(R.id.fab);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(EmptyMainActivity.this, AddNewTaskActivity.class);
            startActivity(intent);
        });

    }

}