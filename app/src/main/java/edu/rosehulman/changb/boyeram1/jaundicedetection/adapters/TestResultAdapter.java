package edu.rosehulman.changb.boyeram1.jaundicedetection.adapters;

import android.support.design.widget.Snackbar;
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

import java.util.ArrayList;
import java.util.List;

import edu.rosehulman.changb.boyeram1.jaundicedetection.R;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.TestResult;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.TestResultTime;
import edu.rosehulman.changb.boyeram1.jaundicedetection.utils.SharedPrefsUtils;

/**
 * Created by changb on 4/22/2018.
 */
public class TestResultAdapter extends RecyclerView.Adapter<TestResultAdapter.ViewHolder> {

    private TestResultAdapter.NavActivityCallback mCallback;
    private List<TestResult> mTestResults;
    private DatabaseReference mTestResultsRef;
    private RecyclerView mRecyclerView;

    private static String TAG = "JD-TestResAdapt";

    public TestResultAdapter(TestResultAdapter.NavActivityCallback callback, RecyclerView recyclerView) {
        mTestResults = new ArrayList<>();
        mRecyclerView = recyclerView;

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

    public void removeTestResult(final int position) {
        final TestResult testResult = this.mTestResults.get(position);
        Log.d(TAG, "Removing: " + testResult.getKey());
        mTestResults.remove(position);
        notifyItemRemoved(position);

        Snackbar.make(this.mRecyclerView, "Test removed", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (testResult != null) {
                            mTestResults.add(position, testResult);
                            notifyItemInserted(position);
                            Snackbar.make(mRecyclerView, "Test restored", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }).addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                Log.d(TAG, "Snackbar dismissed: " + event);
                if(event != Snackbar.Callback.DISMISS_EVENT_ACTION && event != Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE) {
                    mTestResultsRef.child(testResult.getKey()).removeValue();
                }
            }
        }).show();
    }

    @Override
    public TestResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "Created View Holder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_results_row_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TestResultAdapter.ViewHolder holder, int position) {
        final TestResult testResult = mTestResults.get(position);

        if(testResult.getResult() >= 50) {
            holder.mImageView.setImageResource(R.drawable.ic_bad);
        } else {
            holder.mImageView.setImageResource(R.drawable.ic_good);
        }

        TestResultTime testResultTime = testResult.getTestResultTime();
        holder.mTestResultDateTextView.setText(testResultTime.dateToString() + " " + testResultTime.timeToString());
        holder.mTestResultPercentageTextView.setText(testResult.getResult() + "% chance of jaundice");
    }

    @Override
    public int getItemCount() {
        return mTestResults.size();
    }

    public interface NavActivityCallback {
        void onTestLongPressed(TestResult testResult, View v, int position);
    }

    class TestResultChildEventListener implements ChildEventListener {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            TestResult testResult = dataSnapshot.getValue(TestResult.class);
            testResult.setKey(dataSnapshot.getKey());
            mTestResults.add(0, testResult);
            mRecyclerView.getLayoutManager().scrollToPosition(0);
            notifyItemInserted(0);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String key = dataSnapshot.getKey();
            TestResult updatedTestResult = dataSnapshot.getValue(TestResult.class);

            int i = 0;
            for(TestResult testResult : mTestResults) {
                if(testResult.getKey().equals(key)) {
                    testResult.setValues(updatedTestResult);
                    notifyItemChanged(i);
                    return;
                }
                i++;
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            String key = dataSnapshot.getKey();

            int i = 0;
            for(TestResult testResult : mTestResults) {
                if(testResult.getKey().equals(key)) {
                    mTestResults.remove(i);
                    notifyItemRemoved(i);
                    return;
                }
                i++;
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            // empty
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.d(TAG, "Database error: " + databaseError);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView mImageView;
        TextView mTestResultDateTextView;
        TextView mTestResultPercentageTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image_button);
            mTestResultDateTextView = (TextView) itemView.findViewById(R.id.test_date_text_view);
            mTestResultPercentageTextView = (TextView) itemView.findViewById(R.id.test_percentage_text_view);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // opens the next activity/fragment

        }

        @Override
        public boolean onLongClick(View v) {
            Log.d(TAG, "Test Result Long Pressed");
            mCallback.onTestLongPressed(mTestResults.get(getAdapterPosition()), v, getAdapterPosition());
            return true;
        }
    }
}
