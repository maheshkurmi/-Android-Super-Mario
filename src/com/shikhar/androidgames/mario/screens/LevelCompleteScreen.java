package com.shikhar.androidgames.mario.screens;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import com.shikhar.androidgames.framework.Game;
import com.shikhar.androidgames.framework.Input.KeyEvent;
import com.shikhar.androidgames.framework.Input.TouchEvent;
import com.shikhar.androidgames.framework.Screen;
import com.shikhar.androidgames.framework.gfx.AndroidGame;
import com.shikhar.androidgames.mario.core.GameLoader;
import com.shikhar.androidgames.mario.core.GameRenderer;
import com.shikhar.androidgames.mario.core.MarioResourceManager;
import com.shikhar.androidgames.mario.core.MarioGame;
import com.shikhar.androidgames.mario.core.Settings;
import com.shikhar.androidgames.mario.core.tile.GameTile;
import com.shikhar.androidgames.mario.core.tile.TileMap;
import com.shikhar.androidgames.mario.objects.base.Creature;
import com.shikhar.androidgames.mario.objects.creatures.Coin;
import com.shikhar.androidgames.mario.objects.mario.Mario;

public class LevelCompleteScreen extends Screen {

	private  Mario mario;
	private TileMap map;
	private TileMap backgroundMap;
	private TileMap foregroundMap;
	private GameRenderer renderer;
	public GameLoader gameLoader;
	public int period = 20;
	Paint paint, paint2;
	Bitmap frameBuffer;
	public Bitmap tmpBitmap;
	private Canvas tmpCanvas;
	private Bitmap background;
    private boolean lockInputs=true;
	private int blink=0;
	private Bitmap bmp;
	private Point initialPt,finalPt;
	ArrayList<Point> pearls;
	private int pearlSize=10;
	private Bitmap bmpLevel;
	private boolean lockUpdates=false;
	public LevelCompleteScreen(Game game) {
		super(game);
		frameBuffer = ((AndroidGame) game).getBuffer();
		tmpBitmap=Bitmap.createBitmap(2*frameBuffer.getWidth(), 2*frameBuffer.getHeight(), Config.RGB_565);
		Creature.WAKE_UP_VALUE_DOWN_RIGHT=game.getScreenWidth()/8;
		new Canvas(frameBuffer);
		tmpCanvas=new Canvas(tmpBitmap);
		// Initialize game objects here
		// Defining a paint object
		paint = new Paint();
		paint.setTextSize(12);
		paint.setTextAlign(Paint.Align.CENTER);
		// paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
		// paint.setFilterBitmap(true);

		paint2 = new Paint();
		paint2.setTextSize(60);
		paint2.setTextAlign(Paint.Align.CENTER);
		paint2.setAntiAlias(true);
		paint2.setColor(Color.WHITE);
		//Settings.loadPreferences((((AndroidGame)game)).getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, 0));
		pearls=new ArrayList<Point>();
		pearlSize=MarioResourceManager.pearl1.getWidth();
		lockInputs=true;
		loadGame();
		lockUpdates=false;
	}

