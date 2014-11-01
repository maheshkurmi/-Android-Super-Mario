package com.shikhar.androidgames.mario.objects.creatures;




import java.util.Random;

import android.graphics.Bitmap;

import com.shikhar.androidgames.mario.core.MarioResourceManager;
import com.shikhar.androidgames.mario.core.MarioSoundManager;
import com.shikhar.androidgames.mario.core.animation.Animation;
import com.shikhar.androidgames.mario.objects.base.Creature;







public class Goomba extends Creature {
	
	private Animation waddle, dead, flip;
	private static Bitmap w1,w2,smashed,flipped;
	private static boolean initialised=false;
	public Goomba(int x, int y, MarioSoundManager soundManager) {
		
		super(x, y, soundManager);
		if (!initialised){
			 w1 = MarioResourceManager.Goomba_Normal_1;
			 w2 = MarioResourceManager.Goomba_Normal_2;
			 smashed = MarioResourceManager.Goomba_Dead;
			 flipped = MarioResourceManager.Goomba_Flip;
			 initialised=true;
		}

		final class DeadAfterAnimation extends Animation {
			public void endOfAnimationAction() {
				kill();
			}
		}

		waddle = new Animation(150).addFrame(w1).addFrame(w2);
		dead = new DeadAfterAnimation().setDAL(100).addFrame(smashed).setDAL(20).addFrame(smashed);
		flip = new Animation().addFrame(flipped).addFrame(flipped);
		setAnimation(waddle);
	}
	
	public void wakeUp(boolean isLeft) {
		super.wakeUp();
		dx=isLeft?-0.03f:0.03f;
		//dx = (r.nextInt(25) == 0) ? .03f : -.03f;
	}
	
	public void jumpedOn() {
		setAnimation(dead);
		setIsCollidable(false);
		dx = 0;
	}
	
	public void flip() {
		setAnimation(flip);
		setIsFlipped(true);
		setIsCollidable(false);
		dy = -.2f;
		dx = 0;
	}
}
