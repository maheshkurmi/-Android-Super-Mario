package com.shikhar.androidgames.mario.objects.creatures;



import android.graphics.Bitmap;

import com.shikhar.androidgames.mario.core.GameRenderer;
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
	protected float vx=0.05f,vy=0.025f;
	private float g=0.005f;
	private float t;

	/**
	 * 	0=	Steady, 1=Vertical, 2=Horizontal,4=both
	 */
	private boolean mayfall=false;
	private boolean beginfall=false;
	private int wait=0;
	
	public Platform(int pixelX, int pixelY,float vx, float vy, boolean mayfall) {
		this(pixelX, pixelY);
		this.mayfall=mayfall;
		if (mayfall)canJumpThrough=true;
		this.vx=vx;
		this.vy=vy;
		if (vy==0 && vx<0) t=1.57f; //platform is horizontal change its phase by pi if vel is -ve

	}
		
	public Platform(int pixelX, int pixelY) {
		super(pixelX, pixelY);
		setIsAlwaysRelevant(true);
		setIsPlatform(true);
		turn = 1;
		dx = 0;
		dy = 0;
		oldX = x;
		oldY = y;
		mayfall=true;
		canJumpThrough=true;
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

		if (mayfall) {
			if (beginfall) {
				wait--;
				if (wait>0)return;
				oldDy = dy;
				dy = dy + g;
				oldY = y;
				y = y + time * dy;
				if (g < 0.008)
					g += 0.002f;
				if (dy > 0.4){
					dy = 0.4f;
					oldDy = 0.4f;
					
				}
								
				if (y > GameRenderer.tilesToPixels(map.getHeight()))
					kill();
			}
			return;
		}
		
		if (vy==0){
			oldX = x;
			oldDx = x-oldX;
			t+=time/1000.0;
			dx = 1/1000.0f*(float) (64*Math.cos(2*t));;
			x=x+time*dx;///(float) (cx+32*Math.sin(2*t));
			dy=0;
			oldDy=0;
			dy = 0;
			isHorizontal = true;
			isVertical = false;
			return;
		}
		
		if (dx != 0) {
			isHorizontal = true;
		}
		if (dy != 0) {
			isVertical = true;
		}
		if (turn == 221) {
			turn = 1;
		}
		if (turn <= 110) {
			oldDx = dx;
			oldDy = dy;
			dx = vx;
			dy = vy;
		} else if (turn > 110) {
			oldDy = dy;
			oldDx = dx;
			dx = -vx;
			dy = -vy;
		}
		turn = turn + 1;
		oldX = x;
		oldY = y;
		x = x + 15 * dx;
		y = y + time * dy;

		if ((oldDx > 0 && dx < 0) || (oldDx < 0 && dx > 0)) {
			this.switchedHorizontal = true;
		} else {
			this.switchedHorizontal = false;
		}

		if ((oldDy > 0 && dy < 0) || (oldDy < 0 && dy > 0)) {
			this.switchedVertical = true;
		} else {
			this.switchedVertical = false;
		}
		// dx = 0;
		// dy = 0;

	}


	public boolean mayfall() {
		return mayfall;
	}

	public boolean isfalling() {
		return beginfall;
	}

	public void accelerateFall() {
		if (mayfall){
			beginfall=true;
			dy=0.02f;
			wait=5;
		}
	}


}