 	public void loadGame() {
  		try {
			gameLoader = new GameLoader((AndroidGame) game);
			renderer = new GameRenderer();
			renderer.setDrawHudEnabled(false);
			renderer.setBackground(null);//MarioResourceManager.loadImage("backgrounds/smb.png"));
			background=MarioResourceManager.loadImage("backgrounds/smb.png");
			bmpLevel=MarioResourceManager.loadImage("gui/"+Settings.world+".png");
			Log.e("D","Loading LevelComplete screen ");
			map = gameLoader.loadMap("maps/world"+Settings.world+"/map.txt",((MarioGame) game).soundManager); 
			mario = new Mario(((MarioGame) game).soundManager);
			mario.setX(32);
			mario.setY(32);
			map.setPlayer(mario); // set the games main player to mario
			if (Settings.level==0){
				lockInputs=false;
				Point p=map.bookMarks().get(0);
				initialPt=p;
				mario.setX(GameRenderer.tilesToPixels(p.x));
				mario.setY(GameRenderer.tilesToPixels(p.y+1)-mario.getHeight());//);
				pearls.add(new Point((int)(mario.getX()),(int) mario.getY()));
				finalPt=p;
				return;
			}
			if (map.bookMarks().size()>=Settings.level){
				Point p=map.bookMarks().get(Settings.level-1);
				initialPt=p;
				mario.setX(GameRenderer.tilesToPixels(p.x));
				mario.setY(GameRenderer.tilesToPixels(p.y+1)-mario.getHeight());//);
				finalPt=map.bookMarks().get(Settings.level);
				if (Settings.level >= 2) {
					Point p1 = map.bookMarks().get(0);
					Point p2 = map.bookMarks().get(1);

					for (int i = GameRenderer.tilesToPixels(p1.x); i < GameRenderer
							.tilesToPixels(p2.x); i += pearlSize) {
						pearls.add(new Point(i, GameRenderer
								.tilesToPixels(p1.y)));
					}
					for (int i = GameRenderer.tilesToPixels(p1.y); i < GameRenderer
							.tilesToPixels(p2.y); i += pearlSize) {
						pearls.add(new Point(GameRenderer.tilesToPixels(p2.x),
								i));
					}
				}if (Settings.level >= 3) {
					Point p1 = map.bookMarks().get(1);
					Point p2 = map.bookMarks().get(2);

					for (int i = GameRenderer.tilesToPixels(p1.x); i < GameRenderer
							.tilesToPixels(p2.x); i += pearlSize) {
						pearls.add(new Point(i, GameRenderer
								.tilesToPixels(p1.y)));
					}
					for (int i = GameRenderer.tilesToPixels(p1.y); i < GameRenderer
							.tilesToPixels(p2.y); i += pearlSize) {
						pearls.add(new Point(GameRenderer.tilesToPixels(p2.x),
								i));
					}
				}
				
				pearls.add(new Point((int)(mario.getX()),(int) mario.getY()));//+mario.getHeight()/2)));
			}else{
				finalPt=new Point(map.getWidth(),map.getHeight());
			}
			
		} catch (IOException e) {
			System.out.println("Invalid Map.");
			Log.e("Errrr", "invalid map");
		}	
 	}

   	@Override
	public void update(float deltaTime) {
 		if(((AndroidGame)game).isScreenTransitionActive()) return;
   		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		updateRunning(touchEvents, deltaTime);
	}
	
	private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {
		// if (state != GameState.Running)return;
		Point pt=pearls.get(pearls.size()-1);
		if (mario.getX()<GameRenderer.tilesToPixels(finalPt.x)){
			mario.setX(mario.getX()+2);
			if (mario.getX()-pt.x>pearlSize){
				pearls.add(new Point((int) mario.getX(),pt.y));
			}
		}else if(mario.getY()<GameRenderer.tilesToPixels(finalPt.y+1)-mario.getHeight()){
			mario.setY(mario.getY()+1);
			if (Math.abs(mario.getY()-pt.y)>=pearlSize){
				pearls.add(new Point(pt.x,(int) mario.getY()));
			}
		}else{
			
		}

		if (mario.getX()>=GameRenderer.tilesToPixels(finalPt.x)){
			lockInputs=false;
		}
		
		if (!lockInputs && (touchEvents.size() > 0 || game.getInput().getKeyEvents().size() > 0)) {
			Settings.level++;
			System.out.println("rrrSwitch levelecoplete screen");
			
			if (Settings.level > 3) {
				Settings.level =0;
				/*
				Settings.world++;
				if (Settings.getWorldsUnlocked() < Settings.world) {
					Settings.setWorldsUnlocked(Settings.world);
				}
				*/
				((AndroidGame) game).setScreenWithFade(new WorldScreen(game));
			} else {
				GameScreen gameScreen=new GameScreen(game);
				((AndroidGame) game).setScreenWithFade(gameScreen);
			}
			lockUpdates=true;
			lockInputs=true;
		}
	
		touchEvents.clear();
		game.getInput().getKeyEvents().clear();
	}

	@Override
	public void paint(float deltaTime) {
		drawRunningUI();
	}
	
	private void goToMenu() {
		if (lockInputs || Settings.level==0)return;
		GuiMenuScreen mainMenuScreen = new GuiMenuScreen(game);
		((AndroidGame) game).setScreenWithFade(mainMenuScreen);
	}

