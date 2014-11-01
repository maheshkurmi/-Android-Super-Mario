package com.shikhar.androidgames.mario.objects.creatures;



import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.FloatMath;

import com.shikhar.androidgames.mario.core.GameRenderer;
import com.shikhar.androidgames.mario.core.MarioResourceManager;
import com.shikhar.androidgames.mario.core.animation.Animation;
import com.shikhar.androidgames.mario.core.tile.TileMap;
import com.shikhar.androidgames.mario.objects.base.Creature;





public class Score extends Creature {
	
	public Animation oneHundred;
	private static Bitmap one_hundred;
	/**type=0 score100, 2=+1up, 3=200*/
	private int type;
	private float t;
	private int initY;
	private String msg="";
	public Score(int x, int y, String msg) {
		this(x, y,-1);
		setIsItem(true);
		initY=y-5;
		type=-1;
		this.msg=msg;
	}

	public Score(int x, int y, int type) {
		super(x, y);
		setIsItem(true);
		initY=y-5;
		this.type=type;
		dy = -.45f;
		switch(type){
		case 0:
			one_hundred = MarioResourceManager.Score_100_New6;
			break;
		case 1:
			one_hundred = MarioResourceManager.Score_1_Up;
			dy = -.25f;
			break;
		case 2:
			one_hundred = MarioResourceManager.Score_1000;
			dy = -.25f;
			break;
		case 3:
			one_hundred = MarioResourceManager.Score_200;
			dy = -.1f;
			break;
				
		default:
			one_hundred = MarioResourceManager.Score_100_New6;
			break;
		}
		
		final class DeadAfterAnimation extends Animation {
			public void endOfAnimationAction() {
				kill();
			}
		}
		
		oneHundred = new DeadAfterAnimation();
		oneHundred.addFrame(one_hundred, type==0?380:600);
		oneHundred.addFrame(one_hundred, type==0?380:400);	
		setAnimation(oneHundred);
	}
	
	@Override
	public void draw(Canvas g, int x, int y) {
		if (type==-1){
			GameRenderer.drawStringDropShadow(g, msg, x, y,1,0);
		}else{
			super.draw(g, x+getWidth()/2, y);
		}
	}
	
	public void updateCreature(TileMap map, int time) {
		this.update((int) time);
		if(type!=0){
			if (t<0.8)t+=time/1000.0f;
			if (type>0)
				y= initY-15+15*FloatMath.cos(4*t);
			else{
				y= initY-25+25*FloatMath.cos(4*t);
			}
			return;
		}
		y = y + dy * time;
		if(dy < 0) {
			if(type!=3)dy = dy + .032f;
			else
				dy=0.8f*dy;
		} else {
			dy = 0;
		}
	}

}
