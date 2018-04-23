package edu.rosehulman.changb.boyeram1.jaundicedetection.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.rosehulman.changb.boyeram1.jaundicedetection.R;
import edu.rosehulman.changb.boyeram1.jaundicedetection.fragments.TestResultListFragment;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.TestResult;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.TestResultTime;

/**
 * Created by changb on 4/22/2018.
 */

public class TestResultAdapter extends RecyclerView.Adapter<TestResultAdapter.ViewHolder> {

    private Context mContext;
    private List<TestResult> mTestResults;
    private TestResultListFragment.Callback mCallback;

    public TestResultAdapter(Context context, TestResultListFragment.Callback callback) {
        this.mCallback = callback;
        this.mTestResults = new ArrayList<>();
        this.mContext = context;
        mTestResults.add(new TestResult("0", "Abe", 1, new TestResultTime(1,1,1,1,1)));
        mTestResults.add(new TestResult("1", "Blinkin", 100, new TestResultTime(2,3,4,5,6)));
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
        holder.mTestResultChildName.setText(testResult.getChildName());
        holder.mTestResultPercentage.setText(Integer.toString(testResult.getPercentage()));
    }

    @Override
    public int getItemCount() {
        return mTestResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTestResultChildName;
        TextView mTestResultPercentage;

        public ViewHolder(View itemView) {
            super(itemView);
            mTestResultChildName = (TextView) itemView.findViewById(R.id.test_result_child_name_text_view);
            mTestResultPercentage = (TextView) itemView.findViewById(R.id.test_result_percentage_text_view);
        }
    }
}
