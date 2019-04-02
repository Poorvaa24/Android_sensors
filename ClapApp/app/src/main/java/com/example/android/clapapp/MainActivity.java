package com.example.android.clapapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mProximity;
    private MediaPlayer mp;

    private static final int SENSOR_SENSITIVITY = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);


    }


    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void stopMedia()
    {
        try {
            mp.reset();
            mp.prepare();
            mp.stop();
            mp.release();
            mp=null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
                //near

                mp = MediaPlayer.create(MainActivity.this, R.raw.clapp);
                mp.start();
                Toast.makeText(getApplicationContext(), "Playing", Toast.LENGTH_SHORT).show();



            } else {
                //far


                stopMedia();


                Toast.makeText(getApplicationContext(), "Stop Playing", Toast.LENGTH_SHORT).show();


            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onStop()
    {

        super.onStop();
        if (mp.isPlaying()) {

            stopMedia();
        }
    }
}
