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
	
	private static Bitmap stay,rotate_1,rotate_2,rotate_3, flipped;
	private static boolean initialized=false;
	public RedShell(int x, int y, TileMap map, MarioSoundManager soundManager, boolean isStill) {
		
		super(x, y, soundManager);
		this.map = map;
		setIsAlwaysRelevant(true);
 		
		if (!initialized){
			 stay = MarioResourceManager.Red_Shell_1;
			 rotate_1 = MarioResourceManager.Red_Shell_2;
			 rotate_2 = MarioResourceManager.Red_Shell_3;
			 rotate_3 = MarioResourceManager.Red_Shell_4;
			 flipped = MarioResourceManager.Red_Shell_Flip;
			 initialized=true;
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
		rotate.addFrame(rotate_1, 30);
		rotate.addFrame(stay, 30);
		rotate.addFrame(rotate_2, 30);
		rotate.addFrame(rotate_3, 30);
		rotate.addFrame(rotate_1, 30);
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
}