	private void drawRunningUI() {
		tmpCanvas.drawBitmap(background, null,new Rect(0,0,frameBuffer.getWidth()*2,frameBuffer.getHeight()*2), null);		
		tmpCanvas.save();
		
		tmpCanvas.translate(0, frameBuffer.getHeight()/2);
		
		renderer.draw(tmpCanvas, map, backgroundMap, foregroundMap,
				(int) (2*frameBuffer.getWidth()), frameBuffer.getHeight());
		tmpCanvas.restore();
		
		//gameCanvas.drawBitmap(MarioResourceManager.Background, 0, 0, null);		
		//gameCanvas.save();
		//gameCanvas.translate(0, frameBuffer.getHeight()/3);
		//gameCanvas.drawBitmap(tmpBitmap, null, new Rect(0,0,frameBuffer.getWidth(),frameBuffer.getHeight()/2),null);
		//gameCanvas.restore();
		
		tmpCanvas.drawBitmap(MarioResourceManager.logo, frameBuffer.getWidth()-MarioResourceManager.logo.getWidth()/2, 15, null);
		tmpCanvas.drawBitmap(bmpLevel, frameBuffer.getWidth()+MarioResourceManager.logo.getWidth()/2-15, 10, null);
		
		//GameRenderer.drawStringDropShadowAsEntity(gameCanvas, "WORLD-1", 19, 13);
		blink++;
		if (blink>=30){
			//if (lockInputs)
			  // GameRenderer.drawStringDropShadowAsHud(tmpCanvas, "MARIO IS ENTERING THE WORLD-"+ Settings.world + "AT STAGE" + (Settings.level+1), 10, frameBuffer.getHeight()/4-4);
			//else
			   tmpCanvas.drawBitmap(MarioResourceManager.TapToStart, frameBuffer.getWidth()-MarioResourceManager.TapToStart.getWidth()/2, 2*frameBuffer.getHeight()-2*MarioResourceManager.TapToStart.getHeight(), null);
		}
		
		if (blink==80)blink=0;
		if (blink % 20 <10){
			bmp=MarioResourceManager.pearl1;
		}else{
			bmp=MarioResourceManager.pearl2;
		}
		/*
		for (int i=0; i<mario.getX(); i+=MarioResourceManager.pearl1.getWidth()+3){
			gameCanvas.drawBitmap(bmp,i,mario.getY()+2*mario.getHeight(),null);
		}
		*/
		for (int i=0; i<pearls.size(); i++){
			tmpCanvas.drawBitmap(bmp,pearls.get(i).x+GameRenderer.xOffset,pearls.get(i).y+GameRenderer.yOffset+frameBuffer.getHeight()/2,null);
		}
		
		tmpCanvas.drawBitmap(MarioResourceManager.levelComplete, pearls.get(0).x-4+GameRenderer.xOffset,pearls.get(0).y-4+frameBuffer.getHeight()/2, paint);
		tmpCanvas.drawBitmap(MarioResourceManager.levelComplete, GameRenderer.tilesToPixels(initialPt.x)-5+GameRenderer.xOffset,GameRenderer.tilesToPixels(initialPt.y)+frameBuffer.getHeight()/2-15, paint);
		//if(Settings.level>2)
		Point p=map.bookMarks().get(map.bookMarks().size()-1);
		tmpCanvas.drawBitmap(MarioResourceManager.Castle, GameRenderer.tilesToPixels(p.x)+GameRenderer.xOffset-MarioResourceManager.Castle.getWidth()/2,GameRenderer.tilesToPixels(p.y+1)+GameRenderer.yOffset+frameBuffer.getHeight()/2-MarioResourceManager.Castle.getHeight(), paint);
		if (Settings.level==3 && lockInputs==false ){
			if (blink>=30) GameRenderer.drawStringDropShadow(tmpCanvas,"CONGRATUATIONS!! ",frameBuffer.getWidth(), 2*frameBuffer.getHeight()-4*MarioResourceManager.TapToStart.getHeight(),2,0);
			GameRenderer.drawStringDropShadow(tmpCanvas,"You Have Completed World " +Settings.world,frameBuffer.getWidth(), 2*frameBuffer.getHeight()-3*MarioResourceManager.TapToStart.getHeight(),2,0);
		}
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		Creature.WAKE_UP_VALUE_DOWN_RIGHT=game.getScreenWidth()/16;
		//tmpBitmap.recycle();
	}

	@Override
	public void onBackPressed() {
		goToMenu();
	}

		@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
	
	public Bitmap getBitmap(){
		return tmpBitmap;
	}
	
}