
package com.shikhar.androidgames.mario.core;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import com.shikhar.androidgames.mario.util.SpriteMap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;


/**
 * 
 * this class will be responsible for loading/unloading our game resources (art, fonts, audio) 
 * this class will use singleton holder, which means we will be able to access this class from the global level.
 * @author Mahesh Kurmi
 */
public class MarioResourceManager
{
    //---------------------------------------------
    // VARIABLES
    //---------------------------------------------
    public static Activity activity;
    public static InputStream inputStream;
    public static BitmapFactory.Options options;
    
	private static boolean firstTimeCreate = true;

	//left mario
	public static Bitmap Mario_Big_Left_Still, Mario_Big_Left_1,Mario_Big_Left_2,
			Mario_Big_Left_Run_1,Mario_Big_Left_Run_2, Mario_Big_Crouch_Left,
			Mario_Big_Jump_Left, Mario_Big_Change_Direction_Left;
	//Right mario
	public static Bitmap Mario_Big_Right_Still, Mario_Big_Right_1,Mario_Big_Right_2,
			Mario_Big_Right_Run_1,Mario_Big_Right_Run_2, Mario_Big_Crouch_Right,
			Mario_Big_Jump_Right, Mario_Big_Change_Direction_Right;
	//Fire Left mario
	public static Bitmap Mario_Big_Left_Still_Fire, Mario_Big_Left_1_Fire,Mario_Big_Left_2_Fire,
				Mario_Big_Left_Run_1_Fire,Mario_Big_Left_Run_2_Fire, Mario_Big_Crouch_Left_Fire,
				Mario_Big_Jump_Left_Fire, Mario_Big_Change_Direction_Left_Fire;
	//Fire Right mario
	public static Bitmap Mario_Big_Right_Still_Fire, Mario_Big_Right_1_Fire,Mario_Big_Right_2_Fire,
				Mario_Big_Right_Run_1_Fire,Mario_Big_Right_Run_2_Fire, Mario_Big_Crouch_Right_Fire,
				Mario_Big_Jump_Right_Fire, Mario_Big_Change_Direction_Right_Fire;
		
	
	public static Bitmap Mario_Small_Left;
	public static Bitmap Mario_Small_Right;
	public static Bitmap Mario_Flip;

	public static  Bitmap[] Mario_swim_left_small, Mario_swim_left_big,Mario_swim_left_fire;
	public static  Bitmap[] Mario_swim_right_small, Mario_swim_right_big,Mario_swim_right_fire;

	
	//coin
	public static Bitmap Coin_Icon;
	public static Bitmap Coin_1, Coin_2,Coin_3,Coin_4;
	public static Bitmap Coin_5, Coin_6,Coin_7,Coin_8;
	//goomba
	public static Bitmap Goomba_Normal_1,Goomba_Normal_2,Goomba_Dead,Goomba_Flip;
	public static Bitmap Fly_Goomba;

	//mushroom
	public static Bitmap Mushroom;
	public static Bitmap LifeUp;
	public static Bitmap Fire_Flower;

	//platform
	public static Bitmap Red_Platform_2;
	//FlyredKoopa
	public static Bitmap  Fly_Koopa_Red_Left,Fly_Koopa_Red_Right;
	//redKoopa
	public static Bitmap  Koopa_Red_Left_1,Koopa_Red_Left_2,Koopa_Red_Right_1,Koopa_Red_Right_2;
  	//redShell
	public static Bitmap  Red_Shell_1,Red_Shell_2,Red_Shell_3,Red_Shell_4,Red_Shell_Still,Red_Shell_Flip;
	//greenKoopa
	public static Bitmap[]  Green_Koopa;
	//greenShell
	public static Bitmap[]  Green_Shell;
	//Thorny
	public static Bitmap[] Thorny;
	//Thorny
	public static Bitmap[] Latiku;
	public static Bitmap[] Latiku_Fire;

	//ThornyBomb
	public static Bitmap[] Fire_Thorny;
	//Bowser
	public static Bitmap[] Bowser_Left;
	public static Bitmap[] Bowser_Right;
	public static Bitmap[] BoomRang_left;
	public static Bitmap[] BoomRang_right;
	
	//Score
	public static Bitmap  Score_100_New6;
	public static Bitmap  Score_1000;
	public static Bitmap  Score_1_Up;
	public static Bitmap  Score_200;
	//tree
	public static Bitmap  Tree_1,Tree_2;
	//bush
	public static Bitmap  Bush_1,Bush_2;

