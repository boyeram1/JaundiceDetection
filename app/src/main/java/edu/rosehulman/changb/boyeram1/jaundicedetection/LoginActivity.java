package edu.rosehulman.changb.boyeram1.jaundicedetection;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseUser;

import edu.rosehulman.changb.boyeram1.jaundicedetection.NotificationUtils.NotificationPublisher;
import edu.rosehulman.changb.boyeram1.jaundicedetection.fragments.FamilyLoginFragment;
import edu.rosehulman.changb.boyeram1.jaundicedetection.fragments.LoginFragment;

public class LoginActivity extends AppCompatActivity implements LoginFragment.OnLoginListener, FamilyLoginFragment.OnLogoutListener {

    private FirebaseAuth mAuth;
    private AuthStateListener mAuthStateListener;
    private OnCompleteListener<AuthResult> mOnCompleteListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        initializeListeners();
        scheduleNotification(getNotification("5 sec delay"), 5000);
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
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    public void onLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(mOnCompleteListener);
    }

    @Override
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
}
