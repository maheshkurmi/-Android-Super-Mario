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
	private static Sound bump, kick, coin, jump, pause, itemSprout, bonusPoints, healthUp, healthDown, brick_shatter,fireball,die,powerUp,powerDown,stomp;
    
	private static Sound hurt1, hurt2, yahoo1, yahoo2,stage_clear,stage_begin;
	private static Sound click, gulp,switchScreen;

	private static Music music, gameMusic,menuMusic;
	/**volume of sound in float (0 to 1)*/
	private static float soundVolume=0.9f;
	 /**volume of music in float (0 to 1)*/
	private static float musicVolume=0.7f;
    
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
	 		die=createSound("sounds/die.wav");
	 		powerUp=createSound("sounds/power_up.wav");
			powerDown=createSound("sounds/power_down.wav");
			stage_clear=createSound("sounds/win_stage.wav");
			stage_begin=createSound("sounds/level_enter.wav");
			stomp=createSound("sounds/stomp.wav");
			gulp=createSound("sounds/gulp.wav");
			switchScreen=createSound("sounds/level_enter.wav");
			click=createSound("sounds/coin.wav");
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
		menuMusic=createMusic("music/smw_map.mid");//main_menu_music.midi");
		Random r = new Random();
		int rNum = r.nextInt(4);
		if(rNum == 0) {
			gameMusic=createMusic("music/smwovr2.mid");
		} else if(rNum == 1) {
			gameMusic=createMusic("music/smwovr1.mid");
		} else if(rNum == 2) {
			gameMusic=createMusic("music/smb_hammerbros.mid");
		} else  {
			gameMusic=createMusic("music/smrpg_nimbus1.mid");
		}
		if (menuMusic!=null)menuMusic.setLooping(true);
		if (gameMusic!=null){
			gameMusic.setLooping(true);
			music=menuMusic;
			if (musicEnabled && music!=null)music.play();
		}

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
		kick.play(soundVolume);
	}
	
	public void playGulp() {
		gulp.play(soundVolume);
	}
	
	public void playStomp() {
		stomp.play(soundVolume);
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
	
	public void playDie() {
		if (soundEnabled) die.play(soundVolume);
	}
	
	public void playPowerUp() {
		if (soundEnabled) powerUp.play(soundVolume);
	}
	
	public void playPowerDown() {
		if (soundEnabled) powerDown.play(soundVolume);
	}
	
	public void playHurt() {
		if (!soundEnabled)return;
		Random r = new Random();
		int rNum = r.nextInt(2);
		if(rNum == 0) {
			hurt1.play(soundVolume);
		} else {
			hurt2.play(soundVolume);
		}
	}
	
	public void playCelebrate() {
		if (!soundEnabled)return;
		Random r = new Random();
		int rNum = r.nextInt(2);
		if(rNum == 0) {
			yahoo1.play(soundVolume);
		} else {
			yahoo2.play(soundVolume);
		}
	}
	
	public void playStageEnter() {
		if (soundEnabled)stage_begin.play(soundVolume);
	}
	
	public void playStageClear() {
		if (soundEnabled) stage_clear.play(soundVolume);
	}
	
	public void playswitchScreen() {
		if (soundEnabled) switchScreen.play(soundVolume);
	}
	
	public void playClick() {
		if (soundEnabled) click.play(soundVolume);
	}
	
	public void loadMenuMusic(){
		if(music==menuMusic)return;
		if (music!=null)music.stop();
		music=menuMusic;
		if (musicEnabled && music!=null)music.play();
	}
	
	public  void loadGameMusic(){
		if(music==gameMusic)return;
		if (music!=null)music.stop();
		music=gameMusic;
		if (musicEnabled && music!=null)music.play();
	}
	
	public static void playMusic(){
		if (music!=null)music.play();
	}
	
	public static void pauseMusic(){
		if (music!=null)music.pause();
	}
	
	public static void stopMusic(){
		if (music!=null)music.stop();
	}
	

	public static void setMusicEnabled(boolean enabled){
		if (music==null)return;
		musicEnabled=enabled;
		if (enabled){
			music.play();
		}else{
			music.stop();
		}
	}
	
	public static void setSoundEnabled(boolean enabled){
		soundEnabled=enabled;
	}
	
	public static float getSoundVolume() {
		return soundVolume;
	}

	public static void setSoundVolume(float volume) {
		soundVolume = volume;
	}

	public static float getMusicVolume() {
		return musicVolume;
	}

	public static void setMusicVolume(float volume) {
		musicVolume = volume;
		if (music!=null)music.setVolume(musicVolume);
	}
	
	
}
