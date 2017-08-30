package ramt57.infotrench.com.callrecorder.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;

import ramt57.infotrench.com.callrecorder.BroadcastReciver.ExtendedReciver;
import ramt57.infotrench.com.callrecorder.MainActivity;
import ramt57.infotrench.com.callrecorder.R;
import ramt57.infotrench.com.callrecorder.SqliteDatabase.DatabaseHelper;
import ramt57.infotrench.com.callrecorder.adapter.OutgoingAdapter;
import ramt57.infotrench.com.callrecorder.adapter.RecyclerAdapter;
import ramt57.infotrench.com.callrecorder.contacts.ContactProvider;
import ramt57.infotrench.com.callrecorder.listener.RecyclerViewTouchListener;
import ramt57.infotrench.com.callrecorder.pojo_classes.Contacts;

/**
 * A simple {@link Fragment} subclass.
 */
public class Outgoing extends Fragment {
    private OutgoingAdapter recyclerAdapter;
    RecyclerView recyclerView;
    ArrayList<Contacts> allContactList=new ArrayList<>();
    ArrayList<String> recording2=new ArrayList<>();
    ArrayList<Contacts> recordedContacts=new ArrayList<>();
    ArrayList<Contacts> searchPeople=new ArrayList<>();
    boolean mensu=false;
    Context ctx;
    public Outgoing() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_blank,container,false);
        ctx=view.getContext();
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
        recyclerAdapter=new OutgoingAdapter();
        recyclerView.setAdapter(recyclerAdapter);
        Bundle bundle;
        bundle=getArguments();
        recording2=bundle.getStringArrayList("RECORDING");
        allContactList= ContactProvider.getContacts(view.getContext());
        boolean hascontact=false;
        recordedContacts=ContactProvider.getCallList(view.getContext(),recording2,"OUT");
        recyclerAdapter.setContacts(recordedContacts);
        recyclerAdapter.notifyDataSetChanged();
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(view.getContext(), recyclerView, new RecyclerAdapter.itemClickListener() {
            @Override
            public void onClick(View view, int position) {
                ArrayList<String> records=ContactProvider.getRecordingList(view.getContext(),recording2,"OUT");
                if(mensu){
                    Contacts contacts1=searchPeople.get(position);
                    ContactProvider.openMaterialSheetDialog(getLayoutInflater(),position,records.get(position),contacts1);
                }else {
                    ContactProvider.openMaterialSheetDialog(getLayoutInflater(),position,records.get(position),recordedContacts.get(position));
                }
                ContactProvider.setItemrefresh(new ContactProvider.refresh() {
                    @Override
                    public void refreshList(boolean var) {
                        if(var)
                            recyclerAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        MainActivity.setQueylistener3(new MainActivity.querySearch3() {
            @Override
            public void Search_name3(String name) {
                ArrayList<Contacts> records=new ArrayList<Contacts>();
                DatabaseHelper databaseHelper=new DatabaseHelper(ctx);
                records=databaseHelper.AllContacts();
                if(name.length()>1){
                    mensu=true;
                    searchPeople.clear();
                    for(Contacts contacts:recordedContacts){
                        if(contacts.getNumber().contains(name)){
                            //dsd
                            searchPeople.add(contacts);
                            continue;
                        }
                        if(contacts.getName()!=null&&contacts.getName().toLowerCase().contains(name.toLowerCase())){
                            searchPeople.add(contacts);
                        }
                    }
                    recyclerAdapter.setContacts(searchPeople);
                    recyclerAdapter.notifyDataSetChanged();
                }else{
                    mensu=false;
                    recyclerAdapter.setContacts(recordedContacts);
                    recyclerAdapter.notifyDataSetChanged();
                }
            }
        });
        return view;
    }
}
