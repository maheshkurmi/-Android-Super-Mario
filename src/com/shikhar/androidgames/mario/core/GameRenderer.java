package com.shikhar.androidgames.mario.core;



import java.text.DecimalFormat;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.Point;
import com.shikhar.androidgames.mario.core.animation.Sprite;
import com.shikhar.androidgames.mario.core.tile.GameTile;
import com.shikhar.androidgames.mario.core.tile.Tile;
import com.shikhar.androidgames.mario.core.tile.TileMap;
import com.shikhar.androidgames.mario.objects.base.Creature;
import com.shikhar.androidgames.mario.objects.creatures.Platform;
import com.shikhar.androidgames.mario.objects.mario.Mario;


/**
 The GameRenderer class is responsible for all the drawing onto the screen.
 Also contains useful static methods for converting tiles->pixels, pixels->tiles
 and a method for locating which tile a sprite has collided with.
*/
public class GameRenderer {
	
	// AdjustYScroll is used to record the previous value of player.getY(). This way I can 
	// continue to draw on the same y level if there is no significant change in Y. I use 
	// the player jumping as a measure of significant change. Hides errors in my animations, 
	// keeping the screen from bobbing when there is a change in height of the player animation. 
	private int AdjustYScroll = 0;
	private ArrayList<TileMap> maps = new ArrayList<TileMap>();
	private DecimalFormat df2 = new DecimalFormat("0,000");//("#,###,###,##0000");

	// the size in bits of the tile
    private static final int TILE_SIZE = 16;
    // Math.pow(2, TILE_SIZE_BITS) == TILE_SIZE
    private static final int TILE_SIZE_BITS = 4;
   
    private boolean drawHudEnabled=true;
    private Bitmap background;
    public static int xOffset=0;
	public static int yOffset=0;
	int waveOffset=0;
    // Converts a pixel position to a tile position.
    public static int pixelsToTiles(float pixels) {
        return pixelsToTiles(Math.round(pixels));
    }

    // Converts a pixel position to a tile position.
    public static int pixelsToTiles(int pixels) {
        // use shifting to get correct values for negative pixels
        return pixels >> TILE_SIZE_BITS;
        // or, for tile sizes that aren't a power of two,
        // use the floor function: return (int)Math.floor((float)pixels / TILE_SIZE);
    }

    // Converts a tile position to a pixel position.
    public static int tilesToPixels(int numTiles) {
        // no real reason to use shifting here. it's slighty faster, but doesn't add up to much
        // on modern processors.
        return numTiles << TILE_SIZE_BITS;
        // use this if the tile size isn't a power of 2:
        //return numTiles * TILE_SIZE;
    }

    // Sets the background to draw.
    public void setBackground(Bitmap background) {
        this.background = background;
        MarioResourceManager.Background=background;
    }
    
	// Returns the tile that a Sprite has collided with. Returns null if no 
	// collision was detected. The last parameter, right, is used to check if multiple blocks
	// are hit when a sprite jumps.
	public static Point getTileCollision(TileMap map, Sprite sprite, float currX, float currY, float newX, float newY) {

	    float fromX = Math.min(currX, newX);
	    float fromY = Math.min(currY, newY);
	    float toX = Math.max(currX, newX);
	    float toY = Math.max(currY, newY);
	
	    // get the tile locations
	    int fromTileX = GameRenderer.pixelsToTiles(fromX);
	    int fromTileY = GameRenderer.pixelsToTiles(fromY);
	    int toTileX = GameRenderer.pixelsToTiles(toX + sprite.getWidth() - 1);
	    int toTileY = GameRenderer.pixelsToTiles(toY + sprite.getHeight() - 1);
	
	    // check each tile for a collision
	    for (int x=fromTileX; x<=toTileX; x++) {
	        for (int y=fromTileY; y<=toTileY; y++) {
	            if (x < 0 || x >= map.getWidth() || map.getImage(x, y) != null) {
	            	Tile tile = map.getTile(x,y);
	            	if(tile != null && map.getTile(x, y).isCollidable()) {
	                // collision found and the tile is collidable, return the tile
	            		return new Point(x,y);
	            	} 
	            }
	        }
	    }
	    // no collision found, return null
	    return null;
	}
	
