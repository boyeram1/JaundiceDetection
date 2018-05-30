package edu.rosehulman.changb.boyeram1.jaundicedetection;

<<<<<<< HEAD
=======
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
>>>>>>> 5ead7cba6f04e51bb64fb19dd2d5568a28824121
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
<<<<<<< HEAD
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
=======
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
>>>>>>> 5ead7cba6f04e51bb64fb19dd2d5568a28824121
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseUser;

<<<<<<< HEAD
import edu.rosehulman.changb.boyeram1.jaundicedetection.adapters.FamilyAdapter;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.Family;
import edu.rosehulman.changb.boyeram1.jaundicedetection.utils.SharedPrefsUtils;
=======
import edu.rosehulman.changb.boyeram1.jaundicedetection.NotificationUtils.NotificationPublisher;
import edu.rosehulman.changb.boyeram1.jaundicedetection.fragments.FamilyLoginFragment;
import edu.rosehulman.changb.boyeram1.jaundicedetection.fragments.LoginFragment;

public class LoginActivity extends AppCompatActivity implements LoginFragment.OnLoginListener, FamilyLoginFragment.OnLogoutListener {
>>>>>>> 5ead7cba6f04e51bb64fb19dd2d5568a28824121

    private FirebaseAuth mAuth;
    private AuthStateListener mAuthStateListener;
    private OnCompleteListener<AuthResult> mOnCompleteListener;

<<<<<<< HEAD
    protected static final String EXTRA_FAMILY = "FAMILY_NAME";
    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION = 3;

=======
>>>>>>> 5ead7cba6f04e51bb64fb19dd2d5568a28824121


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
<<<<<<< HEAD
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
=======
        mAuth = FirebaseAuth.getInstance();
        initializeListeners();
        scheduleNotification(getNotification("5 sec delay"), 5000);
>>>>>>> 5ead7cba6f04e51bb64fb19dd2d5568a28824121
    }

    private void initializeListeners() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Log.d(Constants.TAG, "User: " + user);
                if (user != null) {
                    switchToFamilyLoginFragment("users/" + user.getUid());
                } else {
                    switchToLoginFragment();
                }
            }
        };
        mOnCompleteListener = new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    showLoginError("Authentication failed");
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
<<<<<<< HEAD
    public void onSelect(Family family) {
        Log.d("Family onSelect", family.getName() + " selected");
        Intent intent = new Intent(this, NavActivity.class);
        intent.putExtra(EXTRA_FAMILY, family);
        SharedPrefsUtils.setContext(this);
        SharedPrefsUtils.setCurrentFamilyKey(family.getKey());
        startActivity(intent);
=======
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
>>>>>>> 5ead7cba6f04e51bb64fb19dd2d5568a28824121
    }

    @Override
    public void onLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(mOnCompleteListener);
    }

    @Override
<<<<<<< HEAD
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
                        builder.setTitle(R.string.popup_remove_title);
                        builder.setMessage(R.string.popup_remove_message);
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

=======
    public void onLogout() {
        mAuth.signOut();
    }

    // MARK: Provided Helper Methods
    private void switchToLoginFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.login_fragment_swap, new LoginFragment(), "Login");
        ft.commit();
    }

    private void switchToFamilyLoginFragment(String path) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment familyLoginFragment = new FamilyLoginFragment();
        ft.replace(R.id.login_fragment_swap, familyLoginFragment, "families");
        ft.commit();
    }

    private void showLoginError(String message) {
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag("Login");
        loginFragment.onLoginError(message);
    }

    private void scheduleNotification(Notification notification, int delay) {
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification(String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "default");
        builder.setContentTitle("Scheduled Jaundice Retest");
        builder.setContentText(content);
        builder.setSmallIcon(android.R.mipmap.sym_def_app_icon);
        Intent intent = new Intent(getApplicationContext(), edu.rosehulman.changb.boyeram1.jaundicedetection.LoginActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);
        Log.d("Notifications", "notification gotten");
        return builder.build();
    }
>>>>>>> 5ead7cba6f04e51bb64fb19dd2d5568a28824121
}
