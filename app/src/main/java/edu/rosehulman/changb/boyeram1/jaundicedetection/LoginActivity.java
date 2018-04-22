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
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements FamilyAdapter.FamilyCallback {

    protected static final String EXTRA_FAMILY = "FAMILY_NAME";

    private FamilyAdapter mFamilyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_login);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEditFamily(new Family(), false);
            }
        });

        RecyclerView view = (RecyclerView)findViewById(R.id.recycler_view_login);
        view.setLayoutManager(new LinearLayoutManager(this));
        view.setHasFixedSize(true);
        this.mFamilyAdapter = new FamilyAdapter(this, view);
        view.setAdapter(this.mFamilyAdapter);
    }

    public void addEditFamily(final Family family, final boolean isEditing) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        View view = this.getLayoutInflater().inflate(R.layout.dialog_add_family, null, false);
        builder.setView(view);
        final EditText nameEditText = (EditText) view.findViewById(R.id.edit_family_name);
        if (isEditing) {
            nameEditText.setText(family.getName());
        }
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = nameEditText.getText().toString();
                if (name.isEmpty()) {
                    return;
                }
                if (isEditing) {
                    family.setName(name);
                    mFamilyAdapter.notifyDataSetChanged();
                } else {
                    mFamilyAdapter.addFamily(new Family(name));
                }
            }
        });
        builder.setTitle(R.string.add_family_dialog_title);
        builder.setNegativeButton(android.R.string.cancel, null);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }

    @Override
    public void onSelect(Family family) {
        Log.d("Family onSelect", family.getName() + " selected");
        Intent intent = new Intent(this, FamilyActivity.class);
        intent.putExtra(EXTRA_FAMILY, family);
        startActivity(intent);
    }

    @Override
    public void onEdit(Family family) {
        addEditFamily(family, true);
    }
}
