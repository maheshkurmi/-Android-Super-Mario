package com.shikhar.androidgames.mario.core;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import com.shikhar.androidgames.mario.util.SpriteMap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


/**
 * @author Mahesh Kurmi
 * this class will be responsible for loading/unloading our game resources (art, fonts, audio) 
 * this class will use singleton holder, which means we will be able to access this class from the global level.
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
    //coin
	public static Bitmap Coin_1, Coin_2,Coin_3,Coin_4;
	public static Bitmap Coin_5, Coin_6,Coin_7,Coin_8;
	//goomba
	public static Bitmap Goomba_Normal_1,Goomba_Normal_2,Goomba_Dead,Goomba_Flip;
	//mushroom
	public static Bitmap Mushroom;
	//platform
	public static Bitmap Red_Platform_2;
	//redKoopa
	public static Bitmap  Koopa_Red_Left_1,Koopa_Red_Left_2,Koopa_Red_Right_1,Koopa_Red_Right_2;
   //redShell
	public static Bitmap  Red_Shell_1,Red_Shell_2,Red_Shell_3,Red_Shell_4,Red_Shell_Flip;
	//Thorny
	public static Bitmap[] Thorny;
	//Score
	public static Bitmap  Score_100_New6;
	//tree
	public static Bitmap  Tree_1,Tree_2;
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
	//grass
	public static Bitmap Sloped_Tile;
	public static Bitmap Grass_Edge; 
	public static Bitmap Grass_Center; 
	//bricks shatter
	public static Bitmap brick_particle;
	//fireball
	public static Bitmap fb_1,fb_2,fb_3,fb_4,fb_5,fb_6;
	//piranha
	public static Bitmap Piranha_1,Piranha_2;
	//Hud
	public static Bitmap[] digits;
	
	
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
			// coin
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
			// mushroom
			Mushroom = loadImage("items/Mushroom.png");
			// Platform
			Red_Platform_2 = loadImage("items/Red_Platform_2.png");
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
			Red_Shell_Flip = loadImage("baddies/Red_Shell_Flip.png");
			//Thorny
			Thorny = new SpriteMap( loadImage("baddies/thorny.png"), 5, 1).getSprites();

			// Score
			Score_100_New6 = loadImage("items/Score_100_New6.png");
			// Tree
			Tree_1 = loadImage("items/Tree_1.png");
			Tree_2 = loadImage("items/Tree_2.png");
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
			
			// fireball
			fb_1 = loadImage("baddies/Fireball_1.png");
			fb_2 = loadImage("baddies/Fireball_2.png");
			fb_3 = loadImage("baddies/Fireball_3.png");
			fb_4 = loadImage("baddies/Fireball_4.png");
			fb_5 = loadImage("baddies/Fireball_5.png");
			fb_6 = loadImage("baddies/Fireball_6.png");
			
			//Piranha
			Piranha_1=loadImage("baddies/Piranha_1.png");
			Piranha_2=loadImage("baddies/Piranha_2.png");
			
			
			//background
			loadBackground();
			
			//hud
			digits = new SpriteMap( loadImage("menu/digits.png"), 11, 1).getSprites();
			
		}
		firstTimeCreate=false;
	}
	
	//loads random background
	private void loadBackground(){
		Random r = new Random();
		int rNum = r.nextInt(4);
		if(rNum == 0) {
			Background=loadImage("backgrounds/Icy_Background.png");
		} else if(rNum == 1) {
			Background=loadImage("backgrounds/NeoHillsBGnew.png");
		} else if(rNum == 2) {
			Background=loadImage("backgrounds/background2.png");
		} else  {
			Background=loadImage("backgrounds/NeoHillsBGnew.png");
		}
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