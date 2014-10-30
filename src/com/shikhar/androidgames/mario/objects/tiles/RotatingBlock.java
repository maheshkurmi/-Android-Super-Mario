package com.shikhar.androidgames.mario.objects.tiles;


import android.graphics.Bitmap;

import com.shikhar.androidgames.mario.core.MarioResourceManager;
import com.shikhar.androidgames.mario.core.animation.Animation;
import com.shikhar.androidgames.mario.core.tile.GameTile;


public class RotatingBlock extends GameTile {
	
	private Animation rotate;
	private Animation idle;
	
	public RotatingBlock(int pixelX, int pixelY) {
		
		// int pixelX, int pixelY, Animation anim, Image img, boolean isUpdateable
		super(pixelX, pixelY, null, null);
		setIsSloped(false);
		
		Bitmap rotate_1 = MarioResourceManager.Rotating_Block_Hit_1;
		Bitmap rotate_2 = MarioResourceManager.Rotating_Block_Hit_2;
		Bitmap rotate_3 = MarioResourceManager.Rotating_Block_Hit_3;
		Bitmap still = MarioResourceManager.Rotating_Block_Still;
		
     	final class RotateAnimation extends Animation {
     		public void endOfAnimationAction() {
     			setAnimation(idle);
     			setIsCollidable(true);
     		}
		}
		
		idle = new Animation(10000).addFrame(still);
		rotate = new RotateAnimation();
		
		int rotateTime = 90;
		for(int i = 1; i <= 3; i++) {
			for(int j = 1; j <= 3; j++) {
				rotate.addFrame(rotate_1, rotateTime);
				rotate.addFrame(rotate_2, rotateTime);
				rotate.addFrame(rotate_3, rotateTime);
			}
			rotateTime += 90;
		}
		setAnimation(idle);
	}
	
	public void doAction() {
		setAnimation(rotate);
		setIsCollidable(false);
	}
}
