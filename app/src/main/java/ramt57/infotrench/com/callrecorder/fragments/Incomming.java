package ramt57.infotrench.com.callrecorder.fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
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

import ramt57.infotrench.com.callrecorder.BroadcastReciver.ExtendedReciver;
import ramt57.infotrench.com.callrecorder.MainActivity;
import ramt57.infotrench.com.callrecorder.R;
import ramt57.infotrench.com.callrecorder.SqliteDatabase.DatabaseHelper;
import ramt57.infotrench.com.callrecorder.adapter.IncommingAdapter;
import ramt57.infotrench.com.callrecorder.contacts.ContactProvider;
import ramt57.infotrench.com.callrecorder.pojo_classes.Contacts;

/**
 * A simple {@link Fragment} subclass.
 */
public class Incomming extends Fragment {
   private IncommingAdapter recyclerAdapter;
//    RecyclerAdapter recyclerAdapter;
    RecyclerView recyclerView;
    Context ctx;
    boolean mensu=false;
    int temp;
    ArrayList<Contacts> searchPeople=new ArrayList<>();
    ArrayList<String> recordings=new ArrayList<>();
    ArrayList<Integer> integers=new ArrayList<>();
    ArrayList<Contacts> recordedContacts=new ArrayList<>();
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    public Incomming() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.incoming_fragment,container,false);
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
        recyclerAdapter=new IncommingAdapter();
        recyclerView.setAdapter(recyclerAdapter);
        Bundle bundle;
        bundle=getArguments();
        recordings=bundle.getStringArrayList("RECORDING");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ctx.checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            recordedContacts=ContactProvider.getCallList(view.getContext(),recordings,"IN");
            recyclerAdapter.notifyDataSetChanged();
        }

        recyclerAdapter.setContacts(recordedContacts);
        recyclerAdapter.setListener(new IncommingAdapter.itemClickListener() {
            @Override
            public void onClick(View v, int position) {
                ArrayList<String> records=ContactProvider.getRecordingList(v.getContext(),recordings,"IN");
                Contacts contacts1=searchPeople.get(position);
                if(mensu){
                    if(Build.VERSION.SDK_INT>18){
                        ContactProvider.openMaterialSheetDialog(getLayoutInflater(),position,records.get(integers.get(position)),contacts1);
                    }else{
                        ContactProvider.showDialog(v.getContext(),records.get(integers.get(position)),contacts1);
                    }
                }else {
                    if(Build.VERSION.SDK_INT>18){
                        ContactProvider.openMaterialSheetDialog(getLayoutInflater(),position,records.get(position),contacts1);
                    }else{
                        ContactProvider.showDialog(v.getContext(),records.get(position),contacts1);
                    }
                }
                ContactProvider.setItemrefresh(new ContactProvider.refresh() {
                    @Override
                    public void refreshList(boolean var) {
                        if(var)
                            recyclerAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        MainActivity.setQueylistener2(new MainActivity.querySearch2() {
            @Override
            public void Search_name2(String name) {
                ArrayList<Contacts> records=new ArrayList<Contacts>();
                DatabaseHelper databaseHelper=new DatabaseHelper(ctx);
                records=databaseHelper.AllContacts();
                if(name.length()>1){
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
                            searchPeople.add(contacts);
                            integers.add(temp);
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
                showContacts();
            } else {
                Toast.makeText(getContext(), "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showContacts() {
        recordedContacts=ContactProvider.getCallList(getContext(),recordings,"IN");
        recyclerAdapter.notifyDataSetChanged();
    }
}
