package com.shikhar.androidgames.mario.core.animation;


import com.shikhar.androidgames.framework.Input.KeyEvent;

import android.graphics.Bitmap;
import android.graphics.Canvas;


public class Sprite extends Animatible {
	
	protected float x;
    protected float y;
    protected float dx;
    protected float dy;
	
	public Sprite() { 
		this(0, 0);
	}
	
	public Sprite(int x, int y) {
		this.x = x;
		this.y = y;
		dx = 0;
		dy = 0;
	}
	
	public void draw(Canvas g, int x, int y) {
		g.drawBitmap(currentAnimation().getImage(), x, y, null);
	}
	
	public void draw(Canvas g, int x, int y, int offsetX, int offsetY) {
		draw(g, x + offsetX, y + offsetY);
		
	}
	public Bitmap getImage() {
		return (currentAnimation() == null) ? null : currentAnimation().getImage();
	}
	
    public float getX() {
        return x;
    }
    
    public void setX(float x) {
    	this.x = x;
    }
    
    public float getY() {
        return y;
    }
    
    public void setY(float y) {
    	this.y = y;
    }
    
    public float getdX() {
    	return dx;
    }
    
    public void setdX(float dx) {
    	this.dx = dx;	
    }
    
    public void setdY(float dy) {
    	this.dy = dy;
    }
    
    public float getdY() {
    	return dy;
    }
    
    public int getHeight() {
    	return currentAnimation().getHeight();
    }
    
    public int getWidth() {
    	return currentAnimation().getWidth();
    }
    
    public void keyReleased(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {} 
    public void keyTyped(KeyEvent e) {} 
    
	// Checks simple collision between sprites.
	// Checks if two Sprites collide with one another. Returns false if the two Sprites 
	// are the same. Returns false if one of the Sprites is a Creature that is not alive.
	public static boolean isCollision(Sprite s1, Sprite s2) {
	    // if the Sprites are the same, return false
	    if (s1 == s2) {
	        return false;     
	    }
	
	    // get the pixel location of the Sprites
	    int s1x = Math.round(s1.getX());
	    int s1y = Math.round(s1.getY());
	    int s2x = Math.round(s2.getX());
	    int s2y = Math.round(s2.getY());
	
	    // check if the two sprites' boundaries intersect
	    return (s1x < s2x + s2.getWidth() && s2x < s1x + s1.getWidth() && 
	    		s1y < s2y + s2.getHeight() && s2y < s1y + s1.getHeight());
 
	}
}

