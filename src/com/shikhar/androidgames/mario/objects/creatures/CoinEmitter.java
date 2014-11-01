package com.shikhar.androidgames.mario.objects.creatures;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.FloatMath;

import com.shikhar.androidgames.mario.core.MarioResourceManager;
import com.shikhar.androidgames.mario.core.MarioSoundManager;
import com.shikhar.androidgames.mario.core.animation.Animation;
import com.shikhar.androidgames.mario.core.tile.TileMap;
import com.shikhar.androidgames.mario.objects.base.Creature;


public class CoinEmitter extends Creature {


	private Animation steady, fire,flip;;
	private static Bitmap fd_1, fd_2,fd_3,fd_4, fd_5;;
	private static boolean initialized = false;
    private float t;
	public boolean readytoFire=false;
	private int A=120;
	private int dormantTime=-1;
	private int initY;
    private Random random;
	public CoinEmitter(int x, int y, MarioSoundManager soundManager) {

		super(x, y, soundManager);
		if (!initialized) {
			Bitmap[] images = MarioResourceManager.Latiku_Fire;
			fd_1 = images[0];
			fd_2 = images[1];
			images = MarioResourceManager.Latiku;
			fd_3 = images[0];
			fd_4 = images[1];
			fd_5 = images[2];
			initialized = true;
		}
		steady = new Animation(250).addFrame(fd_3).addFrame(fd_4);

		final class FireAnimation extends Animation {
			public void endOfAnimationAction() {
				setAnimation(steady);
			}
		}
		fire = new FireAnimation().setDAL(250).addFrame(fd_3).addFrame(fd_2)
				.addFrame(fd_1).addFrame(fd_2).addFrame(fd_4);
	
		flip = new Animation().addFrame(fd_5).addFrame(fd_5);		
		///flip.addFrame(fd_5, 100);
		//flip.addFrame(fd_5, 1200);
		setAnimation(steady);
		setIsItem(true);
		setGravityFactor(0);
		//setIsCollidable(false);
		setIsAlwaysRelevant(true);
		setIsItem(true);
		initY=-2*getHeight();
		//this.y=initY-getHeight();
		//this.x=x;
		random=new Random();
		//setOffsetY(-getHeight());
		this.y=map.getHeight()*16+3*getHeight();
	}

	@Override
	public void xCollide(Point p) {

	}
	
	public void creatureXCollide() {
		
	}
	
	public void wakeUp(boolean isleft) {
		super.wakeUp();
		//x=map.getWidth()8*16;
		y=map.getHeight()*16+3*getHeight();
	}

	@Override
	public void update(int time) {
		super.update(time);
		t++;
		if (t>200){
			readytoFire=true;
			t=0;
		}
	}
	
	@Override
	public void updateCreature(TileMap map, int time) {
		//update(time);
		if(random.nextInt(700)==300){
				fireRandomCoins(map);
				readytoFire=false;
		}
	}
	
	public void jumpedOn() {

	}

	private void fireRandomCoins(TileMap map){
		float theta;
		int n=12+random.nextInt(5);
		float dx,dy;
		Coin c;
		int x=(int) (map.getWidth()/2-8+Math.random()*(16))*16;
		for (int i = 0; i <= n; i++) {
			float v=(float) (0.32f+0.025f*Math.random());
			theta=(float) ((0.30+0.4*i/n)*Math.PI);
			dx=0.30f*v*FloatMath.cos(theta);
			dy=-v*FloatMath.sin(theta);
			c=new Coin(x,(int)getY(),dx,dy);
			c.setIsAlwaysRelevant(true);
			map.creaturesToAdd().add(c);
		}
		
	}

}
