package ramt57.infotrench.com.callrecorder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.chaos.view.PinView;

public class NewPinLock extends AppCompatActivity {
    PinLockView mPinLockView;
    IndicatorDots mIndicatorDots;
    String TAG="JUST";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences sharedPreferences=getSharedPreferences("LOCK",MODE_PRIVATE);
        final String pin=sharedPreferences.getString("PIN","");
        boolean sets=getIntent().getBooleanExtra("SET",false);
        if(pin.isEmpty()){
            setContentView(R.layout.setuppinlayout);
            //code for setting new password
            Button set,cancel;
            final PinView pinEntry = (PinView) findViewById(R.id.txt_pin_entry);
            final PinView pinEntry2 = (PinView) findViewById(R.id.txt_pin_entry2);
            set=(Button)findViewById(R.id.set);
            cancel=(Button)findViewById(R.id.cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pinEntry.setText(null);
                    pinEntry2.setText(null);
                }
            });
            set.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(pinEntry.getText().toString().equals(pinEntry2.getText().toString())){
                        if(pinEntry.length()==4){
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("PIN",pinEntry.getText().toString());
                            editor.apply();
                            Toast.makeText(NewPinLock.this, "password set", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            intent.putExtra("AUTH",true);
                            finish();
                            startActivity(intent);
                        }else {
                            pinEntry.setError("Enter 4 digit pin");
                        }
                    }else{
                        pinEntry2.setError("pin not match");
                        pinEntry2.setText(null);
                    }
                }
            });
        }else if(sets) {
            setContentView(R.layout.setuppinlayout);
            //code for setting new password
            Button set,cancel;
            final PinView pinEntry = (PinView) findViewById(R.id.txt_pin_entry);
            final PinView pinEntry2 = (PinView) findViewById(R.id.txt_pin_entry2);
            set=(Button)findViewById(R.id.set);
            cancel=(Button)findViewById(R.id.cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pinEntry.setText(null);
                    pinEntry2.setText(null);
                }
            });
            pinEntry2.setVisibility(View.VISIBLE);
            set.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(pinEntry.getText().toString().equals(pinEntry2.getText().toString())){
                        if(pinEntry.length()==4){
                            //write to shared prefrence
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("PIN",pinEntry.getText().toString());
                            editor.apply();
                            Toast.makeText(NewPinLock.this, "password changed", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            intent.putExtra("AUTH",true);
                            finish();
                            startActivity(intent);
                        }else {
                            pinEntry.setError("Enter 4 digit pin");
                        }
                    }else{
                        pinEntry2.setError("pin not match");
                        pinEntry2.setText(null);
                    }
                }
            });
        } else{
            //code for entering or setting password
            setContentView(R.layout.pinlocknew);
                SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                boolean b = SP.getBoolean("LOCK", false);
                if (b) {
                    //authenticate user
                    mPinLockView = (PinLockView) findViewById(R.id.pin_lock_view);
                    mIndicatorDots = (IndicatorDots) findViewById(R.id.indicator_dots);
                    mPinLockView.attachIndicatorDots(mIndicatorDots);
                    mPinLockView.setPinLockListener(new PinLockListener() {
                        @Override
                        public void onComplete(String pin1) {
//                            Log.d(TAG, "Pin complete: " + pin);
                            if (pin.toString().equals(pin1)) {
                                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                intent.putExtra("AUTH",true);
                                finish();
                                startActivity(intent);
                            } else {
                               mPinLockView.resetPinLockView();
                                Toast.makeText(NewPinLock.this, "wrong pin", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onEmpty() {
//                            Log.d(TAG, "Pin empty");
                        }

                        @Override
                        public void onPinChange(int pinLength, String intermediatePin) {
//                            Log.d(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
                        }
                    });

                } else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("AUTH", true);
                    finish();
                    startActivity(intent);
                }
            }
        }
}
