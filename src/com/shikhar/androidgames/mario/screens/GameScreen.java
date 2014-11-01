package com.shikhar.androidgames.mario.screens;

import java.io.IOException;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import com.shikhar.androidgames.framework.Game;
import com.shikhar.androidgames.framework.Graphics;
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
import com.shikhar.androidgames.mario.objects.creatures.CoinEmitter;
import com.shikhar.androidgames.mario.objects.creatures.CreatureEmitter;
import com.shikhar.androidgames.mario.objects.creatures.Latiku;
import com.shikhar.androidgames.mario.objects.mario.Mario;
import com.shikhar.androidgames.mario.preferences.PreferenceConstants;

public class GameScreen extends Screen {
	enum GameState {
		Ready, Running, SwitchLevel,Paused,GameOver
	}

	GameState state = GameState.Ready;

	// Variable Setup
	int livesLeft = 1;
	private static Mario mario;
	private TileMap map;
	private TileMap backgroundMap;
	private TileMap foregroundMap;
	private GameRenderer renderer;
	public GameLoader gameLoader;
	public int period = 20;
	Paint paint, paint2;
	Bitmap frameBuffer;
	boolean pauseDrawn = false;
	boolean readyDrawn = false;
	/** to simulate key right key left using accelerometer */
	private int eventID = 0;
	private Canvas gameCanvas;
    private boolean isSystemDriven=false;
    /**time in seconds  */
    private float timeRemaining=0;
	private float switchTime=0;
	private boolean lockInputs=true;
	private String msg="";
	private boolean savedScores=false;
	
    public GameScreen(Game game) {
		super(game);
		frameBuffer = ((AndroidGame) game).getBuffer();
		gameCanvas = new Canvas(frameBuffer);
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
		if (mario==null){
			Log.i("Mario","New Mario Created" +state);
			mario = new Mario(((MarioGame) game).soundManager);
			}
		loadGame();	
		Settings.setPlayer(mario);
		lockInputs=false;
		state = GameState.Running;
		((MarioGame)game).soundManager.loadGameMusic();
	}

