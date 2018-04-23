package edu.rosehulman.changb.boyeram1.jaundicedetection.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import edu.rosehulman.changb.boyeram1.jaundicedetection.FamilyActivity;
import edu.rosehulman.changb.boyeram1.jaundicedetection.R;
import edu.rosehulman.changb.boyeram1.jaundicedetection.adapters.ChildAdapter;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.BirthDateTime;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.Child;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.TestResult;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link edu.rosehulman.changb.boyeram1.jaundicedetection.adapters.ChildAdapter.NavActivityCallback} interface
 * to handle interaction events.
 */
public class ChildListFragment extends Fragment {

    private ChildAdapter.NavActivityCallback mNavActivityCallback;
    private ChildAdapter mAdapter;

    public ChildListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RecyclerView recyclerView = (RecyclerView)inflater.inflate(R.layout.fragment_recycler_list, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        mAdapter = new ChildAdapter(this.mNavActivityCallback, recyclerView);
        recyclerView.setAdapter(mAdapter);
        return recyclerView;
    }

    public void showAddEditDialog(final Child child) {
        AlertDialog.Builder builder = new AlertDialog.Builder((Context)mNavActivityCallback);
        builder.setTitle(R.string.add_child_dialog_title);

        final View view = getLayoutInflater().inflate(R.layout.dialog_add_child, null, false);
        builder.setView(view);

        final EditText nameEditText = (EditText) view.findViewById(R.id.edit_child_name);
        final EditText dayOfBirthEditText = (EditText) view.findViewById(R.id.edit_child_day_of_birth);
        final EditText timeOfBirthEditText = (EditText) view.findViewById(R.id.edit_child_time_of_birth);
        final BirthDateTime birthDateTime = new BirthDateTime();

        if(child != null) {
            nameEditText.setText(child.getName());
            dayOfBirthEditText.setText(child.getBirthDateTime().dateToString());
            timeOfBirthEditText.setText(child.getBirthDateTime().timeToString());

            birthDateTime.setDay(child.getBirthDateTime().getDay());
            birthDateTime.setMonth(child.getBirthDateTime().getMonth());
            birthDateTime.setYear(child.getBirthDateTime().getYear());
            birthDateTime.setHour(child.getBirthDateTime().getHour());
            birthDateTime.setMinute(child.getBirthDateTime().getMinute());

            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    // empty
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    // empty
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String name = nameEditText.getText().toString();
                    String dayOfBirth = dayOfBirthEditText.getText().toString();
                    String timeOfBirth = timeOfBirthEditText.getText().toString();
                    BirthDateTime birthDateTime = new BirthDateTime(dayOfBirth, timeOfBirth);
                    mAdapter.updateChild(child, name, birthDateTime);
                }
            };

            nameEditText.addTextChangedListener(textWatcher);
            dayOfBirthEditText.addTextChangedListener(textWatcher);
            timeOfBirthEditText.addTextChangedListener(textWatcher);
        }

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

                if(child == null) {
                    Child newChild = new Child(name, birthDateTime);
                    mAdapter.addChild(newChild);
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }

    private void showDatePickerDialog(final BirthDateTime birthDateTime, final EditText dateEditText) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder((Context)mNavActivityCallback);

        View dialog_view = getLayoutInflater().inflate(R.layout.dialog_select_date, null, false);
        builder.setView(dialog_view);

        final DatePicker datePicker = (DatePicker) dialog_view.findViewById(R.id.child_day_of_birth_picker);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
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
        AlertDialog.Builder builder = new AlertDialog.Builder((Context)mNavActivityCallback);

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

    public void setNavActivityCallback (ChildAdapter.NavActivityCallback navActivityCallback) {
        Log.d("ChildListFrag", "ChildAdapter.NavActivityCallback Set");
        this.mNavActivityCallback = navActivityCallback;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ChildAdapter.NavActivityCallback) {
            mNavActivityCallback = (ChildAdapter.NavActivityCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ChildAdapter.NavActivityCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mNavActivityCallback = null;
    }

}
