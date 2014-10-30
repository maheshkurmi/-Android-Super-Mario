package com.shikhar.androidgames.mario.objects.creatures;



import android.graphics.Bitmap;

import com.shikhar.androidgames.mario.core.MarioResourceManager;
import com.shikhar.androidgames.mario.core.animation.Animation;
import com.shikhar.androidgames.mario.core.tile.TileMap;
import com.shikhar.androidgames.mario.objects.base.Creature;





public class Score extends Creature {
	
	public Animation oneHundred;
	private static Bitmap one_hundred;
	private static boolean initialized=false;
	public Score(int x, int y) {
		super(x, y);
		setIsItem(true);
		
		dy = -.45f;
		if (!initialized){
			one_hundred = MarioResourceManager.Score_100_New6;
			initialized=true;
		}
		final class DeadAfterAnimation extends Animation {
			public void endOfAnimationAction() {
				kill();
			}
		}
		
		oneHundred = new DeadAfterAnimation();
		
		oneHundred.addFrame(one_hundred, 380);
		oneHundred.addFrame(one_hundred, 380);	
		setAnimation(oneHundred);
	}
	
	public void updateCreature(TileMap map, int time) {
		this.update((int) time);
		y = y + dy * time;
		if(dy < 0) {
			dy = dy + .032f;
		} else {
			dy = 0;
		}
	}

}
