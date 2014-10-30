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

	/**
	 * The constructor takes a Context, from which it gets a SensorManager
	 * instance to set up the event listening.
	 * 
	 * @param context
	 */
	public AccelerometerHandler(Context context) {
		SensorManager manager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		if (manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
			Sensor accelerometer = manager.getSensorList(
					Sensor.TYPE_ACCELEROMETER).get(0);
			manager.registerListener(this, accelerometer,
					SensorManager.SENSOR_DELAY_GAME);
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
}