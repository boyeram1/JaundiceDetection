package edu.rosehulman.changb.boyeram1.jaundicedetection.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.rosehulman.changb.boyeram1.jaundicedetection.R;
import edu.rosehulman.changb.boyeram1.jaundicedetection.adapters.TestResultAdapter;
import edu.rosehulman.changb.boyeram1.jaundicedetection.modelObjects.TestResult;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TestResultAdapter.Callback} interface
 * to handle interaction events.
 */
public class TestResultListFragment extends Fragment {

    private TestResultAdapter.Callback mCallback;

    public TestResultListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RecyclerView view = (RecyclerView)inflater.inflate(R.layout.fragment_recycler_list, container, false);
        view.setLayoutManager(new LinearLayoutManager(getContext()));
        TestResultAdapter adapter = new TestResultAdapter(getContext(), mCallback);
        view.setAdapter(adapter);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
