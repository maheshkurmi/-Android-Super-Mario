package com.shikhar.androidgames.mario.core.animation;

import android.graphics.Canvas;


/**
 * Animatible is an abstract class that a class should extend if it wants to be drawn using
 * an Animation. 
 */

abstract public class Animatible {
	
	private Animation currAnim;
	private int offsetX;
	private int offsetY;

	public abstract void draw(Canvas g, int pixelX, int pixelY);
	public abstract void draw(Canvas g, int pixelX, int pixelY, int offsetX, int offsetY);
	public abstract int getHeight();
	public abstract int getWidth();

	public Animation currentAnimation() {
		return currAnim;
	}
	
	public void setAnimation(Animation currAnim) {
		this.currAnim = currAnim;
	}
	/**
	 * updates animation
	 * @param time
	 */
	public void update(int time) {
		currAnim.update(time);
	}
	
	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}
	
	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}
	
	public int getOffsetX() {
		return offsetX;
	}
	
	public int getOffsetY() {
		return offsetY;
	}
}