	/**
	 * @return A List of Points, where each Point corresponds to the location of a tile the sprite is 
	 * colliding with in map.tiles().
	 */
	public static ArrayList<Point> getTileCollisionAll(TileMap map, Sprite sprite, float currX, float currY, float newX, float newY) {
		
		ArrayList<Point> collisionPoints = new ArrayList<Point>(); 
	    float fromX = Math.min(currX, newX);
	    float fromY = Math.min(currY, newY);
	    float toX = Math.max(currX, newX);
	    float toY = Math.max(currY, newY);
	
	    // get the tile locations
	    int fromTileX = GameRenderer.pixelsToTiles(fromX);
	    int fromTileY = GameRenderer.pixelsToTiles(fromY);
	    int toTileX = GameRenderer.pixelsToTiles(toX + sprite.getWidth() - 1);
	    int toTileY = GameRenderer.pixelsToTiles(toY + sprite.getHeight() - 1);
	
	    // check each tile for a collision
	    for (int x=fromTileX; x<=toTileX; x++) {
	        for (int y=fromTileY; y<=toTileY; y++) {
	            if (x < 0 || x >= map.getWidth() || map.getImage(x, y) != null) {
	            	Tile tile = map.getTile(x,y);
	            	if(tile != null && map.getTile(x, y).isCollidable()) {
	                // collision found and the tile is collidable, return the tile
	            		collisionPoints.add(new Point(x,y));
	            	} 
	            }
	        }
	    }
	    // no collision found, return null
	    return collisionPoints;
	}
    