	//FireTile
	public static Bitmap[] Fire_Tile;
	//QuestionBlock
	public static Bitmap  Question_Block_0,Question_Block_1,Question_Block_2,Question_Block_3,Question_Block_Dead;
	//RotatingBlock
	public static Bitmap  Rotating_Block_Hit_1,Rotating_Block_Hit_2,Rotating_Block_Hit_3,Rotating_Block_Still;
	//PlainTileMap
	public static Bitmap Plain_Tiles;
	//Background
	public static Bitmap Background;
	//splash
	public static Bitmap Splash;
	//menu
	public static Bitmap menu;
	//3stars
	public static Bitmap star3;
	// mario pic for main menu
	public static Bitmap marioPic;
	//grass
	public static Bitmap Sloped_Tile;
	public static Bitmap Grass_Edge; 
	public static Bitmap Grass_Center; 
	//bricks shatter
	public static Bitmap brick_particle;
	public static Bitmap[] Sparkle_Particles;
	public static Bitmap[] Smoke_Particles;
	public static Bitmap[] Bomb_Particles;
	
	//fireball
	public static Bitmap fb_1,fb_2,fb_3,fb_4,fb_5,fb_6;
	//firedisc
	public static Bitmap[] FireDisc;
	//piranha
	public static Bitmap Piranha_1,Piranha_2;
	//Spring
	public static Bitmap Spring_1,Spring_2,Spring_3;
	public static Bitmap[] Virus;
	public static Bitmap[] RedFish;
	public static Bitmap[] BlueFish;

	//Hud
	public static  Bitmap[] fontSmall, fontMedium, fontLarge;

	public static Bitmap level;
	public static Bitmap level_1star;
	public static Bitmap level_2star;
	public static Bitmap level_3star;
	public static Bitmap level_locked;
	public static Bitmap levelComplete;
	public static Bitmap waterWave;

	public static Bitmap Btn_Next;
	public static Bitmap Btn_Prev;
	public static Bitmap Btn_Fire;
	public static Bitmap Btn_Jump;
	public static Bitmap logo;
	public static Bitmap pearl1,pearl2;
	public static Bitmap Castle;
	public static Bitmap TapToStart;

	
	public  MarioResourceManager(Activity pActivity)
    {
    	activity=pActivity;
    	options=new BitmapFactory.Options();
    	options.inPreferredConfig = Bitmap.Config.RGB_565;
    }
    
