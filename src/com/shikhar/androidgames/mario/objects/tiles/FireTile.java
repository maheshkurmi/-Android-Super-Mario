package com.shikhar.androidgames.mario.objects.tiles;

import com.shikhar.androidgames.mario.core.MarioResourceManager;
import com.shikhar.androidgames.mario.core.animation.Animation;
import com.shikhar.androidgames.mario.core.tile.GameTile;


public class FireTile extends GameTile {

	private Animation active;
	
	public FireTile(int pixelX, int pixelY) {
		super(pixelX, pixelY, null, null);
		setIsSloped(false);
		active = new Animation(400).addFrame(MarioResourceManager.Fire_Tile[0]).addFrame(MarioResourceManager.Fire_Tile[1]);
		setAnimation(active);
	}
	
	public void update(int time) {
		super.update(time);
	}
	
}