package ramt57.infotrench.com.callrecorder.fragments;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.TreeMap;
import ramt57.infotrench.com.callrecorder.MainActivity;
import ramt57.infotrench.com.callrecorder.R;
import ramt57.infotrench.com.callrecorder.adapter.RecyclerAdapter;
import ramt57.infotrench.com.callrecorder.contacts.ContactProvider;
import ramt57.infotrench.com.callrecorder.pojo_classes.Contacts;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllFragment extends Fragment {
    RecyclerAdapter recyclerAdapter;
    RecyclerView recyclerView;
    ArrayList<String> recording=new ArrayList<>();
    ArrayList<Contacts> recordedContacts=new ArrayList<>();
    ArrayList<Object> searchPeople=new ArrayList<>();
    ArrayList<Object> realrecordingcontacts=new ArrayList<>();
    TreeMap<String ,ArrayList<Contacts>> headerevent=new TreeMap<>();
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ctx.checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            showContacts();
        }
          recyclerAdapter.setContacts(realrecordingcontacts);
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
                            searchPeople.add(contacts);
                            ++temp;
                            continue;
                        }
                        if(contacts.getName()!=null&&contacts.getName().toLowerCase().contains(name.toLowerCase())){
                            searchPeople.add(contacts);
                        }
                        ++temp;
                    }
                            recyclerAdapter.setContacts(searchPeople);
                            recyclerAdapter.notifyDataSetChanged();

                }else{
                    mensu=false;
                            recyclerAdapter.setContacts(realrecordingcontacts);
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
        recyclerAdapter.setListener(new RecyclerAdapter.OnitemClickListener() {
            @Override
            public void onClick(View v, int position) {
                if(mensu){
                    Contacts contacts1= (Contacts) searchPeople.get(position);
                    String records=ContactProvider.getRecordsList(v.getContext(),recording,"",contacts1);
                    if(Build.VERSION.SDK_INT>18){
                        ContactProvider.openMaterialSheetDialog(getLayoutInflater(),position,records,contacts1);
                    }else{
                        ContactProvider.showDialog(v.getContext(),records,contacts1);
                    }
                }else {
                    Contacts contacts= (Contacts) realrecordingcontacts.get(position);
                    String records=ContactProvider.getRecordsList(v.getContext(),recording,"",contacts);
                    if(Build.VERSION.SDK_INT>18){
                        ContactProvider.openMaterialSheetDialog(getLayoutInflater(),position,records,contacts);
                    }else{
                        ContactProvider.showDialog(v.getContext(),records,contacts);
                    }
                }
                ContactProvider.setItemrefresh(new ContactProvider.refresh() {
                    @Override
                    public void refreshList(boolean var) {
                        if(var)
//                        recyclerAdapter.notifyDataSetChanged();
                        showContacts();
                    }
                });
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(getContext(), "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showContacts() {
        headerevent.clear();
        if(!realrecordingcontacts.isEmpty()){
            realrecordingcontacts.clear();
        }
        if(!recordedContacts.isEmpty()){
            recordedContacts.clear();
        }
        recordedContacts=ContactProvider.getCallList(getContext(),recording,"");
        for (Contacts contacts:recordedContacts){
            if(contacts.getView()==1){
                if(!headerevent.containsKey("1")){
                    headerevent.put("1",new ArrayList<Contacts>());
                }
                headerevent.get("1").add(contacts);
            }else if(contacts.getView()==2){
                if(!headerevent.containsKey("2")){
                    headerevent.put("2",new ArrayList<Contacts>());
                }
                headerevent.get("2").add(contacts);
            }else {
                if(!headerevent.containsKey(contacts.getDate())){
                    headerevent.put(contacts.getDate(),new ArrayList<Contacts>());
                }
                headerevent.get(contacts.getDate()).add(contacts);
            }
        }
        for (String date:headerevent.keySet()){

           for (Contacts contacts:headerevent.get(date)){
               realrecordingcontacts.add(contacts);
           }
            realrecordingcontacts.add(date);
        }
        recyclerAdapter.notifyDataSetChanged();
    }
}
