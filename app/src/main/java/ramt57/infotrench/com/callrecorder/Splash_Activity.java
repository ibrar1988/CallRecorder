package ramt57.infotrench.com.callrecorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Splash_Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
