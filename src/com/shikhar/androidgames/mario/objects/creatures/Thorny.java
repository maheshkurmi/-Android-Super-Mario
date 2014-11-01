package com.shikhar.androidgames.mario.objects.creatures;




import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.shikhar.androidgames.mario.core.MarioResourceManager;
import com.shikhar.androidgames.mario.core.MarioSoundManager;
import com.shikhar.androidgames.mario.core.animation.Animation;
import com.shikhar.androidgames.mario.objects.base.Creature;





public class Thorny extends Creature {
	
	private Animation left;
	private Animation right;
	private Animation flip;
	private Random r;
	
	private static Bitmap left_1,left_2,right_1,right_2,flipped;
	private static boolean initialized=false;
	public Thorny(int x, int y, MarioSoundManager soundManager) {
		
		super(x, y, soundManager);
		r = new Random();
		if (!initialized){
			 left_1 = MarioResourceManager.Thorny[0];
			 left_2 = MarioResourceManager.Thorny[1];
			 right_1 = MarioResourceManager.Thorny[2];
			 right_2 = MarioResourceManager.Thorny[3];
			 flipped = MarioResourceManager.Thorny[4];
			 initialized=true;
		}
		left = new Animation(150).addFrame(left_1).addFrame(left_2);
		right = new Animation(150).addFrame(right_1).addFrame(right_2);
		
		final class DeadAfterAnimation extends Animation {
			public void endOfAnimationAction() {
				kill();
			}
		}
		
		//dead = new DeadAfterAnimation();
		flip = new DeadAfterAnimation();
		//dead.addFrame(shell, 10);
		//dead.addFrame(shell, 10);
		flip.addFrame(flipped, 1200);
		flip.addFrame(flipped, 1200);
		setAnimation(left);
		setIsAlwaysRelevant(true);
		//align base of creature to tile
		this.y=y-getHeight()+16;
	}
	
	public void xCollide(Point p) {
		super.xCollide(p);
		/*
		if(currentAnimation() == left) {
			setAnimation(right);
		} else {
			setAnimation(left);
		}
		*/
		if(dx>0) {
			setAnimation(right);
		} else if(dx<0) {
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
		setGravityFactor(0.7f);
		dy = -.2f;
		dx = 0;
	}
	
	public void wakeUp(boolean isLeft) {
		super.wakeUp();
		if(isLeft) {
			dx = -.032f;
			setAnimation(left);
		} else {
			dx = .032f;
			setAnimation(right);
		}
	}
	
	public void jumpedOn() {
		//setAnimation(dead);
		//setIsCollidable(false);
		//dx = 0;
	}
}
