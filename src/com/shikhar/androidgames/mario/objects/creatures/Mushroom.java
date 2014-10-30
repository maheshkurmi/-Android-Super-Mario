package com.shikhar.androidgames.mario.objects.creatures;

import android.graphics.Bitmap;

import com.shikhar.androidgames.mario.core.MarioResourceManager;
import com.shikhar.androidgames.mario.core.animation.Animation;
import com.shikhar.androidgames.mario.core.tile.TileMap;
import com.shikhar.androidgames.mario.objects.base.Creature;






public class Mushroom extends Creature {
	
	private Animation redMushroom;
	private int updateNum;

	public Mushroom(int pixelX, int pixelY) {
		super(pixelX, pixelY);
		setIsItem(true);
		setIsAlwaysRelevant(true);
		Bitmap shroom = MarioResourceManager.Mushroom;
		redMushroom = new Animation();
		redMushroom.addFrame(shroom, 1000);
		redMushroom.addFrame(shroom, 1000);
		setAnimation(redMushroom);
		updateNum = 0;
		dy = -.15f;
		dx = .07f;
	}
	
	public void updateCreature(TileMap map, int time) {
		if(updateNum < 10) {
			setX(getX() + getdX()*time);
			setY(getY() + getdY()*time);
		} else if(updateNum < 200){
			super.updateCreature(map, time);
		} else if(updateNum < 300) {
			if(updateNum % 4 == 0 || updateNum % 4 == 1) {
				setIsInvisible(true);
			} else {
				setIsInvisible(false);
			}
			super.updateCreature(map, time);
		} else {
			kill();
		}
		updateNum += 1;
	}
}

