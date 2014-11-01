package com.shikhar.androidgames.mario.screens;

import com.shikhar.androidgames.framework.Game;
import com.shikhar.androidgames.framework.Graphics;
import com.shikhar.androidgames.framework.Graphics.ImageFormat;
import com.shikhar.androidgames.framework.Screen;

public class SplashLoadingScreen extends Screen {
    
	
	
	public SplashLoadingScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
       Graphics g = game.getGraphics();
       game.setScreen(new LoadingScreen(game));
    }

    @Override
    public void paint(float deltaTime) {
    	 
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