package edu.rosehulman.changb.boyeram1.jaundicedetection;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

public class FamilyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Family family = intent.getParcelableExtra(LoginActivity.EXTRA_FAMILY);

        TextView familyNameTextView = findViewById(R.id.family_name_textview);
        familyNameTextView.setText(family.getName());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEditChild(false);
            }
        });
    }

    private void addEditChild(final boolean isEditing) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FamilyActivity.this);

        final View view = getLayoutInflater().inflate(R.layout.dialog_add_child, null, false);
        builder.setView(view);

        final EditText nameEditText = (EditText) view.findViewById(R.id.edit_child_name);
        final EditText dayOfBirthEditText = (EditText) view.findViewById(R.id.edit_child_day_of_birth);

        dayOfBirthEditText.setInputType(InputType.TYPE_NULL);
        dayOfBirthEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(dayOfBirthEditText);
            }
        });
        dayOfBirthEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                showDatePickerDialog(dayOfBirthEditText);
            }
        });

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                Log.d("addEditChild", "Added new child: " + nameEditText.getText());
                Log.d("addEditChild", "Added new child: " + dayOfBirthEditText.getText());
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }

    private void showDatePickerDialog(final EditText dateText) {
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

                dateText.setText(String.format("%d/%d/%d", day, month, year));
            }
        });

        builder.create().show();
    }
}
