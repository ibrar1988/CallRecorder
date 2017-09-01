package ramt57.infotrench.com.callrecorder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import ramt57.infotrench.com.callrecorder.contacts.ContactProvider;

public class ListenActivity extends AppCompatActivity {
    String number;
    ArrayList<String> recordinglist=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        number=getIntent().getStringExtra("NUMBER");
        String path= ContactProvider.getFolderPath(this);
        File file=new File(path);
        if(!file.exists()){
            //no folder empty data
            file.mkdirs();
        }
        File listfiles[]=file.listFiles();
        if(listfiles!=null){
            for(File list:listfiles){
                recordinglist.add(list.getName());
            }
        }
        if(!recordinglist.isEmpty()){
            for(String s:recordinglist){
                String numb[]=s.split("__");
                if(number.equals(numb[0])){
//                    Toast.makeText(this,s+"",Toast.LENGTH_SHORT).show();
                    ContactProvider.playmusic(this,ContactProvider.getFolderPath(this)+"/" +s);
                }
            }
        }
    }
}
