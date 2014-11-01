package com.shikhar.androidgames.mario.particles;

import com.shikhar.androidgames.mario.core.tile.TileMap;
import com.shikhar.androidgames.mario.objects.base.Creature;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;

public class BubbleParticle extends Creature
{
	private float r=1.5f;
	private int borderColor=Color.argb(150, 150,160,240);
	private int fillColor=Color.argb(90, 250,250,245);
	private Paint paint;
    /**
     * 
     * @param x Origin-X of particles
     * @param y Origin-Y of particles
     * @param vx Velocity-X of particles
     * @param vy Velocity-Y of particles
     * @param timeSpan
     */
    public BubbleParticle(int x, int y)
    {
    	super(x, y);
        this.x = x;
        this.y = y;
        dy=-0.1f;
        setIsItem(true);
		setIsAlwaysRelevant(true);
		setIsCollidable(false);
		this.setGravityFactor(-1f);
		paint=new Paint(fillColor);
		paint.setAntiAlias(true);
    }

    public void update(int time){
    	if (y<0) kill();
    	 dx=-1f+(float) (2*Math.random());
       	 r+=time/1800.0f;
     	 if(dy > -3f) { // apply gravity...this must be done first
			dy = dy + getGravityFactor()*GRAVITY * time;
		}
     	x+=dx;
     	y+=dy;
    	// super.update(time);
 
    }
   
       
    public void draw(Canvas g, int x, int y) {
      	paint.setStyle(Style.FILL);
    	paint.setColor(fillColor);
    	//g.drawCircle(x, y, r,paint);
    	//paint.setColor(borderColor);
    	paint.setStyle(Style.STROKE);
    	g.drawCircle(x, y, r,paint);
	}
    
    public void draw(Canvas g, int x, int y, int offsetX, int offsetY) {
		draw(g, x + offsetX, y + offsetY);
	}

	public Bitmap getImage() {
		return null;
	}
	
    public int getHeight() {
    	return 8;
    }
    
    public int getWidth() {
    	return 8;
    }
  
    public void xCollide(Point p) {}
    
    @Override
	public void creatureXCollide() {}
	
    public void updateCreature(TileMap map, int time){
    	update( time);
    }
}