    private void reLoadGame(int beginX) {
    	gameLoader = new GameLoader((AndroidGame) game);
    	try {
			gameLoader.reLoadMap(map,"maps/world"+Settings.world+"/map"+Settings.level+"/map3.txt",
					((MarioGame) game).soundManager,beginX);
	    	Settings.setLives(mario.getHealth());
	    	if (Settings.world!=4)timeRemaining=(map.getWidth()-beginX)/2+5;
	    	Settings.setTime((int) timeRemaining);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   // use the ResourceManager
    	msg="";
    	Settings.setLives(mario.getHealth());

    }

	public void loadGame() {
		// soundManager = new MarioSoundManager((Activity) game);
		// ResourceManager.prepareManager((Activity) game);
		readyDrawn = false;
		lockInputs=true;
		//lockUpdates=true;
		isSystemDriven=true;
		try {
			gameLoader = new GameLoader((AndroidGame) game);
			renderer = new GameRenderer();
			Log.e("D","maps/world"+Settings.world+"/map"+Settings.level+"/map3.txt");
			map = gameLoader.loadMap("maps/world"+Settings.world+"/map"+Settings.level+"/map3.txt",
					((MarioGame) game).soundManager); // use the ResourceManager
			int w= game.getScreenWidth()-map.getWidth()*16;
			if (w>0){
				w=w/16+1;
				TileMap tempMap=new TileMap(map.getWidth()+w,map.getHeight());
				gameLoader.reLoadMap(tempMap,"maps/world"+Settings.world+"/map"+Settings.level+"/map3.txt",
						((MarioGame) game).soundManager,0);
				for (int i= map.getWidth();i<tempMap.getWidth();i++){
					for (int j=0;j<map.getHeight();j++){
					  tempMap.setTile(i, j, map.getTile(map.getWidth()-1, j));	
					}
				}
				for(int i = 0; i < map.creatures().size(); i++) { 
					if((map.creatures().get(i) instanceof CoinEmitter) ||(map.creatures().get(i) instanceof Latiku) ||(map.creatures().get(i) instanceof CreatureEmitter)){
						tempMap.creatures().add(map.creatures().get(i));	 
			         } 
				}
				map=tempMap;
				
			}
			backgroundMap = gameLoader.loadOtherMaps("maps/world"+Settings.world+"/map"+Settings.level+"/back3.txt");
			foregroundMap = gameLoader.loadOtherMaps("maps/world"+Settings.world+"/map"+Settings.level+"/fore3.txt");
			MarioResourceManager.loadBackground(gameLoader.getBackGroundImageIndex());
			renderer.setBackground(MarioResourceManager.Background);
				
			Settings.resetScores();
			//mario.setAlive(true);
			//mario.reloadAnimation();
			//mario.setX(32);
			//mario.setY(1);
			//mario.setdY(0.1f);
			
			Log.i("Mario","begin new stage" +state);
			if (mario!=null && mario.getHealth()>=0){
				mario.Reset();
				Log.i("Mario","Reset Mario" +state);
			}else{
				mario.Reset();
				mario.resetHealth();
			}
			
			timeRemaining=((Settings.world==4)?25:0.5f)*map.getWidth();
			eventID = 0;
			map.setPlayer(mario); // set the games main player to mario
			Settings.setPlayer(mario);
	    	Settings.setLives(mario.getHealth());
	    	Settings.setTime((int) timeRemaining);
			//state = GameState.Ready;
			//isSystemDriven=true;
		} catch (IOException e) {
			System.out.println("Invalid Map.");
			Log.e("Errrr", "invalid map");
		}
		msg="";
		
	}


	@Override
	public void update(float deltaTime) {
		if(((AndroidGame)game).isScreenTransitionActive()) return;
		   
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
	    // We have four separate update methods
		// Depending on the state of the game, we call different update methods.
		
		if (state == GameState.Ready){
			updateReady(touchEvents,deltaTime);
		}else if (state == GameState.Running){
			//if (lockUpdates)return;
			updateRunning(touchEvents, deltaTime);
		}else if (state == GameState.Paused){
			updatePaused(touchEvents);
		}else if (state == GameState.GameOver){
			updateGameOver(touchEvents);
		}
	}

	private void updateReady(List<TouchEvent> touchEvents,float deltaTime) {
		// It starts with a "Ready" screen.
		// When the user touches the screen, the game begins.
		// state now becomes GameState.Running.
		// Now the updateRunning() method will be called!
		if (touchEvents.size() > 0)
		{
			state = GameState.Running;
			isSystemDriven=false;
			/*
			if (lockUpdates==false){
				isSystemDriven=true;
				switchTime=0;
			}else{
				lockUpdates=false;
			}
			*/
			touchEvents.clear();
			updateRunning(touchEvents, 0);
			paint(0);
			lockInputs=false;
			//((MarioGame) game).soundManager.playStageEnter();
		}
		lockInputs=true;
		updateRunning(touchEvents, 0);
	}

	private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {
		// if (state != GameState.Running)return;
		period=(int) (1000*deltaTime);
		if (isSystemDriven){
			//mario.setY(mario.getY()+0.1f);
			//mario.setdY(0);
			mario.setX(32);
			if (mario.getY()>=140 || Math.abs(mario.getdY())<=0.001){
				isSystemDriven=false;
				lockInputs=false;
				eventID=0;
				switchTime=0;
			}else{
				//Log.i("Time:", deltaTime+"s");
				mario.update(map, period,true);
				//return;
			}
		}
		
		if (!mario.isAlive()) {
			//msg="Mario Died !";
			if (mario.getY() > map.getHeight() * 16 + mario.getHeight()) {
				msg="";
				if (mario.getHealth() >= 0 && timeRemaining >0) {
					Point p = map.getRecentbookMarkLocation();
					switchTime=6;		
					lockInputs=true;
					mario.Reset();
					reLoadGame(p.x);
					eventID = 0;
					mario.setX(GameRenderer.tilesToPixels(p.x));
					mario.setY(GameRenderer.tilesToPixels(p.y)
							-mario.getHeight());
					Log.i("Mario ","died and reset");
					//Settings.setLives(mario.getHealth())
					//mario.update(map, period, true);
					//return;
				} else {
					state = GameState.GameOver;
					//msg="Game Over";
					renderer.draw(gameCanvas, map, backgroundMap,
							foregroundMap, frameBuffer.getWidth(),
							frameBuffer.getHeight());
					(game.getGraphics()).drawARGB(170, 0, 10, 0);
					//game.setScreen(new GuiLevelFailedScreen(game));
					return;
				}
			}
		}
		
		if (mario.isLevelClear){
			//save Time
			if (!savedScores){
				Settings.addRecordTime(Settings.world, Settings.level, map.getWidth()/2-(int) timeRemaining);
				savedScores=true;                      
			}
			if (timeRemaining>=-40){
				timeRemaining-=1;              
				if (timeRemaining>=0){ 
					Settings.setTime((int) timeRemaining);
					Settings.addScore(20);
					if ((int)timeRemaining % 5==0)((MarioGame) game).soundManager.playCoin();
				}
			}else{
				//timeRemaining=0;
				Settings.setTime(0);
				msg="Level Cleared !";
				Settings.addHighScore(Settings.world, Settings.level, Settings.getScore());
				((AndroidGame) game).setScreenWithFade(new LevelCompleteScreen(game));
				mario.isLevelClear=false;
				lockInputs=true;
			}
			if (timeRemaining<0)return;
		}
		
		if (timeRemaining<0){
			if (Settings.world==4){
				Settings.setTime(0);
				msg="Congratulations!! You have completed the Level";
				Settings.level=3;
				mario.killMario();
				state = GameState.GameOver;
				//msg="Game Over";
				renderer.draw(gameCanvas, map, backgroundMap,
						foregroundMap, frameBuffer.getWidth(),
						frameBuffer.getHeight());
				(game.getGraphics()).drawARGB(170, 0, 10, 0);
				//game.setScreen(new GuiLevelFailedScreen(game));
				return;
			}
			mario.killMario();	
			msg="TIME OVER";
			timeRemaining=map.getWidth()/2;
			return;
		}else if (!lockInputs){
			timeRemaining=timeRemaining-deltaTime;
			if(!lockInputs)Settings.setTime((int) timeRemaining);
		}
		
		
		if (switchTime>0){
			switchTime=0.99f*switchTime-deltaTime;
		}

		// 1. All touch input is handled here:
		if (!lockInputs && touchEvents != null && touchEvents.size() > 0) {
			int len = touchEvents.size();
			for (int i = 0; i < len; i++) {
				TouchEvent event = touchEvents.get(i);
				processTouchEvent(event);
			}
		}
		
		List<KeyEvent> keyEvents = game.getInput().getKeyEvents();
		if (!lockInputs && keyEvents != null && keyEvents.size() > 0) {
			int len = keyEvents.size();
			for (int i = 0; i < len; i++) {
				KeyEvent event = keyEvents.get(i);
				if (event.type ==KeyEvent.KEY_UP){
					mario.keyReleased(event);
				}else if (event.type ==KeyEvent.KEY_DOWN){
					mario.keyPressed(event);
				}
			}
		}
		
		
		if (!lockInputs && Settings.mUseOrientationForMovement) {
			// 2. Handle accelerometer
			float screenX = Settings.accelerometerSenseFactor
					* game.getInput().getAccelY();
			// use screenX, screenY, as accelerometer values now!
			int iD = 1;
			if (screenX > 1) {
				iD = 1;
			} else if (screenX < -1) {
				iD = -1;
			} else {
				iD = 3;
			}
			if (eventID != iD) {
				eventID = iD;
				mario.processEvent(eventID);
			}

		}
	
		
		for (GameTile tile : map.animatedTiles()) {
			tile.collidingCreatures().clear(); // clear the colliding sprites
			// on the tile
			tile.update(20);
		}

		for (GameTile tile : backgroundMap.animatedTiles()) {
			tile.collidingCreatures().clear(); // clear the colliding sprites on
												// the tile
			tile.update(20);
		}

		for (GameTile tile : foregroundMap.animatedTiles()) {
			tile.collidingCreatures().clear(); // clear the colliding sprites on
												// the tile
			tile.update(20);
		}

		// Update all relevant Creatures.
		for (int i = 0; i < map.relevantCreatures().size(); i++) {
			Creature c = map.relevantCreatures().get(i);
			if (!(c instanceof Coin)) {
				c.updateCreature(map, period);
				mario.playerCollision(map, c);
				for (Creature other : map.relevantCreatures()) {
					c.creatureCollision(other);
				}
				
			} else {
				c.updateCreature(map, period);
				mario.playerCollision(map, c);
			}
		}

		 if (map.particleSystem != null)map.particleSystem.updatePhysics(period);
		// Debugging information:
		// System.out.println("relevant creatures size: " +
		// map.relevantCreatures().size());
		// System.out.println("creatures size: " + map.creatures().size());
		// System.out.println(map.platforms().size());

		// Add creatures that need to be created. They are added here to avoid
		// concurrent modifcation errors.
		for (Creature c : map.creaturesToAdd()) {
			map.creatures().add(c);
		}

		map.creaturesToAdd().clear(); // This line MUST be called BEFORE
										// mario.update(). Why?
										// If it is called after, all the
										// creatures that are created
										// as a result of mario colliding are
										// not added next update because
										// they are cleared immediately
										// afterwards.

		mario.update(map, period);
		Coin.turn.update(period);
		map.relevantCreatures().clear();
		map.platforms().clear();
	}

	
	private void updatePaused(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = (TouchEvent) touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				if (inBounds(event, 0, 0, 200, 240)) {

					if (!inBounds(event, 0, 0, 35, 35)) {
						resume();
					}
				}

				if (inBounds(event, 0, 160, 300, 160)) {
					// nullify();
					goToMenu();
				}
			}
		}
	}

	
	private void updateGameOver(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				GameOver();
				return;
			}
		}
		
		List<KeyEvent> keyEvents = game.getInput().getKeyEvents();
		if ( keyEvents != null && keyEvents.size() > 0) {
			GameOver();
			return;
		}
		lockInputs=true;
		updateRunning(touchEvents, 0);
        paint(0);
	}


	@Override
	public void paint(float deltaTime) {
		// First draw the game elements.
		// Secondly, draw the UI above the game elements.
		if (state == GameState.Ready)
			drawReadyUI();
		if (state == GameState.Running)
			drawRunningUI();
		if (state == GameState.SwitchLevel)
			drawSwitchGameUI();
		if (state == GameState.Paused)
			drawPausedUI();
		if (state == GameState.GameOver)
			drawGameOverUI();
	}
	
	private void drawSwitchGameUI(){
		renderer.draw(gameCanvas, map, backgroundMap, foregroundMap,
				frameBuffer.getWidth(), frameBuffer.getHeight());
		Graphics g = game.getGraphics();
		g.drawARGB((int) (50*switchTime), 0, 0, 0);
	}
	
	private void goToMenu() {
		GuiMenuScreen mainMenuScreen = new GuiMenuScreen(game);
		((AndroidGame) game).setScreenWithFade(mainMenuScreen);
		//mario=null;
	}

	private void drawReadyUI() {
		//if (readyDrawn)
		//	return;
		readyDrawn = true;
		Graphics g = game.getGraphics();
		// Canvas canvas=new Canvas(frameBuffer);
		renderer.draw(gameCanvas, map, backgroundMap, foregroundMap,
				frameBuffer.getWidth(), frameBuffer.getHeight());
		g.drawARGB((int) (42*switchTime), 0, 0, 0);
		GameRenderer.drawStringDropShadowAsHud(gameCanvas,"PAUSED!",frameBuffer.getWidth()/2,frameBuffer.getHeight()/2-16,2,0);
		GameRenderer.drawStringDropShadowAsHud(gameCanvas,"TAP TO RESUME, PRESS BACK TO QUIT",frameBuffer.getWidth()/2+50,frameBuffer.getHeight()/2+8,1,0);
	}

	private void drawRunningUI() {
		// Graphics g = game.getGraphics();
		// Canvas gameCanvas=new Canvas(frameBuffer);
		renderer.draw(gameCanvas, map, backgroundMap, foregroundMap,
				frameBuffer.getWidth(), frameBuffer.getHeight());
		if (msg.length()>0)GameRenderer.drawStringDropShadowAsHud(gameCanvas, msg,frameBuffer.getWidth()/2,frameBuffer.getHeight()/2 ,1,0);
		if (switchTime<3){
			lockInputs=false;
		}else if(switchTime>0) {
			Graphics g = game.getGraphics();
			g.drawARGB((int) (42*switchTime), 0, 0, 0);
		}
	}

	private void drawPausedUI() {
		if (pauseDrawn)
			return;
		pauseDrawn = true;
		Graphics g = game.getGraphics();
		// Darken the entire screen so you can display the Paused screen.
		g.drawARGB(120, 0, 0, 0);
		/*
		 * int w=frameBuffer.getWidth(); int h=frameBuffer.getHeight();
		 * //g.drawRect(0, 0, w, h, Color.argb(200, 100, 100, 100));
		 * g.drawString("Resume",w/2-50, h/2-30, paint2); g.drawString("Menu",
		 * w/2-40, h/2+30, paint2);
		 */
	}

	private void drawGameOverUI() {
		renderer.draw(gameCanvas, map, backgroundMap, foregroundMap,
				frameBuffer.getWidth(), frameBuffer.getHeight());
		GameRenderer.drawStringDropShadowAsHud(gameCanvas,"GAME OVER...",frameBuffer.getWidth()/2,frameBuffer.getHeight()/2-16,2,0);
		GameRenderer.drawStringDropShadowAsHud(gameCanvas,"TAP TO RETURN TO MAIN MENU",frameBuffer.getWidth()/2+50,frameBuffer.getHeight()/2+8,1,0);
	}
	
	@Override
	public void pause() {
		if (state == GameState.Running) {
			state=GameState.Ready;
			lockInputs=true;
		}
	}
	
	public void showPauseDialog() {
		lockInputs=true;
		//state = GameState.Paused;
		AlertDialog dialog = new AlertDialog.Builder((AndroidGame) game)
					.setTitle("Quit Game")
					.setPositiveButton("Quit",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									goToMenu();
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									state = GameState.Running;
								}
							}).setMessage("Return to main menu?")
					.setOnCancelListener(new OnCancelListener() {
						@Override
						public void onCancel(DialogInterface arg0) {
							state = GameState.Running;
							//lockInputs=false;
							//lockUpdates=false;
						}
					}).create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	
	private void GameOver() {
		if (Settings.world==4){
			Settings.addHighScore(4, 1, Settings.getScore());
			Settings.addRecordTime(4, 1, 10000);
		}
		state = GameState.Running;
		isSystemDriven=false;
		lockInputs=false;
		mario.resetHealth();
		((AndroidGame) game).setScreenWithFade(new GuiMenuScreen(game));
		goToMenu();
		return;
        /*
		((AndroidGame)game).runOnUiThread(new Runnable(){
			   @Override
				public void run() {
					// TODO Auto-generated method stub
				   showGameOverDialog(); 
				}
		   }
		);
		*/
	}	
	
	private void showGameOverDialog(){
		AlertDialog dialog = new AlertDialog.Builder((AndroidGame) game)
					.setTitle("GameOver")
					.setPositiveButton("Ok, Let Me Retry!",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									state = GameState.Running;
									Point p = map.getRecentbookMarkLocation();
									switchTime=6;		
									lockInputs=true;
									mario.Reset();
									mario.resetHealth();
									reLoadGame(p.x);
									eventID = 0;
									mario.setX(GameRenderer.tilesToPixels(p.x));
									mario.setY(GameRenderer.tilesToPixels(p.y)
											-mario.getHeight());
									Log.i("Mario ","died and reset");
								}
							})
					.setNegativeButton("No, I Quit..",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									mario.resetHealth();
									((AndroidGame) game).setScreenWithFade(new GuiMenuScreen(game));
								}
							}).setMessage("You have no more lives left... \nWanna Play Same Level again?")
					.setOnCancelListener(new OnCancelListener() {
						@Override
						public void onCancel(DialogInterface arg0) {
							mario.resetHealth();
							((AndroidGame) game).setScreenWithFade(new GuiMenuScreen(game));

						}
					}).create();
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
	}

	
	@Override
	public void resume() {
		//state = GameState.Ready;
		//lockUpdates=false;
		///if (state == GameState.Paused)
		//	state = GameState.Running;
		//pauseDrawn = false;
	}

	@Override
	public void dispose() {

	}

	
	@Override
	public void onBackPressed() {
		//pause();
		if (switchTime>0||isSystemDriven||mario.isSystemDriven())return;
		if (state == GameState.Ready) {
			goToMenu();
		}else if (state == GameState.Running) {
			pause();
			//showPauseDialog();
		} else if (state == GameState.GameOver) {
			goToMenu();
		}

	}

	private boolean inBounds(TouchEvent event, int x, int y, int width,
			int height) {

		if (event.x > x && event.x < x + width - 1 && event.y > y
				&& event.y < y + height - 1)
			return true;
		else
			return false;
	}
	
	private void processTouchEvent(TouchEvent event ){
		if (!Settings.mUseOnScreenControls) {
			if (event.type == TouchEvent.TOUCH_DOWN) {
				if (event.y > 200 && mario.isFireMan()){
					mario.processEvent(2);
				}else if (event.y < 10 && event.x < 10){
					if (Settings.level<3 && Settings.world<4)game.setScreen(new LevelCompleteScreen(game));
				}else{
					mario.processEvent(0);
				}
			}
			return;
		}
		
		
		KeyEvent keyEvent=new KeyEvent();
		
		if (inBounds(event, 5,game.getScreenHeight()-54, 48, 48)){
			keyEvent.keyCode=android.view.KeyEvent.KEYCODE_DPAD_LEFT;
		}else if  (inBounds(event, 60,game.getScreenHeight()-54, 48, 48)){
			keyEvent.keyCode=android.view.KeyEvent.KEYCODE_DPAD_RIGHT;
		}else if  (inBounds(event, game.getScreenWidth()-108,game.getScreenHeight()-54, 48, 48)){
			keyEvent.keyCode=android.view.KeyEvent.KEYCODE_S;
		}else if  (inBounds(event, game.getScreenWidth()-53,game.getScreenHeight()-54, 48, 48)){
			keyEvent.keyCode=android.view.KeyEvent.KEYCODE_SPACE;
		}else{
			//mario.processEvent(0);
		}
		
		if (event.type==TouchEvent.TOUCH_DOWN){
			mario.keyPressed(keyEvent);
		}else if (event.type==TouchEvent.TOUCH_UP) {
			mario.keyReleased(keyEvent);
		}
	}
	
	public static Mario getMario(){
		return mario;
	}
}