package com.shikhar.androidgames.mario.screens;

import android.R.color;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Paint.Align;

import com.shikhar.androidgames.framework.Game;
import com.shikhar.androidgames.framework.Graphics;
import com.shikhar.androidgames.framework.Screen;
import com.shikhar.androidgames.framework.gfx.AndroidGame;
import com.shikhar.androidgames.framework.gfx.AndroidGraphics;
import com.shikhar.androidgames.mario.core.MarioGame;
import com.shikhar.androidgames.mario.core.MarioResourceManager;
import com.shikhar.androidgames.mario.core.Settings;
import com.shikhar.androidgames.mario.util.SpriteMap;

public class LoadingScreen extends Screen {
	private boolean drawnOnce=false;
	private Bitmap[] font;
  
	public LoadingScreen(Game game) {
         super(game);
	     font=new SpriteMap(MarioResourceManager.loadImage("items/font_white_8.png"),96,1).getSprites();
    }

    @Override
    public void update(float deltaTime) {
    	if (!drawnOnce) return;
        ((MarioGame)game).soundManager.loadResouces();
        ((MarioGame)game).resourceManager.loadResouces();
        ((AndroidGame) game).setScreenWithFade(new GuiMenuScreen(game));//mainMenuScreen);
        Settings.loadPreferences((Context) game);
     }

    @Override
    public void paint(float deltaTime) {
    	
        Graphics g = game.getGraphics();
        //g.drawARGB(200, , g, b)
        Paint paint=new Paint(color.holo_blue_bright);
        paint.setTextAlign(Align.CENTER);
        paint.setTextSize(18);
        paint.setAntiAlias(true);
        //g.drawString("Loading....", 100,200,paint);//game.getScreenWidth()/2,game.getScreenHeight()/2, paint);
        ((AndroidGraphics) g).drawBitmapFont(font,"LOADING GAME....",game.getScreenWidth()/2-50,game.getScreenHeight()/2-6,8);
        //g.drawImage(Assets.splash,0, 0);
       	drawnOnce=true;
        
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void onBackPressed() {

    }
}