package com.shikhar.androidgames.mario.objects.creatures;

import android.graphics.Bitmap;

import com.shikhar.androidgames.mario.core.MarioResourceManager;
import com.shikhar.androidgames.mario.core.animation.Animation;
import com.shikhar.androidgames.mario.core.tile.TileMap;
import com.shikhar.androidgames.mario.objects.base.Creature;






public class Mushroom extends Creature {
	
	private Animation redMushroom;
	private int updateNum;
    private boolean isHealthUp=false;
	public Mushroom(int pixelX, int pixelY, boolean isHealthUp) {
		super(pixelX, pixelY);
		setIsItem(true);
		setIsAlwaysRelevant(true);
		setHealthUp(isHealthUp);
		Bitmap shroom = isHealthUp?MarioResourceManager.LifeUp:MarioResourceManager.Mushroom;
		redMushroom = new Animation();
		redMushroom.addFrame(shroom, 1000);
		redMushroom.addFrame(shroom, 1000);
		setAnimation(redMushroom);
		updateNum = 0;
		dy = -.13f;
		dx = .07f;
	}
	
	public void wakeUp(boolean isLeft) {
		super.wakeUp();
		dx=Creature.map.getPlayer().getdX()<0?-0.07f:0.07f;
	}
	
	public void updateCreature(TileMap map, int time) {
		if(updateNum < 1){
			for (int i=0; i<map.waterZones().size();i++){
				if (map.waterZones().get(i).contains((int)getX()/16,(int)getY()/16)){
					inWater=true;
					break;
				}
			}
			if (inWater) {
				dy = -.03f;
				dx = .06f;
			}
		} else if(updateNum < 10) {
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

	public boolean isHealthUp() {
		return isHealthUp;
	}

	public void setHealthUp(boolean isHealthUp) {
		this.isHealthUp = isHealthUp;
	}
}

