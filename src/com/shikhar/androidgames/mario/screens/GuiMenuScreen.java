package com.shikhar.androidgames.mario.screens;

import java.util.List;

import com.shikhar.androidgames.framework.Game;
import com.shikhar.androidgames.framework.Graphics;
import com.shikhar.androidgames.framework.Input.TouchEvent;
import com.shikhar.androidgames.framework.Screen;
import com.shikhar.androidgames.framework.gfx.AndroidGame;
import com.shikhar.androidgames.framework.gfx.AndroidGraphics;
import com.shikhar.androidgames.framework.input.AndroidInput;
import com.shikhar.androidgames.gui.AndroidButton;
import com.shikhar.androidgames.gui.AndroidPanel;
import com.shikhar.androidgames.gui.AndroidPic;
import com.shikhar.androidgames.gui.AndroidSlider;
import com.shikhar.androidgames.gui.Component;
import com.shikhar.androidgames.gui.ComponentClickListener;
import com.shikhar.androidgames.mario.core.MarioGame;
import com.shikhar.androidgames.mario.core.MarioResourceManager;
import com.shikhar.androidgames.mario.core.R;
import com.shikhar.androidgames.mario.core.Settings;
import com.shikhar.androidgames.mario.preferences.PreferenceConstants;
import com.shikhar.androidgames.mario.preferences.SetPreferencesActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.text.Html;
import android.util.Log;

public class GuiMenuScreen extends Screen {

	AndroidPanel panel;
	AndroidButton btnPlay, btnHighscore, btnOptions, btnHelp, btnAbout;
	AndroidPic marioPic;
	AndroidSlider slider;
	private int width,height;
	private String mSelectedControlsString;
	private Canvas gameCanvas;
	private Bitmap background;
	Bitmap frameBuffer;
	public GuiMenuScreen(Game game) {
		super(game);
		width = game.getScreenWidth();
		height =game.getScreenHeight();
		frameBuffer = ((AndroidGame) game).getBuffer();
		gameCanvas = new Canvas(frameBuffer);
		background=MarioResourceManager.loadImage("backgrounds/smb.png");
		panel = new AndroidPanel(" SUPERMARIO ", 0, 0, width, height);
		panel.setTitleBarheight(36);
		panel.setForeColor(Color.WHITE);
		int tbh = panel.getTitleBarheight()-10;

		btnPlay = new AndroidButton("PLAY", 10, tbh + 10, 140, 35);
		btnHighscore = new AndroidButton("HIGHSCORE", 10, tbh + 60, 140, 35);
		btnOptions = new AndroidButton("OPTIONS", 10, tbh + 110, 140, 35);
		btnHelp = new AndroidButton("ABOUT", 10, tbh + 160, 140, 35);
		btnAbout = new AndroidButton("i", 10, tbh + 170, 50, 35);
		marioPic = new AndroidPic(MarioResourceManager.marioPic, "", width
				- MarioResourceManager.marioPic.getWidth() - 15, tbh + 45,
				MarioResourceManager.marioPic.getWidth(),
				MarioResourceManager.marioPic.getHeight());
		btnPlay.setTextSize(20);
		btnHighscore.setTextSize(20);
		btnOptions.setTextSize(20);
		btnHelp.setTextSize(20);
		btnAbout.setTextSize(20);
		/*
		 * btnPlay.setForeColor(Color.WHITE);
		 * btnHighscore.setForeColor(Color.WHITE);
		 * btnOptions.setForeColor(Color.WHITE);
		 * btnHelp.setForeColor(Color.WHITE);
		 * btnAbout.setForeColor(Color.WHITE);
		 */
		panel.addComponent(btnPlay);
		panel.addComponent(btnHighscore);
		panel.addComponent(btnOptions);
		panel.addComponent(btnHelp);
		//panel.addComponent(btnAbout);
		panel.addComponent(marioPic);
		((MarioGame)game).soundManager.loadMenuMusic();
	}

