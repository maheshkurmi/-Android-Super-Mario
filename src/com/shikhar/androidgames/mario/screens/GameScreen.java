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
import android.util.Log;

import com.shikhar.androidgames.framework.Game;
import com.shikhar.androidgames.framework.Graphics;
import com.shikhar.androidgames.framework.Input.TouchEvent;
import com.shikhar.androidgames.framework.Screen;
import com.shikhar.androidgames.framework.gfx.AndroidGame;
import com.shikhar.androidgames.mario.core.GameLoader;
import com.shikhar.androidgames.mario.core.GameRenderer;
import com.shikhar.androidgames.mario.core.MarioResourceManager;
import com.shikhar.androidgames.mario.core.MarioGame;
import com.shikhar.androidgames.mario.core.tile.GameTile;
import com.shikhar.androidgames.mario.core.tile.TileMap;
import com.shikhar.androidgames.mario.objects.base.Creature;
import com.shikhar.androidgames.mario.objects.creatures.Coin;
import com.shikhar.androidgames.mario.objects.mario.Mario;

public class GameScreen extends Screen {
    enum GameState {
        Ready, Running, Paused, GameOver
    }
    GameState state = GameState.Ready;

    // Variable Setup
    int livesLeft = 1;
	private Mario mario;
	private TileMap map;
	private TileMap backgroundMap;
	private TileMap foregroundMap;
	private GameRenderer renderer;
	public GameLoader gameLoader;
	public int period=20;
    Paint paint,paint2;
    Bitmap frameBuffer;
    boolean pauseDrawn=false;
    boolean readyDrawn=false;
	/**to simulate key right key left using accelerometer*/
	private int eventID=0;
	private Canvas gameCanvas;
    public GameScreen(Game game) {
        super(game);
        frameBuffer=((AndroidGame) game).getBuffer();
        gameCanvas=new Canvas(frameBuffer);
        // Initialize game objects here
        // Defining a paint object
        paint = new Paint();
        paint.setTextSize(12);
        paint.setTextAlign(Paint.Align.CENTER);
        //paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
       // paint.setFilterBitmap(true);
        
        paint2 = new Paint();
		paint2.setTextSize(60);
		paint2.setTextAlign(Paint.Align.CENTER);
		paint2.setAntiAlias(true);
		paint2.setColor(Color.WHITE);
        loadGame();
     }

     public void loadGame(){
    	//soundManager = new MarioSoundManager((Activity) game);
    	//ResourceManager.prepareManager((Activity) game);
    	mario = new Mario(((MarioGame)game).soundManager);
    	readyDrawn=false; 
		try {
			gameLoader = new GameLoader((AndroidGame) game);
			renderer = new GameRenderer();
			renderer.setBackground(MarioResourceManager.Background);
			map = gameLoader.loadMap("maps/map3.txt", ((MarioGame) game).soundManager); // use the ResourceManager to load the game map
			backgroundMap = gameLoader.loadOtherMaps("maps/back3.txt");
			foregroundMap = gameLoader.loadOtherMaps("maps/fore3.txt");
			map.setPlayer(mario); // set the games main player to mario
		} catch (IOException e){
			System.out.println("Invalid Map.");
			Log.e("Errrr", "invalid map");
		}
	}
     
    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        // We have four separate update methods in this example.
        // Depending on the state of the game, we call different update methods.
    
