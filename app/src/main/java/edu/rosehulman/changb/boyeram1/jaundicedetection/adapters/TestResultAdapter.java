package edu.rosehulman.changb.boyeram1.jaundicedetection.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import edu.rosehulman.changb.boyeram1.jaundicedetection.NavActivity;
import edu.rosehulman.changb.boyeram1.jaundicedetection.R;
import edu.rosehulman.changb.boyeram1.jaundicedetection.fragments.TestResultListFragment;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.Child;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.TestResult;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.TestResultTime;
import edu.rosehulman.changb.boyeram1.jaundicedetection.utils.SharedPrefsUtils;

/**
 * Created by changb on 4/22/2018.
 */
public class TestResultAdapter extends RecyclerView.Adapter<TestResultAdapter.ViewHolder> {

    private ChildAdapter.NavActivityCallback mCallback;
    private List<TestResult> mTestResults;

    private DatabaseReference mTestResultsRef;
    private DatabaseReference mChildrenRef;

    public TestResultAdapter(ChildAdapter.NavActivityCallback callback) {
        mTestResults = new ArrayList<>();
        mCallback = callback;

        mTestResultsRef = FirebaseDatabase.getInstance().getReference("test-results");
        mTestResultsRef.keepSynced(true);
        String childKey = SharedPrefsUtils.getCurrentChildKey();
        Query myTestResultsRef = mTestResultsRef.orderByChild("childKey").equalTo(childKey);
        myTestResultsRef.addChildEventListener(new TestResultChildEventListener());
    }

    public void addTestResult(TestResult testResult) {
        mTestResultsRef.push().setValue(testResult);
    }

    @Override
    public TestResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("CreateViewHolder", "Created View Holder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_results_row_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TestResultAdapter.ViewHolder holder, int position) {
        final TestResult testResult = mTestResults.get(position);
        holder.mImageView.setImageBitmap(testResult.getPhoto().getImageBitmap());
        holder.mTestResultDateTextView.setText(testResult.getTestResultTime().dateToString());
        holder.mTestResultPercentageTextView.setText(testResult.getResult() + "% chance of jaundice");
    }

    @Override
    public int getItemCount() {
        return mTestResults.size();
    }

    public interface Callback {
        // TODO: Update argument type and name
        void onTestSelected(TestResult testResult);
    }

    class TestResultChildEventListener implements ChildEventListener {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            TestResult testResult = dataSnapshot.getValue(TestResult.class);
            testResult.setKey(dataSnapshot.getKey());
            mTestResults.add(0, testResult);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView mTestResultDateTextView;
        TextView mTestResultPercentageTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image_view);
            mTestResultDateTextView = (TextView) itemView.findViewById(R.id.test_date_text_view);
            mTestResultPercentageTextView = (TextView) itemView.findViewById(R.id.test_percentage_text_view);
        }
    }
}
