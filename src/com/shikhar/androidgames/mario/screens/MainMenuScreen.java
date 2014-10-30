package com.shikhar.androidgames.mario.screens;

import java.util.List;

import android.graphics.Rect;
import android.util.Log;

import com.shikhar.androidgames.framework.Game;
import com.shikhar.androidgames.framework.Graphics;
import com.shikhar.androidgames.framework.Input.TouchEvent;
import com.shikhar.androidgames.framework.Screen;
import com.shikhar.androidgames.mario.core.Assets;

public class MainMenuScreen extends Screen {
	Rect playBound=new Rect(6,42,100,77);
	Rect scoreBound=new Rect(6,95,150,117);
	Rect optionBound=new Rect(6,141,99,163);
	
	public MainMenuScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
            	if (inBounds(event, playBound)) {
            	    GameScreen gameScreen=new GameScreen(game);
            		game.setScreen(gameScreen);
            		gameScreen.loadGame();
            		Log.i("Game","starts");
            	}else if(inBounds(event, scoreBound)){
            		Log.i("Game","hight Score clicked");
            	}else if(inBounds(event, optionBound)){
            		Log.i("Game","Options clicked");
            	}
            }
        }
    }

     
    @SuppressWarnings("unused")
	private boolean inBounds(TouchEvent event, int x, int y, int width,
            int height) {
        if (event.x > x && event.x < x + width - 1 && event.y > y
                && event.y < y + height - 1)
            return true;
        else
            return false;
    }

    private boolean inBounds(TouchEvent event, Rect r) {
       	if (event.x > r.left && event.x < r.right && event.y > r.top
                && event.y < r.bottom)
            return true;
        else
            return false;
       	//r.contains(x,y);
    }

    
    
    @Override
    public void paint(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawImage(Assets.menu, 0, 0);
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
        android.os.Process.killProcess(android.os.Process.myPid());

    }
}