    /**
	 * loads resources in memory if resources are not already loaded
	 */
	public  void loadResouces(){
		if (firstTimeCreate==true){
			options.inPreferredConfig = Bitmap.Config.ARGB_4444;
			// left mario
			Mario_Big_Left_Still = loadImage("mario/Mario_Big_Left_Still.png");
			Mario_Big_Left_1 = loadImage("mario/Mario_Big_Left_1.png");
			Mario_Big_Left_2 = loadImage("mario/Mario_Big_Left_2.png");
			Mario_Big_Left_Run_1 = loadImage("mario/Mario_Big_Left_Run_1.png");
			Mario_Big_Left_Run_2 = loadImage("mario/Mario_Big_Left_Run_2.png");
			Mario_Big_Crouch_Left = loadImage("mario/Mario_Big_Crouch_Left.png");
			Mario_Big_Jump_Left = loadImage("mario/Mario_Big_Jump_Left.png");
			Mario_Big_Change_Direction_Left = loadImage("mario/Mario_Big_Change_Direction_Left.png");
			// Right mario
			Mario_Big_Right_Still = loadImage("mario/Mario_Big_Right_Still.png");
			Mario_Big_Right_1 = loadImage("mario/Mario_Big_Right_1.png");
			Mario_Big_Right_2 = loadImage("mario/Mario_Big_Right_2.png");
			Mario_Big_Right_Run_1 = loadImage("mario/Mario_Big_Right_Run_1.png");
			Mario_Big_Right_Run_2 = loadImage("mario/Mario_Big_Right_Run_2.png");
			Mario_Big_Crouch_Right = loadImage("mario/Mario_Big_Crouch_Right.png");
			Mario_Big_Jump_Right = loadImage("mario/Mario_Big_Jump_Right.png");
			Mario_Big_Change_Direction_Right = loadImage("mario/Mario_Big_Change_Direction_Right.png");
			//Fire Left Mario
			Mario_Big_Left_Still_Fire = loadImage("mario/Mario_Big_Left_Still_Fire.png");
			Mario_Big_Left_1_Fire = loadImage("mario/Mario_Big_Left_1_Fire.png");
			Mario_Big_Left_2_Fire = loadImage("mario/Mario_Big_Left_2_Fire.png");
			Mario_Big_Left_Run_1_Fire = loadImage("mario/Mario_Big_Left_Run_1_Fire.png");
			Mario_Big_Left_Run_2_Fire = loadImage("mario/Mario_Big_Left_Run_2_Fire.png");
			Mario_Big_Crouch_Left_Fire = loadImage("mario/Mario_Big_Crouch_Left_Fire.png");
			Mario_Big_Jump_Left_Fire = loadImage("mario/Mario_Big_Jump_Left_Fire.png");
			Mario_Big_Change_Direction_Left_Fire = loadImage("mario/Mario_Big_Change_Direction_Left_Fire.png");
			
			//Fire Right Mario
			Mario_Big_Right_Still_Fire = loadImage("mario/Mario_Big_Right_Still_Fire.png");
			Mario_Big_Right_1_Fire = loadImage("mario/Mario_Big_Right_1_Fire.png");
			Mario_Big_Right_2_Fire = loadImage("mario/Mario_Big_Right_2_Fire.png");
			Mario_Big_Right_Run_1_Fire = loadImage("mario/Mario_Big_Right_Run_1_Fire.png");
			Mario_Big_Right_Run_2_Fire = loadImage("mario/Mario_Big_Right_Run_2_Fire.png");
			Mario_Big_Crouch_Right_Fire = loadImage("mario/Mario_Big_Crouch_Right_Fire.png");
			Mario_Big_Jump_Right_Fire = loadImage("mario/Mario_Big_Jump_Right_Fire.png");
			Mario_Big_Change_Direction_Right_Fire = loadImage("mario/Mario_Big_Change_Direction_Right_Fire.png");
			
			Mario_Small_Left=loadImage("mario/Mario_Small_Left.png");
			Mario_Small_Right=loadImage("mario/Mario_Small_Right.png");
			
			Mario_swim_left_small=new SpriteMap(loadImage("mario/Mario_Small_Swim_Left.png"), 4, 1).getSprites();
			Mario_swim_right_small=new SpriteMap(loadImage("mario/Mario_Small_Swim_Right.png"), 4, 1).getSprites();
			Mario_swim_left_big=new SpriteMap(loadImage("mario/Mario_Big_Swim_Left.png"), 4, 1).getSprites();
			Mario_swim_right_big=new SpriteMap(loadImage("mario/Mario_Big_Swim_Right.png"), 4, 1).getSprites();
			Mario_swim_left_fire=new SpriteMap(loadImage("mario/Mario_Fire_Swim_Left.png"), 4, 1).getSprites();
			Mario_swim_right_fire=new SpriteMap(loadImage("mario/Mario_Fire_Swim_Right.png"), 4, 1).getSprites();
			  
				
				
			Mario_Flip=loadImage("mario/Mario_Flip.png");
			// coin
			Coin_Icon = loadImage("items/Coin_Icon.png");
			Coin_1 = loadImage("items/Coin_1.png");
			Coin_2 = loadImage("items/Coin_2.png");
			Coin_3 = loadImage("items/Coin_3.png");
			Coin_4 = loadImage("items/Coin_4.png");
			Coin_5 = loadImage("items/Coin_5.png");
			Coin_6 = loadImage("items/Coin_6.png");
			Coin_7 = loadImage("items/Coin_7.png");
			Coin_8 = loadImage("items/Coin_8.png");
			// goomba
			Goomba_Normal_1 = loadImage("baddies/Goomba_Normal_1.png");
			Goomba_Normal_2 = loadImage("baddies/Goomba_Normal_2.png");
			Goomba_Dead = loadImage("baddies/Goomba_Dead.png");
			Goomba_Flip = loadImage("baddies/Goomba_Flip.png");
			Fly_Goomba = loadImage("baddies/Fly_Goomba.png");;

			// mushroom
			Mushroom = loadImage("items/Mushroom.png");
			LifeUp = loadImage("items/1_Up.png");
			Fire_Flower= loadImage("items/Fire_Flower.png");
			// Platform
			Red_Platform_2 = loadImage("items/brett.png");
			
			// Fly redKoopa
			Fly_Koopa_Red_Left = loadImage("baddies/Fly_Koopa_Red_Left.png");
			Fly_Koopa_Red_Right = loadImage("baddies/Fly_Koopa_Red_Right.png");
					
			// redKoopa
			Koopa_Red_Left_1 = loadImage("baddies/Koopa_Red_Left_1.png");
			Koopa_Red_Left_2 = loadImage("baddies/Koopa_Red_Left_2.png");
			Koopa_Red_Right_1 = loadImage("baddies/Koopa_Red_Right_1.png");
			Koopa_Red_Right_2 = loadImage("baddies/Koopa_Red_Right_2.png");
			// redshell
			Red_Shell_1 = loadImage("baddies/Red_Shell_1.png");
			Red_Shell_2 = loadImage("baddies/Red_Shell_2.png");
			Red_Shell_3 = loadImage("baddies/Red_Shell_3.png");
			Red_Shell_4 = loadImage("baddies/Red_Shell_4.png");
			Red_Shell_Still = loadImage("baddies/Red_Shell_Still.png");
			Red_Shell_Flip = loadImage("baddies/Red_Shell_Flip.png");
			//Green_Koopa
			Green_Koopa = new SpriteMap( loadImage("baddies/Green_Koopa.png"), 4, 1).getSprites();
			//GreenShell
			Green_Shell = new SpriteMap( loadImage("baddies/Green_Shell.png"), 6, 1).getSprites();
			//Thorny
			Thorny = new SpriteMap( loadImage("baddies/thorny.png"), 5, 1).getSprites();
			Fire_Thorny = new SpriteMap( loadImage("baddies/Thorny_Bomb.png"), 2, 1).getSprites();
			Latiku = new SpriteMap( loadImage("baddies/Latiku.png"), 3, 1).getSprites();
			Latiku_Fire = new SpriteMap( loadImage("baddies/Latiku_Fire.png"), 2, 1).getSprites();
			
			Bowser_Left=new SpriteMap( loadImage("baddies/Bowser_Left.png"), 5, 1).getSprites();
			Bowser_Right=new SpriteMap( loadImage("baddies/Bowser_Right.png"), 5, 1).getSprites();
			
			BoomRang_left=new SpriteMap( loadImage("baddies/BoomRang_left.png"), 4, 1).getSprites();
			BoomRang_right=new SpriteMap( loadImage("baddies/BoomRang_right.png"), 4, 1).getSprites();
			
			// Score
			Score_100_New6 = loadImage("items/Score_100_New6.png");
			Score_1000 = loadImage("items/Score_1000.png");
			Score_200 = loadImage("items/Score_200.png");
			Score_1_Up=loadImage("items/Score_1_Up.png");
			// Tree
			Tree_1 = loadImage("items/Tree_1.png");
			Tree_2 = loadImage("items/Tree_2.png");
			// Bush
			Bush_1 = loadImage("items/Bush_1.png");
			Bush_2 = loadImage("items/Bush_2.png");
			//Fire_Tile
			Fire_Tile=new SpriteMap( loadImage("items/FireTile.png"), 2, 1).getSprites();
			// QuwestionBlock
			Question_Block_0 = loadImage("items/Question_Block_0.png");
			Question_Block_1 = loadImage("items/Question_Block_1.png");
			Question_Block_2 = loadImage("items/Question_Block_2.png");
			Question_Block_3 = loadImage("items/Question_Block_3.png");
			Question_Block_Dead = loadImage("items/Question_Block_Dead.png");
			//rotatingBlock
			Rotating_Block_Hit_1 = loadImage("items/Rotating_Block_Hit_1.png");
			Rotating_Block_Hit_2 = loadImage("items/Rotating_Block_Hit_2.png");
			Rotating_Block_Hit_3 = loadImage("items/Rotating_Block_Hit_3.png");
			Rotating_Block_Still = loadImage("items/Rotating_Block_Still.png");
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			//plainTiles
			Plain_Tiles=loadImage("tiles/Plain_Tiles.png");
			//grass
			Sloped_Tile = loadImage("items/Sloped_Tile.png");
			Grass_Edge = loadImage("items/Grass_Edge.png");
			Grass_Center = loadImage("items/Grass_Center.png");
			
			brick_particle=loadImage("items/particle_brick.png");
			Smoke_Particles = new SpriteMap(MarioResourceManager.loadImage("items/Smoke_Particle.png"), 8, 1).getSprites();
			Sparkle_Particles = new SpriteMap(MarioResourceManager.loadImage("items/Sparkle_Particle.png"), 8, 1).getSprites();
			Bomb_Particles = new SpriteMap(MarioResourceManager.loadImage("items/Bomb_Particle.png"), 8, 1).getSprites();
			
			RedFish=new SpriteMap( loadImage("baddies/Red_Fish.png"), 5, 1).getSprites();
			BlueFish=new SpriteMap( loadImage("baddies/Blue_Fish.png"), 5, 1).getSprites();
			Virus=new SpriteMap( loadImage("baddies/Virus.png"), 3, 1).getSprites();
			
			// fireball
			fb_1 = loadImage("baddies/Fireball_1.png");
			fb_2 = loadImage("baddies/Fireball_2.png");
			fb_3 = loadImage("baddies/Fireball_3.png");
			fb_4 = loadImage("baddies/Fireball_4.png");
			fb_5 = loadImage("baddies/Fireball_5.png");
			fb_6 = loadImage("baddies/Fireball_6.png");
			
			FireDisc=new SpriteMap(MarioResourceManager.loadImage("baddies/Fire_Disc.png"),3,1).getSprites();
			//Piranha
			Piranha_1=loadImage("baddies/Piranha_1.png");
			Piranha_2=loadImage("baddies/Piranha_2.png");
			
			//Spring
			Spring_1=loadImage("items/Spring_1.png");
			Spring_2=loadImage("items/Spring_2.png");
			Spring_3=loadImage("items/Spring_3.png");
			waterWave=loadImage("items/Water_Wave2.png");
			//background
			loadBackground(0);
			
			//hud```````````````````````````1!!!!!!!!!!!!!!!!!!!!!!!!112	
		    fontSmall=new SpriteMap(MarioResourceManager.loadImage("items/font_white_8.png"),96,1).getSprites();
		    fontMedium=new SpriteMap(MarioResourceManager.loadImage("items/font_white_6.png"),96,1).getSprites();
	        fontLarge=new SpriteMap(MarioResourceManager.loadImage("items/font_orange_16.png"),96,1).getSprites();
	
			// menu
			marioPic = loadImage("menu/marioPic.png");
			level = loadImage("menu/level.png");
			level_1star = loadImage("menu/level_1star.png");
			level_2star = loadImage("menu/level_2star.png");
			level_3star = loadImage("menu/level_3star.png");
			level_locked = loadImage("menu/level_locked.png");
			star3 = loadImage("menu/star3.png");
			levelComplete = MarioResourceManager.loadImage("baddies/level.png");
			Btn_Next= MarioResourceManager.loadImage("gui/Button_Next.png");
			Btn_Prev= MarioResourceManager.loadImage("gui/Button_Prev.png");
			Btn_Fire= MarioResourceManager.loadImage("gui/Button_Fire.png");
			Btn_Jump= MarioResourceManager.loadImage("gui/Button_Jump.png");
			logo=MarioResourceManager.loadImage("gui/Title.png");
			pearl1=MarioResourceManager.loadImage("items/pearl1.png");
			pearl2=MarioResourceManager.loadImage("items/pearl2.png");
			Castle=MarioResourceManager.loadImage("items/Castle.png");
			TapToStart=MarioResourceManager.loadImage("gui/TaptoStart.png");
		}
		firstTimeCreate=false;
	}
	
