package edu.rosehulman.changb.boyeram1.jaundicedetection;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class FamilyActivity extends AppCompatActivity {

    private ChildAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Family family = intent.getParcelableExtra(LoginActivity.EXTRA_FAMILY);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEditChild(false);
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_family);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        mAdapter = new ChildAdapter(this, recyclerView);
        recyclerView.setAdapter(mAdapter);
    }

    private void addEditChild(final boolean isEditing) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FamilyActivity.this);

        final View view = getLayoutInflater().inflate(R.layout.dialog_add_child, null, false);
        builder.setView(view);

        final EditText nameEditText = (EditText) view.findViewById(R.id.edit_child_name);
        final EditText dayOfBirthEditText = (EditText) view.findViewById(R.id.edit_child_day_of_birth);
        final EditText timeOfBirthEditText = (EditText) view.findViewById(R.id.edit_child_time_of_birth);
        final BirthDateTime birthDateTime = new BirthDateTime();

        dayOfBirthEditText.setInputType(InputType.TYPE_NULL);
        dayOfBirthEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(birthDateTime, dayOfBirthEditText);
            }
        });

        timeOfBirthEditText.setInputType(InputType.TYPE_NULL);
        timeOfBirthEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(birthDateTime, timeOfBirthEditText);
            }
        });

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String name = nameEditText.getText().toString();
                Child newChild = new Child(name, birthDateTime);
                mAdapter.addChild(newChild);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }

    private void showDatePickerDialog(final BirthDateTime birthDateTime, final EditText dateEditText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FamilyActivity.this);

        View dialog_view = getLayoutInflater().inflate(R.layout.dialog_select_date, null, false);
        builder.setView(dialog_view);

        final DatePicker datePicker = (DatePicker) dialog_view.findViewById(R.id.child_day_of_birth_picker);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();

                birthDateTime.setDay(day);
                birthDateTime.setMonth(month);
                birthDateTime.setYear(year);

                dateEditText.setText(birthDateTime.dateToString());
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }

    private void showTimePickerDialog(final BirthDateTime birthDateTime, final EditText timeEditText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FamilyActivity.this);

        View dialog_view = getLayoutInflater().inflate(R.layout.dialog_select_time, null, false);
        builder.setView(dialog_view);

        final TimePicker timePicker = (TimePicker) dialog_view.findViewById(R.id.child_time_of_birth_picker);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();

                birthDateTime.setHour(hour);
                birthDateTime.setMinute(minute);

                timeEditText.setText(birthDateTime.timeToString());
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }
}
