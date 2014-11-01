package com.shikhar.androidgames.framework.input;

import android.content.Context;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * The class stores three members by holding the acceleration on each of the
 * three accelerometers’ axes.Note that if no accelerometer is installed, the
 * handler will happily return zero acceleration on all axes throughout its
 * life. Therefore, we don’t need any extra error-checking or exception-throwing
 * code.
 */

public class AccelerometerHandler implements SensorEventListener {
	float accelX;
	float accelY;
	float accelZ;
	 private  SensorManager mSensorManager;
     private  Sensor mAccelerometer;
     private boolean enabled=false;
	/**
	 * The constructor takes a Context, from which it gets a SensorManager
	 * instance to set up the event listening.
	 * 
	 * @param context
	 */
	public AccelerometerHandler(Context context) {
		mSensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		if (mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
			 mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			if (mAccelerometer!=null) enabled=true;
		}
        
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// nothing to do here
	}

	public void onSensorChanged(SensorEvent event) {
		accelX = event.values[0];
		accelY = event.values[1];
		accelZ = event.values[2];
	}

	public float getAccelX() {
		return accelX;
	}

	public float getAccelY() {
		return accelY;
	}

	public float getAccelZ() {
		return accelZ;
	}
	
	protected void registerListener() {
		enabled=mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

	protected void unRegisterListener() {
	   mSensorManager.unregisterListener(this);
	   enabled=false;
	}

	public boolean isEnabled() {
		return enabled;
	}

}