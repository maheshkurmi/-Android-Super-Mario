package com.shikhar.androidgames.mario.screens;

import java.io.IOException;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

public class WorldScreen extends Screen {

	private  Mario mario;
	private TileMap map;
	private TileMap backgroundMap;
	private TileMap foregroundMap;
	private GameRenderer renderer;
	public GameLoader gameLoader;
	public int period = 20;
	Paint paint, paint2;
	Bitmap frameBuffer;
	/** to simulate key right key left using accelerometer */
	private int eventID = 0;
	private Canvas gameCanvas;
    private boolean isSystemDriven=false;
    /**time in seconds  */
 	private boolean lockUpdates=true;
	private boolean lockInputs=true;
	private int blink=0;
	private int[] worldLocations;
	
	public WorldScreen(Game game) {
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

		loadGame();
		lockUpdates=false;
		lockInputs=false;
		Settings.world=1;
		Settings.level=0;
		//((MarioGame)game).soundManager.loadWorldMusic();
	}

 	public void loadGame() {
  		lockUpdates=true;
	
		try {
			gameLoader = new GameLoader((AndroidGame) game);
			renderer = new GameRenderer();
			renderer.setDrawHudEnabled(false);
			//renderer.setBackground(MarioResourceManager.loadImage("backgrounds/gray.png"));
			MarioResourceManager.Background=MarioResourceManager.loadImage("backgrounds/gray.png");
			renderer.setBackground(MarioResourceManager.Background);
			Log.e("D","maps/map3.txt");
			map = gameLoader.loadMap("maps/map3.txt",
					((MarioGame) game).soundManager); // use the ResourceManager
			int w= game.getScreenWidth()-map.getWidth()*16;
			if (w>0){
				w=Math.round(w/16.0f)+1;
				TileMap tempMap=new TileMap(map.getWidth()+w,map.getHeight());
				gameLoader.reLoadMap(tempMap,"maps/map3.txt",((MarioGame) game).soundManager,0);
				for (int i= map.getWidth();i<tempMap.getWidth();i++){
					for (int j=0;j<map.getHeight();j++){
					  tempMap.setTile(i, j, map.getTile(map.getWidth()-1, j));	
					}
				}
				map=tempMap;
			}
			backgroundMap = gameLoader.loadOtherMaps("maps/back3.txt");
			foregroundMap = gameLoader.loadOtherMaps("maps/fore3.txt");
			Settings.resetScores();
			mario = new Mario(((MarioGame) game).soundManager);
			mario.setX(48);
			mario.setY(0);
			eventID = 0;
			map.setPlayer(mario); // set the games main player to mario
			worldLocations=new int[map.creatures().size()];
			for (int i=0; i<map.creatures().size();i++){
				worldLocations[i]=(int) map.creatures().get(i).getX();
			}
			
		} catch (IOException e) {
			System.out.println("Invalid Map.");
			Log.e("Errrr", "invalid map");
		}
		lockUpdates=false;
		//Settings.loadPreferences((((AndroidGame)game)).getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, 0));
	}

   	
	@Override
	public void update(float deltaTime) {
		if(((AndroidGame)game).isScreenTransitionActive()) return;
		if (lockUpdates)return;
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		updateRunning(touchEvents, deltaTime);
	}
	
	private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {
		// if (state != GameState.Running)return;
	
		if (isSystemDriven){
			if (mario.getY()>=GameRenderer.tilesToPixels(map.getHeight())){
				isSystemDriven=false;
				lockInputs=false;
			}
			//Log.i("Time:", deltaTime+"s");
			mario.update(map, period,true);
			return;
		}
		
		if (mario.isLevelClear){
			for (int i=0; i<worldLocations.length;i++){
				if (mario.getX()==worldLocations[i]){
					Settings.world=i+1;
					Settings.level=0;
					break;
				}
			}
			((AndroidGame) game).setScreenWithFade(new LevelCompleteScreen(game));
			mario.isLevelClear=false;
			lockInputs=true;
			lockUpdates=true;
			return;
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
			int iD = eventID;
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

	@Override
	public void paint(float deltaTime) {
			drawRunningUI();
	}
	
	private void goToMenu() {
		GuiMenuScreen mainMenuScreen = new GuiMenuScreen(game);
		((AndroidGame) game).setScreenWithFade(mainMenuScreen);
	}

	private void drawRunningUI() {
		renderer.draw(gameCanvas, map, backgroundMap, foregroundMap,
				frameBuffer.getWidth(), frameBuffer.getHeight());
		
		GameRenderer.drawStringDropShadowAsEntity(gameCanvas, "WORLD-1",  worldLocations[0]+8, 100,0,0);
		GameRenderer.drawStringDropShadowAsEntity(gameCanvas, "WORLD-2",  worldLocations[1]+8, 100,0,0);
		GameRenderer.drawStringDropShadowAsEntity(gameCanvas, "WORLD-3",  worldLocations[2]+8, 100,0,0);
		GameRenderer.drawStringDropShadowAsEntity(gameCanvas, "WORLD-4",  worldLocations[3]+8, 100,0,0);
		
		blink++;
		if (blink>=30){
			GameRenderer.drawStringDropShadowAsHud(gameCanvas, "GET THE STAR TO ENTER......... ",frameBuffer.getWidth()/2, 16,0,0);
		}
		if (blink==80)blink=0;
	}


	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {

	}

	@Override
	public void onBackPressed() {
		goToMenu();
		//showControls_SetUp_Dialog();
	}

	private boolean inBounds(TouchEvent event, int x, int y, int width,
			int height) {

		if (event.x > x && event.x < x + width - 1 && event.y > y
				&& event.y < y + height - 1)
			return true;
		else
			return false;
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
	
	private void processTouchEvent(TouchEvent event ){
		if (!Settings.mUseOnScreenControls) {
			if (event.type == TouchEvent.TOUCH_DOWN) {
				if (event.y > 200 && mario.isFireMan())
					mario.processEvent(2);
				else
					mario.processEvent(0);
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
	

	
}