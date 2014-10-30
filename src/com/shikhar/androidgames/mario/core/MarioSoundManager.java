package com.shikhar.androidgames.mario.core;


import java.util.Random;

import android.app.Activity;

import com.shikhar.androidgames.framework.Music;
import com.shikhar.androidgames.framework.Sound;
import com.shikhar.androidgames.framework.sfx.AndroidAudio;

/**
 * Manages sound and musics for the game
 * @author mahesh
 *
 */
public class MarioSoundManager extends AndroidAudio{
	private static Sound bump, kick, coin, jump, pause, itemSprout, bonusPoints, healthUp, healthDown, brick_shatter,fireball;
    
	private static Sound hurt1, hurt2, yahoo1, yahoo2;

	private static Music music;
	/**volume of sound in float (0 to 1)*/
	private  float soundVolume=0.9f;
	 /**volume of music in float (0 to 1)*/
	private  float musicVolume=0.7f;
    
	private static boolean firstTimeCreate = true;
	
	public static boolean musicEnabled=true;
	public static boolean soundEnabled=true;
	public MarioSoundManager(Activity activity) {
		super(activity);
 	}
	
	/**
	 * loads resources in memory if resources are not already loaded
	 */
	public  void loadResouces(){
		if (firstTimeCreate==true){
			bump = createSound("sounds/bump.wav");
	 		kick = createSound("sounds/kick.wav");
	 		coin = createSound("sounds/coin.wav");
	 		jump = createSound("sounds/jump.wav");
	 		pause = createSound("sounds/pause.wav");
	 		itemSprout = createSound("sounds/item_sprout.wav");
	 		bonusPoints = createSound("sounds/veggie_throw.wav");
	 		healthUp = createSound("sounds/power_up.wav");
	 		healthDown = createSound("sounds/power_down.wav");
	 		
	 		hurt1 = createSound("sounds/mario_ooh.wav");
	 		hurt2 = createSound("sounds/mario_oh.wav");
	 		yahoo1 = createSound("sounds/mario_waha.wav");
	 		yahoo2 = createSound("sounds/mario_woohoo.wav");
	 		brick_shatter=createSound("sounds/crack.wav");
	 		fireball=createSound("sounds/fireball.wav");
	 		loadMusic();
		}
		firstTimeCreate=false;
	}
	
	/**
	 * returns true if resources are ready (loaded in memory)
	 * @return
	 */
	public boolean isReady(){
		return !firstTimeCreate;
	}
	
	private void loadMusic(){
		//music=createMusic(filename);
		Random r = new Random();
		int rNum = r.nextInt(4);
		if(rNum == 0) {
			music=createMusic("sounds/smwovr2.mid");
		} else if(rNum == 1) {
			music=createMusic("sounds/smwovr2.mid");
		} else if(rNum == 2) {
			music=createMusic("music/smb_hammerbros.mid");
		} else  {
			music=createMusic("music/smrpg_nimbus1.mid");
		}
		if (music!=null)music.setLooping(true);
	}
	
	
	public void playHealthUp() {
		if (soundEnabled) healthUp.play(soundVolume);
	}
	
	public void playHealthDown() {
		if (soundEnabled) healthDown.play(soundVolume);
	}
	
	public void playBonusPoints() {
		if (soundEnabled) bonusPoints.play(soundVolume);
	}
	
	public void playItemSprout() {
		if (soundEnabled) itemSprout.play(soundVolume);
	}
	
	public void playCoin() {
		if (soundEnabled) coin.play(soundVolume);
	}
	
	public void playKick() {
		//kick.play(soundVolume);
	}
	
	public void playBump() {
		if (soundEnabled) bump.play(soundVolume);
	}
	
	public void playJump() {
		if (soundEnabled) jump.play(soundVolume);
	}
	
	public void playPause() {
		if (soundEnabled) pause.play(soundVolume);
	}

	public void playBrickShatter() {
		if (soundEnabled) brick_shatter.play(soundVolume);
	}

	public void playFireBall() {
		if (soundEnabled) fireball.play(soundVolume);
	}
	
	public void playHurt() {
		Random r = new Random();
		int rNum = r.nextInt(2);
		if(rNum == 0) {
			hurt1.play(soundVolume);
		} else {
			hurt2.play(soundVolume);
		}
	}
	
	public void playCelebrate() {
		Random r = new Random();
		int rNum = r.nextInt(2);
		if(rNum == 0) {
			yahoo1.play(soundVolume);
		} else {
			yahoo2.play(soundVolume);
		}
	}
	
	public void playMusic(){
		if (music!=null)music.play();
	}
	
	public void pauseMusic(){
		if (music!=null)music.pause();
	}
	
	public void stopMusic(){
		if (music!=null)music.stop();
	}
	
	public float getSoundVolume() {
		return soundVolume;
	}

	public void setSoundVolume(float soundVolume) {
		this.soundVolume = soundVolume;
	}

	public float getMusicVolume() {
		return musicVolume;
	}

	public void setMusicVolume(float musicVolume) {
		this.musicVolume = musicVolume;
	}
}
