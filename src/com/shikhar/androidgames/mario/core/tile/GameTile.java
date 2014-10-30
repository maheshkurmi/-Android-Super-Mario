package com.shikhar.androidgames.mario.core.tile;



import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;

import com.shikhar.androidgames.mario.core.animation.Animation;
import com.shikhar.androidgames.mario.objects.base.Creature;




public class GameTile extends Tile {
	
	// fields
	private boolean isCollidable = true;
	private boolean isSloped = false;
	private List<Creature> collidingCreatures;
	
	/**
	 * Constructs a new GameTile at the pixel (x,y) position with the Animation anim
	 * and Image img.
	 */
	public GameTile(int pixelX, int pixelY, Animation anim, Bitmap img) {
		super(pixelX, pixelY, anim, img);
		collidingCreatures = new LinkedList<Creature>();
	}
	
	/**
	 * Constructs a new GameTile at the pixel (x,y) position with no Animation
	 * and the constant Image img.
	 */
	public GameTile(int pixelX, int pixelY, Bitmap img) {
		this(pixelX, pixelY, null, img);
	}
	
	/**
	 * Override to add action to this GameTile.
	 */
	public void doAction() { }
	
	/**
	 * @return true if this GameTile is collidable, else false.
	 */
	public boolean isCollidable() {
		return isCollidable;
	}
	
	/**
	 * @effects sets isCollidable to true or false.
	 */
	public void setIsCollidable(boolean isCollidable) {
		this.isCollidable = isCollidable;
	}
	
	public boolean isSloped() {
		return isSloped;
	}
	
	public void setIsSloped(boolean isSloped) {
		this.isSloped = isSloped;
	}

	/**
	 * @return a list of Creatures who are currently colliding with this GameTile.
	 */
	public List<Creature> collidingCreatures() {
		return collidingCreatures;
	}
}
