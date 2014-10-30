package com.shikhar.androidgames.mario.util;
// Object responsible for dealing with and cutting up a BufferedImage into individual pieces
// - Contains a BufferedImage spriteMap which is the image to be cut 
// - Contains an array of BufferedImages, sprites[], which store the cut images

// To use this object, create a SpriteMap with parameters image to be cut,
// the number of columns, and the number of rows. Then call getSprites() on
// the object to retrieve the array of cut images.


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class SpriteMap {
	
    private Bitmap spriteMap;
    private Bitmap[] sprites; 
    
    public SpriteMap(Bitmap spriteMap, int c, int r) {
        this.loadSprites(spriteMap, c, r);
    }
    
    // returns the sprites array
	public Bitmap[] getSprites() {
		return sprites;
	}
    
    // loads a BufferedImage into spriteSheet and then cuts the image
    // into individual sprites based on amount of columns and rows
    private void loadSprites(Bitmap spriteMap, int c, int r) {
    	this.spriteMap = spriteMap;
    	sprites = splitSprites(c, r);
    }
    
	
	// Splits a given sprite sheet into it's individual sprites and
	// returns the array containing each sprite from left to right, top to bottom.
	// This is acomplished by drawing a portion of the larger image onto a new BufferedImage
	// by calling the graphics of each new BufferedImage.
	private Bitmap[] splitSprites(int c, int r) {
		int pWidth = spriteMap.getWidth() / c; // width of each sprite
		int pHeight = spriteMap.getHeight() / r; // height of each sprite
		Bitmap[] sprites = new Bitmap[c*r];
		int n = 0; // used to count sprites
		
		//int xOff = 0; if needed to adjust cutting precision
		int yOff = 0;
	
		for(int y=0; y < r; y++) {
			for(int x = 0; x < c; x++) {
				sprites[n] = Bitmap.createBitmap(pWidth, pHeight, Bitmap.Config.ARGB_4444);
                Canvas g = new Canvas(sprites[n]); // retrieve graphics to draw onto the BufferedImage
                // draws a portion of the spriteMap into sprites by directly drawing on the BufferedImage
                g.drawBitmap(spriteMap, new Rect(pWidth*x, pHeight*y,pWidth*x+pWidth, pHeight*y+pHeight-yOff),new Rect(0, 0, pWidth, pHeight ),null); 
                n++; // next sprite
			}
		}
		return sprites;
	} 
}
