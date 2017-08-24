package ramt57.infotrench.com.callrecorder.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.Date;

import ramt57.infotrench.com.callrecorder.R;
import ramt57.infotrench.com.callrecorder.adapter.RecyclerAdapter;
import ramt57.infotrench.com.callrecorder.contacts.ContactProvider;
import ramt57.infotrench.com.callrecorder.divider.MyItemDecorator;
import ramt57.infotrench.com.callrecorder.pojo_classes.Contacts;
import ramt57.infotrench.com.callrecorder.utils.StringUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class Incomming extends Fragment {
    RecyclerAdapter recyclerAdapter;
    RecyclerView recyclerView;
    ArrayList<Contacts> allContactList=new ArrayList<>();
    ArrayList<String> recording=new ArrayList<>();
    ArrayList<Contacts> recordedContacts=new ArrayList<>();
    public Incomming() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_incomming,container,false);
        recyclerView=view.findViewById(R.id.recyclerView);
//        MyItemDecorator decoration = new MyItemDecorator(getContext(), Color.parseColor("#dadde2"), 0.5f);
//        recyclerView.addItemDecoration(decoration);
        recyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getContext())
                        .color(Color.parseColor("#dadde2"))
                        .sizeResId(R.dimen.divider)
                        .marginResId(R.dimen.leftmargin, R.dimen.rightmargin)
                        .build());
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(view.getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter=new RecyclerAdapter(recordedContacts);
        recyclerView.setAdapter(recyclerAdapter);
        Bundle bundle;
        bundle=getArguments();
        recording=bundle.getStringArrayList("RECORDING");
        allContactList= ContactProvider.getContacts(view.getContext());
        boolean hascontact=false;
        for (String filename:recording){
            String recordedfilearray[]=filename.split("__");      //recorded file_array
            for(Contacts people:allContactList){
                if(StringUtils.prepareContacts(view.getContext(),people.getNumber()).equalsIgnoreCase(recordedfilearray[0])){
                    long timestamp=new Long(recordedfilearray[1]).longValue();
                    String relative_time= getrelative(timestamp);
                    people.setTime(relative_time);
                    recordedContacts.add(people);
                    hascontact=true;
                    break;
                }
            }
            if(!hascontact){
                //no contact show them
                long timestamp=new Long(recordedfilearray[1]).longValue();
                getrelative(timestamp);
              String relative_time= getrelative(timestamp);
                Contacts nocontact=new Contacts();
                nocontact.setNumber(recordedfilearray[0]);
                nocontact.setTime(relative_time);
                recordedContacts.add(nocontact);
            }else{
                hascontact=false;
            }
        }
        recyclerAdapter.notifyDataSetChanged();
        return view;
    }

    private String getrelative(long time) {
        long d=(System.currentTimeMillis()/1000)-time;
        String remainingTime="";
        if(d<60){
                //seconds
            remainingTime=((((d % 31536000) % 86400) % 3600) % 60)+" seconds ago";
        }else if (d>60&&d<3600){
            //in minutes
            remainingTime=Math.round((((d % 31536000) % 86400) % 3600) / 60)+" minutes ago";
        }else if (d>3600&&d<86400){
            //in hours
            remainingTime=Math.round(((d % 31536000) % 86400) / 3600)+" hours ago";
        }else if(d>86400&&d<31536000){
            //in days
            remainingTime=Math.round((d % 31536000) / 86400)+" days ago";
        }else {
            //in years
            remainingTime=Math.round(d / 31536000)+" years ago";
        }
        return remainingTime;
    }


}