	/**
	 * Draws all game elements. I did the best I can to separate all updating
	 * from drawing. However, it seems its much more efficient to do some
	 * updating here where I have all the information I need to make important
	 * decisions. So calling draw() DOES change the game state.
	 */
	public void draw(Canvas g, TileMap mainMap, TileMap backgroundMap,
			TileMap foregroundMap, int screenWidth, int screenHeight) {
		// add the three maps to the list of maps to draw, only mainMap is
		// interactive
		if (backgroundMap != null)
			maps.add(backgroundMap);
		maps.add(mainMap);
		if (foregroundMap != null)
			maps.add(foregroundMap);
		Mario player = mainMap.getPlayer();
		int mapWidth = tilesToPixels(mainMap.getWidth());
		int mapHeight = tilesToPixels(mainMap.getHeight());

		// get the scrolling position of the map based on player's position...

		int offsetX = screenWidth / 2 - Math.round(player.getX()) -TILE_SIZE;
		offsetX = Math.min(offsetX, 0); // if this gets set to 0, player is
										// within a screen width
		offsetX = Math.max(offsetX, screenWidth - mapWidth);

		int round = Math.round(player.getY());

		// initialize AdjustYScroll
		if (AdjustYScroll == 0) {
			AdjustYScroll = round;
		}

		// if the player is jumping, change the level at which the screen is
		// drawn.
		if (player.isJumping() || player.isAbovePlatform()
				|| player.isOnSlopedTile()) {
			AdjustYScroll = round;
		}

		int offsetY = screenHeight / 2 - AdjustYScroll - TILE_SIZE;
		offsetY = Math.min(offsetY, 0);
		offsetY = Math.max(offsetY, screenHeight - mapHeight);

		//Store offsetValues
		GameRenderer.xOffset=offsetX;
		GameRenderer.yOffset=offsetY;
		
		// draw parallax background image
		if (background != null) {
			// x and y are responsible for fitting the background image to the
			// size of the map
			int x;
			if (mapWidth - screenWidth >= 16)
				x = offsetX * (screenWidth - background.getWidth())
						/ (screenWidth - mapWidth);
			else
				x = 0;
			int y;
			if (mapHeight - screenHeight >= 16)
				y = offsetY * (screenHeight - background.getHeight())
						/ (screenHeight - mapHeight);
			else
				y = 0;
			g.drawBitmap(background, x, y, null);
		}

		int firstTileX = pixelsToTiles(-offsetX);
		int lastTileX = firstTileX + pixelsToTiles(screenWidth) + 1;
		int firstTileY = pixelsToTiles(-offsetY);
		int lastTileY = firstTileY + pixelsToTiles(screenHeight) + 1;

		if (lastTileX>= mainMap.getWidth())lastTileX= mainMap.getWidth()-1;
		for (TileMap map : maps) {
			// draw the visible tiles
			if (map != null && map.isVisible()) {
				for (int y = firstTileY; y <= lastTileY; y++) {
					for (int x = firstTileX; x <= lastTileX; x++) {
						GameTile tile = map.getTile(x, y);
						if (tile != null) {
							tile.draw(g, tilesToPixels(x), tilesToPixels(y),
									tile.getOffsetX() + offsetX,
									tile.getOffsetY() + offsetY);
						}
					}
				}
			}

			if (map == backgroundMap)
				if (background != null)g.drawARGB(50, 255, 255, 255); // /alpha 0 ==transparent
			if (map.isVisible()) {

				for (int i = 0; i < map.creatures().size(); i++) {

					Creature c = map.creatures().get(i);
					int x = Math.round(c.getX()) + offsetX;
					int y = Math.round(c.getY()) + offsetY;
					int tileX = pixelsToTiles(x);
					int tileY = pixelsToTiles(y);

					if (!c.isAlive()) {
						map.creatures().remove(i);
						i--;
					} else {
						if (Creature.WAKE_UP_VALUE_UP_LEFT <= tileX
								&& Creature.WAKE_UP_VALUE_DOWN_RIGHT >= tileX
								&& Creature.WAKE_UP_VALUE_UP_LEFT <= tileY
								&& Creature.WAKE_UP_VALUE_DOWN_RIGHT >= tileY) {

							// Only want to deal with platforms that are awake.
							if (c instanceof Platform) {
								map.platforms().add((Platform) c);
							}
							// Wake up the creature the first time the sprite is
							// in view.
							if (c.isSleeping()) {
								c.wakeUp(c.getX()>player.getX());
							}

							c.setIsOnScreen(true);
							if (!c.isInvisible()) {
								c.draw(g, x, y); // draw the creature
							}
							map.relevantCreatures().add(c);

						} else {
							if (c.isAlwaysRelevant()) {
								map.relevantCreatures().add(c);
							}
							c.setIsOnScreen(false);
						}
					}

					
					
				}
				// Draw the player.

				if (map == mainMap && !(((Mario) player).isInvisible())) {
					((Mario) player).draw(g, Math.round(player.getX())
							+ offsetX, Math.round(player.getY()) + offsetY,
							player.getOffsetX(), player.getOffsetY());
					if (map.particleSystem != null) {
						map.particleSystem.doDraw(g, offsetX, offsetY);
					}

				}
			}
		}

       // Paint paint =new Paint(Color.BLACK);
        //float dd2dec = new Float(df2.format(player.getdX())).floatValue();
        //g.drawText("dx: " + dd2dec, 300, 17,paint);
       
        if (drawHudEnabled){
    	  drawStringDropShadowAsHud(g,"MARIO x "+Settings.getLives(),8,4,0,1);
    	  drawStringDropShadowAsHud(g,df2.format(Settings.getScore()),8,20,0,1);
     	 
    	  g.drawBitmap(MarioResourceManager.Coin_Icon, 108,4, null);
    	  drawStringDropShadowAsHud(g,"x "+Settings.getCoins(),120,3,0,1);
    	  //drawStringDropShadow(g,"x "+Settings.getCoins(),9,0);
    	  drawStringDropShadowAsHud(g,"WORLD",170,4,0,1);
    	  drawStringDropShadowAsHud(g,Settings.world + "-" + Settings.level,170,20,0,1);
       	 
    	  drawStringDropShadowAsHud(g,"TIME-"+Settings.getTime(),screenWidth-4,4,0,-1);
        }	
    	    //g.drawBitmap(MarioResourceManager.waterWave, 65*(waveOffset/11), 40, null);
    		//g.drawBitmap(MarioResourceManager.waterWave, 65*(waveOffset/11)+MarioResourceManager.waterWave.getWidth(),40, null);
     		Paint p=new Paint();
    		p.setColor(Color.argb( 110,110, 150,204));
    		p.setStyle(Style.FILL);
       		//g.drawRect(0,40+MarioResourceManager.waterWave.getHeight(),screenWidth,screenHeight,p);
    		waveOffset++;
    		if (waveOffset*65/11>=MarioResourceManager.waterWave.getWidth())waveOffset=0;
    		
    		for(int i=0; i<mainMap.waterZones().size();i++){
    			Rect VisibleRect=new  Rect(firstTileX,firstTileY,lastTileX,lastTileY);
    			if (VisibleRect.intersect(mainMap.waterZones().get(i))){
    				int w=Math.min(MarioResourceManager.waterWave.getWidth()-65*(waveOffset/11),VisibleRect.width()*16);
    				int h=MarioResourceManager.waterWave.getHeight();

    				g.drawRect(VisibleRect.left*16+offsetX,VisibleRect.top*16+offsetY+h/2,VisibleRect.right*16+offsetX,VisibleRect.bottom*16+offsetY,p);
    				Rect dst=new Rect(VisibleRect.left*16+offsetX,VisibleRect.top*16+offsetY-h/2,VisibleRect.left*16+offsetX+w,VisibleRect.top*16+offsetY+h/2);
    				g.drawBitmap(MarioResourceManager.waterWave, new Rect(65*(waveOffset/11),0,65*(waveOffset/11)+w,MarioResourceManager.waterWave.getHeight()),dst,null);
    				
    				if(w<VisibleRect.width()*16){
    					dst=new Rect(VisibleRect.left*16+offsetX+w,VisibleRect.top*16+offsetY-h/2,VisibleRect.left*16+offsetX+VisibleRect.width()*16,VisibleRect.top*16+offsetY+h/2);
    					g.drawBitmap(MarioResourceManager.waterWave,new Rect(0,0,VisibleRect.width()*16-w,MarioResourceManager.waterWave.getHeight()),dst,null);
    					//g.drawBitmap(MarioResourceManager.waterWave,0,VisibleRect.top*16-MarioResourceManager.waterWave.getHeight()+offsetY,null);
    				}
    	    	}
    		}
        
        if (Settings.mUseOnScreenControls){
    	  g.drawBitmap(MarioResourceManager.Btn_Prev,5,screenHeight-54,null);
    	  g.drawBitmap(MarioResourceManager.Btn_Next,60,screenHeight-54,null);
    	  g.drawBitmap(MarioResourceManager.Btn_Next,60,screenHeight-54,null);
    	  g.drawBitmap(MarioResourceManager.Btn_Fire,screenWidth-108,screenHeight-54,null);
       	  g.drawBitmap(MarioResourceManager.Btn_Jump,screenWidth-53,screenHeight-54,null);
       	 
       }
       
		//g.drawBitmap(MarioResourceManager.waterWave, waveOffset-30, 26, null);
		//g.drawBitmap(MarioResourceManager.waterWave, waveOffset+MarioResourceManager.waterWave.getWidth()-30, 26, null);

	
    	  /*
        if(lastLife != player.getHealth()); {
	        lastLife = player.getHealth();
	        
      	 // drawStringDropShadow(g,"MARIO-"+lastLife,1,1);
      	//  drawStringDropShadow(g,"COINS-012",12,1);
      	 // drawStringDropShadow(g,"SCORE-02342",30,1);
	
	       
	       
        	int myColor = Color.argb(50, 50, 50, 50);
	        paint.setColor(myColor);
	        paint.setStyle(Style.FILL_AND_STROKE);
	        paint.setTextSize(16);
	        paint.setTypeface(Typeface.DEFAULT_BOLD);
	        paint.setTextAlign(Paint.Align.LEFT);
	        paint.setAntiAlias(true);
	        paint.setColor(Color.WHITE);
	        //g.draw3DRect(2, 2, screenWidth - 10, 18, true);
	        g.drawBitmap(MarioResourceManager.Mario_Big_Crouch_Right, 5,5, null);
	        g.drawText(" x "+lastLife, MarioResourceManager.Mario_Big_Crouch_Right.getWidth()+8, 6+14,paint);
	        
	        g.drawBitmap(MarioResourceManager.Coin_1, screenWidth-MarioResourceManager.Coin_1.getWidth()-3,5, null);
	        paint.setTextAlign(Paint.Align.RIGHT);
	        g.drawText(Settings.getScore()+" - ", screenWidth-MarioResourceManager.Coin_1.getWidth()-4, 6+14,paint);
	        
	       
	        g.drawRect(2, 2, screenWidth - 10, 18,paint);
	        //g.fill3DRect(2, 2, screenWidth - 10, 18, true);
	        paint.setColor(Color.BLACK);
	        int hbStart = 4;
	        int hbWidth = 35;  
	        paint.setStyle(Style.STROKE);	        
	        g.drawRect(hbStart, 4, hbWidth, 13,paint);
	        g.drawRect(hbStart + hbWidth, 4, hbWidth, 13, paint);
	        g.drawRect(hbStart + 2*hbWidth, 4, hbWidth, 13, paint);
	        
	        //g.draw3DRect(hbStart, 4, hbWidth, 13, true);
	        //g.draw3DRect(hbStart + hbWidth, 4, hbWidth, 13, true);
	        //g.draw3DRect(hbStart + 2*hbWidth, 4, hbWidth, 13, true);
	        
	        //Color myColor2 = new Color(200, 60, 60, 50);
	       
	        paint.setColor(Color.RED);
	        paint.setStyle(Style.FILL);
	        for(int i=0; i < player.getHealth(); i++) {
	        	g.drawRect(hbStart + i*hbWidth, 4, hbWidth, 13, paint);
	        } 
	        
	    }
	    */
        //drawHud( g);
        maps.clear(); 
    }
    
    
    private void drawHud(Canvas g){
        g.drawBitmap(MarioResourceManager.Mario_Big_Crouch_Right, 5,5, null);
        //g.drawText("-"+Settings.getLives(), MarioResourceManager.Mario_Big_Crouch_Right.getWidth()+3, 5,paint);
    }
    
