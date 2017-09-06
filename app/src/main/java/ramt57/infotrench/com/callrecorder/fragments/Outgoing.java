package ramt57.infotrench.com.callrecorder.fragments;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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
import ramt57.infotrench.com.callrecorder.SqliteDatabase.DatabaseHelper;
import ramt57.infotrench.com.callrecorder.adapter.OutgoingAdapter;
import ramt57.infotrench.com.callrecorder.adapter.RecyclerAdapter;
import ramt57.infotrench.com.callrecorder.contacts.ContactProvider;
import ramt57.infotrench.com.callrecorder.pojo_classes.Contacts;

/**
 * A simple {@link Fragment} subclass.
 */
public class Outgoing extends Fragment {
    private OutgoingAdapter recyclerAdapter;
    RecyclerView recyclerView;
    int temp;
    ArrayList<String> recording2=new ArrayList<>();
    ArrayList<Contacts> recordedContacts=new ArrayList<>();
    ArrayList<Contacts> searchPeople=new ArrayList<>();
    ArrayList<Integer> integers=new ArrayList<>();
    ArrayList<Object> realrecordingcontacts=new ArrayList<>();
    TreeMap<String ,ArrayList<Contacts>> headerevent=new TreeMap<>();
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    boolean mensu=false;
    Context ctx;
    public Outgoing() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.outgoing_fragment,container,false);
        ctx=view.getContext();
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ctx.checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            recordedContacts=ContactProvider.getCallList(ctx,recording2,"OUT");
            recyclerAdapter.notifyDataSetChanged();
        }

        recyclerAdapter.setContacts(recordedContacts);
        MainActivity.setQueylistener3(new MainActivity.querySearch3() {
            @Override
            public void Search_name3(String name) {
                ArrayList<Contacts> records=new ArrayList<Contacts>();
                DatabaseHelper databaseHelper=new DatabaseHelper(ctx);
                records=databaseHelper.AllContacts();
                if(name.length()>1){
                    mensu=true;
                    searchPeople.clear();
                    temp=0;
                    for(Contacts contacts:recordedContacts){
                        if(contacts.getNumber().contains(name)){
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
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                recordedContacts=ContactProvider.getCallList(ctx,recording2,"OUT");
                recyclerAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
