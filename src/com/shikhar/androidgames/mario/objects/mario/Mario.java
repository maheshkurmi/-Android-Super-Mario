package com.shikhar.androidgames.mario.objects.mario;



import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.shikhar.androidgames.mario.core.GameRenderer;
import com.shikhar.androidgames.mario.core.MarioResourceManager;
import com.shikhar.androidgames.mario.core.MarioSoundManager;
import com.shikhar.androidgames.mario.core.Settings;
import com.shikhar.androidgames.mario.core.animation.Animation;
import com.shikhar.androidgames.mario.core.animation.CollidableObject;
import com.shikhar.androidgames.mario.core.tile.GameTile;
import com.shikhar.androidgames.mario.core.tile.TileMap;
import com.shikhar.androidgames.mario.objects.base.Collision;
import com.shikhar.androidgames.mario.objects.base.Creature;
import com.shikhar.androidgames.mario.objects.creatures.Coin;
import com.shikhar.androidgames.mario.objects.creatures.FireBall;
import com.shikhar.androidgames.mario.objects.creatures.Goomba;
import com.shikhar.androidgames.mario.objects.creatures.Mushroom;
import com.shikhar.androidgames.mario.objects.creatures.Platform;
import com.shikhar.androidgames.mario.objects.creatures.RedKoopa;
import com.shikhar.androidgames.mario.objects.creatures.RedShell;
import com.shikhar.androidgames.mario.objects.creatures.Score;
import com.shikhar.androidgames.mario.objects.creatures.Thorny;
import com.shikhar.androidgames.mario.objects.tiles.Brick;
import com.shikhar.androidgames.mario.objects.tiles.QuestionBlock;
import com.shikhar.androidgames.mario.objects.tiles.RotatingBlock;



/**
 * Mario is the main object in the game and is the center of the screen and attention at all
 * time. As a result, he is also the most complicated object in terms of animation, collision detection,
 * user input etc. 
 */

public class Mario extends CollidableObject{
	
	/* Static Constant Fields.
	 * Gravity:   Effects the amount of pull objects feel toward the ground. pixels/ms
	 * Friction:  Effects the amount of sliding an object displays before coming to a stop.
	 * S_X:       Starting X position of Mario.
	 * S_Y:       Starting Y position of Mario.
	 * S_DY:      Starting Dy of Mario.
	 * S_JH:      Effects the height of Mario's first jump.
	 * Anim_Time: The time between each of Mario's Animations. 
	 * 
	 * Terminal_Walking_Dx:  Max speed when Mario is walking.
	 * Terminal_R3unning_Dx:  Max speed when Mario is running.
	 * Terminal_Fall_Dy:     Max speed Mario can fall at.
	 * Walking_Dx_Inc:       The increase in speed per update when walking until terminal runnning is reached.
	 * Running_Dx_Inc:       The increase in speed per update when running until terminal walking is reached.
	 * Start_Run_Anim_Thres: The speed where mario switches to the running animation.
	 */

	public static final float GRAVITY = 0.0008f;
	public static final float FRICTION = 0.0004f;                   
	private static final int STARTING_X = 25;
	private static final int STARTING_Y = 140;
	private static final float STARTING_DY = .03f;
	private static final float INITIAL_JUMP_HEIGHT = -.34f; 
	private static final float JUMP_MULTIPLIER = .46f;
	private static final float TERMINAL_WALKING_DX = .10f;
	private static final float WALKING_DX_INC = .01f;
	private static final float TERMINAL_RUNNING_DX = .21f;
	private static final float START_RUN_ANIM_THRESHOLD = .2f;
	private static final float RUNNING_DX_INC = .001f;
	private static final float TERMINAL_FALL_DY = .22f;
	private static final int STARTING_LIFE = 3;
	private static final int ANIM_TIME = 125;
	
	/*boolean variable to identify if fireball is to be fired*/
	private boolean loadFireBall=false;

	/* INITIAL_JUMP_HEIGHT + dx*JUMP_MULTIPLIER */
	private float jumpHeight; 
	
