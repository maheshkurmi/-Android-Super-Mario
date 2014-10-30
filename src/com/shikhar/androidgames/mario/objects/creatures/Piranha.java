package com.shikhar.androidgames.mario.objects.creatures;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.shikhar.androidgames.mario.core.MarioResourceManager;
import com.shikhar.androidgames.mario.core.MarioSoundManager;
import com.shikhar.androidgames.mario.core.animation.Animation;
import com.shikhar.androidgames.mario.core.tile.TileMap;
import com.shikhar.androidgames.mario.objects.base.Creature;





public class Piranha extends Creature {
	
	private static Bitmap p1;
	private static Bitmap p2;
	public static Animation turn ;
	private static boolean initialized = false;

	float dy=0.3f;
	float y1=0;
	float initY;
	static int wait=0;
	public Piranha(int pixelX, int pixelY, MarioSoundManager soundManager) {
		super(pixelX, pixelY,soundManager);
		if (!initialized) {
			p1 =  MarioResourceManager.Piranha_1;
			p2 =  MarioResourceManager.Piranha_2;
			initialized = true;
		}
		
		turn = new Animation(200).addFrame(p1).addFrame(p2);
		setAnimation(turn);
		dy= 0.5f;
		initY=pixelY;
		setOffsetX(-3);
	}
	
	@Override
	public void draw(Canvas g, int x, int y) {
		g.drawBitmap(currentAnimation().getImage(),new Rect(0,0,getWidth(),getHeight()-(int)y1),new Rect(x+getOffsetX() , y+getOffsetY(),x +getWidth()+getOffsetX(),y+getOffsetY() +  getHeight()-(int)y1),null);
	}
	
	@Override
	public void updateCreature(TileMap map, int time) {
		if (wait>0){
			wait--;
			return;
		}
		super.update(time);
		y1=y1+dy;
		if (y1 >getHeight()){
			y1=getHeight();
			dy=-Math.abs(dy);
			wait=75;
		}else if (y1<0){
			y1=0;
			dy=Math.abs(dy);
		}
		y=initY+y1;
		//setOffsetY(getOffsetY()+1); 
	}
	
	public void flip() {
		kill();
	}
}
