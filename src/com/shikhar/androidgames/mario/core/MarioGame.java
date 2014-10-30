package com.shikhar.androidgames.mario.core;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;

import com.shikhar.androidgames.framework.Screen;
import com.shikhar.androidgames.framework.gfx.AndroidGame;
import com.shikhar.androidgames.mario.screens.SplashLoadingScreen;

/**
 * Main Entry Class for the game (Only activity used in game)
 * @author mahesh
 *
 */
public class MarioGame extends AndroidGame{
	boolean firstTimeCreate = true;
	public MarioResourceManager resourceManager;
	public MarioSoundManager soundManager;
	public static final int QUIT_GAME_DIALOG = 0;
	public Screen getStartScreen() {
		 if (firstTimeCreate) {
	            //Assets.load(this);
	            soundManager=new MarioSoundManager(this);
	            resourceManager=new MarioResourceManager(this);
	            firstTimeCreate = false;
	        }
         return new SplashLoadingScreen(this);
	}

	
	@Override
	public void onBackPressed() {
		getCurrentScreen().onBackPressed();
	}

	 @Override
	 public void onResume() {
	      super.onResume();
	      if (soundManager!=null)soundManager.playMusic();
	      
	 }

	@Override
    public void onPause() {
	     super.onPause();
	   	 if (soundManager!=null)soundManager.pauseMusic();
    }
	
	
	 @Override
	    protected Dialog onCreateDialog(int id) {
	        Dialog dialog = null;
	        if (id == QUIT_GAME_DIALOG) {
	        	
	            dialog = new AlertDialog.Builder(this)
	                .setTitle("Quit Game")
	                .setPositiveButton("Return to main menu?", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                    	finish();
	                    }
	                })
	                .setNegativeButton("Quit", null)
	                .setMessage("Cancel")
	                .create();
	        }
	        return dialog;
	    }
	
}
