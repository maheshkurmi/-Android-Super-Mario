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
import com.shikhar.androidgames.mario.util.SpriteMap;





public class FlyGoomba extends Creature {
	
	private Animation waddle;
	private Animation fly;
	private Animation walk;

	private Animation dead;
	private Animation flip;
	private Random r;
	
	private static Bitmap img1,img2,img3,smashed,flipped;;
	
	private static boolean initialized=false;
	public boolean canFly=true;
	private float dormantTime=0;
	public FlyGoomba(int x, int y, MarioSoundManager soundManager) {
		
		super(x, y, soundManager);
		r = new Random();
		if (!initialized){
			Bitmap[] l= new SpriteMap( MarioResourceManager.Fly_Goomba,3,1).getSprites();
			img1=l[0];
			img2=l[1];
			img3=l[2];
			smashed = MarioResourceManager.Goomba_Dead;
			flipped = MarioResourceManager.Goomba_Flip;
			initialized=true;
		}
		fly = new Animation(300).addFrame(img1).setDAL(200).addFrame(img2).setDAL(200).addFrame(img3);
		walk = new Animation(300).addFrame(img2).setDAL(200).addFrame(img3);
	
		waddle=fly;
		final class DeadAfterAnimation extends Animation {
			public void endOfAnimationAction() {
				kill();
			}
		}
		dead = new DeadAfterAnimation().setDAL(100).addFrame(smashed).setDAL(20).addFrame(smashed);
		flip = new Animation().addFrame(flipped).addFrame(flipped);

		setAnimation(waddle);
		setGravityFactor(0.4f);
	}
	

	public void flip() {
		setAnimation(flip);
		setIsFlipped(true);
		setIsCollidable(false);
		setGravityFactor(0.7f);
		canFly=false;
		dy = -.2f;
		dx = 0;
	}
	
	public void wakeUp(boolean isLeft) {
		super.wakeUp();
			if(isLeft) {
				dx = -.035f;
			} else {
				dx = .035f;
			}
		//dy=-0.15f;
	}
	
	public void jumpedOn() {
		if (canFly){
			dormantTime=0;
			waddle=walk;
			canFly=false;
			setGravityFactor(0.7f);
			dy=0;
			dormantTime=0;
		}else{
			setAnimation(dead);
			canFly=false;
			setIsCollidable(false);
			dx = 0;
		}
	}
	
	@Override
	protected void useAI(TileMap map){
		if (!canFly) {
			dormantTime++;
			if (dormantTime>=800){
				dormantTime=0;
				waddle=fly;
				canFly=true;
				setGravityFactor(0.4f);
				dy=-0.2f;
				return;
			}
		}
		
	}
	
}
