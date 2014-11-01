package com.shikhar.androidgames.mario.objects.creatures;




import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.shikhar.androidgames.mario.core.GameRenderer;
import com.shikhar.androidgames.mario.core.MarioResourceManager;
import com.shikhar.androidgames.mario.core.MarioSoundManager;
import com.shikhar.androidgames.mario.core.animation.Animation;
import com.shikhar.androidgames.mario.core.tile.TileMap;
import com.shikhar.androidgames.mario.objects.base.Creature;





public class RedKoopa extends Creature {
	
	private Animation left;
	private Animation right;
	private Animation dead;
	private Animation flip;
	private Random r;
	private boolean isGreen=true;
	private  Bitmap left_1,left_2,right_1,right_2,shell,flipped;
	private static boolean initialized=false;
	
	public RedKoopa(int x, int y, MarioSoundManager soundManager,boolean isGreen) {
		
		super(x, y, soundManager);
		this.isGreen=isGreen;
		r = new Random();
			if (!isGreen){
			 left_1 = MarioResourceManager.Koopa_Red_Left_1;
			 left_2 = MarioResourceManager.Koopa_Red_Left_2;
			 right_1 = MarioResourceManager.Koopa_Red_Right_1;
			 right_2 = MarioResourceManager.Koopa_Red_Right_2;
			 shell = MarioResourceManager.Red_Shell_1;
			 flipped = MarioResourceManager.Red_Shell_Flip;
			 initialized=true;
			}else{
			 left_1 = MarioResourceManager.Green_Koopa[0];
			 left_2 = MarioResourceManager.Green_Koopa[1];
			 right_1 = MarioResourceManager.Green_Koopa[2];
			 right_2 = MarioResourceManager.Green_Koopa[3];
			 shell = MarioResourceManager.Green_Shell[4];
			 flipped = MarioResourceManager.Green_Shell[5];
		}
		left = new Animation(150).addFrame(left_1).addFrame(left_2);
		right = new Animation(150).addFrame(right_1).addFrame(right_2);
		
		final class DeadAfterAnimation extends Animation {
			public void endOfAnimationAction() {
				kill();
			}
		}
		
		dead = new DeadAfterAnimation();
		flip = new DeadAfterAnimation();
		dead.addFrame(shell, 10);
		dead.addFrame(shell, 10);
		flip.addFrame(flipped, 1200);
		flip.addFrame(flipped, 1200);
		setAnimation(left);
		//align base of creature to tile
		this.y=y-getHeight()+16;
	}
	
	@Override
	public void xCollide(Point p) {
		super.xCollide(p);
		/*
		if(currentAnimation() == left) {
			setAnimation(right);
		} else {
			setAnimation(left);
		}
		*/
		if(dx>0) {
			setAnimation(right);
		} else if(dx<0) {
			setAnimation(left);
		}
	}
	@Override
	public void creatureXCollide() {
		if(dx > 0) {
			x = x - 2;
			setAnimation(left);
		} else {
			setAnimation(right);
			x = x + 2;
		}
		dx = -dx;
	}
	
	public void flip() {
		setAnimation(flip);
		setIsFlipped(true);
		setIsCollidable(false);
		dy = -.2f;
		dx = 0;
	}
	
	public void wakeUp(boolean isLeft) {
		super.wakeUp();
			if(isLeft) {
				dx = -.03f;
				setAnimation(left);
			} else {
				dx = .03f;
				setAnimation(right);
			}
	}
	
	@Override
	protected void useAI(TileMap map){
		int tileY = GameRenderer.pixelsToTiles(y+getHeight()-1);
		if (dx>0){
			int tileX = GameRenderer.pixelsToTiles(x-1);
			if (tileX+1>=map.getWidth()){
				x = x - 2;
				setAnimation(left);
				dx=-dx;
				return;
			}
			if (map.getTile(tileX+1, tileY+1)==null &&  (map.getTile(tileX+1, tileY+2)==null)){
				x = x - 2;
				setAnimation(left);
				dx=-dx;
			}
		}else if(dx<0){
			int tileX = GameRenderer.pixelsToTiles(x+getWidth()-1);
			if (tileX-1<0){
				x = x + 2;
				setAnimation(right);
				dx=-dx;
				return;
			}
			if (map.getTile(tileX-1, tileY+1)==null &&  (map.getTile(tileX-1, tileY+2)==null)){
				x = x + 2;
				setAnimation(right);
				dx=-dx;
			}
		}
	}
	
	public void jumpedOn() {
		setAnimation(dead);
		setIsCollidable(false);
		dx = 0;
	}

	public boolean isGreen() {
		return isGreen;
	}


}
