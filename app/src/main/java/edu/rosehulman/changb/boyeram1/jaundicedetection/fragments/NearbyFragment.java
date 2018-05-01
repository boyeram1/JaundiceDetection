package edu.rosehulman.changb.boyeram1.jaundicedetection.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import edu.rosehulman.changb.boyeram1.jaundicedetection.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearbyFragment extends Fragment {

    private GoogleMap mMap;
    private FragmentActivity fragmentActivity;
    private OnMapReadyCallback mOnMapReadyCallback;

    public NearbyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SupportMapFragment mapFragment = (SupportMapFragment) fragmentActivity.getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(mOnMapReadyCallback);
        return inflater.inflate(R.layout.fragment_nearby, container, false);
    }

    @Override
    public void onAttach(Context context) {
        fragmentActivity =(FragmentActivity) context;
        super.onAttach(context);
    }

    public void setOnMapReadyCallback (OnMapReadyCallback onMapReadyCallback) {
        Log.d("ChildListFrag", "ChildAdapter.NavActivityCallback Set");
        mOnMapReadyCallback = onMapReadyCallback;
    }

}
