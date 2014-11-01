package com.shikhar.androidgames.mario.particles;


import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;

class Particle {
	protected float x;
    protected float y;
    protected float dx;
    protected float dy;
	
	private Bitmap bitmap;
    protected static float GRAVITY = .0007f; //0.0008f
    protected static float gravityEffect = .20f; 
	
	public Particle(int x, int y, float dx, float dy, Bitmap bmp) {
		this.dx=dx;
		this.dy=dy;
		this.x=x;
		this.y=y;
		//this.direction = Math.PI * new Random().nextFloat();//.nextInt(NO_OF_DIRECTION)/ NO_OF_DIRECTION;
		//this.speedX =(float) (speed*Math.cos(direction));
		//this.speedY = -(float) (speed*Math.sin(direction));
		//this.color = new Random().nextInt(3);
		bitmap = bmp;
	}


	public void updatePhysics(int time ) {
		if(dy < gravityEffect) { // apply gravity...this must be done first
			dy = dy*0.95f + GRAVITY * time;
		}
	
		x =  x + dx *time;
		y =  y + dy * time;
	}

	public void doDraw(Canvas canvas,int offsetX,int offsetY) {
		canvas.drawBitmap(bitmap, offsetX+x,offsetY+y, null);
	}

}