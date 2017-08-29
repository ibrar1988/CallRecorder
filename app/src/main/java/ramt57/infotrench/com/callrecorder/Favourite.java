package ramt57.infotrench.com.callrecorder;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;

import ramt57.infotrench.com.callrecorder.SqliteDatabase.DatabaseHelper;
import ramt57.infotrench.com.callrecorder.adapter.RecyclerAdapter;
import ramt57.infotrench.com.callrecorder.contacts.ContactProvider;
import ramt57.infotrench.com.callrecorder.listener.RecyclerViewTouchListener;
import ramt57.infotrench.com.callrecorder.pojo_classes.Contacts;

/**
 * Created by sandhya on 29-Aug-17.
 */

public class Favourite  extends AppCompatActivity{
    RecyclerAdapter recyclerAdapter;
    RecyclerView recyclerView;
    ArrayList<Contacts> allContactList=new ArrayList<>();
    ArrayList<String> recording=new ArrayList<>();
    ArrayList<Contacts> recordedContacts=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_blank);
        init();
        DatabaseHelper db=new DatabaseHelper(getApplicationContext());
        recordedContacts=db.getAllContacts();
        recyclerAdapter.setContacts(recordedContacts);
        recyclerAdapter.notifyDataSetChanged();
    }
    private void init() {
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getApplicationContext())
                        .color(Color.parseColor("#dadde2"))
                        .sizeResId(R.dimen.divider)
                        .marginResId(R.dimen.leftmargin, R.dimen.rightmargin)
                        .build());
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter=new RecyclerAdapter();
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), recyclerView, new RecyclerAdapter.itemClickListener() {
            @Override
            public void onClick(View view, int position) {
                ArrayList<String> records= ContactProvider.getRecordingList(view.getContext(),recording,"");
                Contacts contacts=recordedContacts.get(position);
                ContactProvider.openMaterialSheetDialog(getLayoutInflater(),position,records.get(position),contacts);
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