	@Override
	public void update(float deltaTime) {
		if(((AndroidGame)game).isScreenTransitionActive()) return;
		
		Graphics g = game.getGraphics();
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		
		if (touchEvents ==null || touchEvents.size()==0) return;
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			btnPlay.processEvent(event);
			btnHighscore.processEvent(event);
			btnOptions.processEvent(event);
			btnHelp.processEvent(event);
			btnAbout.processEvent(event);
			
			btnPlay.addListener(new ComponentClickListener() {
				@Override
				public void onClick(Component c) {
					 WorldScreen worldScreen=new WorldScreen(game);
					 configureInputs();
					 ((AndroidGame) game).setScreenWithFade(worldScreen);
					 worldScreen.loadGame();
					 if (GameScreen.getMario()!=null)GameScreen.getMario().resetHealth();
	            	 Log.i("Game","starts");
				}
			});
			
			btnHighscore.addListener(new ComponentClickListener() {
				@Override
				public void onClick(Component c) {
					((AndroidGame) game).setScreenWithFade(new HighScoreScreen(game));
				}
			});
			
			btnOptions.addListener(new ComponentClickListener() {
				@Override
				public void onClick(Component c) {
					//game.setScreen(new GuiOptionScreen(game));
					
				     Intent i = new Intent(((AndroidGame)game).getBaseContext(), SetPreferencesActivity.class);
			            i.putExtra("controlConfig", false);
			            ((AndroidGame)game).startActivity(i); 
				}
			});

			btnHelp.addListener(new ComponentClickListener() {
				@Override
				public void onClick(Component c) {
					Intent i = new Intent(((AndroidGame)game).getBaseContext(), SetPreferencesActivity.class);
                    i.putExtra("moreInfo", true);
                    ((AndroidGame)game).startActivity(i);  
				}
			});
			btnAbout.addListener(new ComponentClickListener() {
				@Override
				public void onClick(Component c) {
					Intent i = new Intent(((AndroidGame)game).getBaseContext(), SetPreferencesActivity.class);
                    i.putExtra("controlConfig", true);
                    ((AndroidGame)game).startActivity(i);  
				}
			});
		}
	}

	@Override
	public void paint(float deltaTime) {

		// game.getGraphics().drawARGB(155, 100, 100,100);
		//Canvas g = ((AndroidGraphics) (game.getGraphics())).getCanvas();
		//g.drawRGB((Color.BLACK & 0xff0000) >> 16, (Color.BLACK & 0xff00) >> 8,
		//		(Color.BLACK & 0xff));
		gameCanvas.drawBitmap(background, null,new Rect(0,0,frameBuffer.getWidth(),frameBuffer.getHeight()), null);	
		gameCanvas.drawBitmap(MarioResourceManager.logo,frameBuffer.getWidth()/2,15, null);	
		panel.draw(gameCanvas, 0, -10);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

	@Override
	public void onBackPressed() {
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	private void configureInputs(){
	    	SharedPreferences prefs = ((AndroidGame)game).getSharedPreferences(PreferenceConstants.PREFERENCE_MAIN_NAME, 0);
	        final int lastVersion = prefs.getInt(PreferenceConstants.PREFERENCE_LAST_VERSION, 0);
	        if (lastVersion == 0) {
	        	// This is the first time the game has been run.  
	        	// Pre-configure the control options to match the device.
	        	// The resource system can tell us what this device has.
	        	// TODO: is there a better way to do this?  Seems like a kind of neat
	        	// way to do custom device profiles.
	        	final String navType = ((AndroidGame)game).getString(R.string.nav_type);
	        	mSelectedControlsString = ((AndroidGame)game).getString(R.string.control_setup_dialog_trackball);
	        	Log.i("Mario", "navType="+navType);
	        	if (navType != null) {
	        		if (navType.equalsIgnoreCase("DPad")) {
	        			// Turn off the click-to-attack pref on devices that have a dpad.
	        			SharedPreferences.Editor editor = prefs.edit();
	                	editor.putBoolean(PreferenceConstants.PREFERENCE_CLICK_ATTACK, false);
	                	editor.commit();
	                	mSelectedControlsString = ((AndroidGame)game).getString(R.string.control_setup_dialog_dpad);
	        		} else if (navType.equalsIgnoreCase("None")) {
	        			SharedPreferences.Editor editor = prefs.edit();
	        			//SensorManager manager = (SensorManager) ((AndroidGame)game).getSystemService(Context.SENSOR_SERVICE);
	        			if (((AndroidInput) game.getInput()).hasAccelerometer()) {
	        				//Log.i("Mario", "Accelerometer:"+manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size());
	        				// Turn on tilt controls if available
	                    	editor.putBoolean(PreferenceConstants.PREFERENCE_TILT_CONTROLS, true);
	                    	editor.putBoolean(PreferenceConstants.PREFERENCE_SCREEN_CONTROLS, false);
	                    	mSelectedControlsString = ((AndroidGame)game).getString(R.string.control_setup_dialog_tilt);
	         			}else{
	         				//Use on ScreenControl if there's nothing else.
	        				editor.putBoolean(PreferenceConstants.PREFERENCE_SCREEN_CONTROLS, true);
	        				editor.putBoolean(PreferenceConstants.PREFERENCE_TILT_CONTROLS, false);
	        				mSelectedControlsString = ((AndroidGame)game).getString(R.string.control_setup_dialog_screen);
	        			}
	        			editor.commit();
	               	
	        		}			
	           	}
	        	SharedPreferences.Editor editor = prefs.edit();
	       	 	editor.putInt(PreferenceConstants.PREFERENCE_LAST_VERSION, MarioGame.VERSION);
	        	editor.commit();
	        	Settings.loadPreferences((AndroidGame)game);
	        	((AndroidGame)game).runOnUiThread(new Runnable(){
	        		public void run(){
	        			showControls_SetUp_Dialog();
	        		}
	        	});
	    
	        }
	    }
	
	private void showControls_SetUp_Dialog(){
		String messageFormat =  ((AndroidGame)game).getResources().getString(R.string.control_setup_dialog_message);  
		String message = String.format(messageFormat, mSelectedControlsString);
		CharSequence sytledMessage = Html.fromHtml(message);  // lame.
		AlertDialog dialog = new AlertDialog.Builder((AndroidGame) game)
		.setTitle(R.string.control_setup_dialog_title)
     	.setPositiveButton(R.string.control_setup_dialog_ok, null)
        .setNegativeButton(R.string.control_setup_dialog_change, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	Intent i = new Intent(((AndroidGame)game).getBaseContext(), SetPreferencesActivity.class);
                    i.putExtra("controlConfig", true);
                    ((AndroidGame)game).startActivity(i);  
                }
         })
	    .setMessage(sytledMessage)
		.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		
	}
	


}
