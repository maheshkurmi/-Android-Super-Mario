package com.shikhar.androidgames.mario.objects.creatures;




import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.shikhar.androidgames.mario.core.MarioResourceManager;
import com.shikhar.androidgames.mario.core.MarioSoundManager;
import com.shikhar.androidgames.mario.core.animation.Animation;
import com.shikhar.androidgames.mario.objects.base.Creature;





public class RedKoopa extends Creature {
	
	private Animation left;
	private Animation right;
	private Animation dead;
	private Animation flip;
	private Random r;
	
	private static Bitmap left_1,left_2,right_1,right_2,shell,flipped;
	private static boolean initialized=false;
	public RedKoopa(int x, int y, MarioSoundManager soundManager) {
		
		super(x, y, soundManager);
		r = new Random();
		if (!initialized){
			 left_1 = MarioResourceManager.Koopa_Red_Left_1;
			 left_2 = MarioResourceManager.Koopa_Red_Left_2;
			 right_1 = MarioResourceManager.Koopa_Red_Right_1;
			 right_2 = MarioResourceManager.Koopa_Red_Right_2;
			 shell = MarioResourceManager.Red_Shell_1;
			 flipped = MarioResourceManager.Red_Shell_Flip;
			 initialized=true;
		}
		left = new Animation(150).addFrame(left_1).addFrame(left_2);
		right = new Animation(150).addFrame(right_1).addFrame(right_2);
		
		final class DeadAfterAnimation extends Animation {
			public void endOfAnimationAction() {
				kill();
			}
		}
		
		dead = new DeadAfterAnimation();
		flip = new DeadAfterAnimation();
		dead.addFrame(shell, 10);
		dead.addFrame(shell, 10);
		flip.addFrame(flipped, 1200);
		flip.addFrame(flipped, 1200);
		setAnimation(left);
	}
	
	public void xCollide(Point p) {
		super.xCollide(p);
		if(currentAnimation() == left) {
			setAnimation(right);
		} else {
			setAnimation(left);
		}
	}
	
	public void creatureXCollide() {
		if(dx > 0) {
			x = x - 2;
			setAnimation(left);
		} else {
			setAnimation(right);
			x = x + 2;
		}
		dx = -dx;
	}
	
	public void flip() {
		setAnimation(flip);
		setIsFlipped(true);
		setIsCollidable(false);
		dy = -.2f;
		dx = 0;
	}
	
	public void wakeUp() {
		super.wakeUp();
		int rNum = r.nextInt(3);
			if(rNum == 0 || rNum == 1) {
				dx = -.03f;
				setAnimation(left);
			} else {
				dx = .03f;
				setAnimation(right);
			}
	}
	
	public void jumpedOn() {
		setAnimation(dead);
		setIsCollidable(false);
		dx = 0;
	}
}
