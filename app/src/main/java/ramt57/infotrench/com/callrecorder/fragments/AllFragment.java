package ramt57.infotrench.com.callrecorder.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.microsoft.onedrivesdk.saver.SaverException;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;

import ramt57.infotrench.com.callrecorder.BroadcastReciver.ExtendedReciver;
import ramt57.infotrench.com.callrecorder.MainActivity;
import ramt57.infotrench.com.callrecorder.R;
import ramt57.infotrench.com.callrecorder.SqliteDatabase.DatabaseHelper;
import ramt57.infotrench.com.callrecorder.adapter.RecyclerAdapter;
import ramt57.infotrench.com.callrecorder.contacts.ContactProvider;
import ramt57.infotrench.com.callrecorder.listener.RecyclerViewTouchListener;
import ramt57.infotrench.com.callrecorder.pojo_classes.Contacts;
import ramt57.infotrench.com.callrecorder.utils.StringUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllFragment extends Fragment {
    RecyclerAdapter recyclerAdapter;
    RecyclerView recyclerView;
    ArrayList<Contacts> allContactList=new ArrayList<>();
    ArrayList<String> recording=new ArrayList<>();
    ArrayList<Contacts> recordedContacts=new ArrayList<>();
    ArrayList<Contacts> searchPeople=new ArrayList<>();
    ArrayList<Integer> integers=new ArrayList<>();
    Context ctx;
    public AllFragment() {
        // Required empty public constructor
    }
    boolean mensu=false;
    int temp;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_blank,container,false);
        init(view);
        ctx=view.getContext();
        Bundle bundle;
        bundle=getArguments();
        recording=bundle.getStringArrayList("RECORDING");
        recordedContacts=ContactProvider.getCallList(view.getContext(),recording,"");
        recyclerAdapter.setContacts(recordedContacts);
        recyclerAdapter.notifyDataSetChanged();
        MainActivity.setQueylistener(new MainActivity.querySearch() {
            @Override
            public void Search_name(String name) {
                //working interface
                if(name.length()>2){
                    mensu=true;
                    searchPeople.clear();
                    temp=0;
                    for(Contacts contacts:recordedContacts){
                        if(contacts.getNumber().contains(name)){
                            //dsd
                            integers.add(temp);
                            searchPeople.add(contacts);
                            ++temp;
                            continue;
                        }
                        if(contacts.getName()!=null&&contacts.getName().toLowerCase().contains(name.toLowerCase())){
                            integers.add(temp);
                            searchPeople.add(contacts);
                        }
                        ++temp;
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

    private void init(View view) {
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
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(view.getContext(), recyclerView, new RecyclerAdapter.itemClickListener() {
            @Override
            public void onClick(View view, int position) {
                ArrayList<String> records=ContactProvider.getRecordingList(view.getContext(),recording,"");
                Contacts contacts=recordedContacts.get(position);
                if(mensu){
                    Contacts contacts1=searchPeople.get(position);
                    ContactProvider.openMaterialSheetDialog(getLayoutInflater(),position,records.get(integers.get(position)),contacts1);
                }else {
                    ContactProvider.openMaterialSheetDialog(getLayoutInflater(),position,records.get(position),contacts);
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
    }
}
