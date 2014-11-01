package com.shikhar.androidgames.mario.objects.creatures;

import com.shikhar.androidgames.mario.core.MarioResourceManager;
import com.shikhar.androidgames.mario.core.MarioSoundManager;
import com.shikhar.androidgames.mario.core.animation.Animation;
import com.shikhar.androidgames.mario.core.tile.TileMap;
import com.shikhar.androidgames.mario.objects.base.Creature;
import android.graphics.Bitmap;
import android.graphics.Point;


public class FireDisc extends Creature {


	private Animation ball;
	private static Bitmap fd_1, fd_2, fd_3;
	private static boolean initialized = false;
    private float radius=55;
    private double theta,omega=2*Math.PI/6;
    private int cx,cy;
	public FireDisc(int x, int y, MarioSoundManager soundManager) {

		super(x, y, soundManager);
		cx=x;
		cy=y;
		theta=Math.random()*Math.PI*2;
		this.x=(float) (cx+radius*Math.cos(theta));
		this.y=(float) (cy+radius*Math.sin(theta));
		radius=(float) (radius+Math.random()*10);
		if (!initialized) {
			fd_1 =  MarioResourceManager.FireDisc[0];
			fd_2 =  MarioResourceManager.FireDisc[1];
			fd_3 =  MarioResourceManager.FireDisc[2];
			initialized = true;
		}
		ball = new Animation(200).addFrame(fd_1).addFrame(fd_2).addFrame(fd_3);
		setAnimation(ball);
		setIsItem(true);
		setGravityFactor(0);
	}

	@Override
	public void xCollide(Point p) {

	}
	
	public void creatureXCollide() {
		
	}
	
	@Override
	public void updateCreature(TileMap map, int time) {
		x=(float) (cx+radius*Math.cos(theta));
		y=(float) (cy+radius*Math.sin(theta));
		theta+=omega*time/1000.0;
		super.update(time);
	}
	
	public void jumpedOn() {

	}

}
