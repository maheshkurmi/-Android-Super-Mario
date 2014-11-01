package com.shikhar.androidgames.mario.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.view.KeyEvent;

import com.shikhar.androidgames.framework.FileIO;
import com.shikhar.androidgames.mario.objects.mario.Mario;
import com.shikhar.androidgames.mario.preferences.PreferenceConstants;


public class Settings {
	private static final float ORIENTATION_DEAD_ZONE_MIN = 0.03f;
	private static final float ORIENTATION_DEAD_ZONE_MAX = 0.1f;
	private static final float ORIENTATION_DEAD_ZONE_SCALE = 0.75f;

	 // Raw trackball input is filtered by this value. Increasing it will 
    // make the control more twitchy, while decreasing it will make the control more precise.
    private final static float ROLL_FILTER = 0.4f;
    private final static float ROLL_DECAY = 8.0f;
	
    private final static float KEY_FILTER = 0.25f;
    private final static float SLIDER_FILTER = 0.25f;


    public static boolean soundEnabled = true;
    public static boolean musicEnabled = true;
    
    public static int soundVolume = 50;
    public static int musicVolume = 50;
    public static int tiltSensitivity = 50;
    public static int movementSensitivity = 50;
    
    public static int mLeftKeyCode = KeyEvent.KEYCODE_DPAD_LEFT;
    public static int mRightKeyCode = KeyEvent.KEYCODE_DPAD_RIGHT;
    public static int mJumpKeyCode = KeyEvent.KEYCODE_SPACE;
    public static int mAttackKeyCode = KeyEvent.KEYCODE_SHIFT_LEFT;
    
    public static  boolean mUseClickButtonForAttack = true;
    public static  boolean mUseOrientationForMovement = false;
    public static  boolean mUseOnScreenControls = false;

    public static int[] highScores = new int[] { 0, 0, 0, 0, 0 ,0,0,0,0,0};
    public  static int[] recodTimes =new int[] { 10000, 10000, 10000, 10000, 10000 ,10000,10000,10000,10000,10000};
    
    public final static String file = ".supermario";

    private static int score=0;
	private static int lives=0;
	private static int time=0;
	private static int coins=0;
	private static int levelsUnlocked=0;
	private static int worldsUnlocked=0;
	private static Context context;
    private static Mario mario;
	public static void loadPreferences(Context c){
		context=c;
		SharedPreferences prefs=context.getSharedPreferences(PreferenceConstants.PREFERENCE_MAIN_NAME, Context.MODE_PRIVATE);
		soundEnabled = prefs.getBoolean(PreferenceConstants.PREFERENCE_SOUND_ENABLED, true);
	        musicEnabled = prefs.getBoolean(PreferenceConstants.PREFERENCE_MUSIC_ENABLED, true);
	        soundVolume = prefs.getInt(PreferenceConstants.PREFERENCE_SOUND_VOLUME,50);
	        musicVolume = prefs.getInt(PreferenceConstants.PREFERENCE_MUSIC_VOLUME, 50);
	        MarioSoundManager.setMusicVolume(musicVolume/100.0f);
	        MarioSoundManager.setSoundVolume(soundVolume/100.0f);
	        MarioSoundManager.setMusicEnabled(musicEnabled);
	        MarioSoundManager.setSoundEnabled(soundEnabled);

	        final boolean safeMode = prefs.getBoolean(PreferenceConstants.PREFERENCE_SAFE_MODE, false);
	        mUseClickButtonForAttack = prefs.getBoolean(PreferenceConstants.PREFERENCE_CLICK_ATTACK, true);
	        mUseOrientationForMovement = prefs.getBoolean(PreferenceConstants.PREFERENCE_TILT_CONTROLS, false);
	        tiltSensitivity = prefs.getInt(PreferenceConstants.PREFERENCE_TILT_SENSITIVITY, 50);
	        accelerometerSenseFactor=0.1f+tiltSensitivity/55.0f;
	        movementSensitivity = prefs.getInt(PreferenceConstants.PREFERENCE_MOVEMENT_SENSITIVITY, 100);
	        mUseOnScreenControls= prefs.getBoolean(PreferenceConstants.PREFERENCE_SCREEN_CONTROLS, false);

	        mLeftKeyCode = prefs.getInt(PreferenceConstants.PREFERENCE_LEFT_KEY, KeyEvent.KEYCODE_DPAD_LEFT);
	        mRightKeyCode = prefs.getInt(PreferenceConstants.PREFERENCE_RIGHT_KEY, KeyEvent.KEYCODE_DPAD_RIGHT);
	        mJumpKeyCode = prefs.getInt(PreferenceConstants.PREFERENCE_JUMP_KEY, KeyEvent.KEYCODE_SPACE);
	        mAttackKeyCode = prefs.getInt(PreferenceConstants.PREFERENCE_ATTACK_KEY, KeyEvent.KEYCODE_SHIFT_LEFT);
	        
	        SharedPreferences scorePrefs=context.getSharedPreferences(PreferenceConstants.PREFERENCE_SCORE_NAME, Context.MODE_PRIVATE);
	        for (int i=0; i<highScores.length;i++){
	        	 highScores[i]=scorePrefs.getInt("levelScore"+i, 0);
	        	 recodTimes[i]=scorePrefs.getInt("levelTime"+i, 10000);
	        }
	 	}
	
