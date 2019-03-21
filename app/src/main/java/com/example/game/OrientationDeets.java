package com.example.game;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.provider.SyncStateContract;

public class OrientationDeets implements SensorEventListener {
    private SensorManager cool;
    private Sensor accel;
    private Sensor mag;

    private Context tem;

    private float[] accelOut;
    private float[] magOut;

    private float[] orientation;
    public float[] getOrientation() {
        return orientation;
    }

    private float[] initOrient;

    public float[] getInitOrient() {
        return initOrient;
    }

    public void initialise(){
        initOrient = null;
    }

    public OrientationDeets(){
        cool = (SensorManager) tem.getSystemService(Context.SENSOR_SERVICE);
        accel = cool.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mag = cool.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void keepLog(){
        cool.registerListener(this,accel,SensorManager.SENSOR_DELAY_GAME);
        cool.registerListener(this,mag,SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelOut = event.values;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magOut = event.values;
        }
        if (accelOut != null && magOut != null){
            float[] rotationMatrix = new float[9];
            float[] inclinationMatrix = new float[9];
            boolean check = SensorManager.getRotationMatrix(rotationMatrix,inclinationMatrix,accelOut,magOut);
            if (check){
                SensorManager.getOrientation(rotationMatrix,orientation);
                if (initOrient == null){
                    initOrient = new float[orientation.length];
                    System.arraycopy(orientation,0,initOrient,0,orientation.length);
                }
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
