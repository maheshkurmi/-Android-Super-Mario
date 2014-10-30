package com.shikhar.androidgames.mario.core.tile;


import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.shikhar.androidgames.mario.core.GameRenderer;
import com.shikhar.androidgames.mario.core.animation.Animatible;
import com.shikhar.androidgames.mario.core.animation.Animation;



public class Tile extends Animatible {
	
	// fields
	private int tileX;
	private int tileY;
	private int pixelX;
	private int pixelY;
	protected Bitmap img;
	
	public Tile(int pixelX, int pixelY, Animation anim, Bitmap img) {
		setTileX(GameRenderer.pixelsToTiles(pixelX));
		setTileY(GameRenderer.pixelsToTiles(pixelY));
		this.pixelX = pixelX;
		this.pixelY = pixelY;
		this.img = img;
		setAnimation(anim);
	}
	
	public Tile(int pixelX, int pixelY, Bitmap img) {
		this(pixelX, pixelY, null, img);
	}
	
	public void draw(Canvas g, int pixelX, int pixelY) {
		g.drawBitmap(getImage(), pixelX, pixelY, null);
	}
	
	public void draw(Canvas g, int pixelX, int pixelY, int offsetX, int offsetY) {
		draw(g, pixelX + offsetX, pixelY + offsetY);
	}
	
	public Bitmap getImage() {
		return (currentAnimation() == null) ? img : currentAnimation().getImage();
	}
	
	public int getPixelX() {
		return pixelX;
	}
	
	public int getPixelY() {
		return pixelY;
	}
	
	public int getWidth() {
		return getImage().getWidth();
	}
	
	public int getHeight() {
		return getImage().getHeight();
	}

	public int getTileX() {
		return tileX;
	}

	public void setTileX(int tileX) {
		this.tileX = tileX;
	}

	public int getTileY() {
		return tileY;
	}

	public void setTileY(int tileY) {
		this.tileY = tileY;
	}
} // Tile
