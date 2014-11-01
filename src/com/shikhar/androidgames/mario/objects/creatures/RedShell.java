package com.shikhar.androidgames.mario.objects.creatures;



import android.graphics.Bitmap;
import android.graphics.Point;

import com.shikhar.androidgames.mario.core.MarioResourceManager;
import com.shikhar.androidgames.mario.core.MarioSoundManager;
import com.shikhar.androidgames.mario.core.animation.Animation;
import com.shikhar.androidgames.mario.core.tile.GameTile;
import com.shikhar.androidgames.mario.core.tile.TileMap;
import com.shikhar.androidgames.mario.objects.base.Creature;







public class RedShell extends Creature {
	
	private Animation still;
	private Animation rotate;
	private Animation flip;
	
	private TileMap map;
	private boolean isMoving;
	private boolean isGreen=false;
	private  Bitmap stay,rotate_1,rotate_2,rotate_3,rotate_4, flipped;
	private static boolean initialized=false;
	private float dormantTime=0;
	public int creatureHitcount=0;
	public RedShell(int x, int y, TileMap map, MarioSoundManager soundManager, boolean isStill, boolean isGreen) {
		super(x, y, soundManager);
		this.map = map;
		setIsAlwaysRelevant(true);
		this.isGreen=isGreen;
			if (!isGreen){
			 stay = MarioResourceManager.Red_Shell_Still;
			 rotate_1 = MarioResourceManager.Red_Shell_2;
			 rotate_2 = MarioResourceManager.Red_Shell_3;
			 rotate_3 = MarioResourceManager.Red_Shell_4;
			 rotate_4 = MarioResourceManager.Red_Shell_1;
				
			 flipped = MarioResourceManager.Red_Shell_Flip;
		    }else{
		     stay = MarioResourceManager.Green_Shell[4];
			 rotate_1 = MarioResourceManager.Green_Shell[1];
			 rotate_2 = MarioResourceManager.Green_Shell[3];
			 rotate_3 = MarioResourceManager.Green_Shell[2];
		     rotate_4 = MarioResourceManager.Green_Shell[0];
			 flipped = MarioResourceManager.Green_Shell[5];
		    }
		final class DeadAfterAnimation extends Animation {
			public void endOfAnimationAction() {
				kill();
			}
		}
		
		still = new Animation();
		rotate = new Animation();
		flip = new DeadAfterAnimation();
		
		still.addFrame(stay, 150);
		rotate.addFrame(rotate_1, 40);
		rotate.addFrame(rotate_4, 40);
		rotate.addFrame(rotate_2, 40);
		rotate.addFrame(rotate_3, 40);
		rotate.addFrame(rotate_1, 40);
		flip.addFrame(flipped, 1200);
		flip.addFrame(flipped, 1200);
		
		wakeUp();
		isMoving = false;
		setAnimation(still);
		dx = 0;
	}
	
	public boolean isMoving() {
		return isMoving;
	}
	
	public void xCollide(Point p) {
		super.xCollide(p);
		GameTile tile = map.getTile(p.x, p.y);
		if(this.isOnScreen()) {
			soundManager.playBump();
			if(tile != null) {
				tile.doAction();
			}
		}
	}
	
	public void flip() {
		setAnimation(flip);
		setIsFlipped(true);
		setIsCollidable(false);
		dy = -.2f;
		dx = 0;
	}
	
	// if you run or jump on the shell faster, the shell moves faster.
	public void jumpedOn(boolean fromRight, float attackerSpeed) {
		if(isMoving) {
			isMoving = false;
			setAnimation(still);
			dx = 0;
		} else {
			isMoving = true;
			setAnimation(rotate);
			if(fromRight) {
				if(attackerSpeed > .2f) {
					dx = .24f;
				} else if(attackerSpeed > .16) { 
					dx = .23f;
				} else {
					dx = .16f;
				}
			} else {
				if(attackerSpeed < -.2f) {
					dx = -.24f;
				} else if(attackerSpeed < -.16) {
					dx = -.23f;
				} else {
					dx = -.16f;
				}
			}
		}
	}

	public boolean isGreen() {
		return isGreen;
	}

	@Override
	protected void useAI(TileMap map){
		if (currentAnimation()==still) {
			dormantTime++;
			if (dormantTime>=500){
				dormantTime=0;
				kill();
				map.creaturesToAdd().add(new RedKoopa(Math.round(getX()), 
						Math.round(getY()-13), soundManager, isGreen()));
				soundManager.playGulp();
					return;
			}
		}else{
			dormantTime=0;
		}
	}
}
