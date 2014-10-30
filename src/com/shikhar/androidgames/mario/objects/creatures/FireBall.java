package com.shikhar.androidgames.mario.objects.creatures;

import java.util.Random;

import com.shikhar.androidgames.mario.core.MarioResourceManager;
import com.shikhar.androidgames.mario.core.MarioSoundManager;
import com.shikhar.androidgames.mario.core.animation.Animation;
import com.shikhar.androidgames.mario.objects.base.Creature;

import android.graphics.Bitmap;
import android.graphics.Point;


public class FireBall extends Creature {


	private Animation dead;
	private Animation ball;
	private static Bitmap fb_1, fb_2, fb_3, fb_4, fb_5,fb_6;
	private static boolean initialized = false;
	public static int fireballsCount=0;

	public FireBall(int x, int y, float direction, MarioSoundManager soundManager) {

		super(x, y, soundManager);
		fireballsCount++;
		if (!initialized) {
			fb_1 =  MarioResourceManager.fb_1;//loadImage("baddies/fireball_1.png");
			fb_2 =  MarioResourceManager.fb_2;//loadImage("baddies/fireball_2.png");
			fb_3 =  MarioResourceManager.fb_3;//loadImage("baddies/fireball_3.png");
			fb_4 =  MarioResourceManager.fb_4;//loadImage("baddies/fireball_4.png");
			fb_5 =  MarioResourceManager.fb_5;//loadImage("baddies/fireball_5.png");
			fb_6 =  MarioResourceManager.fb_6;//loadImage("baddies/fireball_6.png");
			
			initialized = true;
		}
		ball = new Animation(150).addFrame(fb_1).addFrame(fb_2).addFrame(fb_3).addFrame(fb_4);
		
		final class DeadAfterAnimation extends Animation {
			public void endOfAnimationAction() {
				kill();
				fireballsCount--;
			}
		}
		dead = new DeadAfterAnimation().setDAL(50).addFrame(fb_5).setDAL(100).addFrame(fb_6);
		//dead = new DeadAfterAnimation();
		// dead.addFrame(fb_5, 10);
		//dead.addFrame(fb_6, 10);
		setAnimation(ball);
		dx=direction*.12f;
		//dy=0.3f;
	}

	@Override
	public void xCollide(Point p) {
		dx=0;
		dy=0;
		//super.xCollide(p);
		setAnimation(dead);
		soundManager.playKick();
		//setIsCollidable(false);
		//dx = 0;
		//dy=0;
	}


}
