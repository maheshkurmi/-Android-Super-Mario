package com.shikhar.androidgames.mario.core;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;



import android.graphics.Bitmap;

import com.shikhar.androidgames.framework.gfx.AndroidGame;
import com.shikhar.androidgames.mario.core.tile.GameTile;
import com.shikhar.androidgames.mario.core.tile.TileMap;
import com.shikhar.androidgames.mario.objects.creatures.Coin;
import com.shikhar.androidgames.mario.objects.creatures.Goomba;
import com.shikhar.androidgames.mario.objects.creatures.Piranha;
import com.shikhar.androidgames.mario.objects.creatures.Platform;
import com.shikhar.androidgames.mario.objects.creatures.RedKoopa;
import com.shikhar.androidgames.mario.objects.creatures.RedShell;
import com.shikhar.androidgames.mario.objects.creatures.Thorny;
import com.shikhar.androidgames.mario.objects.creatures.Tree;
import com.shikhar.androidgames.mario.objects.tiles.Brick;
import com.shikhar.androidgames.mario.objects.tiles.QuestionBlock;
import com.shikhar.androidgames.mario.objects.tiles.RotatingBlock;
import com.shikhar.androidgames.mario.objects.tiles.SlopedTile;
import com.shikhar.androidgames.mario.util.SpriteMap;


/**
 * map loader class
 * @author mahesh
 *
 */
public class GameLoader {
	
	private ArrayList<Bitmap> plain;
	private Bitmap[] plainTiles;
	
	private Bitmap sloped_image;
	private Bitmap grass_edge;
	private Bitmap grass_center;
	private AndroidGame gameActivity;
	public GameLoader(AndroidGame activity) {
		this.gameActivity=activity;
		plain = new ArrayList<Bitmap>();
		plainTiles = (new SpriteMap(MarioResourceManager.Plain_Tiles, 6, 17)).getSprites();
		
		for (Bitmap bImage : plainTiles) {
			plain.add(bImage);
		}
		
		sloped_image = MarioResourceManager.Sloped_Tile;
		grass_edge = MarioResourceManager.Grass_Edge;
		grass_center =MarioResourceManager.Grass_Center;
	}
	
	
	// BufferedImage -> Image
	// public static Image toImage(BufferedImage bufferedImage) {
	//    return Toolkit.getDefaultToolkit().createImage(bufferedImage.getSource());
	// }

