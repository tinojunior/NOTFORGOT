package com.example.notforgotto_do;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.notforgotto_do.Model.ToDoModel;
import com.example.notforgotto_do.Utils.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

public class
AddNewTaskActivity extends BottomSheetDialogFragment implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    public static final String TAG = "ActionBottomDialog";
    private EditText newTaskText;
    private Button newTaskSaveButton;
    private DatabaseHandler db;
    public ImageView calendarView;
    public  TextView calendarTxt;
    public Spinner categorySpnr;
    public  Spinner prioritySpinner;


    //functions
    public static AddNewTaskActivity newInstance()
    {
        return new AddNewTaskActivity();

    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_new_task, container, false);
        Objects.requireNonNull(getDialog()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        newTaskText = Objects.requireNonNull(getView()).findViewById(R.id.new_task_text);
        newTaskSaveButton = getView().findViewById(R.id.new_task_btn);



        //Calendar icon
        calendarView = getView().findViewById(R.id.smallcalendaricon);
        calendarView.setOnClickListener(v -> {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(Objects.requireNonNull(getFragmentManager()), "date picker");

        });
        //Category spinner
        categorySpnr = getView().findViewById(R.id.catspinner);
        ArrayAdapter<CharSequence> adapter  = ArrayAdapter.createFromResource( this.getContext() ,R.array.categories , android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpnr.setAdapter(adapter);
        categorySpnr.setOnItemSelectedListener(this);

        //Priority spinner
        prioritySpinner = getView().findViewById(R.id.priorityspinner);
        ArrayAdapter<CharSequence> adapter1  = ArrayAdapter.createFromResource( this.getContext() , R.array.priorities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter1);
        prioritySpinner.setOnItemSelectedListener(this);


        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate = true;
            String task = bundle.getString("task");
            newTaskText.setText(task);
            if(task.length()>0)
                newTaskSaveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()),R.color.colorAccent));
        }
        newTaskText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                //not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(s.toString().equals(""))
                {
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.GRAY);
                }
                else
                {
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor((ContextCompat.getColor(Objects.requireNonNull(getContext()),R.color.colorAccent)));
                }

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                //not needed

            }
        });
        final boolean finalIsUpdate = isUpdate;
        newTaskSaveButton.setOnClickListener(v -> {
            String text = newTaskText.getText().toString();
            if ( finalIsUpdate)
            {
                db.updateTask(bundle.getInt("id"), text);
            } else
            {
                ToDoModel task = new ToDoModel();
                task.setTask(text);
                task.setStatus(0);
            }
            dismiss();
        });

    }
    @Override
    public void onDismiss(@NonNull DialogInterface dialog)
    {
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener)
        {
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }
    }


    //This part of the code has the calendar functionality
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);

        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        calendarTxt = Objects.requireNonNull(getView()).findViewById(R.id.dateselected);
        calendarTxt.setText(currentDateString);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
        //Empty adapter as it has nothing to do .

    }
}
