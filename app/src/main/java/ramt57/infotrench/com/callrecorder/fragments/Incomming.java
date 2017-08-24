package ramt57.infotrench.com.callrecorder.fragments;


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

import java.util.ArrayList;

import ramt57.infotrench.com.callrecorder.R;
import ramt57.infotrench.com.callrecorder.adapter.RecyclerAdapter;
import ramt57.infotrench.com.callrecorder.contacts.ContactProvider;
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
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(),DividerItemDecoration.HORIZONTAL));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
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
                    recordedContacts.add(people);
                    hascontact=true;
//                    Long l=Long.valueOf(d[0]);
//                    DateUtils.getRelativeDateTimeString(l.longValue());
//                    Toast.makeText(view.getContext(),"equals : "+l.longValue()+d[1],Toast.LENGTH_LONG).show();

                    break;
                }
            }
            if(!hascontact){
                //no contact show them

            }else{
                hascontact=false;
            }
        }
        recyclerAdapter.notifyDataSetChanged();
//            sampleText.setText(bundle.getString("NUMBER"));
//           Log.d("Hello",bundle.getString("NUMBER"));
        return view;

    }

}
