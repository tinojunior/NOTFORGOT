package com.example.notforgotto_do;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notforgotto_do.Adapter.ToDoAdapter;
import com.example.notforgotto_do.Model.ToDoModel;
import com.example.notforgotto_do.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener{

    private ToDoAdapter tasksAdapter;
    private  RecyclerView.Adapter mAdapter;
    private  RecyclerView.LayoutManager mLayoutManager;
    private List<ToDoModel> tasksList;
    private DatabaseHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        db = new DatabaseHandler(this);
        db.openDatabase();
        tasksList = new ArrayList<>();


        RecyclerView tasksRecyclerview = findViewById(R.id.workrecycler);
        RecyclerView tasksRecyclerview2 = findViewById(R.id.studyrecyler);

        tasksRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        tasksRecyclerview2.setLayoutManager(new LinearLayoutManager(this));

        tasksRecyclerview.setHasFixedSize(true);
        tasksRecyclerview2.setHasFixedSize(true);

        tasksAdapter = new ToDoAdapter(db, this);

        tasksRecyclerview.setAdapter(tasksAdapter);
        tasksRecyclerview2.setAdapter(tasksAdapter);

        //applying the recyclerview
        ArrayList<ExampleItem> exampleItems = new ArrayList<>();
        exampleItems.add(new ExampleItem(R.layout.task_layout_blue, "Line1", "Line2"));
        exampleItems.add(new ExampleItem(R.layout.task_layout_green, "Line3", "Line4"));
        exampleItems.add(new ExampleItem(R.layout.task_layout_red, "Line5", "Line6"));
        exampleItems.add(new ExampleItem(R.layout.task_layout_yellow, "Line7", "Line8"));
        exampleItems.add(new ExampleItem(R.layout.task_layout_blue, "Line9", "Line10"));
        exampleItems.add(new ExampleItem(R.layout.task_layout_green, "Line11", "Line12"));
        exampleItems.add(new ExampleItem(R.layout.task_layout_red, "Line13", "Line14"));
        exampleItems.add(new ExampleItem(R.layout.task_layout_yellow, "Line15", "Line16"));

        tasksAdapter = new ToDoAdapter(exampleItems);
        tasksRecyclerview.setAdapter(tasksAdapter);
        tasksRecyclerview2.setAdapter(tasksAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerTouchItemHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerview);
        itemTouchHelper.attachToRecyclerView(tasksRecyclerview2);

        ToDoModel task = new ToDoModel();
        task.setTask("This is a test Task");
        task.setStatus(0);
        task.setId(1);
        tasksList.add(task);
        tasksList.add(task);
        tasksList.add(task);
        tasksList.add(task);

        tasksAdapter.setTasks(tasksList);

        tasksList = db.getAllTasks();
        Collections.reverse(tasksList);
        tasksAdapter.setTasks(tasksList);

        fab.setOnClickListener(v -> AddNewTaskActivity.newInstance().show(getSupportFragmentManager(), AddNewTaskActivity.TAG));


    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void handleDialogClose(DialogInterface dialog){
        tasksList = db.getAllTasks();
        Collections.reverse(tasksList);
        tasksAdapter.setTasks(tasksList);
        tasksAdapter.notifyDataSetChanged();
    }
}