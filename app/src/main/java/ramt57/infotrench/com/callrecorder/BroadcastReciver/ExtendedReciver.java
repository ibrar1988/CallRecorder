package ramt57.infotrench.com.callrecorder.BroadcastReciver;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ramt57.infotrench.com.callrecorder.SqliteDatabase.DatabaseHelper;
import ramt57.infotrench.com.callrecorder.contacts.ContactProvider;
import ramt57.infotrench.com.callrecorder.fragments.Incomming;
import ramt57.infotrench.com.callrecorder.pojo_classes.Contacts;
import ramt57.infotrench.com.callrecorder.utils.StringUtils;

/**
 * Created by sandhya on 22-Aug-17.
 */

public class ExtendedReciver extends MyReceiver{
    String formated_number;
   static refreshlist refreshlist;
    public static void setListener(refreshlist refresh){
      refreshlist=refresh;
    }

    @Override
    protected void onIncomingCallStarted(Context ctx, String number, Date start) {
       //incoming call ringing
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        //out going call started
        formated_number= StringUtils.prepareContacts(ctx,number+"");
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(ctx);
        boolean b=SP.getBoolean("STATE",true);
        if (b&&ContactProvider.checkContactToRecord(ctx,number)){
            startRecord(formated_number+"__"+ ContactProvider.getCurrentTimeStamp()+"__"+"OUT__2");
            addtoDatabase(ctx,number);
            ContactProvider.sendnotification(ctx);
        }
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        //incoming call ended
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(ctx);
        boolean b=SP.getBoolean("STATE",true);
        if(b){
            stopRecording();
        }
        NotificationManager notificationManager=(NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        refreshlist.notify(true);
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
        //outgoing call ended
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(ctx);
        boolean b=SP.getBoolean("STATE",true);
        if(b){
            stopRecording();
        }
        NotificationManager notificationManager=(NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        refreshlist.notify(true);
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
        //miss call
    }

    @Override
    protected void onIncomingCallAnswered(Context ctx, String number, Date start) {
        //incoming call answered
        formated_number= StringUtils.prepareContacts(ctx,number);
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(ctx);
        boolean b=SP.getBoolean("STATE",true);
        if(b&&ContactProvider.checkContactToRecord(ctx,number)){
            startRecord(formated_number+"__"+ContactProvider.getCurrentTimeStamp()+"__"+"IN__2");
            addtoDatabase(ctx,number);
            ContactProvider.sendnotification(ctx);
        }
    }

    public void addtoDatabase(Context ctx,String number){
        DatabaseHelper db=new DatabaseHelper(ctx);
        if(db.isContact(number).getNumber()!=null){

        }else{
            Contacts contacts=new Contacts();
            contacts.setFav(0);
            contacts.setState(0);
            contacts.setNumber(StringUtils.prepareContacts(ctx,number));
            db.addContact(contacts);
        }
    }
    public interface  refreshlist{
        public void notify(boolean vaar);
    }

}

