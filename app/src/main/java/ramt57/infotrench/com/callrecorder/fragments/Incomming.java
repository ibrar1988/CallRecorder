package ramt57.infotrench.com.callrecorder.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class Incomming extends Fragment {
    TextView sampleText;
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
//        sampleText=view.findViewById(R.id.text);
        Log.d("fragment","hello");
//        Bundle bundle=getArguments();
            Bundle bundle;
            bundle=getArguments();
            recording=bundle.getStringArrayList("RECORDING");
//       String s[]= arg.split("__");
        allContactList= ContactProvider.getContacts(view.getContext());
        for (String filename:recording){
            String s[]=filename.split("__");
            for(Contacts people:allContactList){
                if(people.getNumber().equalsIgnoreCase(s[0])){
                    recordedContacts.add(people);
                    Toast.makeText(view.getContext(),"equals : "+people.getNumber(),Toast.LENGTH_LONG).show();
                    break;
                }else{
//                    Toast.makeText(view.getContext(),""+s[0]+" : "+people.getNumber(),Toast.LENGTH_LONG).show();
                }
            }
        }
        recyclerAdapter.notifyDataSetChanged();
//            sampleText.setText(bundle.getString("NUMBER"));
//           Log.d("Hello",bundle.getString("NUMBER"));
        return view;

    }

}
