package edu.rosehulman.changb.boyeram1.jaundicedetection.fragments;

<<<<<<< HEAD
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
=======
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
>>>>>>> 5ead7cba6f04e51bb64fb19dd2d5568a28824121
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
<<<<<<< HEAD
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
=======
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
>>>>>>> 5ead7cba6f04e51bb64fb19dd2d5568a28824121
import java.util.Locale;

import edu.rosehulman.changb.boyeram1.jaundicedetection.Constants;
import edu.rosehulman.changb.boyeram1.jaundicedetection.NavActivity;
<<<<<<< HEAD
import edu.rosehulman.changb.boyeram1.jaundicedetection.NotificationUtils.NotificationPublisher;
=======
>>>>>>> 5ead7cba6f04e51bb64fb19dd2d5568a28824121
import edu.rosehulman.changb.boyeram1.jaundicedetection.R;
import edu.rosehulman.changb.boyeram1.jaundicedetection.adapters.ChildAdapter;
import edu.rosehulman.changb.boyeram1.jaundicedetection.adapters.TestResultAdapter;
<<<<<<< HEAD
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.Child;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.Family;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.Photo;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.TestResult;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.TestResultTime;
import edu.rosehulman.changb.boyeram1.jaundicedetection.svm.SVMTasks;
import edu.rosehulman.changb.boyeram1.jaundicedetection.utils.SharedPrefsUtils;
import edu.rosehulman.changb.boyeram1.jaundicedetection.utils.Utils;
=======
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.Photo;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.TestResult;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.TestResultTime;
import edu.rosehulman.changb.boyeram1.jaundicedetection.utils.SharedPrefsUtils;

import static android.app.Activity.RESULT_OK;
>>>>>>> 5ead7cba6f04e51bb64fb19dd2d5568a28824121

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TestResultAdapter.NavActivityCallback} interface
 * to handle interaction events.
 */
<<<<<<< HEAD
public class TestResultListFragment extends Fragment
        implements INavDrawerFragment, View.OnClickListener,
        Utils.FragmentNeedingChildList, SVMTasks.SVMConsumer, TestResultAdapter.NavActivityCallback {

    public static final String TAG = "JD-TestResultListFrag";
    protected static final String EXTRA_FAMILY = "FAMILY_NAME";

    private TestResultAdapter.NavActivityCallback mCallback;
    private NavActivity mNavActivityCallback;
    private RecyclerView mRecyclerView;
    private TestResultAdapter mAdapter;
    private Family mFamily;

    private View mAddNewTestBuilderView;
    private AlertDialog mAddNewTestDialog;

    private View mSelectPhotoTypeBuilderView;
    private AlertDialog.Builder mSelectPhotoTypeBuilder;
    private AlertDialog mSelectPhotoTypeDialog;

    private List<Child> mCurrentChildList;

    private boolean isUploading = false;
    private boolean isCapturing = false;

    private static final int RC_TAKE_PICTURE = 1;
    private static final int RC_CHOOSE_PICTURE = 2;

    // SVM Tasks
    private SVMTasks svmTasks;

=======
public class TestResultListFragment extends Fragment implements INavDrawerFragment, View.OnClickListener {

    private TestResultAdapter.Callback mCallback;
    private NavActivity mNavActivityCallback;
    private TestResultAdapter mAdapter;

    private View mAddNewTestBuilderView;
    private AlertDialog mAddNewTestDialog;

    private View mSelectPhotoTypeBuilderView;
    private AlertDialog.Builder mSelectPhotoTypeBuilder;
    private AlertDialog mSelectPhotoTypeDialog;

    private boolean isUploading = false;
    private boolean isCapturing = false;

    private static final int RC_TAKE_PICTURE = 1;
    private static final int RC_CHOOSE_PICTURE = 2;
>>>>>>> 5ead7cba6f04e51bb64fb19dd2d5568a28824121

    public TestResultListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.setHasOptionsMenu(true);

        // Inflate the layout for this fragment
<<<<<<< HEAD
        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_recycler_list, container, false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new TestResultAdapter(mNavActivityCallback, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        svmTasks = new SVMTasks(getContext(), this);
        Utils.getChildNameByKey(this);
        Utils.getCurrentChildren(this);

        return mRecyclerView;
    }

    private void showSelectChildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.dialog_choose_child_title);

        String[] childNames = new String[mCurrentChildList.size()];
        for(int i = 0; i < mCurrentChildList.size(); i++) {
            childNames[i] = mCurrentChildList.get(i).getName();
        }

        if(!mCurrentChildList.isEmpty()) {
            SharedPrefsUtils.setCurrentChildKey(mCurrentChildList.get(0).getKey());
            setToolbarTitle(mCurrentChildList.get(0).getName());
        }

        builder.setSingleChoiceItems(childNames, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Child currentChild = mCurrentChildList.get(which);
                SharedPrefsUtils.setCurrentChildKey(currentChild.getKey());
                setToolbarTitle(currentChild.getName());
            }
        });
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAdapter = new TestResultAdapter(mNavActivityCallback, mRecyclerView);
                mRecyclerView.setAdapter(mAdapter);
            }
        });
        builder.create().show();
