package edu.rosehulman.changb.boyeram1.jaundicedetection.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

import edu.rosehulman.changb.boyeram1.jaundicedetection.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {


    public InfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        InputStream is = getContext().getResources().openRawResource(R.raw.info);
        String s = null;
        try {
            s = IOUtils.toString(is, (String) null);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        TextView infoView = (TextView) view.findViewById(R.id.info_body);
        infoView.setText(s);
        return view;
    }

}