	//loads random background
	public static void loadBackground(int index){
		/*
		Random r = new Random();
		int rNum = r.nextInt(5);
		rNum=-1;
		if(rNum == 0) {
			Background=loadImage("backgrounds/Icy_Background.png");
		} else if(rNum == 1) {
			Background=loadImage("backgrounds/underground.png");
		} else if(rNum == 2) {
			Background=loadImage("backgrounds/sewage.png");
		} else if(rNum == 3) {
			Background=loadImage("backgrounds/background2.png");	
		} else  {
			Background=loadImage("backgrounds/water1.png");
		}
		*/
		Background=loadImage("backgrounds/background"+index+".png");
	}
	
	
    public static Bitmap loadImage(String fileName)
    {
 		try {
 			Log.i("resource",fileName);
			inputStream = activity.getAssets().open(fileName);	
			
			return BitmapFactory.decodeStream(inputStream, null, new BitmapFactory.Options());
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			Log.e("resource",e.getMessage()+fileName);
		}
		return null;
    }
    
	/** Horizontally flips img. */
	public static Bitmap horizontalFlip(Bitmap img) {   
        /*
		int w = img.getWidth();   
        int h = img.getHeight();   
        Bitmap dimg = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);     
        Canvas g = new Canvas(dimg); 
        g.drawBitmap(img, new Rect(w, 0, 0, h),new Rect(0, 0, w, h), null);   
        */
        return img;   
    }  
 
}