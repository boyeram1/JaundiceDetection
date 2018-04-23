package edu.rosehulman.changb.boyeram1.jaundicedetection.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.rosehulman.changb.boyeram1.jaundicedetection.fragments.TestResultsFragment;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.TestResult;

/**
 * Created by changb on 4/22/2018.
 */

public class TestResultsAdapter extends RecyclerView.Adapter<TestResultsAdapter.ViewHolder> {

    private List<TestResult> mTestResults;
    private TestResultsFragment.Callback mCallback;

    public TestResultsAdapter(Context context, TestResultsFragment.Callback callback) {
        this.mCallback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
