package ramt57.infotrench.com.callrecorder.fragments;


import android.graphics.Color;
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

import ramt57.infotrench.com.callrecorder.R;
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
    public AllFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_blank,container,false);
        init(view);

        Bundle bundle;
        bundle=getArguments();
        recording=bundle.getStringArrayList("RECORDING");
        allContactList= ContactProvider.getContacts(view.getContext());
        boolean hascontact=false;
        recordedContacts=ContactProvider.getCallList(view.getContext(),recording,"");
        recyclerAdapter.setContacts(recordedContacts);
        recyclerAdapter.notifyDataSetChanged();
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
                ContactProvider.openMaterialSheetDialog(getLayoutInflater(),position);
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(view.getContext(), recordedContacts.get(position).getNumber() + " is long pressed!", Toast.LENGTH_SHORT).show();
            }
        }));
    }

}