	public void drawText(Canvas g, String line, int x, int y) {
		int len = line.length();
		Rect srcRect=new Rect(0,0,x+11,11);
		Rect dstRect=new Rect(x,y,x+11,y+11);
		int pos;
		for (int i = 0; i < len; i++) {
			char character = line.charAt(i);
			int srcX = 0;
			pos=(character - '0') * 11;
			
			if (pos>10 || pos <0) {
				pos=10;
			}
			srcX = pos * 11;
			srcRect.left=srcX;
			srcRect.right=srcX+11;
			
			dstRect.left=x;
			dstRect.right=x+11;
			
			//g.drawBitmap(MarioResourceManager.digits[pos],srcRect,dstRect,null);
			x += 11;
		}
	}
	
	public static void drawStringDropShadowAsEntity(Canvas g, String text,
			int x, int y,int type, int alignmrnt) {
		drawString(g, text, x, y, xOffset, type,alignmrnt);
	}

	public static void drawStringDropShadowAsHud(Canvas g, String text, int x,
			int y,int type,int alignment) {
		drawString(g, text, x, y, 0,type,alignment);
	}

	public static void drawStringDropShadow(Canvas g, String text, int x,
			int y,int type, int alignment) {
		drawString(g, text, x, y, 0,type,alignment);
	}
	
