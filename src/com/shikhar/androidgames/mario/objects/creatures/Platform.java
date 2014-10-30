package com.shikhar.androidgames.mario.objects.creatures;



import android.graphics.Bitmap;

import com.shikhar.androidgames.mario.core.MarioResourceManager;
import com.shikhar.androidgames.mario.core.animation.Animation;
import com.shikhar.androidgames.mario.core.tile.TileMap;
import com.shikhar.androidgames.mario.objects.base.Creature;





public class Platform extends Creature {
	
	protected Animation move;
	protected int turn;
	protected boolean isVertical = false;
	protected boolean isHorizontal = false;
	protected boolean switchedVertical = false;
	protected boolean switchedHorizontal = false;
	protected boolean canJumpThrough = false;
	protected float oldX;
	protected float oldY;
	protected float oldDx;
	protected float oldDy;
	
	public Platform(int pixelX, int pixelY) {
		super(pixelX, pixelY);
		setIsAlwaysRelevant(true);
		setIsPlatform(true);
		turn = 1;
		dx = 0;
		dy = 0;
		Bitmap red_platform = MarioResourceManager.Red_Platform_2;
		move = new Animation(2000).addFrame(red_platform);
		setAnimation(move);
	}
	
	/**
	 * @return the oldX
	 */
	public float getOldX() {
		return oldX;
	}

	/**
	 * @param oldX the oldX to set
	 */
	public void setOldX(float oldX) {
		this.oldX = oldX;
	}

	/**
	 * @return the oldY
	 */
	public float getOldY() {
		return oldY;
	}

	/**
	 * @param oldY the oldY to set
	 */
	public void setOldY(float oldY) {
		this.oldY = oldY;
	}

	public boolean canJumpThrough() {
		return canJumpThrough;
	}
	public boolean isHorizontal() {
		return isHorizontal;
	}
	
	public boolean isVertical() {
		return isVertical;
	}
	
	public float getLastdX() {
		return oldDx;
	}
	
	public float getLastdY() {
		return oldDy;
	}
	
	public boolean switchedVertical() {
		return switchedVertical;
	}
	
	public boolean switchedHorizontal() {
		return switchedHorizontal;
	}
	
	public void updateCreature(TileMap map, int time) {

		
		if(dx != 0) {
			isHorizontal = true;
		}
		if(dy != 0) {
			isVertical = true;
		}
		if(turn == 801) {
			turn = 1;
		}
		if(turn <= 400) {
			oldDx = dx;
			oldDy = dy;
			dx = .05f;
			dy = .025f;
		} else if(turn > 400) {
			oldDy = dy;
			oldDx = dx;
			dx = -.05f;
			dy = -.025f;
		} 
		turn = turn + 1;
		oldX = x;
		oldY = y;
		x = x + time*dx;
		y = y + time*dy;
		
		if((oldDx > 0 && dx < 0) || (oldDx < 0 && dx > 0)) {
			this.switchedHorizontal = true;
		} else {
			this.switchedHorizontal = false;
		}
		
		if((oldDy > 0 && dy < 0) || (oldDy < 0 && dy > 0)) {
			this.switchedVertical = true;
		} else {
			this.switchedVertical = false;
		}
		//dx = 0;
		//dy = 0;
		
	}

}