	/* Boolean variables used to identify which keys are pressed. */
	private boolean isDownHeld, isRightHeld, isLeftHeld, isShiftHeld, isSpaceHeld;
	/* Boolean variables used to identify where Mario is with respect to Platforms. */
	private boolean isRightOfPlatform, isLeftOfPlatform, isBelowPlatform, isAbovePlatform;
	/* Boolean variables used to identify where Mario is with respect to Slopes. */
	private boolean isUpSlope, isDownSlope, onSlopedTile;
	/* Boolean variables used to identify the state of Mario. */
	private boolean isJumping, frictionLock, isInvisible;
	
	/* Animation variables. */
	private Animation walkLeft, runLeft, stillLeft, jumpLeft, crouchLeft, changeLeft, currLeftAnim,fireLeftAnim, fireRightAnim;;
	private Animation walkRight, runRight, 
	stillRight, jumpRight, crouchRight, changeRight, currRightAnim;
	
	private int health;
	private int grace;
	private Platform platform;
	/*boolean which keeps track of direction of mario*/
    private boolean left=false;
    
	public Mario(MarioSoundManager soundManager) {
		
		super(STARTING_X, STARTING_Y, soundManager);
		setIsJumping(true);
		dy = STARTING_DY;
		jumpHeight = INITIAL_JUMP_HEIGHT;
		health = STARTING_LIFE;
		
		Bitmap[] l = { MarioResourceManager.Mario_Big_Left_Still, MarioResourceManager.Mario_Big_Left_1,
				MarioResourceManager.Mario_Big_Left_2, MarioResourceManager.Mario_Big_Left_Run_1,
				MarioResourceManager.Mario_Big_Left_Run_2, MarioResourceManager.Mario_Big_Crouch_Left,
				MarioResourceManager.Mario_Big_Jump_Left, MarioResourceManager.Mario_Big_Change_Direction_Left };
		Bitmap[] r = { MarioResourceManager.Mario_Big_Right_Still, MarioResourceManager.Mario_Big_Right_1,
				MarioResourceManager.Mario_Big_Right_2, MarioResourceManager.Mario_Big_Right_Run_1,
				MarioResourceManager.Mario_Big_Right_Run_2, MarioResourceManager.Mario_Big_Crouch_Right,
				MarioResourceManager.Mario_Big_Jump_Right, MarioResourceManager.Mario_Big_Change_Direction_Right };
		/*
		Bitmap[] r = { null, null, null, null, null, null, null, null };
		for(int i = 0; i < l.length; i++) {
			r[i] = ResourcesManager.horizontalFlip(l[i]); // Flip every image in l.
		}
		*/		
		// Create left animations.
    	stillLeft = new Animation(ANIM_TIME).addFrame(l[0]);
		walkLeft = new Animation(ANIM_TIME).addFrame(l[1]).addFrame(l[2]);
		runLeft = new Animation(ANIM_TIME - 30).addFrame(l[3]).addFrame(l[4]);
		crouchLeft = new Animation(ANIM_TIME).addFrame(l[5]);
		jumpLeft = new Animation(ANIM_TIME).addFrame(l[6]);
		changeLeft = new Animation(ANIM_TIME).addFrame(l[7]);
		
		// Create right animations.
		stillRight = new Animation(ANIM_TIME).addFrame(r[0]);
		walkRight = new Animation(ANIM_TIME).addFrame(r[1]).addFrame(r[2]);
		runRight = new Animation(ANIM_TIME - 30).addFrame(r[3]).addFrame(r[4]);
		crouchRight = new Animation(ANIM_TIME).addFrame(r[5]);
		jumpRight = new Animation(ANIM_TIME).addFrame(r[6]);
		changeRight = new Animation(ANIM_TIME).addFrame(r[7]);
		
		setAnimation(stillRight);
		currLeftAnim = walkLeft;
		currRightAnim = walkRight;
		
		final class FireAnimation extends Animation {
			public void endOfAnimationAction() {
				setAnimation(left?stillLeft:stillRight);
			}
		}
		fireLeftAnim = new FireAnimation().setDAL(100).addFrame(l[6]).setDAL(100).addFrame(l[2]);
		fireRightAnim = new FireAnimation().setDAL(100).addFrame(r[6]).setDAL(100).addFrame(r[2]);

	}
	
	public int getHealth() {
		return health;
	}
	
	public boolean isInvisible() {
		return isInvisible;
	}
	
	public boolean isOnSlopedTile() { return onSlopedTile; }

	public void setIsJumping(boolean isJumping) { this.isJumping = isJumping; }

