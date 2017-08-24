package ramt57.infotrench.com.callrecorder.BroadcastReciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by sandhya on 22-Aug-17.
 */

public abstract class MyReceiver extends BroadcastReceiver {
    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming;
    private static String savedNumber;
    static MediaRecorder recorder= new MediaRecorder();;
    static File audiofile;
    public static boolean record = false;


    public MyReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
        } else {
            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            int state = 0;
            if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                state = TelephonyManager.CALL_STATE_IDLE;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                state = TelephonyManager.CALL_STATE_OFFHOOK;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                state = TelephonyManager.CALL_STATE_RINGING;
            }

            onCallStateChanged(context, state, number);
        }
    }

    protected void onIncomingCallStarted(Context ctx, String number, Date start) {
    }

    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
    }

    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
    }

    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
    }

    protected void onMissedCall(Context ctx, String number, Date start) {
    }

    protected void onIncomingCallAnswered(Context context, String savedNumber, Date callStartTime) {
    }

    public void onCallStateChanged(Context context, int state, String number) {
        if (lastState == state) {
            //No change
            return;
        }
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = new Date();
                savedNumber = number;
                onIncomingCallStarted(context, number, callStartTime);
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:

                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    Log.d("recived", savedNumber+"");
                    isIncoming = false;
                    callStartTime = new Date();
                    onOutgoingCallStarted(context, savedNumber, callStartTime);
//
                } else {
                    Log.d("recived incoming",savedNumber+"");
                    isIncoming = true;
                    callStartTime = new Date();
                    onIncomingCallAnswered(context, savedNumber, callStartTime);
                }

                break;
            case TelephonyManager.CALL_STATE_IDLE:
                //call ended
                Log.d("call ended", savedNumber+"");
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    // a miss call
                    onMissedCall(context, savedNumber, callStartTime);
                } else if (isIncoming) {
                    onIncomingCallEnded(context, savedNumber, callStartTime, new Date());
                    Log.d("incoming ended", savedNumber+"");
                    isIncoming = false;
                } else {
                    onOutgoingCallEnded(context, savedNumber, callStartTime, new Date());
                    Log.d("out going ended", savedNumber+"");
                }
                break;
        }
        lastState = state;
    }

    public  void startRecord(String name){

        Log.d("Msg",name);
        File sampleDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/CallRecorder");
        if (!sampleDir.exists()) {
            sampleDir.mkdirs();
        }
        String file_name = name;
        try {
            audiofile = File.createTempFile(file_name, ".amr", sampleDir);

        } catch (IOException e) {
            e.printStackTrace();
        }
//        recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
        recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(audiofile.getAbsolutePath());
        try {
            recorder.prepare();
            recorder.start();
            record = true;

        } catch (IllegalStateException e) {
            e.printStackTrace();
            Log.d("Magic", "call me");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecording() {
        if (record){
            recorder.stop();
            Log.d("Stop", "Stop record");
        }
    }
}


