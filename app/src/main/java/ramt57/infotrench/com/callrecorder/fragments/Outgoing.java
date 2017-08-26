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
import android.widget.Toast;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;

import ramt57.infotrench.com.callrecorder.R;
import ramt57.infotrench.com.callrecorder.adapter.RecyclerAdapter;
import ramt57.infotrench.com.callrecorder.contacts.ContactProvider;
import ramt57.infotrench.com.callrecorder.listener.RecyclerViewTouchListener;
import ramt57.infotrench.com.callrecorder.pojo_classes.Contacts;

/**
 * A simple {@link Fragment} subclass.
 */
public class Outgoing extends Fragment {
    private RecyclerAdapter recyclerAdapter;
    RecyclerView recyclerView;
    ArrayList<Contacts> allContactList=new ArrayList<>();
    ArrayList<String> recording2=new ArrayList<>();
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
                ContactProvider.openMaterialSheetDialog(getLayoutInflater(),position, records.get(position));
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(view.getContext(), recordedContacts.get(position).getNumber() + " is long pressed!", Toast.LENGTH_SHORT).show();
            }
        }));
        return view;
    }

}