	public boolean isJumping() { return isJumping; }
	
	private void slowSpeed(int slowFactor) { setdX(getdX()/slowFactor);	}
	
	private void accelerateFall() { setdY(-getdY()/4); }
	
	public boolean isAbovePlatform() { return isAbovePlatform; }
	
	/**
	 * Debugging method used to print the status of mario with regards to platforms.
	 */
	private void platformStatus() {
		
		if(isAbovePlatform) { System.out.println("Above a platform"); }
		if(isBelowPlatform) { System.out.println("Below a platform"); }
		if(isLeftOfPlatform) { System.out.println("Left of a platform"); }
		if(isRightOfPlatform) { System.out.println("Right of a platform"); }
	}
	
	/**
	 * Fixes Y movement on tiles and platforms where animation height changes by setting the mario's y
	 * value to the difference between animation heights. 
	 */
	public void setAnimation(Animation newAnim) {
		if(currentAnimation() != null) {
			Animation currAnim = currentAnimation();
			int oldHeight = currAnim.getHeight();
			int newHeight = newAnim.getHeight();
			if(newHeight > oldHeight) {
				setY(getY() - (newHeight - oldHeight));	
			} else if(oldHeight > newHeight) {
				setY(getY() + oldHeight - newHeight);
			}
		}
		super.setAnimation(newAnim);
	}
	
	/**
	 * Given the float parameter oldX, oldY, newX, and newY this method returns the first 
	 * Platform in the TileMap map in which the sprite with the given parameters collides with
	 * in the X direction, if any. 
	 * 
	 * Note to self: the exact conditions for a collision are a little fuzzy....document this...
	 * 
	 * @modifies isLeftOfPlatform && isRightOfPlatform
	 * @return the Platform the sprite with the given parameters is colliding with in the X
	 * direction.
	 */
	private Platform getPlatformCollisionX(TileMap map, float oldX, float oldY, float newX, float newY) {
		
		for(Platform platform : map.platforms()) {
	    	float width = getWidth();
	    	float height = getHeight();
	    	float pX = platform.getX();
	    	float pY = platform.getY();
	    	float oldpX = platform.getOldX();
	    	float pWidth = platform.getWidth();
	    	float pHeight = platform.getHeight();
	    	
	    	if(oldX + width <= oldpX && // This is needed to make transparant platforms work
		       !platform.canJumpThrough() &&
	    	   newX + width >= pX && newX + width <= pX + pWidth &&
	    	   pY + pHeight > oldY && pY < oldY + height
	    	   ) {
	    		this.isLeftOfPlatform = true;
	    		this.isRightOfPlatform = false;
	    		return platform;
	    	} else if (
	    	   oldX >= oldpX + pWidth && // This is needed to make transparant platforms work
	    	   !platform.canJumpThrough() &&
	    	   newX <= pX + pWidth && newX >= pX &&
	    	   pY + pHeight > oldY && pY < oldY + height
	    	   ) {
	    		this.isRightOfPlatform = true;
	    		this.isLeftOfPlatform = false;
	    		return platform;
	    	}
	    }
	    this.isRightOfPlatform = false;
	    this.isLeftOfPlatform = false;
	    return null;
	}
	
