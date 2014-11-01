package com.shikhar.androidgames.mario.screens;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.shikhar.androidgames.framework.Game;
import com.shikhar.androidgames.framework.Input.TouchEvent;
import com.shikhar.androidgames.framework.Screen;
import com.shikhar.androidgames.framework.gfx.AndroidGame;
import com.shikhar.androidgames.mario.core.GameRenderer;
import com.shikhar.androidgames.mario.core.MarioResourceManager;
import com.shikhar.androidgames.mario.core.Settings;
import com.shikhar.androidgames.mario.objects.base.Creature;

public class HighScoreScreen extends Screen {

	Paint paint, paint2;
	Bitmap frameBuffer;
	float offsetY=0;
	float downY,Y;
	int offsetYMin,offsetYMax;
	private Canvas gameCanvas;
	private Bitmap background;
	int prevX, prevY;
   private boolean dragging=false;
	public HighScoreScreen(Game game) {
		super(game);
		frameBuffer = ((AndroidGame) game).getBuffer();
		gameCanvas = new Canvas(frameBuffer);
		background=MarioResourceManager.loadImage("backgrounds/smb.png");
		offsetYMax=32;
		offsetYMin=frameBuffer.getHeight()-336;
	}

    	
	@Override
	public void update(float deltaTime) {
		if(((AndroidGame)game).isScreenTransitionActive()) return;
		
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		updateRunning(touchEvents, deltaTime);
		if (!dragging){
			if (offsetY>0){
				offsetY--;
			}else if (offsetY<-24){
				offsetY++;
			}
		}
	}
	
	private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {
		
		if (touchEvents ==null || touchEvents.size()==0) return;
		dragging=false;
		for (TouchEvent  event:touchEvents){
			if (event.type==TouchEvent.TOUCH_DOWN){
				prevY=event.y;
				dragging=true;
			}else if (event.type==TouchEvent.TOUCH_DRAGGED){
				offsetY=offsetY+event.y-prevY;
				if (offsetY<offsetYMin)offsetY=offsetYMin;
				if (offsetY>offsetYMax)offsetY=offsetYMax;
			    prevY=event.y;
			    dragging=true;
			}
		}
		
	
	}

	@Override
	public void paint(float deltaTime) {
		int x,y;
		gameCanvas.drawBitmap(background, null,new Rect(0,0,frameBuffer.getWidth(),frameBuffer.getHeight()), null);		
		GameRenderer.drawStringDropShadow(gameCanvas,"HIGHSCORE",16*8,(int) (offsetY+8),0,1);
		GameRenderer.drawStringDropShadow(gameCanvas,"TIME (s)",30*8,(int) (offsetY+8),0,1);
		
		for (int i=1; i<=4;i++){
			y=(int) (offsetY+8*(10*(i-1)+4));
			x=2*8;
			GameRenderer.drawStringDropShadow(gameCanvas,"WORLD-"+i,x,y,0,1);
			GameRenderer.drawStringDropShadow(gameCanvas,"-------------------------------------",x,y+8,0,1);
			
			for (int j=1; j<=3;j++){
				if(i==4 &&j>1)continue;
				x=4*8;
				y=(int) (offsetY+8*(10*(i-1)+5+2*j));
				GameRenderer.drawStringDropShadow(gameCanvas,"Level-"+j,x,y,1,1);
				x=18*8;
				GameRenderer.drawStringDropShadow(gameCanvas,Settings.getHighScore(i, j)==0?"-":Settings.getHighScore(i, j)+"",x,y,1,1);
				x=32*8;
				GameRenderer.drawStringDropShadow(gameCanvas,Settings.getRecordTime(i, j)==10000 ?"-":Settings.getRecordTime(i, j)+"",x,y,1,1);
			}
		}
	}
	
	private void goToMenu() {
		GuiMenuScreen mainMenuScreen = new GuiMenuScreen(game);
		((AndroidGame) game).setScreenWithFade(mainMenuScreen);
	}


	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		Creature.WAKE_UP_VALUE_DOWN_RIGHT=game.getScreenWidth()/16;
	}

	@Override
	public void onBackPressed() {
		goToMenu();
	}

		@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
	

	
}