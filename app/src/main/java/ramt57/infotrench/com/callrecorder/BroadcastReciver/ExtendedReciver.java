package ramt57.infotrench.com.callrecorder.BroadcastReciver;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ramt57.infotrench.com.callrecorder.contacts.ContactProvider;
import ramt57.infotrench.com.callrecorder.fragments.Incomming;
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
        startRecord(formated_number+"__"+ ContactProvider.getCurrentTimeStamp()+"__"+"OUT__2");
        ContactProvider.sendnotification(ctx);

    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        //incoming call ended
        stopRecording();
        NotificationManager notificationManager=(NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
        //outgoing call ended
        stopRecording();
        NotificationManager notificationManager=(NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
        //miss call
    }

    @Override
    protected void onIncomingCallAnswered(Context ctx, String number, Date start) {
        //incoming call answered
        formated_number= StringUtils.prepareContacts(ctx,number);
        startRecord(formated_number+"__"+ContactProvider.getCurrentTimeStamp()+"__"+"IN__2");
        ContactProvider.sendnotification(ctx);
    }



}