	/**
	 * Given the float parameter oldX, oldY, newX, and newY this method returns the first 
	 * Platform in the TileMap map in which the sprite with the given parameters collides with
	 * in the Y direction, if any. 
	 * 
	 * Note to self: the exact conditions for a collision are a little fuzzy....document this...
	 * Vertically moving platforms aren't compatible with tiles, so don't use them in conjunction.
	 * 
	 * @modifies isLeftOfPlatform && isRightOfPlatform
	 * @return the Platform the sprite with the given parameters is colliding with in the X
	 * direction.
	 */
	private Platform getPlatformCollisionY(TileMap map, float oldX, float oldY, float newX, float newY) {
		for(Platform platform : map.platforms()) {
	    	float width = getWidth();
	    	float height = getHeight();
	    	float pX = platform.getX();
	    	float pY = platform.getY();
	    	float oldpY = platform.getOldY();
	    	float pWidth = platform.getWidth();
	    	float pHeight = platform.getHeight(); 
	    	
	    	// some debug code:
	    	// System.out.println((oldY + height) + " >= " + (pY));
	    	// System.out.println(oldY + height);
	    	// System.out.println((newY + height) + " <= " + (pY));
	    	// System.out.println((newY + height) + " <= " + (pY + pHeight));
	    	
	    	if(//oldY + height <= pY && // This line makes vertical platforms buggy.
	    	   newY + height >= pY &&
	    	   newY + height <= pY + pHeight &&
	    	   oldX + width >= pX &&
	    	   oldX <= pX + pWidth &&
	    	   oldY + height <= oldpY) { 
		    	this.isAbovePlatform = true;
		    	this.isBelowPlatform = false;
		    	return platform;
	    	} else if(!platform.canJumpThrough()) {
	    	   if (oldY >= oldpY + pHeight && 
	    		   newY <= pY + pHeight &&
	    		   newY >= pY &&
		           oldX + width >= pX &&
		           oldX <= pX + pWidth) {
		        	   this.isBelowPlatform = true;
		        	   this.isAbovePlatform = false;
		        	   return platform;
		           }
	    	}
	    }
	    this.isBelowPlatform = false;
	    this.isAbovePlatform = false;
	    return null;
	}
	
