package edu.rosehulman.changb.boyeram1.jaundicedetection.fragments;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupMenu;

import edu.rosehulman.changb.boyeram1.jaundicedetection.NavActivity;
import edu.rosehulman.changb.boyeram1.jaundicedetection.NotificationUtils.NotificationPublisher;
import edu.rosehulman.changb.boyeram1.jaundicedetection.R;
import edu.rosehulman.changb.boyeram1.jaundicedetection.adapters.FamilyAdapter;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.Family;
import edu.rosehulman.changb.boyeram1.jaundicedetection.utils.SharedPrefsUtils;

public class FamilyLoginFragment extends Fragment implements Toolbar.OnMenuItemClickListener, FamilyAdapter.FamilyLoginActivityCallback {

    protected static final String EXTRA_FAMILY = "FAMILY_NAME";

    private FamilyAdapter mFamilyAdapter;
    private OnLogoutListener mListener;
    private Context mContext;

    public FamilyLoginFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_familylogin, container, false);
        // Setup Toolbar
        Toolbar mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.app_name);
        getActivity().getMenuInflater().inflate(R.menu.family_login_menu, mToolbar.getMenu());
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        final View fab = rootView.findViewById(R.id.fab_familylogin);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddEditDialog(null);
            }
        });        //Recycler View
        RecyclerView familyList = (RecyclerView) rootView.findViewById(R.id.recycler_view_login);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        familyList.setLayoutManager(manager);
        mFamilyAdapter = new FamilyAdapter(this, familyList);
        familyList.setAdapter(mFamilyAdapter);

        return rootView;
    }

    public void showAddEditDialog(final Family family) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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
        scheduleNotification(getNotification("5 sec delay"), 5000);
        Intent intent = new Intent(mContext, NavActivity.class);
        SharedPrefsUtils.setContext(mContext);
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
        PopupMenu popupMenu = new PopupMenu((Context) mContext, v);
        popupMenu.inflate(R.menu.popup_edit_remove);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_popup_edit:
                        onEdit(family);
                        break;
                    case R.id.menu_popup_remove:
                        AlertDialog.Builder builder = new AlertDialog.Builder((Context) mContext );
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

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.action_logout:
                Log.d("family login frag", "LOGOUT Menu Item Clicked!");
                mListener.onLogout();
                return true;
        }
        return false;
    }


    public interface OnLogoutListener {
        void onLogout();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnLogoutListener) context;
            mContext = context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnLogoutListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void scheduleNotification(Notification notification, int delay) {
        Intent notificationIntent = new Intent(mContext, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;

        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification(String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext.getApplicationContext(), "default");
        builder.setContentTitle("Scheduled Jaundice Retest");
        builder.setContentText(content);
        builder.setSmallIcon(android.R.mipmap.sym_def_app_icon);
        Intent intent = new Intent(mContext.getApplicationContext(), edu.rosehulman.changb.boyeram1.jaundicedetection.LoginActivity.class);
        PendingIntent pi = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);
        Log.d("Notifications", "notification gotten");
        return builder.build();
    }
}