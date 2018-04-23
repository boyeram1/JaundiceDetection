package edu.rosehulman.changb.boyeram1.jaundicedetection.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.rosehulman.changb.boyeram1.jaundicedetection.R;
import edu.rosehulman.changb.boyeram1.jaundicedetection.adapters.ChildAdapter;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.TestResult;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link edu.rosehulman.changb.boyeram1.jaundicedetection.adapters.ChildAdapter.NavActivityCallback} interface
 * to handle interaction events.
 */
public class ChildListFragment extends Fragment {

    private ChildAdapter.NavActivityCallback mNavActivityCallback;

    public ChildListFragment() {
        // Required empty public constructor
    }

    public void setNavActivityCallback (ChildAdapter.NavActivityCallback navActivityCallback) {
        Log.d("ChildListFrag", "ChildAdapter.NavActivityCallback Set");
        this.mNavActivityCallback = navActivityCallback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RecyclerView recyclerView = (RecyclerView)inflater.inflate(R.layout.fragment_recycler_list, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        ChildAdapter adapter = new ChildAdapter(this.mNavActivityCallback, recyclerView);
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ChildAdapter.NavActivityCallback) {
            mNavActivityCallback = (ChildAdapter.NavActivityCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ChildAdapter.NavActivityCallback");
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mNavActivityCallback = null;
    }

}
