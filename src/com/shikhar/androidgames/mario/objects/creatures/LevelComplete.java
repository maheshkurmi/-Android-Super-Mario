package com.shikhar.androidgames.mario.objects.creatures;

import android.graphics.Bitmap;

import com.shikhar.androidgames.mario.core.MarioResourceManager;
import com.shikhar.androidgames.mario.core.animation.Animation;
import com.shikhar.androidgames.mario.core.tile.TileMap;
import com.shikhar.androidgames.mario.objects.base.Creature;





public class LevelComplete extends Creature {
	
	private Animation level;

	
	public LevelComplete(int pixelX, int pixelY) {
		super(pixelX, pixelY);
		setIsItem(true);
		setIsAlwaysRelevant(true);
		Bitmap shroom = MarioResourceManager.levelComplete;
		level = new Animation();
		level.addFrame(shroom, 1000);
		level.addFrame(shroom, 1000);
		setAnimation(level);
	}
	
	@Override
	public void updateCreature(TileMap map, int time) {
	
	}
}

