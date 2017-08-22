package ramt57.infotrench.com.callrecorder.BroadcastReciver;

import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ramt57.infotrench.com.callrecorder.fragments.Incomming;

/**
 * Created by sandhya on 22-Aug-17.
 */

public class ExtendedReciver extends MyReceiver{

    @Override
    protected void onIncomingCallStarted(Context ctx, String number, Date start) {
        Log.d("Incoming",number+" --  "+start);
//        Incomming incomming=new Incomming();
//        Bundle bundle=new Bundle();
//        bundle.putString("NUMBER",number);
//        incomming.setArguments(bundle);

//        recordstarted = true;
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        Log.d("OutGoing",number+" --  "+start);

    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        Log.d("IncomingEnded",number+" --  "+start);
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
        Log.d("OutGoing Ended",number+" --  "+start);
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
        Log.d("Missed call",number+" --  "+start);
    }

    @Override
    protected void onIncomingCallAnswered(Context context, String number, Date start) {
        Log.d("Incomming Answered",number+" --  "+start);
    }
}