	public void update(TileMap map, float time) {
		
		if (loadFireBall && FireBall.fireballsCount<=4){
			loadFireBall=false;
			map.creaturesToAdd().add(new FireBall((int) (left?x:x+getWidth()),(int)(y+getHeight()*0.13f),left?-2:2,soundManager));
			if (left){
				setAnimation(fireLeftAnim);
			}else{
				setAnimation(fireRightAnim);
			}
			soundManager.playFireBall();
		}
		
		jumpHeight = INITIAL_JUMP_HEIGHT - Math.abs(dx)*JUMP_MULTIPLIER; 
		
		if (!frictionLock && isLeftHeld && !isShiftHeld) {
			//toggleMovement(1);
			if (dx < -TERMINAL_WALKING_DX) {
				dx = dx + WALKING_DX_INC;
			} else if (dx > -TERMINAL_WALKING_DX) {
				dx = dx - WALKING_DX_INC;
			}
		} else if (!frictionLock && isRightHeld && !isShiftHeld) {
			//toggleMovement(1);
			if (dx > TERMINAL_WALKING_DX) {
				dx = dx - WALKING_DX_INC;
			} else if(dx < TERMINAL_WALKING_DX) {
				dx = dx + WALKING_DX_INC;
			}
		} else if (isLeftHeld && isShiftHeld && !frictionLock) {
			if (dx > -TERMINAL_WALKING_DX) {
				dx = dx - WALKING_DX_INC;
			} else if (dx > -TERMINAL_RUNNING_DX) {
				if (dx < -START_RUN_ANIM_THRESHOLD) {
					toggleMovement(2);
				}
				dx -= RUNNING_DX_INC;
			}
		} else if (isRightHeld && isShiftHeld && !frictionLock) {
			if (dx < TERMINAL_WALKING_DX) {
				dx = dx + WALKING_DX_INC;
			}
			if (dx < TERMINAL_RUNNING_DX) {
				if (dx > START_RUN_ANIM_THRESHOLD) {
					toggleMovement(2);
				}
				dx += RUNNING_DX_INC;
			}
		} else {
			toggleMovement(1);
			if (dx != 0) {
				frictionLock = true;
				if (dx > -.05f && dx < .05f) {
					dx = 0;
					frictionLock = false;
				} else if (dx > .005f) {
					dx = dx - FRICTION * time;
				} else if (dx < -.005f) {
					dx = dx + FRICTION * time;
				}
			}
		}
	
		boolean lastFour = grace%8 == 7 || grace%8 == 6 || grace%8 == 5 || grace%8 == 4;
		isInvisible = lastFour ? true : false;
		if(grace != 0) { grace--; }
		
		// Apply gravity.
		if(getdY() < TERMINAL_FALL_DY) { setdY(getdY() + GRAVITY * time); } 
		
		// Slowly reset offset values caused by slopes.
		if(getOffsetX() != 0) { setOffsetX(getOffsetX() - 1);} 

		// Calculate the new X position.
		float oldX = getX();
		float newXCalc = oldX + getdX()*time;
		if(platform != null) { newXCalc += platform.getdX() * time; } 
		// Calculate the new Y position.
		float oldY = getY();
		float newYCalc = oldY + getdY()*time;
		//if(platform != null) { newYCalc = platform.getY() - getHeight(); }
		
		// Calculate all the tile collisions.
		ArrayList<Point> xTile = GameRenderer.getTileCollisionAll(map, this, getX(), getY(), newXCalc, getY());
		ArrayList<Point> yTile = GameRenderer.getTileCollisionAll(map, this, getX(), getY(), getX(), newYCalc); 
		int numOfXTiles = xTile.size();
		int numOfYTiles = yTile.size();

		Platform platformX = getPlatformCollisionX(map, oldX, oldY, newXCalc, newYCalc);
		Platform platformY = getPlatformCollisionY(map, oldX, oldY, newXCalc, newYCalc);
		
		//this.platformStatus();
		
		if(isAbovePlatform) {
			platform = platformY;
		} else {
			platform = null;
		}
		
		// Manage collision in the X direction.
		if(oldX < 0) { // Collision with left side of map.
			setX(GameRenderer.tilesToPixels(0));
			slowSpeed(20);
		} else if(oldX > GameRenderer.tilesToPixels(map.getWidth()) - 21) { // Collision with right side of map.
			setX(GameRenderer.tilesToPixels(map.getWidth()) - 21);
			slowSpeed(20);
		} else {
			if(numOfXTiles == 0) { // No tile collision in the X direction
				setX(newXCalc);
			} else if(numOfXTiles >= 1) { // Tile collision in the X direction. For now, only worry
										  // about the first tile being collided with.
				
				Point xtp = xTile.get(0); // xTilePoint
				Collision c = Creature.tileCollisionX(map.getTile(xtp.x, xtp.y), this);
				toggleMovement(1);
				frictionLock = false;
				if(c == Collision.EAST) { // Left of a tile.
					setX(GameRenderer.tilesToPixels(xtp.x) - getWidth());
				} else if(c == Collision.WEST) { // Right of a tile.
					setX(GameRenderer.tilesToPixels(xtp.x + 1));
				}
				if(!isAbovePlatform) { setdX(0); } // Stop movement only if mario isn't on a Platform
												   // Why do this? If I don't mario gets frozen to tiles
												   // he X collides with while on a platform.
			}
			// Platform collision in X direction.
			if (platformX != null) { 
				slowSpeed(2);
				if(isLeftOfPlatform) {
					setX(platformX.getX() - getWidth() - 1);
				} else if(isRightOfPlatform) {
					setX(platformX.getX() + platformX.getWidth() + 1);
				}
			} 
		}
		
		super.update((int) time); // Update mario's animation.
		
		// Manage collision in the Y direction. 
		boolean upperCollision = false; // will check if mario is above a tile
		if(oldY > GameRenderer.tilesToPixels(map.getHeight()) - getHeight()) { // Off the bottom of the map.
			System.out.println("Mario has died.");
			Settings.subtractLife();
		} else { // No Y collision, allow Y position to update uninterrupted.
			if(numOfYTiles == 0) {
				setY(newYCalc);
				setIsJumping(true);
				jump();
			} else if(numOfYTiles >= 1) { // Y collision detected with a tile 
				Point ytp = yTile.get(0); // yTilePoint
				Collision c = Creature.tileCollisionY(map.getTile(ytp.x, ytp.y), this);
				fixJumping();
				if(c == Collision.NORTH) { // Downward collision with tile.
					upperCollision = true;
					setIsJumping(false);
					setY(GameRenderer.tilesToPixels(ytp.y) - getHeight()); 
				} else if (c == Collision.SOUTH) { // Upward collision with tile.
					for(Point p : yTile) {
						GameTile tile = map.getTile(p.x, p.y);
						if(tile != null) { 
							if (tile instanceof Brick ||tile instanceof QuestionBlock || tile instanceof RotatingBlock) marioToTileToBaddieCollide(tile);
							tile.doAction(); 
						}
					}
					setY(GameRenderer.tilesToPixels(ytp.y + 1));
					soundManager.playBump();
					accelerateFall(); 
				}
			}
			// Platform collision in Y direction.
			if (platformY != null && !upperCollision) { 
				fixJumping();
				if(isAbovePlatform) { // Downward collision with platform.
					setIsJumping(false);
					setY(platformY.getY() - getHeight());
				} else if (isBelowPlatform) { // Upward collision with platform.
					soundManager.playBump();
					setY(platformY.getY() + platformY.getHeight() + 1);
					accelerateFall(); 
				}
			}
		}
	}

