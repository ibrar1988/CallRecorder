package ramt57.infotrench.com.callrecorder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class Splash_Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences SP1= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean b1=SP1.getBoolean("LOCK",false);
        if(b1){
            Intent intent=new Intent(getApplicationContext(),PinLock.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
