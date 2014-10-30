package com.shikhar.androidgames.mario.core.animation;


import com.shikhar.androidgames.mario.core.MarioSoundManager;



public class CollidableObject extends Sprite {
	
	protected MarioSoundManager soundManager;
	private boolean isCollidable;
	private boolean isOnScreen;
	
	public CollidableObject(int pixelX, int pixelY, MarioSoundManager soundManager) {
		super(pixelX, pixelY);
		this.isCollidable = true;
		setIsOnScreen(false);
		this.soundManager = soundManager;
	}
	
	public CollidableObject(int pixelX, int pixelY) {
		this(pixelX, pixelY, null);
	}
	
	public boolean isCollidable() {
		return isCollidable;
	}
	
	public void setIsCollidable(boolean isCollidable) {
		this.isCollidable = isCollidable;
	}
	
	public boolean isOnScreen() {
		return isOnScreen;
	}
	
	public void setIsOnScreen(boolean isOnScreen) {
		this.isOnScreen = isOnScreen;
	}
}