	// called from within marioUpdate() when mario hits a block while moving up.
	public void marioToTileToBaddieCollide(GameTile tile) {
		List<Creature> toRemove = new LinkedList<Creature>();
		for(Creature c : tile.collidingCreatures()) {
			if(c instanceof Goomba || c instanceof RedKoopa || c instanceof RedShell || c instanceof Thorny) {
				c.flip();
				toRemove.add(c);
				//soundManager.playKick();
			}  
		}
		for(Creature c : toRemove) { tile.collidingCreatures().remove(c); }
	}	
	
	// Determines what happens when mario collides with a creature.
	public void playerCollision(TileMap map, Creature creature) {
		
		// only check collision of creatures with this that are not sleeping, are on the screen, and are collidable
		if(!creature.isPlatform() && creature.isCollidable()) { 
			boolean collision = isCollision(this, creature);
			if(collision && !(creature instanceof Score)) {
			
				if(creature instanceof Coin) {
					creature.kill();
					soundManager.playCoin();
					Settings.addScore(Settings.COIN_BONUS);
					map.creaturesToAdd().add(new Score(Math.round(creature.getX()), Math.round(creature.getY()+13)));
					
				} else if(creature instanceof Mushroom) {
					soundManager.playCelebrate();
					Settings.addLife();
					creature.kill();
					if(health == 3) {
						soundManager.playBonusPoints();
						Settings.addScore(Settings.LIFE_BONUS);
						map.creaturesToAdd().add(new Score(Math.round(creature.getX()), Math.round(creature.getY()+13)));
					} else {
						soundManager.playHealthUp();
						health++;
					}	
				} else if(creature instanceof Goomba && isJumping() && getdY() > 0) {
					((Goomba) creature).jumpedOn(); // kill goomba
					this.creatureHop();
					Settings.addScore(Settings.GOOMBA_BONUS);
					soundManager.playKick();
					
				} else if(creature instanceof RedKoopa && isJumping() && getdY() > 0) {
					((RedKoopa) creature).jumpedOn();
					creatureHop();
					soundManager.playKick();
					Settings.addScore(Settings.KOOPA_BONUS);
					map.creaturesToAdd().add(new RedShell(Math.round(creature.getX()), 
							Math.round(creature.getY()+13), map, soundManager, true));
								
				}else if(creature instanceof Thorny && isJumping() && getdY() > 0) {
					((Thorny) creature).jumpedOn();
					//creatureHop();
					//soundManager.playKick();
					getsDamaged();
				} else if(creature instanceof RedShell) {
					
					if(this.isJumping() && this.getdY() > 0) {
						((RedShell) creature).jumpedOn(this.getdX() > 0, this.getdX());
						creatureHop();
						soundManager.playKick();	
						
					} else if(!isJumping() && !((RedShell) creature).isMoving()) {
						boolean right = (this.getdX() > 0);
						((RedShell) creature).jumpedOn(right, this.getdX());
						soundManager.playKick();
						// offset to avoid instant death, needed for sure
						if(right) {
							setX(this.getX() - 3);
						} else {
							setX(this.getX() + 3);
						}
					} else if(!isJumping()){
						getsDamaged();
					}
				} else {
					if (!(creature instanceof FireBall))getsDamaged();
				}
			}
		}
	}
	
	public void getsDamaged() {
		if(grace == 0) {
			health--;
			Settings.subtractLife();
			if(health <= 0) {
				if(health == 0) {
					soundManager.playHurt();
					soundManager.playHealthDown();
				}
				System.out.println("Mario Dies");
			} else {
				soundManager.playHurt();
				soundManager.playHealthDown();
				grace = 160; //80
			}
		}
	}

	public void creatureHop() {
		isJumping = true;
		setY(y -5); // fix offset
		if(!isShiftHeld) {
			setdY(jumpHeight/2f); // jump
		} else {
			setdY(jumpHeight/1.4f);
		}
	}
	