        if (state == GameState.Ready)
            updateReady(touchEvents);
        if (state == GameState.Running)
            updateRunning(touchEvents, deltaTime);
        if (state == GameState.Paused)
            updatePaused(touchEvents);
        if (state == GameState.GameOver)
            updateGameOver(touchEvents);
    }

    private void updateReady(List<TouchEvent> touchEvents) {
        // This example starts with a "Ready" screen.
        // When the user touches the screen, the game begins. 
        // state now becomes GameState.Running.
        // Now the updateRunning() method will be called!
         if (touchEvents.size() > 0)
            state = GameState.Running;
    }

    private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {
    	//if (state != GameState.Running)return;
    			if (mario.getHealth()==0) state = GameState.GameOver;
    	// 1. All touch input is handled here:
    			int len = touchEvents.size();
    			for (int i = 0; i < len; i++) {
    				TouchEvent event = touchEvents.get(i);
    				
    				if (event.type == TouchEvent.TOUCH_DOWN) {
    					if (event.y>200)mario.processEvent(2); 
    					else
    					mario.processEvent(0);
    				}
    			}
    	//	2. Handle accelerometer
    			float screenX = game.getInput().getAccelY();
    			// use screenX, screenY, as  accelerometer values now!
    			int iD=eventID;
    			if (screenX>1){
    				iD=1;
    				//mario.processEvent(-1);
    			}else if (screenX<-1){
    				iD=-1;
    				//mario.processEvent(1);
    			}else{
    				iD=3;
    				//mario.processEvent(3);
    			}
    			if (eventID!=iD){
    				eventID=iD;
    				mario.processEvent(eventID);
    			}
    			
    	// Update all relevant Creatures.
    				for(int i = 0; i < map.relevantCreatures().size(); i++) {
    					Creature c = map.relevantCreatures().get(i);
    					if(!(c instanceof Coin)) {
    						c.updateCreature(map, period);
    						mario.playerCollision(map, c);
    						for(Creature other : map.relevantCreatures()) {
    							c.creatureCollision(other);
    						}
    					} else {
    						c.updateCreature(map, period);
    						mario.playerCollision(map, c);
    					}
    				}
    				
    				// Debugging information:
    				//System.out.println("relevant creatures size: " + map.relevantCreatures().size());
    				//System.out.println("creatures size: " + map.creatures().size());
    				//System.out.println(map.platforms().size());
    				
    				for(GameTile tile : map.animatedTiles()) {
    		            //tile.collidingCreatures().clear();  // clear the colliding sprites on the tile
    		            tile.update(20);
    				}
    	        

    				for(GameTile tile : backgroundMap.animatedTiles()) {
    		            tile.collidingCreatures().clear();  // clear the colliding sprites on the tile
    		            tile.update(20);
    				}
    	        
    				for(GameTile tile : foregroundMap.animatedTiles()) {
    		            tile.collidingCreatures().clear();  // clear the colliding sprites on the tile
    		            tile.update(20);
    				}
    				
    				// Add creatures that need to be created. They are added here to avoid concurrent modifcation errors.
    	            for(Creature c : map.creaturesToAdd()) {
    	            	map.creatures().add(c);
    	            }
    	            
    	            map.creaturesToAdd().clear(); // This line MUST be called BEFORE mario.update(). Why?
    	            							  // If it is called after, all the creatures that are created
    	            							  // as a result of mario colliding are not added next update because
    	            							  // they are cleared immediately afterwards.

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
					//nullify();
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
                if (event.x > 300 && event.x < 980 && event.y > 100
                        && event.y < 500) {
                    nullify();
                    game.setScreen(new MainMenuScreen(game));
                    return;
                }
            }
        }
    }

    @Override
    public void paint(float deltaTime) {
        // First draw the game elements.
       // Secondly, draw the UI above the game elements.
        if (state == GameState.Ready)
            drawReadyUI();
        if (state == GameState.Running)
            drawRunningUI();
        if (state == GameState.Paused)
            drawPausedUI();
        if (state == GameState.GameOver)
            drawGameOverUI();
    }

    private void goToMenu() {
    	MainMenuScreen mainMenuScreen=new MainMenuScreen(game);
  		game.setScreen(mainMenuScreen);
  	}
    
    private void nullify() {
        // Set all variables to null. You will be recreating them in the
        // constructor.
        paint = null;
        // Call garbage collector to clean up memory.
        System.gc();
    }

    private void drawReadyUI() {
    	if (readyDrawn) return;
    	readyDrawn=true;
    	Graphics g = game.getGraphics();
	    //Canvas canvas=new Canvas(frameBuffer);
        renderer.draw(gameCanvas, map, backgroundMap, foregroundMap, frameBuffer.getWidth(), frameBuffer.getHeight());

        g.drawARGB(120, 0, 0, 0);
        
        g.drawString("USe accelerometer to move mario and tap to jump.",
                200, 100, paint);
    }

 
	private void drawRunningUI() {
       // Graphics g = game.getGraphics();
	   //Canvas gameCanvas=new Canvas(frameBuffer);
        renderer.draw(gameCanvas, map, backgroundMap, foregroundMap, frameBuffer.getWidth(), frameBuffer.getHeight());
    }

    private void drawPausedUI() {
        if (pauseDrawn) return;
        pauseDrawn=true;
    	Graphics g = game.getGraphics();
		// Darken the entire screen so you can display the Paused screen.
        g.drawARGB(120, 0, 0, 0);
        /*
        int w=frameBuffer.getWidth();
        int h=frameBuffer.getHeight();
		//g.drawRect(0, 0, w, h, Color.argb(200, 100, 100, 100));
		g.drawString("Resume",w/2-50, h/2-30, paint2);
		g.drawString("Menu", w/2-40, h/2+30, paint2);
		*/
    }

    private void drawGameOverUI() {
        Graphics g = game.getGraphics();
       // g.drawRect(0, 0, 300, 200, Color.BLACK);
        g.drawString("GAME OVER.", 200, 130, paint2);
    }

   
    
    @Override
    public void pause() {
        if (state == GameState.Running)
        {
        	state = GameState.Paused;
        	AlertDialog dialog = new AlertDialog.Builder((AndroidGame)game)
        	 .setTitle("Quit Game")
             .setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int whichButton) {
                 	goToMenu();
                 }
             })
             .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int whichButton) {
                	 state = GameState.Running;
                 }
              })
             .setMessage("Return to main menu?")
             .setOnCancelListener(new OnCancelListener(){
            	 @Override
            	 public void onCancel(DialogInterface arg0) {
					state = GameState.Running;
            	 }
              })
             .create();
        	dialog.setCanceledOnTouchOutside(false);
        	dialog.show();
        }

    }


    
    @Override
	public void resume() {
		if (state == GameState.Paused)
			state = GameState.Running;
		pauseDrawn=false;
	}

    @Override
    public void dispose() {

    }

    @Override
    public void onBackPressed() {
    	pause();
    	
    	if (state == GameState.Running){
			pause();
    	} 	else if(state == GameState.GameOver){
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
}