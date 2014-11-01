package com.shikhar.androidgames.mario.objects.mario;




import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;

import com.shikhar.androidgames.framework.Input.KeyEvent;
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
import com.shikhar.androidgames.mario.objects.creatures.BoomRang;
import com.shikhar.androidgames.mario.objects.creatures.Bowser;
import com.shikhar.androidgames.mario.objects.creatures.Coin;
import com.shikhar.androidgames.mario.objects.creatures.FireBall;
import com.shikhar.androidgames.mario.objects.creatures.FireDisc;
import com.shikhar.androidgames.mario.objects.creatures.FireFlower;
import com.shikhar.androidgames.mario.objects.creatures.FlyGoomba;
import com.shikhar.androidgames.mario.objects.creatures.FlyRedKoopa;
import com.shikhar.androidgames.mario.objects.creatures.Goomba;
import com.shikhar.androidgames.mario.objects.creatures.JumpingFish;
import com.shikhar.androidgames.mario.objects.creatures.Latiku;
import com.shikhar.androidgames.mario.objects.creatures.LevelComplete;
import com.shikhar.androidgames.mario.objects.creatures.Mushroom;
import com.shikhar.androidgames.mario.objects.creatures.Platform;
import com.shikhar.androidgames.mario.objects.creatures.RedFish;
import com.shikhar.androidgames.mario.objects.creatures.RedKoopa;
import com.shikhar.androidgames.mario.objects.creatures.RedShell;
import com.shikhar.androidgames.mario.objects.creatures.Score;
import com.shikhar.androidgames.mario.objects.creatures.Spring;
import com.shikhar.androidgames.mario.objects.creatures.Thorny;
import com.shikhar.androidgames.mario.objects.tiles.Brick;
import com.shikhar.androidgames.mario.objects.tiles.FireTile;
import com.shikhar.androidgames.mario.objects.tiles.QuestionBlock;
import com.shikhar.androidgames.mario.objects.tiles.RotatingBlock;
import com.shikhar.androidgames.mario.particles.BubbleParticle;
import com.shikhar.androidgames.mario.particles.SmokeParticle;
import com.shikhar.androidgames.mario.particles.SparkleParticle;
import com.shikhar.androidgames.mario.util.SpriteMap;


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

	public static final float GRAVITY = 0.0007f;
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
	public TileMap map;
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
	private Animation walkRight, runRight,stillRight, jumpRight, crouchRight, changeRight, currRightAnim;
	private Animation swimActiveLeft,swimActiveRight,swimPassiveLeft,swimPassiveRight;

	private Animation flip;
	private int health;
	
	private int grace;
	private Platform platform;
	/*boolean which keeps track of direction of mario*/
    private boolean left=false;
    /*boolean which keeps track of size of mario*/
    private boolean small=false;
	

	private boolean hasFire=false;
  
	Bitmap[] l_Big, r_Big;
    Bitmap[] l_small, r_small;
    Bitmap[] l_Fire, r_Fire;
    Bitmap[] swim_left_small, swim_left_big,swim_left_fire;
    Bitmap[] swim_right_small, swim_right_big,swim_right_fire;

    private boolean isAlive=true; 
    public boolean isLevelClear=false;
    //boolean to keep track if mario is selfDriven (at stagebegin and end)
    private boolean isSystemDriven=false;
    public boolean isSystemDriven() {
		return isSystemDriven;
	}

	/**
     * 0=stillLeft, 1=walkLeft, 2=runLeft, 3=cruchLeft, 4=JumpLeft, 5=ChangeLeft
     * 6=stillRight, 7=walkRight, 8=runRight, 9=cruchRight, 10=JumpRight, 11=ChangeRight,
     *  12=swimleftactive,13=swimleftpassive, 14=swimrightActive, 15=SwimRightPassive
     */
    public int frameMode=6, tglMovement=-1;

    private boolean inWater=false;
 
	public Mario(MarioSoundManager soundManager) {
		
		super(STARTING_X, STARTING_Y, soundManager);
		setIsJumping(true);
		dy = STARTING_DY;
		jumpHeight = INITIAL_JUMP_HEIGHT;
		health = STARTING_LIFE;
		
		l_Big = new Bitmap[] { MarioResourceManager.Mario_Big_Left_Still, MarioResourceManager.Mario_Big_Left_1,
				MarioResourceManager.Mario_Big_Left_2, MarioResourceManager.Mario_Big_Left_Run_1,
				MarioResourceManager.Mario_Big_Left_Run_2, MarioResourceManager.Mario_Big_Crouch_Left,
				MarioResourceManager.Mario_Big_Jump_Left, MarioResourceManager.Mario_Big_Change_Direction_Left };
		r_Big = new Bitmap[] { MarioResourceManager.Mario_Big_Right_Still, MarioResourceManager.Mario_Big_Right_1,
				MarioResourceManager.Mario_Big_Right_2, MarioResourceManager.Mario_Big_Right_Run_1,
				MarioResourceManager.Mario_Big_Right_Run_2, MarioResourceManager.Mario_Big_Crouch_Right,
				MarioResourceManager.Mario_Big_Jump_Right, MarioResourceManager.Mario_Big_Change_Direction_Right };
		l_Fire = new Bitmap[]{ MarioResourceManager.Mario_Big_Left_Still_Fire, MarioResourceManager.Mario_Big_Left_1_Fire,
				MarioResourceManager.Mario_Big_Left_2_Fire, MarioResourceManager.Mario_Big_Left_Run_1_Fire,
				MarioResourceManager.Mario_Big_Left_Run_2_Fire, MarioResourceManager.Mario_Big_Crouch_Left_Fire,
				MarioResourceManager.Mario_Big_Jump_Left_Fire, MarioResourceManager.Mario_Big_Change_Direction_Left_Fire };
		r_Fire = new Bitmap[]{ MarioResourceManager.Mario_Big_Right_Still_Fire, MarioResourceManager.Mario_Big_Right_1_Fire,
				MarioResourceManager.Mario_Big_Right_2_Fire, MarioResourceManager.Mario_Big_Right_Run_1_Fire,
				MarioResourceManager.Mario_Big_Right_Run_2_Fire, MarioResourceManager.Mario_Big_Crouch_Right_Fire,
				MarioResourceManager.Mario_Big_Jump_Right_Fire, MarioResourceManager.Mario_Big_Change_Direction_Right_Fire };
	    
		  swim_left_small=MarioResourceManager.Mario_swim_left_small;
		   swim_right_small=MarioResourceManager.Mario_swim_right_small;
		   swim_left_big=MarioResourceManager.Mario_swim_left_big;
		   swim_right_big=MarioResourceManager.Mario_swim_right_big;
		   swim_left_fire=MarioResourceManager.Mario_swim_left_fire;
		   swim_right_fire=MarioResourceManager.Mario_swim_right_fire;
		
			
		// Create left animations.
    	stillLeft = new Animation(ANIM_TIME).addFrame(l_Big[0]);
		walkLeft = new Animation(ANIM_TIME).addFrame(l_Big[1]).addFrame(l_Big[2]);
		runLeft = new Animation(ANIM_TIME - 30).addFrame(l_Big[3]).addFrame(l_Big[4]);
		crouchLeft = new Animation(ANIM_TIME).addFrame(l_Big[5]);
		jumpLeft = new Animation(ANIM_TIME).addFrame(l_Big[6]);
		changeLeft = new Animation(ANIM_TIME).addFrame(l_Big[7]);
		
		// Create right animations.
		stillRight = new Animation(ANIM_TIME).addFrame(r_Big[0]);
		walkRight = new Animation(ANIM_TIME).addFrame(r_Big[1]).addFrame(r_Big[2]);
		runRight = new Animation(ANIM_TIME - 30).addFrame(r_Big[3]).addFrame(r_Big[4]);
		crouchRight = new Animation(ANIM_TIME).addFrame(r_Big[5]);
		jumpRight = new Animation(ANIM_TIME).addFrame(r_Big[6]);
		changeRight = new Animation(ANIM_TIME).addFrame(r_Big[7]);
		
		l_small = (new SpriteMap(MarioResourceManager.Mario_Small_Left, 6, 1)).getSprites();
		r_small = (new SpriteMap(MarioResourceManager.Mario_Small_Right, 6, 1)).getSprites();

		setAnimation(stillRight);
		currLeftAnim = walkLeft;
		currRightAnim = walkRight;
		
		final class FireAnimation extends Animation {
			public void endOfAnimationAction() {
				setAnimation(left?stillLeft:stillRight);
			}
		}
		fireLeftAnim = new FireAnimation().setDAL(100).addFrame(l_Fire[6]).setDAL(100).addFrame(l_Fire[4]);
		fireRightAnim = new FireAnimation().setDAL(100).addFrame(r_Fire[6]).setDAL(100).addFrame(r_Fire[4]);

		swimActiveLeft= new Animation(ANIM_TIME).addFrame(swim_left_big[3]).addFrame(swim_left_big[2]);//.addFrame(swim_left_big[0]);
		swimActiveRight=new Animation(ANIM_TIME).addFrame(swim_right_big[0]).addFrame(swim_right_big[1]);//.addFrame(swim_right_big[3]);
		swimPassiveLeft=new Animation(2*ANIM_TIME).addFrame(swim_left_big[1]).addFrame(swim_left_big[2]);
		swimPassiveRight=new Animation(2*ANIM_TIME).addFrame(swim_right_big[2]).addFrame(swim_right_big[1]);
	
		Bitmap flipped = MarioResourceManager.Mario_Flip;
		flip = new Animation().addFrame(flipped).addFrame(flipped);

	}
	
    public boolean isSmall() {
		return small;
	}

    public boolean isFireMan() {
  		return hasFire;
  	}

	@Override
	public void draw(Canvas g, int x, int y, int offsetX, int offsetY) {
		draw(g, x + offsetX, y + offsetY);
	}
	
	public void reloadAnimation() {

		switch (frameMode) {
		case 0:
			currLeftAnim = stillLeft;
			currRightAnim = stillRight;
			setAnimation(stillLeft);
			break;
		case 1:
			currLeftAnim = walkLeft;
			currRightAnim = walkRight;
			setAnimation(walkLeft);
			break;
		case 2:
			currLeftAnim = runLeft;
			currRightAnim = runRight;
			setAnimation(runLeft);
			break;
		case 3:
			setAnimation(crouchLeft);
			break;
		case 4:
			setAnimation(jumpLeft);
			break;
		case 5:
			setAnimation(changeLeft);
		case 6:
			currLeftAnim = stillLeft;
			currRightAnim = stillRight;
			setAnimation(stillRight);
			break;
		case 7:
			currLeftAnim = walkLeft;
			currRightAnim = walkRight;
			setAnimation(walkRight);
			break;
		case 8:
			currLeftAnim = runLeft;
			currRightAnim = runRight;
			setAnimation(runRight);
			break;
		case 9:
			setAnimation(crouchRight);
			break;
		case 10:
			setAnimation(jumpRight);
			break;
		case 11:
			setAnimation(changeRight);
			break;
		case 12:
			setAnimation(swimActiveLeft);
			break;
		case 13:
			setAnimation(swimPassiveLeft);
			break;
		case 14:
			setAnimation(swimActiveRight);
			break;
		case 15:
			setAnimation(swimPassiveRight);
			break;
		}
		
	}
	 
	 public void Reset(){
		
		 setY(80);
		 setX(32);
		 setdY(0.06f);
		 setdX(0);
		 isDownHeld=false;
		 isRightHeld=false;
		 isLeftHeld=false;
		 isShiftHeld=false;
		 isSpaceHeld=false;
		 isAlive=true;
		 isLevelClear=false;
		 isSystemDriven=false;
		 isJumping=true;
		 left=false;
		 frictionLock = false;
		 inWater=false;
		 //hasFire=false;
		 //small=true;
		// grace=59;
		 setAnimation(stillRight);
	 }
	 
	  public void toggleMarioFrames(int type){
			// Create right animations.
			if (type==0) {  //Big Mario
				stillLeft = new Animation(ANIM_TIME).addFrame(l_Big[0]);
				walkLeft = new Animation(ANIM_TIME).addFrame(l_Big[1]).addFrame(l_Big[2]);
				runLeft = new Animation(ANIM_TIME - 30).addFrame(l_Big[3]).addFrame(l_Big[4]);
				crouchLeft = new Animation(ANIM_TIME).addFrame(l_Big[5]);
				jumpLeft = new Animation(ANIM_TIME).addFrame(l_Big[6]);
				changeLeft = new Animation(ANIM_TIME).addFrame(l_Big[7]);

				stillRight = new Animation(ANIM_TIME).addFrame(r_Big[0]);
				walkRight = new Animation(ANIM_TIME).addFrame(r_Big[1])	.addFrame(r_Big[2]);
				runRight = new Animation(ANIM_TIME - 30).addFrame(r_Big[3]).addFrame(r_Big[4]);
				crouchRight = new Animation(ANIM_TIME).addFrame(r_Big[5]);
				jumpRight = new Animation(ANIM_TIME).addFrame(r_Big[6]);
				changeRight = new Animation(ANIM_TIME).addFrame(r_Big[7]);
				swimActiveLeft= new Animation(ANIM_TIME).addFrame(swim_left_big[3]).addFrame(swim_left_big[2]);//.addFrame(swim_left_big[0]);
				swimActiveRight=new Animation(ANIM_TIME).addFrame(swim_right_big[0]).addFrame(swim_right_big[1]);//.addFrame(swim_right_big[3]);
				swimPassiveLeft=new Animation(2*ANIM_TIME).addFrame(swim_left_big[1]).addFrame(swim_left_big[2]);
				swimPassiveRight=new Animation(2*ANIM_TIME).addFrame(swim_right_big[2]).addFrame(swim_right_big[1]);

			} else if (type==1) {//Big Fire Mario
				stillLeft = new Animation(ANIM_TIME).addFrame(l_Fire[0]);
				walkLeft = new Animation(ANIM_TIME).addFrame(l_Fire[1]).addFrame(l_Fire[2]);
				runLeft = new Animation(ANIM_TIME - 30).addFrame(l_Fire[3]).addFrame(l_Fire[4]);
				crouchLeft = new Animation(ANIM_TIME).addFrame(l_Fire[5]);
				jumpLeft = new Animation(ANIM_TIME).addFrame(l_Fire[6]);
				changeLeft = new Animation(ANIM_TIME).addFrame(l_Fire[7]);

				stillRight = new Animation(ANIM_TIME).addFrame(r_Fire[0]);
				walkRight = new Animation(ANIM_TIME).addFrame(r_Fire[1]).addFrame(r_Fire[2]);
				runRight = new Animation(ANIM_TIME - 30).addFrame(r_Fire[3]).addFrame(r_Fire[4]);
				crouchRight = new Animation(ANIM_TIME).addFrame(r_Fire[5]);
				jumpRight = new Animation(ANIM_TIME).addFrame(r_Fire[6]);
				changeRight = new Animation(ANIM_TIME).addFrame(r_Fire[7]);
				swimActiveLeft= new Animation(ANIM_TIME).addFrame(swim_left_fire[3]).addFrame(swim_left_fire[2]);//.addFrame(swim_left_big[0]);
				swimActiveRight=new Animation(ANIM_TIME).addFrame(swim_right_fire[0]).addFrame(swim_right_fire[1]);//.addFrame(swim_right_big[3]);
				swimPassiveLeft=new Animation(2*ANIM_TIME).addFrame(swim_left_fire[1]).addFrame(swim_left_fire[2]);
				swimPassiveRight=new Animation(2*ANIM_TIME).addFrame(swim_right_fire[2]).addFrame(swim_right_fire[1]);

			}else if (type==2){ //Small Fire Mario
				stillLeft = new Animation(ANIM_TIME).addFrame(l_small[0]);
				walkLeft = new Animation(ANIM_TIME).addFrame(l_small[1]).addFrame(l_small[0]);
				runLeft = new Animation(ANIM_TIME - 30).addFrame(l_small[3]).addFrame(l_small[4]);
				//crouchLeft = new Animation(ANIM_TIME).addFrame(l_Big[5]);
				jumpLeft = new Animation(ANIM_TIME).addFrame(l_small[2]);
				changeLeft = new Animation(ANIM_TIME).addFrame(l_small[5]);

				stillRight = new Animation(ANIM_TIME).addFrame(r_small[5]);
				walkRight = new Animation(ANIM_TIME).addFrame(r_small[4])	.addFrame(r_small[5]);
				runRight = new Animation(ANIM_TIME - 30).addFrame(r_small[2]).addFrame(r_small[1]);
				//crouchRight = new Animation(ANIM_TIME).addFrame(r_Big[5]);
				jumpRight = new Animation(ANIM_TIME).addFrame(r_small[3]);
				changeRight = new Animation(ANIM_TIME).addFrame(r_small[0]);
				swimActiveLeft= new Animation(ANIM_TIME).addFrame(swim_left_small[3]).addFrame(swim_left_small[2]);//.addFrame(swim_left_big[0]);
				swimActiveRight=new Animation(ANIM_TIME).addFrame(swim_right_small[0]).addFrame(swim_right_small[1]);//.addFrame(swim_right_big[3]);
				swimPassiveLeft=new Animation(2*ANIM_TIME).addFrame(swim_left_small[1]).addFrame(swim_left_small[2]);
				swimPassiveRight=new Animation(2*ANIM_TIME).addFrame(swim_right_small[2]).addFrame(swim_right_small[1]);

			}
			toggleMovement(tglMovement);
			reloadAnimation();
	} 
	 
	public int getHealth() {
		return health;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}


	public void resetHealth() {
		this.health=STARTING_LIFE;
		small=false;
		hasFire=false;
		toggleMarioFrames(0);
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
		if (!isAlive) return;
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
		update( map,  time,false) ;
	}
	
	public void update(TileMap map, float time,boolean lockInput) {
	
		if (lockInput){
			isLeftHeld=false;
			isRightHeld=false;
			
		}
		if(isLevelClear)return;
		if (isSystemDriven){
			super.update((int) time);
			if(getdY() < TERMINAL_FALL_DY/2) { setdY(getdY() + GRAVITY * time/10); } 
			setY( getY() + getdY()*time);
			//Settings.addScore(20);
			
			if (getY()>GameRenderer.tilesToPixels(map.getHeight()))StageClear();
			return;
		}
		
		if (!isAlive){
			super.update((int) time);
			if(getdY() < TERMINAL_FALL_DY) { setdY(getdY() + 0.7f*GRAVITY * time); } 
			setY( getY() + getdY()*time);
			if (getY()>GameRenderer.tilesToPixels(map.getHeight()))GameOver();
			return;
		}
		
		if (loadFireBall && FireBall.isReady()){
			loadFireBall=false;
			map.creaturesToAdd().add(new FireBall((int) (left?x-3:x+getWidth()-2),(int)(y+getHeight()*0.14f),left?-2:2,soundManager));
			if (left){
				setAnimation(fireLeftAnim);
			}else{
				setAnimation(fireRightAnim);
			}
			soundManager.playFireBall();
		}
		
		boolean water=false;
		for (int i=0; i<map.waterZones().size();i++){
			if (map.waterZones().get(i).contains((int)(x+getWidth()/2)/16,(int)(y+getHeight()-2)/16)){
				water=true;
				break;
			}
		}
		if (water!=inWater){
			inWater=water;
			fixJumping();
		}
		
		if (inWater){
			if (Math.random()>0.97){
        	   map.creaturesToAdd().add(new BubbleParticle((int)getX(), (int)getY()));
			}
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
		if(getdY() < (inWater?0.6f:1)*TERMINAL_FALL_DY) { setdY(getdY() + (inWater?0.3f:1)*GRAVITY * time); } 
			
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
			if (!isSystemDriven && !isLevelClear)killMario();
			//Settings.subtractLife();
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
					for(Point p : yTile) {
						GameTile tile = map.getTile(p.x, p.y);
						if (tile instanceof FireTile){killMario();	return;}
					}
					
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
					if(platformY.mayfall() ){
						setdY(platformY.getdY());
						setY(platformY.getY() - getHeight());
						setIsJumping(false);
						if(!platformY.isfalling())platformY.accelerateFall();                      
					}else{
						setIsJumping(false);
						if (platformY instanceof Spring)platform.jumpedOn();
						setY(platformY.getY() - getHeight());

					}                                     
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
			if(c instanceof Goomba || c instanceof FlyGoomba|| c instanceof RedKoopa || c instanceof RedShell || c instanceof Thorny || c instanceof Bowser) {
				c.flip();
				Settings.addScore(200);
				map.creaturesToAdd().add(new Score(Math.round(c.getX()), Math.round(c.getY()+13),3));
				toRemove.add(c);
				//soundManager.playKick();
			}  
		}
		for(Creature c : toRemove) { tile.collidingCreatures().remove(c); }
	}	
	
	// Determines what happens when mario collides with a creature.
	public void playerCollision(TileMap map, Creature creature) {
		
		if (!isAlive || isSystemDriven) return;
		// only check collision of creatures with this that are not sleeping, are on the screen, and are collidable
		if(!creature.isPlatform() && creature.isCollidable() || creature instanceof BoomRang) { 
			boolean collision = isCollision(this, creature);
			if(collision && !(creature instanceof Score)) {
				if(creature instanceof LevelComplete) {
					creature.kill();
					isSystemDriven=true;
				    left=false;
					this.setAnimation(stillRight);
					this.setX(creature.getX());
					this.setdX(0);
					this.setdY(0.075f);
					//soundManager.playCelebrate();
					soundManager.playStageClear();
					return;
				} else if(creature instanceof Coin) {
					creature.kill();
					soundManager.playCoin();
					Settings.addScore(100);
					Settings.addCoins(1);
					 for (int xx = 0; xx < 2; xx++)
			                for (int yy = 0; yy < 2; yy++)
			                    map.creaturesToAdd().add((new SparkleParticle(Math.round(creature.getX()) + xx * 8 + (int) (Math.random() * 8), Math.round(creature.getY()) + yy * 8 + (int) (Math.random() * 8), 0, 0,5)));

					map.creaturesToAdd().add(new Score(Math.round(creature.getX()), Math.round(creature.getY()+13),0));
				} else if(creature instanceof Mushroom) {
					//soundManager2.playCelebrate();
					grace=40;
					creature.kill();
					if (((Mushroom)creature).isHealthUp()){
						if (health<3){
							soundManager.playHealthUp();
							health++;
							Settings.setLives(health);
						}else{
							soundManager.playBonusPoints();
						} 
					}else{
						if (small){
							small=false;
							toggleMarioFrames(0);
							soundManager.playItemSprout();
						}else if (!hasFire){
							hasFire=true;
							toggleMarioFrames(1);
							soundManager.playItemSprout();
							soundManager.playPowerUp();
						}else{
							soundManager.playBonusPoints();
						}
					}
					Settings.addScore(1000);
					map.creaturesToAdd().add(new Score(Math.round(creature.getX()), Math.round(creature.getY()+13),2));
					
				} else if (creature instanceof FireFlower){
					grace=40;
					creature.kill();
					soundManager.playCelebrate();
					hasFire=true;
					small=false;
					toggleMarioFrames(1);
					soundManager.playItemSprout();
					Settings.addScore(1000);
					map.creaturesToAdd().add(new Score(Math.round(creature.getX()), Math.round(creature.getY()+13),2));

				} else if(creature instanceof Goomba && isJumping() && getdY() > 0) {
					((Goomba) creature).jumpedOn(); // kill goomba
					this.creatureHop();
					//soundManager.playKick();
					for (int i = 0; i < 8; i++)
			        {
			             map.creaturesToAdd().add(new SmokeParticle((int) (x + Math.random() * 16 - 8) + 4, (int) (y +getHeight()- Math.random() * 8) + 4, (float) (Math.random() * 2 - 1)/20, (float) Math.random() * -1/20.0f,10));
			        }
					Settings.addScore(100);
					map.creaturesToAdd().add(new Score(Math.round(creature.getX()), Math.round(creature.getY()+13),3));
				} else if((creature instanceof RedFish || creature instanceof JumpingFish) && isJumping() && getdY() > 0 && creature.getY()>+getY()+getHeight()-3) {
					((RedFish) creature).jumpedOn(); // kill RedFish
					this.creatureHop();
					soundManager.playKick();
					for (int i = 0; i < 8; i++)
			        {
			             map.creaturesToAdd().add(new SmokeParticle((int) (x + Math.random() * 16 - 8) + 4, (int) (y +getHeight()- Math.random() * 8) + 4, (float) (Math.random() * 2 - 1)/20, (float) Math.random() * -1/20.0f,10));
			        }
					
				} else if(creature instanceof FlyRedKoopa && isJumping() && getdY() > 0 ) {
					this.creatureHop();
				    //	soundManager.playKick();
					if (!((FlyRedKoopa)creature).canFly){
						//((FlyRedKoopa) creature).jumpedOn(); //kill
						creature.kill();
						map.creaturesToAdd().add(new RedShell(Math.round(creature.getX()), 
						Math.round(creature.getY()+13), map, soundManager, true,false));
						Settings.addScore(200);
					}else{
						((FlyRedKoopa) creature).jumpedOn();
						Settings.addScore(100);
					}
					map.creaturesToAdd().add(new Score(Math.round(creature.getX()), Math.round(creature.getY()+13),3));

				} else if(creature instanceof FlyGoomba && isJumping() && getdY() > 0 ) {
					creatureHop();
					//soundManager.playKick();
					((FlyGoomba) creature).jumpedOn();
					Settings.addScore(200);
					map.creaturesToAdd().add(new Score(Math.round(creature.getX()), Math.round(creature.getY()+13),3));

				} else if(creature instanceof RedKoopa && isJumping() && getdY() > 0) {
					((RedKoopa) creature).jumpedOn();
					creatureHop();
					//soundManager.playKick();
					map.creaturesToAdd().add(new RedShell(Math.round(creature.getX()), 
							Math.round(creature.getY()+13), map, soundManager, true,((RedKoopa) creature).isGreen()));
					map.creaturesToAdd().add(new Score(Math.round(creature.getX()), Math.round(creature.getY()+13),3));
					Settings.addScore(100);
				}else if(creature instanceof Thorny && isJumping() && getdY() > 0) {
					((Thorny) creature).jumpedOn();
					//creatureHop();
					//soundManager.playKick();
					getsDamaged();
				} else if(creature instanceof RedShell) {
					
					if(this.isJumping() && this.getdY() > 0) {
						boolean dir;
						if (this.getdX()==0){
							if (this.left){
								dir=this.x<creature.getX()+creature.getWidth()/2;
							}else{
								dir=this.x+this.getWidth()<creature.getX()+creature.getWidth()/2;
							}
						}else{
							dir=this.getdX() > 0;
						}
						((RedShell) creature).jumpedOn(dir, this.getdX());
						creatureHop();
						//soundManager.playKick();	
						Settings.addScore(100);
						//map.creaturesToAdd().add(new Score(Math.round(creature.getX()), Math.round(creature.getY()+13),3));
					} else if(!isJumping() && !((RedShell) creature).isMoving()) {
						boolean right = (this.getdX() > 0);
						((RedShell) creature).jumpedOn(right, this.getdX());
						soundManager.playKick();
						Settings.addScore(100);
						// offset to avoid instant death, needed for sure
						if(right) {
							setX(this.getX() - 3);
						} else {
							setX(this.getX() + 3);
						}
					} else if(!isJumping()){
						getsDamaged();
					}
				} else if(creature instanceof Bowser && isJumping() && getdY() > 0) {
					if (creature instanceof Bowser){
						if (((Bowser)creature).getGraceTime()>0)return;
					}
					creature.jumpedOn();
					creatureHop();
					map.creaturesToAdd().add(new Score(Math.round(creature.getX()), Math.round(creature.getY()+13),2));
					Settings.addScore(1000);
				} else if(creature instanceof Latiku && isJumping() && getdY() > 0 && y<creature.getY()-13) {
					creature.jumpedOn();
					creatureHop();
					map.creaturesToAdd().add(new Score(Math.round(creature.getX()), Math.round(creature.getY()+13),2));
					Settings.addScore(500);
				}else {
					if ((!(creature instanceof FireBall) && creature.isItem()==false)  || (creature instanceof FireDisc))getsDamaged();
				}
			}
		}
	}
	
	public void getsDamaged() {
		if (grace == 0) {
			if (small) {
				killMario();
				return;
			} else {
				if (hasFire){
					toggleMarioFrames(0);
				}else{
					small = true;
					toggleMarioFrames(2);	
				}
				hasFire = false;
				soundManager.playHurt();
				soundManager.playHealthDown();
				grace = 120;
			}
		}
	}

	public void creatureHop() {
		isJumping = true;
		soundManager.playStomp();
		setY(y -5); // fix offset
		if(!isShiftHeld) {
			setdY(jumpHeight/2f); // jump
		} else {
			setdY(jumpHeight/1.4f);
		}
	}
	
	public void toggleMovement(int type) {
		tglMovement=type;
		if(type == 1) {
			frameMode=left?1:7;
			currLeftAnim = walkLeft;
			currRightAnim = walkRight;
		}
		if(type == 2) {
			frameMode=left?2:8;
			currLeftAnim =  runLeft;
			currRightAnim =  runRight;
		}
		if(type == 3) {
			frameMode=left?0:6;
			currLeftAnim =  stillLeft;
			currRightAnim =  stillRight;
		}
	}

	public void fixJumping() {
		if(!isRightHeld && !isLeftHeld) {
			if(currentAnimation() == jumpLeft || currentAnimation() == swimPassiveLeft
					|| currentAnimation() == swimActiveLeft) {
				frameMode=0;
				setAnimation(stillLeft);
			}
			if(currentAnimation() == jumpRight || currentAnimation() == swimPassiveRight
					|| currentAnimation() == swimActiveRight) {
				frameMode=6;
    			setX(getX()+getWidth()-stillRight.getWidth());
				setAnimation( stillRight);
			}
		} else {
			if(!this.frictionLock) {
				if(isRightHeld) {
					setX(getX()+getWidth()-currRightAnim.getWidth());
					setAnimation(currRightAnim);
				} else if (isLeftHeld) {
					setAnimation(currLeftAnim);
				}
			} else {
				System.out.println("Do I ever get here");
				if(isRightHeld) {
					frameMode=5;
					setAnimation(changeLeft);
				} else if (isLeftHeld) {
					frameMode=11;
					setAnimation(changeRight);
				}
			}
		}
	}
	
	public void jump() {
		setIsJumping(true);
    	if(currentAnimation() == currLeftAnim || currentAnimation() == stillLeft || currentAnimation() == changeRight) {
    		if (inWater){
     			if (dy<0){
    				setAnimation(swimActiveLeft);
    				frameMode=12;
    			}else{
    				setAnimation(swimPassiveLeft);
    				frameMode=13;
    			}
    			return;
    		}else{
    			setAnimation(jumpLeft);
    			frameMode=4;
    		}
    		
    	}
    	if(currentAnimation() == currRightAnim || currentAnimation() == stillRight || currentAnimation() == changeLeft) {
    		frameMode=10;
    		
    		if (inWater){
    			setX(getX()+getWidth()-swimActiveRight.getWidth());
    			if (dy<0){
    				setAnimation(swimActiveRight);
    				frameMode=14;
    			}else{
    				setAnimation(swimPassiveRight);
    				frameMode=15;
    			}
    			return;
    		}else{
    			setAnimation(jumpRight);
    			frameMode=10;
    		}
    		
       	}
    	
    	if (dy>0 && currentAnimation()==swimActiveRight)setAnimation(swimPassiveRight);
		if (dy<0 && currentAnimation()==swimPassiveRight)setAnimation(swimActiveRight);
		if (dy>0 && currentAnimation()==swimActiveLeft)setAnimation(swimPassiveLeft);
		if (dy<0 && currentAnimation()==swimPassiveLeft)setAnimation(swimActiveLeft);
	
	}

	/**
	 * eventID=-1  left move
	 * eventID=1  right move
	 * eventID=0  jump
	 * @param eventID
	 */
    public void processEvent(int eventID) {
        if (!isAlive())return;
        if (eventID == -1) {
    		isLeftHeld = true;
    		isRightHeld=false;
    		if(!isDownHeld) {
    			setAnimation(currLeftAnim);
    			left=true;
    		}
        }

        if(eventID == 1) {
    		isRightHeld = true;
    		isLeftHeld = false;
    		if(!isDownHeld) {
    			setAnimation(currRightAnim);
    			left=false;
    		}
        }
        
        if(eventID == 0) {
        	if (inWater){
        		//isSpaceHeld = true;
        		dy=0.4f*jumpHeight;
        		soundManager.playGulp();
        		isJumping = true;
        		System.out.println("jump mario touchevent");
        		return;
        	}else if(!isJumping) {
        		//isSpaceHeld = true;
        		soundManager.playJump();
	        	isJumping = true;
	        	dy = jumpHeight;
        	}
        	
        }
        
        if(eventID ==2) {	
        	if(hasFire) loadFireBall=true;
        }
        
        if(eventID ==3) {	
        	if(!isJumping) {
          		if (isLeftHeld) {
          			frameMode=0;
          			setAnimation(stillLeft); 
          			left=true;
          		}
          		else {
          			frameMode=6;
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
    
    public boolean isAlive(){
    	return isAlive;
    }
    public void setAlive(boolean alive){
    	 isAlive=alive;
    }
    
    public void killMario() {
    	health--;
    	small=true;
    	hasFire=false;
    	toggleMarioFrames(2);
    	setAnimation(flip);
    	isAlive=false;
		System.out.println("Mario has died.");		
		setIsCollidable(false);
		dy = -.25f;
		dx = 0;
		soundManager.playDie();
	}
    
    private void GameOver() {
		// TODO Auto-generated method stub
	
	}

	private void StageClear() {
		// TODO Auto-generated method stub
		this.isLevelClear=true;
		//isSystemDriven=false;
	}

	
	   public void keyPressed(KeyEvent e) {
	        int key = e.keyCode;
	        if (!isAlive())return;

	        if (key == android.view.KeyEvent.KEYCODE_DPAD_LEFT) {
	    		isLeftHeld = true;
	    		isRightHeld=false;
	    		if(!isDownHeld) {
	    			setAnimation(currLeftAnim);
	    			left=true;
	    		}
	        }

	        if(key ==  android.view.KeyEvent.KEYCODE_DPAD_RIGHT) {
	    		isRightHeld = true;
	    		isLeftHeld = false;
	    		if(!isDownHeld) {
	    			setAnimation(currRightAnim);
	    			left=false;
	    		}
	        }
	        
	        if(key ==  android.view.KeyEvent.KEYCODE_SHIFT_LEFT||key ==  android.view.KeyEvent.KEYCODE_SHIFT_RIGHT) {
	        	this.isShiftHeld = true;
	        }
	      
	        if(key ==  android.view.KeyEvent.KEYCODE_DPAD_DOWN) {
	        	isDownHeld = true;
	        	if(currentAnimation() == currLeftAnim || currentAnimation() ==stillLeft || currentAnimation() == crouchLeft) {
	        		frameMode=3;
	        		setAnimation(crouchLeft);
	        		left=true;
	        	}
	        	if(currentAnimation() == currRightAnim || currentAnimation() ==  stillRight || currentAnimation() == crouchRight) {
	        		frameMode=9;
	        		setAnimation(crouchRight);
	        		left=false;
	        	}
	        }
	        
	        if(key ==android.view.KeyEvent.KEYCODE_SPACE) {	
	        	if (inWater && !isSpaceHeld){
	        		isSpaceHeld = true;
	        		dy=0.4f*jumpHeight;
	        		isJumping = true;
	        		soundManager.playJump();
	        		return;
	        	}
	        	if(!isJumping && !isSpaceHeld) {
	        		isSpaceHeld = true;
	        		soundManager.playJump();
		        	isJumping = true;
		        	dy = jumpHeight;
	        	}
	        }

	        if(key == android.view.KeyEvent.KEYCODE_S) {	
	        	if (hasFire)  this.loadFireBall=true;
	        }
	    }
	    
	    public void keyReleased(KeyEvent e) {
	    	if (!isAlive())return;

	    	int key = e.keyCode;
	        if (key == android.view.KeyEvent.KEYCODE_DPAD_LEFT) {
	        	isLeftHeld = false;
	        	if(!isJumping) {
	        		frameMode=0;
	        		setAnimation(stillLeft);
	        		left=true;
	        	}
	        	
	        }

	        if(key ==  android.view.KeyEvent.KEYCODE_DPAD_RIGHT) {
	        	isRightHeld = false;
	        	if(!isJumping) {
	        		frameMode=6;
	        		setAnimation(stillRight);
	        		left=false;
	        	}
	        }
	        
	        if(key ==  android.view.KeyEvent.KEYCODE_SHIFT_LEFT||key ==  android.view.KeyEvent.KEYCODE_SHIFT_RIGHT) {
	        	this.isShiftHeld = false;
	        }
	        
	        // responsible for jumps of different heights
	        if(key ==android.view.KeyEvent.KEYCODE_SPACE) {
	        	isSpaceHeld = false;
	        	dy = this.getdY()/2.5f;
	        }

	        if(key ==  android.view.KeyEvent.KEYCODE_DPAD_DOWN) {
	        	isDownHeld = false;
	        	if (currentAnimation() == crouchLeft || currentAnimation() == currLeftAnim || currentAnimation() == changeLeft) {
	        		frameMode=0;
	        		setAnimation(stillLeft);
	        		left=true;
	        	} 
	        	if (currentAnimation() == crouchRight || currentAnimation() == currRightAnim || currentAnimation() == changeRight) {
	        		frameMode=6;
	        		setAnimation(stillRight);
	        		left=false;
	        	}
	        }
	    }
	    
	    public boolean isInWater() {
			return inWater;
		}

}