	public void toggleMovement(int type) {

		if(type == 1) {
			currLeftAnim = walkLeft;
			currRightAnim = walkRight;
		}
		if(type == 2) {
			currLeftAnim = runLeft;
			currRightAnim = runRight;
		}
		if(type == 3) {
			currLeftAnim = stillLeft;
			currRightAnim = stillRight;
		}
	}

	public void fixJumping() {
		if(!isRightHeld && !isLeftHeld) {
			if(currentAnimation() == jumpLeft) {
				setAnimation(stillLeft);
			}
			if(currentAnimation() == jumpRight) {
				setAnimation(stillRight);
			}
		} else {
			if(!this.frictionLock) {
				if(isRightHeld) {
					setAnimation(currRightAnim);
				} else if (isLeftHeld) {
					setAnimation(currLeftAnim);
				}
			} else {
				//System.out.println("Do I ever get here");
				if(isRightHeld) {
					setAnimation(changeLeft);
				} else if (isLeftHeld) {
					setAnimation(changeRight);
				}
			}
		}
	}
	
	public void jump() {
		setIsJumping(true);
    	if(currentAnimation() == currLeftAnim || currentAnimation() == stillLeft || currentAnimation() == changeRight) {
    		setAnimation(jumpLeft);
    	}
    	if(currentAnimation() == currRightAnim || currentAnimation() == stillRight || currentAnimation() == changeLeft) {
    		setAnimation(jumpRight);
    	}
	}

	/**
	 * eventID=-1  left move
	 * eventID=1  right move
	 * eventID=0  jump
	 * @param eventID
	 */
    public void processEvent(int eventID) {
   
        if (eventID == -1) {
    		isLeftHeld = true;
    		if(!isDownHeld) {
    			setAnimation(currLeftAnim);
    			left=true;
    		}
        }

        if(eventID == 1) {
    		isRightHeld = true;
    		if(!isDownHeld) {
    			setAnimation(currRightAnim);
    			left=false;
    		}
        }
        
              
        if(eventID == 0) {	
        	if(!isJumping) {
        		//isSpaceHeld = true;
        		soundManager.playJump();
	        	isJumping = true;
	        	dy = jumpHeight;
        	}
        }
        
        if(eventID ==2) {	
        	 loadFireBall=true;
        }
        
        if(eventID ==3) {	
        	if(!isJumping) {
          		if (isLeftHeld) {
          			setAnimation(stillLeft); 
          			left=true;
          		}
          		else {
          			setAnimation(stillRight);
          			left=false;
          		}
        	}
         	isLeftHeld = false;
        	isRightHeld = false;
        	
         }
        /*
        if(key == KeyEvent.VK_SHIFT) {
        	this.isShiftHeld = true;
        }
      
        
        if(key == KeyEvent.VK_DOWN) {
        	isDownHeld = true;
        	if(currentAnimation() == currLeftAnim || currentAnimation() == stillLeft || currentAnimation() == crouchLeft) {
        		setAnimation(crouchLeft);
        	}
        	if(currentAnimation() == currRightAnim || currentAnimation() == stillRight || currentAnimation() == crouchRight) {
        		setAnimation(crouchRight);
        	}
        }
        */
    }
    
    /*
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
        	isLeftHeld = false;
        	if(!isJumping) {
        		setAnimation(stillLeft);
        	}
        	
        }

        if(key == KeyEvent.VK_RIGHT) {
        	isRightHeld = false;
        	if(!isJumping) {
        		setAnimation(stillRight);
        	}
        }
        
        if(key == KeyEvent.VK_SHIFT) {
        	this.isShiftHeld = false;
        }
        
        // responsible for jumps of different heights
        if(key == KeyEvent.VK_SPACE) {
        	isSpaceHeld = false;
        	dy = this.getdY()/2.5f;
        }

        if(key == KeyEvent.VK_DOWN) {
        	isDownHeld = false;
        	if (currentAnimation() == crouchLeft || currentAnimation() == currLeftAnim || currentAnimation() == changeLeft) {
        		setAnimation(stillLeft);
        	} 
        	if (currentAnimation() == crouchRight || currentAnimation() == currRightAnim || currentAnimation() == changeRight) {
        		setAnimation(stillRight);
        	}
        }
    }
    
    @Override
    public void draw(Canvas g, int x, int y, int offsetX, int offsetY) {
		g.drawBitmap(currentAnimation().getImage(), x + offsetX, y + offsetY,paint);
	}
    */
}

