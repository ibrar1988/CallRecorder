package ramt57.infotrench.com.callrecorder.BroadcastReciver;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Date;

import ramt57.infotrench.com.callrecorder.SqliteDatabase.DatabaseHelper;
import ramt57.infotrench.com.callrecorder.contacts.ContactProvider;
import ramt57.infotrench.com.callrecorder.pojo_classes.Contacts;
import ramt57.infotrench.com.callrecorder.utils.StringUtils;

/**
 * Created by sandhya on 22-Aug-17.
 */

public class ExtendedReciver extends MyReceiver{
    String formated_number;
    @Override
    protected void onIncomingCallStarted(Context ctx, String number, Date start) {
       //incoming call ringing
    }
    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        //out going call started
        formated_number= StringUtils.prepareContacts(ctx,number);
//        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(ctx);
//        boolean b=SP.getBoolean("STATE",true);
        SharedPreferences pref=ctx.getSharedPreferences("TOGGLE",Context.MODE_PRIVATE);
        boolean b=pref.getBoolean("STATE",true);
        if (b&&ContactProvider.checkContactToRecord(ctx,number)){
            startRecord(formated_number+"__"+ ContactProvider.getCurrentTimeStamp()+"__"+"OUT__2");
            addtoDatabase(ctx,formated_number);
            if(getnotifysetting()){
                ContactProvider.sendnotification(ctx);
            }
        }
    }
    @Override
    protected void onIncomingCallAnswered(Context ctx, String number, Date start) {
        //incoming call answered
        formated_number= StringUtils.prepareContacts(ctx,number);
//        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(ctx);
//        boolean b=SP.getBoolean("STATE",true);
        SharedPreferences pref=ctx.getSharedPreferences("TOGGLE",Context.MODE_PRIVATE);
        boolean b=pref.getBoolean("STATE",true);
        if(b&&ContactProvider.checkContactToRecord(ctx,number)){
            startRecord(formated_number+"__"+ContactProvider.getCurrentTimeStamp()+"__"+"IN__2");
            addtoDatabase(ctx,formated_number);
            //
            if(getnotifysetting()){
                ContactProvider.sendnotification(ctx);
            }
        }
    }
    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        //incoming call ended
//        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(ctx);
//        boolean b=SP.getBoolean("STATE",true);
        SharedPreferences pref=ctx.getSharedPreferences("TOGGLE",Context.MODE_PRIVATE);
        boolean b=pref.getBoolean("STATE",true);
        if(b&&ContactProvider.checkContactToRecord(ctx,number)){
            stopRecording();
        }
        NotificationManager notificationManager=(NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
        //outgoing call ended
//        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(ctx);
//        boolean b=SP.getBoolean("STATE",true);
        SharedPreferences pref=ctx.getSharedPreferences("TOGGLE",Context.MODE_PRIVATE);
        boolean b=pref.getBoolean("STATE",true);
        if(b&&ContactProvider.checkContactToRecord(ctx,number)){
            stopRecording();
        }
        NotificationManager notificationManager=(NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
        //miss call
    }

    public void addtoDatabase(Context ctx,String number){
        DatabaseHelper db=new DatabaseHelper(ctx);
        if(db.isContact(number).getNumber()!=null){

        }else{
            Contacts contacts=new Contacts();
            contacts.setFav(0);
            contacts.setState(0);
            contacts.setNumber(number);
            db.addContact(contacts);
        }
    }
    private boolean getnotifysetting(){
       SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean("NOTIFY",true);
    }
}