	/**
	 * 
	 * @param g
	 * @param text
	 * @param x
	 * @param y
	 * @param offset
	 * @param type
	 * @param alignment  -1==left, 0 ==centre, 1=right
	 */
	private static void drawString(Canvas g, String text, int x, int y,int offset, int type,int alignment) {
		if (alignment==0){
			if (type==1)
				x-=text.length()/2*12;
			else if (type==2)			
				x-=text.length()/2*16;
			else
				x-=text.length()/2*8;
		}else if(alignment==-1){
			if (type==1)
				x-=text.length()*12;
			else if (type==2)			
				x-=text.length()*16;
			else
				x-=text.length()*8;
		}
		
		x = x + offset;
		char[] ch = text.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			if (type==1)
				g.drawBitmap(MarioResourceManager.fontMedium[ch[i] - 32], x + i
						* 8, y, null);
			else if(type==2)
				g.drawBitmap(MarioResourceManager.fontLarge[ch[i] - 32], x + i
						* 16, y, null);
			else
				g.drawBitmap(MarioResourceManager.fontSmall[ch[i] - 32], x + i
						* 8, y, null);
			
		}
	}

	public boolean isDrawHudEnabled() {
		return drawHudEnabled;
	}

	public void setDrawHudEnabled(boolean drawHudEnabled) {
		this.drawHudEnabled = drawHudEnabled;
	}

}
