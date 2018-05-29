package edu.rosehulman.changb.boyeram1.jaundicedetection;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupMenu;

import edu.rosehulman.changb.boyeram1.jaundicedetection.NotificationUtils.NotificationPublisher;
import edu.rosehulman.changb.boyeram1.jaundicedetection.adapters.FamilyAdapter;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.Family;
import edu.rosehulman.changb.boyeram1.jaundicedetection.utils.SharedPrefsUtils;

public class LoginActivity extends AppCompatActivity implements FamilyAdapter.LoginActivityCallback {

    protected static final String EXTRA_FAMILY = "FAMILY_NAME";
    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION = 3;


    private FamilyAdapter mFamilyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkPermissions();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_login);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddEditDialog(null);
            }
        });

        RecyclerView view = (RecyclerView)findViewById(R.id.recycler_view_login);
        view.setLayoutManager(new LinearLayoutManager(this));
        view.setHasFixedSize(true);
        this.mFamilyAdapter = new FamilyAdapter(this, view);
        view.setAdapter(this.mFamilyAdapter);
        setTitle(getString(R.string.loginTitle));

        scheduleNotification(getNotification(getString(R.string.notification_content)), 5000);
    }

    public void showAddEditDialog(final Family family) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle(R.string.add_family_dialog_title);

        View view = this.getLayoutInflater().inflate(R.layout.dialog_add_family, null, false);
        builder.setView(view);

        final EditText nameEditText = (EditText) view.findViewById(R.id.edit_family_name);
        if(family != null) {
            nameEditText.setText(family.getName());

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
                    mFamilyAdapter.update(family, name);
                }
            };

            nameEditText.addTextChangedListener(textWatcher);
        }

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(family == null) {
                    String name = nameEditText.getText().toString();
                    mFamilyAdapter.addFamily(new Family(name));
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }

    @Override
    public void onSelect(Family family) {
        Log.d("Family onSelect", family.getName() + " selected");
        Intent intent = new Intent(this, NavActivity.class);
        intent.putExtra(EXTRA_FAMILY, family);
        SharedPrefsUtils.setContext(this);
        SharedPrefsUtils.setCurrentFamilyKey(family.getKey());
        startActivity(intent);
    }

    @Override
    public void onEdit(Family family) {
        Log.d("Family onEdit ", family.getName() + " selected");
        showAddEditDialog(family);
    }

    @Override
    public void showEditRemovePopup(final Family family, View v, final int position) {
        Log.d("Family showEditPopup ", family.getName() + " selected");
        PopupMenu popupMenu = new PopupMenu((Context) LoginActivity.this, v);
        popupMenu.inflate(R.menu.popup_edit_remove);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_popup_edit:
                        LoginActivity.this.onEdit(family);
                        break;
                    case R.id.menu_popup_remove:
                        AlertDialog.Builder builder = new AlertDialog.Builder((Context) LoginActivity.this );
                        builder.setTitle(R.string.login_remove_title);
                        builder.setMessage(R.string.login_remove_message);
                        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mFamilyAdapter.remove(position);
                            }
                        });
                        builder.setNegativeButton(android.R.string.cancel, null);
                        builder.create().show();
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void scheduleNotification(Notification notification, int delay) {
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification(String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "default");
        builder.setContentTitle(getString(R.string.app_name));
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.test_tube);
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);
        Log.d("Notifications", "notification gotten");
        return builder.build();
    }

    private void checkPermissions() {
        // Check to see if we already have permissions
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            // If we do not, request them from the user
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                } else {
                    // permission denied
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
