package ramt57.infotrench.com.callrecorder.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;

import ramt57.infotrench.com.callrecorder.R;
import ramt57.infotrench.com.callrecorder.adapter.RecyclerAdapter;
import ramt57.infotrench.com.callrecorder.contacts.ContactProvider;
import ramt57.infotrench.com.callrecorder.pojo_classes.Contacts;
import ramt57.infotrench.com.callrecorder.utils.StringUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class Outgoing extends Fragment {
    private RecyclerAdapter recyclerAdapter;
    RecyclerView recyclerView;
    ArrayList<Contacts> allContactList=new ArrayList<>();
    ArrayList<String> recording=new ArrayList<>();
    ArrayList<Contacts> recordedContacts=new ArrayList<>();
    public Outgoing() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_blank,container,false);
        Log.d("again","number times");
        recyclerView=view.findViewById(R.id.recyclerView);
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
        recyclerAdapter=new RecyclerAdapter();
        recyclerView.setAdapter(recyclerAdapter);
        Bundle bundle;
        bundle=getArguments();
        recording=bundle.getStringArrayList("RECORDING");
        allContactList= ContactProvider.getContacts(view.getContext());
        boolean hascontact=false;
        if(!recordedContacts.isEmpty()) {
            recordedContacts.clear();
        }
        for (String filename:recording){
            String recordedfilearray[]=filename.split("__");      //recorded file_array
            if(recordedfilearray[2].equals("OUT")){
                //incoming
                for(Contacts people:allContactList){
                    if(StringUtils.prepareContacts(view.getContext(),people.getNumber()).equalsIgnoreCase(recordedfilearray[0])){
                        long timestamp=new Long(recordedfilearray[1]).longValue();
                        String relative_time= ContactProvider.getrelative(timestamp);
                        people.setTime(relative_time);
                        recordedContacts.add(people);
                        hascontact=true;
                        break;
                    }
                }

                if(!hascontact){
                    //no contact show them
                    long timestamp=new Long(recordedfilearray[1]).longValue();
                    ContactProvider.getrelative(timestamp);
                    String relative_time= ContactProvider.getrelative(timestamp);
                    Contacts nocontact=new Contacts();
                    nocontact.setNumber(recordedfilearray[0]);
                    nocontact.setTime(relative_time);
                    recordedContacts.add(nocontact);
                }else{
                    hascontact=false;
                }
            }

        }
        recyclerAdapter.setContacts(recordedContacts);
        recyclerAdapter.notifyDataSetChanged();
        return view;
    }

}
