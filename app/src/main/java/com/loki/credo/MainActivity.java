package com.loki.credo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    TextView caution;
    Button startBtn, mapBtn;
    TelephonyManager mTelephonyManager;
    Animation mAnimBtn, mAnimText, mAnimPara;

    private PhoneCallListener mPhoneCallListener;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeEventManager mShakeEventManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        caution = findViewById(R.id.caution);
        startBtn = findViewById(R.id.callBtn);
        mapBtn = findViewById(R.id.mapBtn);

        Typeface mLight = Typeface.createFromAsset(getAssets(), "fonts/MLight.ttf");
        Typeface mMedium = Typeface.createFromAsset(getAssets(), "fonts/MMedium.ttf");

        mAnimBtn = AnimationUtils.loadAnimation(this, R.anim.anim1);
        mAnimText = AnimationUtils.loadAnimation(this, R.anim.anim2);
        mAnimPara = AnimationUtils.loadAnimation(this, R.anim.anim3);

        caution.setTypeface(mLight);
        startBtn.setTypeface(mMedium);
        mapBtn.setTypeface(mMedium);

        startBtn.startAnimation(mAnimBtn);
        mapBtn.startAnimation(mAnimBtn);
        caution.startAnimation(mAnimPara);


        //telephony manager
        mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);


        if(isTelephonyEnabled()) {
            checkForPhonePermission();

            //register phone listener to monitor phone activity
            mPhoneCallListener = new PhoneCallListener();
            mTelephonyManager.listen(mPhoneCallListener, PhoneStateListener.LISTEN_CALL_STATE);
        }

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeEventManager = new ShakeEventManager();
        mShakeEventManager.setOnShakeListener(new ShakeEventManager.OnShakeListener() {

            @Override
            public void onShake(int count) {
                /*
                 * The following method, "handleShakeEvent(count):" is a stub //
                 * method you would use to setup whatever you want done once the
                 * device has been shook.
                 */
                callNumber();
                Toast.makeText(MainActivity.this, "Shaked!!!", Toast.LENGTH_SHORT).show();
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callNumber();
            }
        });

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

    }

    private boolean isTelephonyEnabled() {
        if (mTelephonyManager != null ) {

            if (mTelephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY) {
                return true;
            }
        }

        return false;
    }

    public void callNumber() {

        String code = "911";

        String phoneNumber = String.format("tel: %s",code);

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(phoneNumber));


        if (callIntent.resolveActivity(getPackageManager()) != null) {
            checkForPhonePermission();
            startActivity(callIntent);
        }
    }

    private void checkForPhonePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // Permission not yet granted. Use requestPermissions().
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (permissions[0].equalsIgnoreCase(Manifest.permission.CALL_PHONE) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, "Permission not granted", Toast.LENGTH_LONG).show();
                    //disableCallButton();
                }

            }
        }
    }

    //phone call listener
    private class PhoneCallListener extends PhoneStateListener {

        private boolean returningFromOffHook = false;

        @Override
        public void onCallStateChanged(int state, String phoneNumber) {

            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    // Incoming call is ringing
                    Toast.makeText(MainActivity.this, "There is an incoming call", Toast.LENGTH_SHORT).show();
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:
                    // phone call is active
                    returningFromOffHook = true;
                    break;

                case TelephonyManager.CALL_STATE_IDLE:
                    //phone is idle before and after call for
                    break;

                default:
                    //must be an error
                    Toast.makeText(MainActivity.this, "Sorry, an error occurred", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    //destroy phone listener after call
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (isTelephonyEnabled()) {
            mTelephonyManager.listen(mPhoneCallListener, PhoneStateListener.LISTEN_NONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeEventManager, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeEventManager);
        super.onPause();
    }


}