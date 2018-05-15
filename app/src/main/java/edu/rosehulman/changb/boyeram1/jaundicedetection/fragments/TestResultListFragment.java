package edu.rosehulman.changb.boyeram1.jaundicedetection.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.rosehulman.changb.boyeram1.jaundicedetection.Constants;
import edu.rosehulman.changb.boyeram1.jaundicedetection.NavActivity;
import edu.rosehulman.changb.boyeram1.jaundicedetection.R;
import edu.rosehulman.changb.boyeram1.jaundicedetection.adapters.TestResultAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TestResultAdapter.Callback} interface
 * to handle interaction events.
 */
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

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    public TestResultListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RecyclerView view = (RecyclerView) inflater.inflate(R.layout.fragment_recycler_list, container, false);
        view.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new TestResultAdapter(getContext(), mCallback);
        view.setAdapter(mAdapter);
        return view;
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
        }
    }

}
