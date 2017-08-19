package ramt57.infotrench.com.callrecorder.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ramt57.infotrench.com.callrecorder.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Outgoing extends Fragment {


    public Outgoing() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_outgoing, container, false);
    }

}