	// loads a tile map, given a map to load..
    // use this to load the background and foreground. Note: the status of the tiles (ie collide etc)
    // is irrelevant. Why? I don't check collision on maps other than the main map. 
    public TileMap loadOtherMaps(String filename) throws IOException {
		// lines is a list of strings, each element is a row of the map
		ArrayList<String> lines = new ArrayList<String>();
		int width = 0;
		int height = 0;
		
		// read in each line of the map into lines
		
		Scanner reader = new Scanner(gameActivity.getAssets().open(filename));
		while(reader.hasNextLine()) {
			String line = reader.nextLine();
			if(!line.startsWith("#")) {
				lines.add(line);
				width = Math.max(width, line.length());
			}
		}
		height = lines.size(); // number of elements in lines is the height
		
		TileMap newMap = new TileMap(width, height);
		for (int y=0; y < height; y++) {
			String line = lines.get(y);
			for (int x=0; x < line.length(); x++) {
				char ch = line.charAt(x);
				int pixelX = GameRenderer.tilesToPixels(x);
				int pixelY = GameRenderer.tilesToPixels(y);
				if (ch == 'n') {
					newMap.setTile(x, y, plain.get(92));
				} else if (ch == 'm') {
					newMap.setTile(x, y, plain.get(93));
				} else if (ch == 'a') {
					newMap.setTile(x, y, plain.get(90));
				} else if (ch == 'b') {
					newMap.setTile(x, y, plain.get(91));
				} else if (ch == 'q') { // rock left
					newMap.setTile(x, y, plain.get(48));
				} else if (ch == 'r') { // rock right
					newMap.setTile(x, y, plain.get(49));
				} else if (ch == 'z') { // tree stem
					newMap.setTile(x, y, plain.get(75));
				} else if (ch=='T') {
					Tree t = new Tree(pixelX, pixelY);
					newMap.setTile(x, y, t);
					newMap.animatedTiles().add(t);
				} else if (ch == 'V') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(56));
					newMap.setTile(x, y, t);
				} else if (ch == '3') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(4));
					newMap.setTile(x, y, t);
				} else if (ch == '4') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(10));
					newMap.setTile(x, y, t);
				} else if (ch == '2') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(86));
					newMap.setTile(x, y, t);
				}else if(ch == '9') {
					SlopedTile t = new SlopedTile(pixelX, pixelY, sloped_image, true);
					newMap.setTile(x, y, t);
					newMap.slopedTiles().add(t);
				} else if(ch == '8') {
					GameTile t = new GameTile(pixelX, pixelY, grass_edge);
					newMap.setTile(x, y, t);
				} else if(ch == '7') {
					GameTile t = new GameTile(pixelX, pixelY, grass_center);
					newMap.setTile(x, y, t);
				} else if(ch == 't') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(96)); //pipe top left
					newMap.setTile(x, y, t);
				} else if(ch == 'u') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(97)); //pipe top middle
					newMap.setTile(x, y, t);
				} else if(ch == 'v') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(98)); //pipe top right
					newMap.setTile(x, y, t);
				} else if(ch == 'w') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(99)); //pipe base left
					newMap.setTile(x, y, t);
				} else if(ch == 'x') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(100)); //pipe base middle
					newMap.setTile(x, y, t);
				} else if(ch == 'y') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(101)); //pipe base right
					newMap.setTile(x, y, t);
				} else if(ch == 'z') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(75)); //tree stem
					newMap.setTile(x, y, t);
				}else if(ch == 'n') {
					newMap.setTile(x, y, plain.get(92));
				} else if (ch == 'm') {
					newMap.setTile(x, y, plain.get(93));
				} else if (ch == 'a') {
					newMap.setTile(x, y, plain.get(90));
				} else if (ch == 'b') {
					newMap.setTile(x, y, plain.get(91));
				} else if (ch == 'q') { // rock left
					newMap.setTile(x, y, plain.get(48));
				} else if (ch == 'r') { // rock right
					newMap.setTile(x, y, plain.get(49));
				}
			}
		}
		return newMap;	
	}
    	
    // Use this to load the main map
	public TileMap loadMap(String filename, MarioSoundManager soundManager) throws IOException {
		// lines is a list of strings, each element is a row of the map
		ArrayList<String> lines = new ArrayList<String>();
		int width = 0;
		int height = 0;
		
		// read in each line of the map into lines
		Scanner reader = new Scanner(gameActivity.getAssets().open(filename));
		while(reader.hasNextLine()) {
			String line = reader.nextLine();
			if(!line.startsWith("#")) {
				lines.add(line);
				width = Math.max(width, line.length());
			}
		}
		height = lines.size(); // number of elements in lines is the height
		
		TileMap newMap = new TileMap(width, height);
		for (int y=0; y < height; y++) {
			String line = lines.get(y);
			for (int x=0; x < line.length(); x++) {
				char ch = line.charAt(x);
				
				int pixelX = GameRenderer.tilesToPixels(x);
				int pixelY = GameRenderer.tilesToPixels(y);
				// enumerate the possible tiles...
				if (ch == 'G') {
					newMap.creatures().add(new Goomba(pixelX, pixelY, soundManager));
				} else if (ch == 'K') {
					newMap.creatures().add(new RedKoopa(pixelX, pixelY, soundManager));
				} else if (ch == 'H') {
					newMap.creatures().add(new Thorny(pixelX, pixelY, soundManager));
				} else if (ch == 'V') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(56));
					newMap.setTile(x, y, t);
				} else if (ch == 'J') {
					Piranha t = new Piranha(pixelX, pixelY,soundManager);
					newMap.creatures().add(t);
				} else if (ch=='B') {
					Brick b = new Brick(pixelX, pixelY,newMap, plain.get(77),soundManager,10*(int)(Math.random()*1.1),false);
					newMap.setTile(x, y, b);
					newMap.animatedTiles().add(b);	
				} else if (ch == 'R') {
					RotatingBlock r = new RotatingBlock(pixelX, pixelY);
					newMap.setTile(x, y, r);
					newMap.animatedTiles().add(r);
				} else if (ch == '3') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(4));
					newMap.setTile(x, y, t);
				} else if (ch == '4') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(10));
					newMap.setTile(x, y, t);
				} else if (ch == '2') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(86));
					newMap.setTile(x, y, t);
				} else if (ch == 'Q') {
					QuestionBlock q = new QuestionBlock(pixelX, pixelY, newMap, soundManager, true, false);
					newMap.setTile(x, y, q);
					newMap.animatedTiles().add(q);
				} else if (ch == 'W') {
					QuestionBlock q = new QuestionBlock(pixelX, pixelY, newMap, soundManager, false, true);
					newMap.setTile(x, y, q);
					newMap.animatedTiles().add(q);
				} else if (ch == 'S') {
					newMap.creatures().add(new RedShell(pixelX, pixelY, newMap, soundManager, true));
				} else if(ch == 'C') {
					newMap.creatures().add(new Coin(pixelX, pixelY));
				} else if(ch == 'P') {
					Platform p = new Platform(pixelX, pixelY);
					newMap.creatures().add(p);
				} else if (ch=='T') {
					Tree t = new Tree(pixelX, pixelY);
					newMap.setTile(x, y, t);
					newMap.animatedTiles().add(t);
				}else if(ch == '9') {
					SlopedTile t = new SlopedTile(pixelX, pixelY, sloped_image, true);
					newMap.setTile(x, y, t);
					newMap.slopedTiles().add(t);
				} else if(ch == '8') {
					GameTile t = new GameTile(pixelX, pixelY, grass_edge);
					newMap.setTile(x, y, t);
				} else if(ch == '7') {
					GameTile t = new GameTile(pixelX, pixelY, grass_center);
					newMap.setTile(x, y, t);
			
				} else if(ch == 't') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(96)); //pipe top left
					newMap.setTile(x, y, t);
				} else if(ch == 'u') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(97)); //pipe top middle
					newMap.setTile(x, y, t);
				} else if(ch == 'v') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(98)); //pipe top right
					newMap.setTile(x, y, t);
				} else if(ch == 'w') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(99)); //pipe base left
					newMap.setTile(x, y, t);
				} else if(ch == 'x') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(100)); //pipe base middle
					newMap.setTile(x, y, t);
				} else if(ch == 'y') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(101)); //pipe base right
					newMap.setTile(x, y, t);
				} else if(ch == 'z') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(75)); //tree stem
					newMap.setTile(x, y, t);
				}
			}
		}
		return newMap;	
	}

	// Fills given tile mapFrom file. Used in Editor
    public void fillMap(String filename, TileMap map) throws IOException {
		// lines is a list of strings, each element is a row of the map
		ArrayList<String> lines = new ArrayList<String>();
		int width = 0;
		int height = 0;
		
		// read in each line of the map into lines
		Scanner reader = new Scanner(new File(filename));
		while(reader.hasNextLine()) {
			String line = reader.nextLine();
			if(!line.startsWith("#")) {
				lines.add(line);
				width = Math.max(width, line.length());
			}
		}
		height = lines.size(); // number of elements in lines is the height
		
		height=Math.min(height,map.getHeight());
		width=Math.min(width, map.getWidth());
		
		for (int y=0; y < height; y++) {
			String line = lines.get(y);
			for (int x=0; x < line.length(); x++) {
				char ch = line.charAt(x);
				
				if (ch == 'n') {
					map.setTile(x, y, plain.get(92));
				} else if (ch == 'm') {
					map.setTile(x, y, plain.get(93));
				} else if (ch == 'a') {
					map.setTile(x, y, plain.get(90));
				} else if (ch == 'b') {
					map.setTile(x, y, plain.get(91));
				} else if (ch == 'q') { // rock left
					map.setTile(x, y, plain.get(48));
				} else if (ch == 'r') { // rock right
					map.setTile(x, y, plain.get(49));
				} else if (ch == 'z') { // tree stem
					map.setTile(x, y, plain.get(75));
				} else if (ch=='T') {
					int pixelX = GameRenderer.tilesToPixels(x);
					int pixelY = GameRenderer.tilesToPixels(y);
					Tree t = new Tree(pixelX, pixelY);
					map.setTile(x, y, t);
					map.animatedTiles().add(t);
				}
			}
		}
		
	}
	
}
