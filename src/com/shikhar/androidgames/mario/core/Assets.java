package com.shikhar.androidgames.mario.core;

import java.util.Random;

import com.shikhar.androidgames.framework.Image;
import com.shikhar.androidgames.framework.Music;
import com.shikhar.androidgames.framework.Sound;

public class Assets {
    
	 public static Image menu, splash, background, character, character2, character3, heliboy, heliboy2, heliboy3, heliboy4, heliboy5;
	    public static Image tiledirt, tilegrassTop, tilegrassBot, tilegrassLeft, tilegrassRight, characterJump, characterDown;
	    public static Image button;
	    public static Sound click, bump, kick, coin, jump, pause, itemSprout, bonusPoints, healthUp, healthDown;
	    public static Sound hurt1, hurt2, yahoo1, yahoo2;

	    public static Music music;

	    
	    public static void load(MarioGame game) {
 	    	//Play random background mUsic
			Random r = new Random();
			int rNum = r.nextInt(4);
			if(rNum == 0) {
				music =game.getAudio().createMusic("sounds/smwovr2.mid");
			} else if(rNum == 1) {
				music =game.getAudio().createMusic("sounds/smwovr2.mid");
			} else if(rNum == 2) {
				music =game.getAudio().createMusic("music/smb_hammerbros.mid");
			} else  {
				music =game.getAudio().createMusic("music/smrpg_nimbus1.mid");
			}
		 	music.setLooping(true);
	    	music.setVolume(0.85f);
	    	music.play();
	    }
	    
}
 