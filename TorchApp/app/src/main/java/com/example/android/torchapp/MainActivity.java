package com.example.android.torchapp;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


    private TextView txt_message;
    static Camera camera = null;
    Camera.Parameters parameters;
    boolean isFlash = false;
    boolean isOn = false;
    private SensorManager mSensorManager;
    private Sensor mLightSensor = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_message = (TextView)findViewById(R.id.textView);
        if(getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH));
        {
            camera = camera.open();
            parameters = camera.getParameters();
            isFlash = true;
        }

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (mLightSensor == null)
        {
            txt_message.setText("No light found");
        }
        else
            {
                float max =  mLightSensor.getMaximumRange();
            //Get Maximum Value From Light sensor
                String max_value = String.valueOf(max);
                txt_message.setText("Maximum Value : " + max_value);
                mSensorManager.registerListener(this,mLightSensor,SensorManager.SENSOR_DELAY_NORMAL);
        }

    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if( event.sensor.getType() == Sensor.TYPE_LIGHT)
        {

            float currentLight = event.values[0];
            if(currentLight<10)
            {
                txt_message = (TextView)findViewById(R.id.textView);
                txt_message.setText("Sensor value : " + String.valueOf(currentLight));

                if(!isOn)
                {
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(parameters);
                    camera.startPreview();
                    isOn = true;
                }

                else
                {
                   // Toast.makeText(MainActivity.this, "Already On", Toast.LENGTH_SHORT).show();
                }

            }


            else
                {
                    txt_message = (TextView)findViewById(R.id.textView);
                    txt_message.setText("Sensor value : " + String.valueOf(currentLight));
                    if(isOn)
                    {
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        camera.setParameters(parameters);
                        camera.stopPreview();
                        isOn = false;
                    }
            }

        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        // register this class as a listener for the lightSensor
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

}
