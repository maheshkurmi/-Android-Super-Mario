package com.shikhar.androidgames.mario.objects.creatures;



import com.shikhar.androidgames.mario.core.MarioResourceManager;
import com.shikhar.androidgames.mario.core.animation.Animation;
import com.shikhar.androidgames.mario.core.tile.TileMap;

public class Spring extends Platform {
	protected Animation steady;
	protected Animation restore;
	boolean kickMario=false;
	int bottomY;
	float k;
	TileMap map;
	public Spring(int pixelX, int pixelY, TileMap map) {
		super(pixelX, pixelY,0,0,false);
		canJumpThrough=true;
		this.map=map;
		steady = new Animation(2000).addFrame(MarioResourceManager.Spring_1);
		
		final class DeadAfterAnimation extends Animation {
			public void endOfAnimationAction() {
			     setAnimation(steady);
			     y=bottomY-getHeight();
				 kickMario=true;
			}
		}
		
		restore= new DeadAfterAnimation().setDAL(100).addFrame(MarioResourceManager.Spring_1).setDAL(120).addFrame(MarioResourceManager.Spring_2).setDAL(100).addFrame(MarioResourceManager.Spring_3).setDAL(100).addFrame(MarioResourceManager.Spring_2).setDAL(100).addFrame(MarioResourceManager.Spring_1);
		//restore= new Animation(200).addFrame(MarioResourceManager.Spring_1).setDAL(200).addFrame(MarioResourceManager.Spring_2).setDAL(200).addFrame(MarioResourceManager.Spring_3).setDAL(200).addFrame(MarioResourceManager.Spring_2).setDAL(200).addFrame(MarioResourceManager.Spring_1);
		
		setAnimation(steady);
		bottomY=pixelY+16;
		y=bottomY-getHeight();
	}
	
	public void updateCreature(TileMap map, int time) {
	
		if (this.currentAnimation()==restore){
			this.update(time);
			y=bottomY-getHeight();
			map.getPlayer().setY(y - map.getPlayer().getHeight());
		}
		
		if ( kickMario){
			//jump mario
			map.getPlayer().setdY(-0.6f*Math.abs(k)-0.3f);
			kickMario=false;
			
		}
	}
		 
	public boolean mayfall() {
		return false;
	}

	
	public boolean isfalling() {
		return false;
	}

	public void jumpedOn() {
		if (this.currentAnimation()!=restore){
			this.setAnimation(restore);
			k=map.getPlayer().getdY();
		}
	}
	
	public void accelerateFall() {
		return;
	}


}
