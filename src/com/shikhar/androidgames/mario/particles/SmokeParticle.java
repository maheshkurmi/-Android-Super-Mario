package com.shikhar.androidgames.mario.particles;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.shikhar.androidgames.mario.core.MarioResourceManager;
import com.shikhar.androidgames.mario.objects.base.Creature;

public class SmokeParticle extends Creature
{
    private int life;
    private int xPic;
    private int xPicStart;
    private int delayTime;
    public SmokeParticle(int x, int y, float dx, float dy,int delay)
    {
    	super(x, y);
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.xPic = (int)(Math.random()*2);
        this.delayTime=delay;
        setIsItem(true);
		setIsAlwaysRelevant(true);
		setIsCollidable(false);
		this.gravityEffect=0.02f;
        int timespan=5;
        life = 10+(int)(Math.random()*timespan);
    }

    public void update(int time){
    	delayTime--;
    	if (delayTime>0) return;
    	if (life>10)
             xPic = 7;
         else
             xPic = xPicStart+(10-life)*4/10;

         if (life--<0) kill();//system.removeSprite(this);

         x+=dx;
         y+=dy;
    }
   
    public void updateCreature()
    {
       
    }
    
    public void draw(Canvas g, int x, int y) {
    	delayTime--;
    	if (delayTime>0) return;
    	g.drawBitmap(MarioResourceManager.Smoke_Particles[xPic], x, y, null);
	}
    
 	public Bitmap getImage() {
		return MarioResourceManager.Smoke_Particles[xPic];
	}
	
    public int getHeight() {
    	return 8;
    }
    
    public int getWidth() {
    	return 8;
    }
  
}
