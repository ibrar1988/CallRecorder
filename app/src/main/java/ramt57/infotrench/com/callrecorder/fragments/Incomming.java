package ramt57.infotrench.com.callrecorder.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ramt57.infotrench.com.callrecorder.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Incomming extends Fragment {
    TextView sampleText;

    public Incomming() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_incomming,container,false);
        sampleText=view.findViewById(R.id.text);
        Log.d("fragment","hello");
//        Bundle bundle=getArguments();
        if(getArguments()!=null){
            sampleText.setText(getArguments().getString("NUMBER"));
           Log.d("Hello",getArguments().getString("NUMBER"));
        }
        return view;

    }

}
