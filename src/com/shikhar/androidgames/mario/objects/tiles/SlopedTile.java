package com.shikhar.androidgames.mario.objects.tiles;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.shikhar.androidgames.mario.core.tile.GameTile;


public class SlopedTile extends GameTile {
	
	private Point startingPoint;
	private Point endingPoint;
	private boolean hasPositiveSlope;
		
	public SlopedTile(int pixelX, int pixelY, Bitmap img, boolean hasPositiveSlope) {
		
		super(pixelX, pixelY, null, img);
		setIsCollidable(false);
		setIsCollidable(true);
		setIsSloped(true);
		
		this.hasPositiveSlope = hasPositiveSlope;
		
		if(hasPositiveSlope) {
			startingPoint = new Point(pixelX, pixelY + 15);
			endingPoint = new Point(pixelX + 16, pixelY - 1);
		} else {
			startingPoint = new Point(pixelX, pixelY);
			endingPoint = new Point(pixelX + 16, pixelY + 14);
		}
	}
	
	public Point getStartingPoint() {
		return startingPoint;
	}
	
	public Point getEndingPoint() {
		return endingPoint;
	}
	
	public boolean hasPositiveSlope() {
		return hasPositiveSlope;
	}
	
	private int correlateY(float pixelX) {
		if(pixelX >= endingPoint.x) {
			return endingPoint.y;
		} else if(pixelX <= startingPoint.x) {
			return startingPoint.y;
		} else {
			return -((int) pixelX) + startingPoint.x + startingPoint.y;
		}
	}
	
	private int correlateX(float pixelY) {
		if(pixelY >= startingPoint.y) {
			return startingPoint.x;
		} else if(pixelY <= endingPoint.y) {
			return endingPoint.x;
		} else {
			return ((int) pixelY) - startingPoint.y + startingPoint.x;
		}
	}
	
	public Point isWithin(float pixelX, float pixelY, float deltaX, float deltaY) {
		int xOnLine = correlateX(pixelY);
		int yOnLine = correlateY(pixelX);
//		System.out.println();
//		System.out.println("X on Line: " + xOnLine);
//		System.out.println("Starting Point X: " + startingPoint.x);
//		System.out.println("Mario bottom right X: " + pixelX);
//		System.out.println("Ending Point X: " + endingPoint.x);
//		System.out.println("Y on Line: " + yOnLine);
//		System.out.println("Starting Point Y: " + startingPoint.y);
//		System.out.println("Mario bottom right Y: " + pixelY);
//		System.out.println("Ending Point Y: " + endingPoint.y);
		if(pixelX >= xOnLine && pixelX >= startingPoint.x && (pixelX <= endingPoint.x + 5 || pixelX + deltaX <= endingPoint.x) && 
		   pixelY >= yOnLine && pixelY >= endingPoint.y && (pixelY <= startingPoint.y + 1 || pixelY - deltaY <= startingPoint.y)
		   ) {
			//System.out.println("SLOPED COLLISION.");
			return new Point(xOnLine, yOnLine);
		} else {
			return null;
		}
	}
}