=======
        RecyclerView view = (RecyclerView) inflater.inflate(R.layout.fragment_recycler_list, container, false);
        view.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new TestResultAdapter(mNavActivityCallback);
        view.setAdapter(mAdapter);

        return view;
>>>>>>> 5ead7cba6f04e51bb64fb19dd2d5568a28824121
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_test_results, menu);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TestResultAdapter.NavActivityCallback) {
            mCallback = (TestResultAdapter.NavActivityCallback) context;

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement TestResultListFragment.NavActivityCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void setNavActivityCallback(NavActivity navActivityCallback) {
<<<<<<< HEAD
        Log.d(TAG, "NavActivityCallback Set");
=======
        Log.d("TestResultListFrag", "NavActivityCallback Set");
>>>>>>> 5ead7cba6f04e51bb64fb19dd2d5568a28824121
        mNavActivityCallback = navActivityCallback;
    }

    @Override
    public void onFABClicked() {
<<<<<<< HEAD
        Log.d(TAG, "TestResultListFragment FAB clicked.");
=======
        Log.d("FAB CLICK", "TestResultListFragment FAB clicked.");
>>>>>>> 5ead7cba6f04e51bb64fb19dd2d5568a28824121
        showAddNewTestDialog();
    }

    private void showAddNewTestDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder((Context) mNavActivityCallback);
        builder.setTitle(getString(R.string.new_test_dialog_title));

        mAddNewTestBuilderView = getLayoutInflater().inflate(R.layout.dialog_add_test, null, false);
        builder.setView(mAddNewTestBuilderView);
<<<<<<< HEAD

        mAddNewTestDialog = builder.create();

        Button cameraButton = mAddNewTestBuilderView.findViewById(R.id.button_camera);
        cameraButton.setOnClickListener(this);

        Button uploadButton = mAddNewTestBuilderView.findViewById(R.id.button_upload);
        uploadButton.setOnClickListener(this);

        Button cancelAddTest = mAddNewTestBuilderView.findViewById(R.id.button_cancel_add_test);
        cancelAddTest.setOnClickListener(this);

        mAddNewTestDialog.show();
    }

    @Override
    public void onClick(View view) {
        int buttonId = view.getId();

        if (mSelectPhotoTypeBuilder == null) {
            mSelectPhotoTypeBuilder = new AlertDialog.Builder((Context) mNavActivityCallback);
            mSelectPhotoTypeBuilderView = getLayoutInflater().inflate(R.layout.dialog_select_image_type, null, false);
            mSelectPhotoTypeBuilder.setView(mSelectPhotoTypeBuilderView);

            Button eyeButton = mSelectPhotoTypeBuilderView.findViewById(R.id.button_eye);
            eyeButton.setOnClickListener(this);

            Button cancelSelectImageButton = mSelectPhotoTypeBuilderView.findViewById(R.id.button_cancel_select_image);
            cancelSelectImageButton.setOnClickListener(this);

            mSelectPhotoTypeDialog = mSelectPhotoTypeBuilder.create();
        }

        switch (buttonId) {
            case R.id.button_camera:
                this.isCapturing = true;
                this.isUploading = false;
                mSelectPhotoTypeDialog.show();
                break;

            case R.id.button_upload:
                this.isCapturing = false;
                this.isUploading = true;
                mSelectPhotoTypeDialog.show();
                break;

            case R.id.button_cancel_add_test:
                this.isCapturing = false;
                this.isUploading = false;
                mAddNewTestDialog.dismiss();
                break;

            case R.id.button_eye:
                pictureOfType(Constants.PIC_TYPE_EYE);
                mSelectPhotoTypeDialog.dismiss();
                mAddNewTestDialog.dismiss();
                break;

            case R.id.button_cancel_select_image:
                this.isCapturing = false;
                this.isUploading = false;
                mSelectPhotoTypeDialog.dismiss();
                break;
        }
    }

    private void pictureOfType(int pictureType) {
        if (isUploading) {
            chooseNewPicture(pictureType);
            Log.d(TAG, "User chose to upload a pic of type: " + pictureType);
            // use gallery chooser
        } else if (isCapturing) {
            Log.d(TAG, "User chose to capture a pic of type: " + pictureType);
            // capture new picture
            captureNewPicture(pictureType);
        }
    }

    private void chooseNewPicture(int pictureType) {
        Intent choosePictureIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (choosePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(choosePictureIntent, RC_CHOOSE_PICTURE);
        }
    }

    private void captureNewPicture(int pictureType) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, RC_TAKE_PICTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RC_TAKE_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    sendCapturedPhotoToAdapter(data);
                }
                break;
            case RC_CHOOSE_PICTURE:
                sendGalleryPhotoToAdapter(data);
                break;
            default:
                Log.d(TAG, "Invalid file request code");
                break;
        }
    }

    private void sendCapturedPhotoToAdapter(Intent data) {
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        DateFormat tf = new SimpleDateFormat("hh:mm", Locale.US);

        Date now = Calendar.getInstance().getTime();
        String date = df.format(now);
        String time = tf.format(now);

        TestResultTime testResultTime = new TestResultTime(date, time);
        String childKey = SharedPrefsUtils.getCurrentChildKey();
        TestResult testResult = new TestResult(childKey, testResultTime, new Photo("new", imageBitmap), 10);
        svmTasks.sendBitmapToSVM(imageBitmap, testResult);
    }

    private void sendGalleryPhotoToAdapter(Intent data) {
        if (data != null && data.getData() != null) {
            Uri uri = data.getData();
            String location = uri.toString();
            try {
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                DateFormat tf = new SimpleDateFormat("hh:mm", Locale.US);

                Date now = Calendar.getInstance().getTime();
                String date = df.format(now);
                String time = tf.format(now);

                TestResultTime testResultTime = new TestResultTime(date, time);
                String childKey = SharedPrefsUtils.getCurrentChildKey();
                TestResult testResult = new TestResult(childKey, testResultTime, new Photo("new", imageBitmap), 10);
                svmTasks.sendBitmapToSVM(imageBitmap, testResult);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setCurrentChildList(List<Child> currentChildList) {
        mCurrentChildList = currentChildList;
        showSelectChildDialog();
    }

    @Override
    public void setToolbarTitle(String childName) {
        String title = getResources().getString(R.string.child_format, childName);
        mNavActivityCallback.setTitle(title);
    }

    @Override
    public void onImageProcessed(TestResult testResult, Double result) {
        Log.d(TAG, String.format("Result: %s", Double.toString(result)));
        testResult.setResult(result.intValue());
        if (result.intValue() > 50) {
            setNotification();
        }
        mAdapter.addTestResult(testResult);
    }

    public void setNotification() {
        scheduleNotification(getNotification(getString(R.string.notification_content)), 3600000);
    }

    private void scheduleNotification(Notification notification, int delay) {
        Intent notificationIntent = new Intent(getContext(), NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification(String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "default");
        builder.setContentTitle(getString(R.string.app_name));
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.test_tube);
        Intent intent = new Intent(getContext(), NavActivity.class);
        intent.putExtra(EXTRA_FAMILY, mFamily);
        PendingIntent pi = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);
        Log.d("Notifications", "notification gotten");
        return builder.build();
    }

    public void setFamily(Family family) {
        mFamily = family;
    }

    @Override
    public void onTestLongPressed(final TestResult testResult, View v, final int position) {
        PopupMenu popupMenu = new PopupMenu((Context) mNavActivityCallback, v);
        popupMenu.inflate(R.menu.popup_remove);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_popup_remove:
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder((Context) mNavActivityCallback);
                        builder.setTitle(R.string.popup_remove_title);
                        builder.setMessage(R.string.popup_remove_message);
                        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAdapter.removeTestResult(position);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_test_results, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_choose_child:
                Utils.getCurrentChildren(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
=======

        mAddNewTestDialog = builder.create();

        Button cameraButton = mAddNewTestBuilderView.findViewById(R.id.button_camera);
        cameraButton.setOnClickListener(this);

        Button uploadButton = mAddNewTestBuilderView.findViewById(R.id.button_upload);
        uploadButton.setOnClickListener(this);

        Button cancelAddTest = mAddNewTestBuilderView.findViewById(R.id.button_cancel_add_test);
        cancelAddTest.setOnClickListener(this);

        mAddNewTestDialog.show();
    }

    @Override
    public void onClick(View view) {
        int buttonId = view.getId();

        if (mSelectPhotoTypeBuilder == null) {
            mSelectPhotoTypeBuilder = new AlertDialog.Builder((Context) mNavActivityCallback);
            mSelectPhotoTypeBuilderView = getLayoutInflater().inflate(R.layout.dialog_select_image_type, null, false);
            mSelectPhotoTypeBuilder.setView(mSelectPhotoTypeBuilderView);

            Button eyeButton = mSelectPhotoTypeBuilderView.findViewById(R.id.button_eye);
            eyeButton.setOnClickListener(this);

            Button palmButton = mSelectPhotoTypeBuilderView.findViewById(R.id.button_palm);
            palmButton.setOnClickListener(this);

            Button footButton = mSelectPhotoTypeBuilderView.findViewById(R.id.button_foot);
            footButton.setOnClickListener(this);

            Button cancelSelectImageButton = mSelectPhotoTypeBuilderView.findViewById(R.id.button_cancel_select_image);
            cancelSelectImageButton.setOnClickListener(this);

            mSelectPhotoTypeDialog = mSelectPhotoTypeBuilder.create();
        }

        switch (buttonId) {
            case R.id.button_camera:
                this.isCapturing = true;
                this.isUploading = false;
                mSelectPhotoTypeDialog.show();
                break;

            case R.id.button_upload:
                this.isCapturing = false;
                this.isUploading = true;
                mSelectPhotoTypeDialog.show();
                break;

            case R.id.button_cancel_add_test:
                this.isCapturing = false;
                this.isUploading = false;
                mAddNewTestDialog.dismiss();
                break;

            case R.id.button_eye:
                pictureOfType(Constants.PIC_TYPE_EYE);
                mSelectPhotoTypeDialog.dismiss();
                mAddNewTestDialog.dismiss();
                break;

            case R.id.button_palm:
                pictureOfType(Constants.PIC_TYPE_PALM);
                mSelectPhotoTypeDialog.dismiss();
                mAddNewTestDialog.dismiss();
                break;

            case R.id.button_foot:
                pictureOfType(Constants.PIC_TYPE_FOOT);
                mSelectPhotoTypeDialog.dismiss();
                mAddNewTestDialog.dismiss();
                break;

            case R.id.button_cancel_select_image:
                this.isCapturing = false;
                this.isUploading = false;
                mSelectPhotoTypeDialog.dismiss();
                break;
        }
    }

    private void pictureOfType(int pictureType) {
        if (isUploading) {
            Log.d("TestResultFrag", "User chose to upload a pic of type: " + pictureType);
            // use gallery chooser
        } else if (isCapturing) {
            Log.d("TestResultFrag", "User chose to capture a pic of type: " + pictureType);
            // capture new picture
            captureNewPicture(pictureType);
        }
    }

    private void captureNewPicture(int pictureType) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, RC_TAKE_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_TAKE_PICTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            DateFormat tf = new SimpleDateFormat("hh:mm", Locale.US);

            Date now = Calendar.getInstance().getTime();
            String date = df.format(now);
            String time = tf.format(now);

            TestResultTime testResultTime = new TestResultTime(date, time);
            String childKey = SharedPrefsUtils.getCurrentChildKey();
            mAdapter.addTestResult(new TestResult(childKey, testResultTime, new Photo("new", imageBitmap), 10));
>>>>>>> 5ead7cba6f04e51bb64fb19dd2d5568a28824121
        }
    }
}
