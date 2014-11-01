package com.shikhar.androidgames.mario.objects.creatures;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.shikhar.androidgames.mario.core.GameRenderer;
import com.shikhar.androidgames.mario.core.MarioResourceManager;
import com.shikhar.androidgames.mario.core.MarioSoundManager;
import com.shikhar.androidgames.mario.core.animation.Animation;
import com.shikhar.androidgames.mario.core.tile.GameTile;
import com.shikhar.androidgames.mario.core.tile.TileMap;
import com.shikhar.androidgames.mario.objects.base.Creature;


public class Fire_Thorny extends Creature {


	private Animation ball;
	private static Bitmap fd_1, fd_2;
	private static boolean initialized = false;
	public Fire_Thorny(int x, int y, MarioSoundManager soundManager) {

		super(x, y, soundManager);
	 
		if (!initialized) {
			 Bitmap[] images = MarioResourceManager.Fire_Thorny;
			fd_1 =  images[0];
			fd_2 =  images[1];
			initialized = true;
		}
		ball = new Animation(120).addFrame(fd_1).addFrame(fd_2);
		setAnimation(ball);
		setIsItem(true);
		setGravityFactor(0.8f);
	}

	@Override
	public void xCollide(Point p) {

	}
	
	public void creatureXCollide() {
		
	}
	
	public void wakeUp() {
		super.wakeUp();
		dy=-0.09f;
	}

	@Override
	public void updateCreature(TileMap map, int time) {
		//super.update(time);
		//super.updateCreature(map,time);
		if(dy < gravityEffect) { // apply gravity...this must be done first
			if (isFlipped()){
				dy = dy + gravityFactor*GRAVITY * time;
			}else{
				dy = dy +  (inWater?0.3f:1)*gravityFactor*GRAVITY * time;
			}
		}
		y= y+ dy * time; 
		
		int tileY = GameRenderer.pixelsToTiles(y + getHeight());
		int tileX = GameRenderer.pixelsToTiles(x + getWidth()/2);
		GameTile tile=map.getTile(tileX, tileY );
		if (tile!= null){
			if (!tile.isCollidable())return;
			kill();
			soundManager.playKick();
			map.creaturesToAdd().add(new Thorny(Math.round(getX()), 
					Math.round(getY()), soundManager));

		}
	}
	
	public void jumpedOn() {

	}

}
