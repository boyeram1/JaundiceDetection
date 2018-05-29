package edu.rosehulman.changb.boyeram1.jaundicedetection.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.rosehulman.changb.boyeram1.jaundicedetection.Constants;
import edu.rosehulman.changb.boyeram1.jaundicedetection.NavActivity;
import edu.rosehulman.changb.boyeram1.jaundicedetection.R;
import edu.rosehulman.changb.boyeram1.jaundicedetection.adapters.TestResultAdapter;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.Child;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.Photo;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.TestResult;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.TestResultTime;
import edu.rosehulman.changb.boyeram1.jaundicedetection.svm.SVMTasks;
import edu.rosehulman.changb.boyeram1.jaundicedetection.utils.SharedPrefsUtils;
import edu.rosehulman.changb.boyeram1.jaundicedetection.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TestResultAdapter.Callback} interface
 * to handle interaction events.
 */
public class TestResultListFragment extends Fragment
        implements INavDrawerFragment, View.OnClickListener,
        Utils.FragmentNeedingChildList, SVMTasks.SVMConsumer {

    public static final String TAG = "JaundiceDetection";

    private TestResultAdapter.Callback mCallback;
    private NavActivity mNavActivityCallback;
    private RecyclerView mRecyclerView;
    private TestResultAdapter mAdapter;

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


    public TestResultListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_recycler_list, container, false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new TestResultAdapter(mNavActivityCallback);
        mRecyclerView.setAdapter(mAdapter);

        Utils.getChildNameByKey(this);

        svmTasks = new SVMTasks(getContext(), this);

        return mRecyclerView;
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
        }
    }

    private void showSelectChildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.dialog_choose_child_title);

        String[] childNames = new String[mCurrentChildList.size()];
        for(int i = 0; i < mCurrentChildList.size(); i++) {
            Log.d(Constants.TAG, mCurrentChildList.get(i).getName());
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
                mAdapter = new TestResultAdapter(mNavActivityCallback);
                mRecyclerView.setAdapter(mAdapter);
            }
        });
        builder.create().show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TestResultAdapter.Callback) {
            mCallback = (TestResultAdapter.Callback) context;
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
        Log.d("TestResultListFrag", "NavActivityCallback Set");
        mNavActivityCallback = navActivityCallback;
    }

    @Override
    public void onFABClicked() {
        Log.d("FAB CLICK", "TestResultListFragment FAB clicked.");
        showAddNewTestDialog();
    }

    private void showAddNewTestDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder((Context) mNavActivityCallback);
        builder.setTitle(getString(R.string.new_test_dialog_title));

        mAddNewTestBuilderView = getLayoutInflater().inflate(R.layout.dialog_add_test, null, false);
        builder.setView(mAddNewTestBuilderView);

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
            Log.d("TestResultFrag", "User chose to upload a pic of type: " + pictureType);
            // use gallery chooser
        } else if (isCapturing) {
            Log.d("TestResultFrag", "User chose to capture a pic of type: " + pictureType);
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
        mAdapter.addTestResult(new TestResult(childKey, testResultTime, new Photo("new", imageBitmap), 10));
        svmTasks.sendBitmapToSVM(imageBitmap);
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
                mAdapter.addTestResult(new TestResult(childKey, testResultTime, new Photo("new", imageBitmap), 10));
                svmTasks.sendBitmapToSVM(imageBitmap);
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
    public void onImageProcessed(Double result) {
        Log.d(TAG, String.format("Result: %s", Double.toString(result)));
    }
}
