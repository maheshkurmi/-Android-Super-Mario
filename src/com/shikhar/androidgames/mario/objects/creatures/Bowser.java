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


public class Bowser extends Creature {
	
	private Animation left;
	private Animation right;
	private Animation left_flip,right_flip;
	private Animation left_fire;
	private Animation right_fire;
	private Random r;
	
	private static Bitmap left_1,left_2,left_3,left_4,right_1,right_2,right_3,right_4,jump,left_flipped,right_flipped;
	private static boolean initialized=false;
	private float t;
	public boolean readytoFire=false;
	private int health;
	private int graceTime;
	
	public Bowser(int x, int y, MarioSoundManager soundManager) {
		
		super(x, y, soundManager);
		r = new Random();
		if (!initialized){
			 left_1 = MarioResourceManager.Bowser_Left[4];
			 left_2 = MarioResourceManager.Bowser_Left[3];
			 left_3 = MarioResourceManager.Bowser_Left[2];
			 left_4 = MarioResourceManager.Bowser_Left[1];
			 left_flipped=MarioResourceManager.Bowser_Left[0];
			 right_1 = MarioResourceManager.Bowser_Right[0];
			 right_2 = MarioResourceManager.Bowser_Right[1];
			 right_3 = MarioResourceManager.Bowser_Right[2];
			 right_4 = MarioResourceManager.Bowser_Right[3];
			 right_flipped = MarioResourceManager.Bowser_Right[4];
			 initialized=true;
		}
		left = new Animation(200).addFrame(left_1).addFrame(left_2);
		right = new Animation(200).addFrame(right_1).addFrame(right_2);
		
		final class FireAnimation extends Animation {
			public void endOfAnimationAction() {
				setAnimation (dx<0?left:right);
			}
		}
			
		left_fire = new FireAnimation().setDAL(100).addFrame(left_3).setDAL(100).addFrame(left_4);
		right_fire = new FireAnimation().setDAL(100).addFrame(right_3).setDAL(100).addFrame(right_4);
	
		final class DeadAfterAnimation extends Animation {
			public void endOfAnimationAction() {
				kill();
			}
		}
		
		left_flip = new DeadAfterAnimation();
		left_flip.addFrame(left_flipped, 1200);
		left_flip.addFrame(left_flipped, 1200);
		right_flip = new DeadAfterAnimation();
		right_flip.addFrame(right_flipped, 1200);
		right_flip.addFrame(right_flipped, 1200);
		setAnimation(left);
		setHealth(3);
	}
	
	public void xCollide(Point p) {
		super.xCollide(p);
		if(currentAnimation() == left) {
			setAnimation(right);
		} else {
			setAnimation(left);
		}
	}
	
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
		if(health==0){
			setAnimation(dx>0?right_flip:left_flip);
			setIsFlipped(true);
			setIsCollidable(false);
			dy = -.2f;
			dx = 0;
			return;
		}
		if (graceTime == 0) {
			health--;
			graceTime=40;
		} 
	}
	
	public void wakeUp(boolean isLeft) {
		super.wakeUp();
		if (isLeft) {
			dx = -.03f;
			setAnimation(left);
		} else {
			dx = .03f;
			setAnimation(right);
		}
	}
	
	@Override
	public void update(int time) {
		super.update(time);
		t++;
		if (t>100){
			readytoFire=true;
			t=0;
		}
		boolean lastFour = graceTime%8 == 7 || graceTime%8 == 6 || graceTime%8 == 5 || graceTime%8 == 4;
		setIsInvisible( lastFour ? true : false);
		if(graceTime != 0) { 
			graceTime--;
		}
	}
	
	/**
	 * Return true if next 3 steps of bowser are free to move (no collision no fall)
	 * @param map
	 * @param tileX
	 * @param tileY
	 * @param alongRight
	 * @return
	 */
	private boolean checkNext3steps(TileMap map, int tileX,int tileY, boolean alongRight){
		if (alongRight){
			for (int i=1; i<=3;i++){
				if ((map.getTile(tileX+i, tileY+1)==null))return false;
				if ((map.getTile(tileX+i, tileY)!=null))return false;
				if ((map.getTile(tileX+i, tileY-1)!=null))return false;
				if ((map.getTile(tileX+i, tileY-2)!=null))return false;
				
				
			}
		}else{
			for (int i=0; i<=2;i++){
				if ((map.getTile(tileX-i, tileY+1)==null))return false;
				if ((map.getTile(tileX-i, tileY)!=null))return false;
				if ((map.getTile(tileX+i, tileY-1)!=null))return false;
				if ((map.getTile(tileX+i, tileY-2)!=null))return false;			
			}
		}
		return true;
	}
	
	@Override
	public void updateCreature(TileMap map,int time){
		if(!isFlipped()) {
			int tileX = GameRenderer.pixelsToTiles(dx>0?x-1:x + getWidth() - 1);
			int tileY = GameRenderer.pixelsToTiles(y+getHeight()-1);

			if (x<map.getPlayer().getX()-60 && dx<0){
				if (checkNext3steps(map, tileX,tileY,true)){
					dx=-dx;
					setAnimation(right);
				}
			}else if(x>map.getPlayer().getX()+80 && dx>0){
				if (checkNext3steps(map, tileX,tileY,false)){
					dx=-dx;
					setAnimation(left);
				}
			}else if (readytoFire){
				BoomRang boomrang;
				if (dx<0){
					boomrang=new BoomRang((int) x-19,(int)(y+6),-2,soundManager);
					setAnimation(left_fire);
				}else{
					boomrang=new BoomRang((int) x+getWidth()-7,(int)(y+6),2,soundManager);
					setAnimation(right_fire);
				}
				boomrang.setdY(map.getPlayer().getY()<y?-0.02f:0.015f);
				map.creaturesToAdd().add(boomrang);
				((Bowser)this).readytoFire=false;
			}else{
				if(map.getPlayer().getY()<y+getHeight()/3 && t==50){
					if (dx>0){
						if (map.getTile(tileX+1, tileY+1)!=null)dy=-0.17f-(float) (Math.random()*0.10f);
					}else{
						if (map.getTile(tileX-1, tileY+1)!=null)dy=-0.17f-(float) (Math.random()*0.10f);
					}
				}
			}
		}
		super.updateCreature(map, time);
	}
	
	public void jumpedOn() {
		flip();
	}
	
	@Override
	protected void useAI(TileMap map) {
		// don't let it fall
		int tileY = GameRenderer.pixelsToTiles(y + getHeight() - 1);
		if (dx > 0) {
			int tileX = GameRenderer.pixelsToTiles(x -1);
			if (tileX + 1 >= map.getWidth()) {
				x = x - 2;
				setAnimation(left);
				dx = -dx;
				return;
			}
			if (map.getTile(tileX + 1, tileY + 1) == null
					&& (map.getTile(tileX + 1, tileY + 2) == null)) {
				x = x - 2;
				setAnimation(left);
				dx = -dx;
			}
		} else if (dx < 0) {
			int tileX = GameRenderer.pixelsToTiles(x + getWidth() - 1);
			if (tileX - 1 < 0) {
				x = x + 2;
				setAnimation(right);
				dx = -dx;
				return;
			}
			if (map.getTile(tileX - 1, tileY + 1) == null
					&& (map.getTile(tileX - 1, tileY + 2) == null)) {
				x = x + 2;
				setAnimation(right);
				dx = -dx;
			}
		}
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getGraceTime() {
		return graceTime;
	}

	public void setGraceTime(int graceTime) {
		this.graceTime = graceTime;
	}
	
}
