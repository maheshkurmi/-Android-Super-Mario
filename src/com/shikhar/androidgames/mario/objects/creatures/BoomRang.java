package com.shikhar.androidgames.mario.objects.creatures;

import com.shikhar.androidgames.mario.core.MarioResourceManager;
import com.shikhar.androidgames.mario.core.MarioSoundManager;
import com.shikhar.androidgames.mario.core.animation.Animation;
import com.shikhar.androidgames.mario.objects.base.Creature;
import android.graphics.Bitmap;
import android.graphics.Point;


public class BoomRang extends Creature {

	private Animation dead;
	private Animation ball_left,ball_right;
	private static Bitmap fl_1, fl_2, fl_3, fr_1,fr_2,fr_3, fb_1,fb_2;
	private static boolean initialized = false;
	public static int fireballsCount=0;

	public BoomRang(int x, int y, float direction, MarioSoundManager soundManager) {

		super(x, y, soundManager);
		fireballsCount++;
		if (!initialized) {
			fl_1 = MarioResourceManager.BoomRang_left[0];
			fl_2 = MarioResourceManager.BoomRang_left[1];
			fl_3 = MarioResourceManager.BoomRang_left[2];

			fr_1 = MarioResourceManager.BoomRang_right[0];
			fr_2 = MarioResourceManager.BoomRang_right[1];
			fr_3 = MarioResourceManager.BoomRang_right[2];
			fb_1 = MarioResourceManager.fb_1;
			fb_2 = MarioResourceManager.fb_2;
			
			initialized = true;
		}
		ball_left = new Animation(100).addFrame(fl_1).addFrame(fl_2).addFrame(fl_3);//.addFrame(fb_4);
		ball_right = new Animation(100).addFrame(fr_1).addFrame(fr_2).addFrame(fr_3);//.addFrame(fb_4);
		
		final class DeadAfterAnimation extends Animation {
			public void endOfAnimationAction() {
				kill();
				fireballsCount--;
			}
		}
		dead = new DeadAfterAnimation().setDAL(50).addFrame(fb_1).setDAL(100).addFrame(fb_2);
		//dead = new DeadAfterAnimation();
		// dead.addFrame(fb_5, 10);
		//dead.addFrame(fb_6, 10);
		dx=direction*.09f;
		dy=0.03f;
		setAnimation((dx<0)?ball_left:ball_right);
		setGravityFactor(0.1f);
		setIsCollidable(false);
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
	
	public void creatureXCollide() {
		
	}
	
	public void jumpedOn() {
		kill();
		setIsCollidable(false);
		dx = 0;
	}
}