	public static int getWorldsUnlocked() {
		return worldsUnlocked;
	}

	public static void setWorldsUnlocked(int worldsUnlocked) {
		Settings.worldsUnlocked = worldsUnlocked;
	}

	public static int level = 1;
	   public static int world = 1;
	   
	public static final int COIN_BONUS=100;
	public static final int GOOMBA_BONUS=300;
	public static final int KOOPA_BONUS=500;
	public static final int LIFE_BONUS=1000;

	public static float accelerometerSenseFactor=1;
 

	public static boolean addHighScore(int world, int level,int newHighScore) {
		int i=world*3+level-4;
		if (highScores[i]<newHighScore){
			highScores[i]=newHighScore;
			SharedPreferences sharedPref = context.getSharedPreferences(PreferenceConstants.PREFERENCE_SCORE_NAME, Context.MODE_PRIVATE);
			Editor editor = sharedPref.edit();
			editor.putInt("levelScore"+i, highScores[i]);
			editor.commit();
			return true;
		}
		return false;
	}
    
	public static boolean addRecordTime(int world, int level,int newTime) {
		int i=world*3+level-4;
		if (recodTimes[i]>newTime){
			recodTimes[i]=newTime;
			SharedPreferences sharedPref = context.getSharedPreferences(PreferenceConstants.PREFERENCE_SCORE_NAME, Context.MODE_PRIVATE);
			Editor editor = sharedPref.edit();
			editor.putInt("levelTime"+i, recodTimes[i]);
			editor.commit();
			return true;
		}
		return false;
	}
    
	
	public static void saveScores(){
		SharedPreferences sharedPref = context.getSharedPreferences("highScores", Context.MODE_PRIVATE);
		Editor editor = sharedPref.edit();
		 for (int i=0; i<highScores.length;i++){
			 editor.putInt("levelScore"+i, highScores[i]);
			 editor.putInt("levelTime"+i, recodTimes[i]);
	     }
		 editor.commit();
	}
	
	public static void setSensitivity(int value){
		tiltSensitivity=value;
		accelerometerSenseFactor=0.1f+tiltSensitivity/55.0f;
	}

	public static int getHighScore(int world, int level) {
		return highScores[world*3+level-4];
	}
	public static int getRecordTime(int world, int level) {
		return recodTimes[world*3+level-4];
	}
	
	public static int getTime() {
		return time;
	}

	public static void setTime(int time) {
		Settings.time = time;
	}

	public static int getCoins() {
		return coins;
	}

	public static void addCoins(int coins) {
		Settings.coins+= coins;
		if (Settings.coins>=50 && mario!=null){
			if (mario.getHealth()<3){
				Settings.coins-=50;
				mario.setHealth(mario.getHealth()+1);
				Settings.setLives(mario.getHealth());
			}
		}
	}
   
	public static int getScore() {
		return score;
	}

	public static void addScore(int newScore) {
		Settings.score += newScore;
	}

	public static void setLives(int n) {
		if (lives>=0)lives=n;
	}

	public static int getLives() {
		return Math.max(0, lives);
	}

	public static void resetScores() {
		Settings.score = 0;
		//Settings.coins = 0;
	}

	public static int getLevelsUnlocked() {
		return levelsUnlocked;
	}

	public static void setLevelsUnlocked(int levelsUnlocked) {
		Settings.levelsUnlocked = levelsUnlocked;
	}
		
	
	public static boolean hasAccelerometer(Context context) {
		SensorManager sensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		Sensor accelerometerSensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		if (accelerometerSensor != null) {
			return true;
		}
		return false;
	}

	public static Mario getPayer() {
		return mario;
	}

	public static void setPlayer(Mario mario) {
		Settings.mario = mario;
	}
}
