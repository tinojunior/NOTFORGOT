package com.example.notforgotto_do.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notforgotto_do.AddNewTaskActivity;
import com.example.notforgotto_do.ExampleItem;
import com.example.notforgotto_do.MainActivity;
import com.example.notforgotto_do.Model.ToDoModel;
import com.example.notforgotto_do.R;
import com.example.notforgotto_do.Utils.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

public  class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder>
{
    private ArrayList<ExampleItem> mExampleList;

    public  static  class ViewHolder extends  RecyclerView.ViewHolder
    {
        public CheckBox task;
        public TextView textView;
        public TextView textView2;
        public DatabaseHandler db;

        public ViewHolder(View view)
        {
            super(view);
            task = view.findViewById(R.id.todocheckbox);
            textView = view.findViewById(R.id.text1);
            textView2 = view.findViewById(R.id.text2);
        }
    }



    public ToDoAdapter(ArrayList<ExampleItem> exampleList)
    {
        mExampleList = exampleList;
    }

    private List<ToDoModel> todoList;
    private MainActivity activity;
    private DatabaseHandler db;

    public ToDoAdapter(DatabaseHandler db, MainActivity activity)
    {
        super();
        this.db = db;
        this.activity = activity;

    }


    @NonNull
    @Override
    public  ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout,parent,false);
        return new ViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        ExampleItem currentItem = mExampleList.get(position);
        holder.textView.setText(currentItem.getmText1());
        holder.textView2.setText(currentItem.getmText2());


        db.openDatabase();
        final ToDoModel item = todoList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus())); //required boolean type.
        holder.task.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked)
            {
                db.updateStatus(item.getId(),1);
            }
            else
            {
                db.updateStatus(item.getId(), 0);
            }
        });

    }

    public int getItemCount(){
        return todoList.size();
    }

    private boolean toBoolean(int n ){

        return  n!=0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTasks(List<ToDoModel> todoList){
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    public Context getContext()
    {
        return activity;
    }

    //this is the function which deletes a task from already existing
    public void deleteItem(int position)
    {
        ToDoModel item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }


    //edit item whenever you try to edit a task
    public void editItem(int position)
    {
        ToDoModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task",item.getTask());
        AddNewTaskActivity fragment = new AddNewTaskActivity();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTaskActivity.TAG);
    }


}
