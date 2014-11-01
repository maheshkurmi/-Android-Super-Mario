package com.shikhar.androidgames.mario.objects.creatures;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.shikhar.androidgames.mario.core.MarioResourceManager;
import com.shikhar.androidgames.mario.core.animation.Animation;
import com.shikhar.androidgames.mario.core.tile.TileMap;
import com.shikhar.androidgames.mario.objects.base.Creature;






public class FireFlower extends Creature {
	private static Bitmap flower;
	private Animation fireFlower;
	private int updateNum;
	private static boolean initialized = false;

	float y1=0;
	float initY;
	public boolean isReady=false;;
	
	public FireFlower(int pixelX, int pixelY) {
		super(pixelX, pixelY);
		setIsItem(true);
		setIsAlwaysRelevant(true);
		flower = MarioResourceManager.Fire_Flower;
		fireFlower = new Animation();

		fireFlower.addFrame(flower, 1000);
		fireFlower.addFrame(flower, 1000);
		
		setAnimation(fireFlower);
		dy=0.5f;
		dx=0;
		updateNum = 0;
		initY=pixelY;
		//setOffsetX(-3);
		y1=getHeight();
	}
	
	@Override
	public void draw(Canvas g, int x, int y) {
		g.drawBitmap(currentAnimation().getImage(),new Rect(0,0,getWidth(),getHeight()-(int)y1),new Rect(x+getOffsetX() , y+getOffsetY(),x +getWidth()+getOffsetX(),y+getOffsetY() +  getHeight()-(int)y1),null);
	}
	
	public void updateCreature(TileMap map, int time) {
		super.update(time);

		if (y1 >0){
			y1=y1-dy;
			if (y1<0)y1=0;
			y=initY-getHeight()+y1;
		}
		
		//setOffsetY(getOffsetY()+1); 
		if(updateNum>500 && updateNum < 600) {
			if(updateNum % 6 == 0 || updateNum % 6 == 1 || updateNum % 6==2) {
				setIsInvisible(true);
			} else {
				setIsInvisible(false);
			}
		} else if (updateNum> 600){
			kill();
		}
		updateNum += 1;
	}
}

