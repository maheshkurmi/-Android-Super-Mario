package com.shikhar.androidgames.mario.core;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;



import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;

import com.shikhar.androidgames.framework.gfx.AndroidGame;
import com.shikhar.androidgames.mario.core.tile.GameTile;
import com.shikhar.androidgames.mario.core.tile.TileMap;
import com.shikhar.androidgames.mario.objects.base.Creature;
import com.shikhar.androidgames.mario.objects.creatures.BlueFish;
import com.shikhar.androidgames.mario.objects.creatures.Bowser;
import com.shikhar.androidgames.mario.objects.creatures.Coin;
import com.shikhar.androidgames.mario.objects.creatures.CoinEmitter;
import com.shikhar.androidgames.mario.objects.creatures.CreatureEmitter;
import com.shikhar.androidgames.mario.objects.creatures.FireDisc;
import com.shikhar.androidgames.mario.objects.creatures.FlyGoomba;
import com.shikhar.androidgames.mario.objects.creatures.FlyRedKoopa;
import com.shikhar.androidgames.mario.objects.creatures.Goomba;
import com.shikhar.androidgames.mario.objects.creatures.JumpingFish;
import com.shikhar.androidgames.mario.objects.creatures.Latiku;
import com.shikhar.androidgames.mario.objects.creatures.LevelComplete;
import com.shikhar.androidgames.mario.objects.creatures.Piranha;
import com.shikhar.androidgames.mario.objects.creatures.Platform;
import com.shikhar.androidgames.mario.objects.creatures.RedFish;
import com.shikhar.androidgames.mario.objects.creatures.RedKoopa;
import com.shikhar.androidgames.mario.objects.creatures.RedShell;
import com.shikhar.androidgames.mario.objects.creatures.Spring;
import com.shikhar.androidgames.mario.objects.creatures.Thorny;
import com.shikhar.androidgames.mario.objects.creatures.Virus;
import com.shikhar.androidgames.mario.objects.tiles.Brick;
import com.shikhar.androidgames.mario.objects.tiles.Bush;
import com.shikhar.androidgames.mario.objects.tiles.FireTile;
import com.shikhar.androidgames.mario.objects.tiles.InfoPanel;
import com.shikhar.androidgames.mario.objects.tiles.QuestionBlock;
import com.shikhar.androidgames.mario.objects.tiles.RotatingBlock;
import com.shikhar.androidgames.mario.objects.tiles.SlopedTile;
import com.shikhar.androidgames.mario.objects.tiles.Tree;
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
	private Bitmap mushroomTree;
	private Bitmap woodenBridge;
	private Bitmap waterRock;
	
	private AndroidGame gameActivity;
	private boolean togglePlatform_velocity=false;
	private int backGroundImageIndex=0;
    private ArrayList<String> infoPanels;
    
	public GameLoader(AndroidGame activity) {
		this.gameActivity=activity;
		plain = new ArrayList<Bitmap>();
		plainTiles = (new SpriteMap(MarioResourceManager.Plain_Tiles, 6, 17)).getSprites();
		mushroomTree=MarioResourceManager.loadImage("items/mushroom_tree.png");
		woodenBridge=MarioResourceManager.loadImage("items/wooden_bridge.png");
		waterRock=MarioResourceManager.loadImage("items/water_rock.png");
		infoPanels =new ArrayList<String>(); 
		
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
		int infoCount=0;
		infoPanels.clear();
		Scanner reader = new Scanner(gameActivity.getAssets().open(filename));
		while(reader.hasNextLine()) {
			String line = reader.nextLine();
			if(!line.startsWith("#")) {
				lines.add(line);
				width = Math.max(width, line.length());
			}else{
				if (line.startsWith("#@")){
					infoPanels.add(line.substring(2).trim());
				}
			}
		}
		height = lines.size(); // number of elements in lines is the height
		int pipeOffsetIndex=(Settings.level==2)?30:96;
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
				} else if (ch=='}') {
					Bush t = new Bush(pixelX, pixelY);
					newMap.setTile(x, y, t);
					newMap.animatedTiles().add(t);
				} else if (ch == 'V') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(56));
					newMap.setTile(x, y, t);
				} else if (ch == '5') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(3));
					newMap.setTile(x, y, t);
				} else if (ch == '3') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(4));
					newMap.setTile(x, y, t);
				} else if (ch == '6') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(5));
					newMap.setTile(x, y, t);
				} else if (ch == '4') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(10));
					newMap.setTile(x, y, t);
				} else if (ch == '2') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(86));
					newMap.setTile(x, y, t);
				} else if (ch == '{') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(81));
					newMap.setTile(x, y, t);
				}else if(ch == '9') {
					GameTile t = new GameTile(pixelX, pixelY, mushroomTree);
					newMap.setTile(x, y, t);
					//newMap.slopedTiles().add(t);
				} else if(ch == '8') {
					GameTile t = new GameTile(pixelX, pixelY, woodenBridge);
					newMap.setTile(x, y, t);
				} else if(ch == '7') {
					GameTile t = new GameTile(pixelX, pixelY, waterRock);
					newMap.setTile(x, y, t);
				} else if(ch == 't') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(pipeOffsetIndex)); //pipe top left
					newMap.setTile(x, y, t);
				} else if(ch == 'u') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(pipeOffsetIndex+1)); //pipe top middle
					newMap.setTile(x, y, t);
				} else if(ch == 'v') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(pipeOffsetIndex+2)); //pipe top right
					newMap.setTile(x, y, t);
				} else if(ch == 'w') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(pipeOffsetIndex+3)); //pipe base left
					newMap.setTile(x, y, t);
				} else if(ch == 'x') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(pipeOffsetIndex+4)); //pipe base middle
					newMap.setTile(x, y, t);
				} else if(ch == 'y') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(pipeOffsetIndex+5)); //pipe base right
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
				} else if(ch == 'g') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(9));
					newMap.setTile(x, y, t);
				} else if(ch == 'h') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(11));
					newMap.setTile(x, y, t);
				}else if(ch == '*') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(78)); //pipe base right
					newMap.setTile(x, y, t);
				} else if(ch == '!') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(84)); //pipe base right
					newMap.setTile(x, y, t);    
				} else if(ch == '%') {
					GameTile t = new GameTile(pixelX, pixelY,plain.get(36)); //BlueRock 
					newMap.setTile(x, y, t);
				} else if(ch == '|') {
					GameTile t = new GameTile(pixelX, pixelY,plain.get(37)); //Yellowrock
					newMap.setTile(x, y, t);
				} else if(ch == '$') {
					GameTile t = new GameTile(pixelX, pixelY,plain.get(38)); //greyrock
					newMap.setTile(x, y, t);
				} else if(ch == '-') {
					GameTile t = new GameTile(pixelX, pixelY,plain.get(40)); // 
					newMap.setTile(x, y, t);
				} else if(ch == '+') {
					GameTile t = new GameTile(pixelX, pixelY,plain.get(46)); //
					newMap.setTile(x, y, t);
				} else if(ch == '~') {
					GameTile t = new GameTile(pixelX, pixelY,plain.get(89)); //watertile
					newMap.setTile(x, y, t);
				} else if(ch == '@') {
					InfoPanel t = new InfoPanel(pixelX, pixelY, "Mario"); //pipe base right
					newMap.setTile(x, y, t);
					if(infoCount<infoPanels.size()){
						t.setInfo(infoPanels.get(infoCount));
						infoCount++;
					}
				} else if (ch == '[') {
					FireTile r = new FireTile(pixelX, pixelY);
					newMap.setTile(x, y, r);
					newMap.animatedTiles().add(r);
				} else if (ch == ']') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(25));
					newMap.setTile(x, y, t);
				} else if (ch == '_') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(28));
					newMap.setTile(x, y, t);
				} else if (ch == ';') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(26));
					newMap.setTile(x, y, t);
				} else if (ch == ':') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(27));
					newMap.setTile(x, y, t);
				}
			}
		}
		return newMap;	
	}

 
    // Use this to load the main map
	public TileMap loadMap(String filename, MarioSoundManager soundManager) throws IOException {
		// lines is a list of strings, each element is a row of the map
		ArrayList<String> lines = new ArrayList<String>();
		ArrayList<Rect> rects = new ArrayList<Rect>();

		int width = 0;
		int height = 0;
		
		int pipeOffsetIndex=(Settings.level==2)?30:96;
		// read in each line of the map into lines
		Scanner reader = new Scanner(gameActivity.getAssets().open(filename));
		while(reader.hasNextLine()) {
			String line = reader.nextLine();
			line="."+line;
			line=line.trim();
			line=line.substring(1,line.length());
			if(!line.startsWith("#")) {
				lines.add(line);
				width = Math.max(width, line.length());
			}else{
				line=line.substring(1).trim();
				if (line.startsWith("background")) {
					setBackGroundImageIndex(Integer.parseInt(line
							.substring(line.length() - 1)));
				} else if (line.startsWith("waterzone")) {
					line=line.substring(10);
					String[] pts = line.split(",");
					{
						int[] x = new int[4];
						for (int i = 0; i <= 3; i++) {
							x[i] = Integer.parseInt(pts[i]);
						}
						rects.add(new Rect(x[0], x[1], x[0] + x[2], x[1] + x[3]));
					}
				}
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
					newMap.creatures().add(new RedKoopa(pixelX, pixelY, soundManager,false));
				} else if (ch == 'L') {
					newMap.creatures().add(new RedKoopa(pixelX, pixelY, soundManager,true));
				} else if (ch == 'I') {
					FireDisc f = new FireDisc(pixelX, pixelY, soundManager);
					newMap.creatures().add(f);
				} else if (ch == 'H') {
					newMap.creatures().add(new Thorny(pixelX, pixelY, soundManager));
				} else if (ch == 'F') {
					newMap.creatures().add(new FlyRedKoopa(pixelX, pixelY, soundManager));
				} else if (ch == 'O') {
					newMap.creatures().add(new FlyGoomba(pixelX, pixelY, soundManager));
				} else if (ch == 'N') {
					newMap.creatures().add(new Bowser(pixelX, pixelY, soundManager));
				} else if (ch == 'V') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(56));
					newMap.setTile(x, y, t);
				} else if (ch == 'J') {
					Piranha t = new Piranha(pixelX, pixelY,soundManager);
					newMap.creatures().add(t);
				} else if (ch == '&') {
					Latiku t = new Latiku(pixelX, pixelY,soundManager);
					newMap.creatures().add(t);
				} else if (ch == '?') {
					Virus t = new Virus(pixelX, pixelY);
					newMap.creatures().add(t);
				} else if (ch == '(') {
					RedFish t = new RedFish(pixelX, pixelY,soundManager);
					newMap.creatures().add(t);
				} else if (ch == ')') {
					BlueFish t = new BlueFish(pixelX, pixelY,soundManager);
					newMap.creatures().add(t);
				} else if (ch == ',') {
					JumpingFish t = new JumpingFish(pixelX, pixelY,Math.random()>0.7f,soundManager);
					newMap.creatures().add(t);
				} else if (ch == '.') {
					CreatureEmitter t = new CreatureEmitter(pixelX, pixelY,soundManager);
					newMap.creatures().add(t);
				} else if (ch == '"') {
					CoinEmitter t = new CoinEmitter(pixelX, pixelY,soundManager);
					newMap.creatures().add(t);
				} else if (ch == 'l') {
					LevelComplete c= new LevelComplete(pixelX, pixelY);
					newMap.creatures().add(c);
				} else if (ch=='B') {
					Brick b = new Brick(pixelX, pixelY,newMap, plain.get(77),soundManager,10*(int)(Math.random()*1.1),false);
					newMap.setTile(x, y, b);
					newMap.animatedTiles().add(b);	
				} else if (ch == 'R') {
					RotatingBlock r = new RotatingBlock(pixelX, pixelY);
					newMap.setTile(x, y, r);
					newMap.animatedTiles().add(r);
				} else if (ch == '[') {
					FireTile r = new FireTile(pixelX, pixelY);
					newMap.setTile(x, y, r);
					newMap.animatedTiles().add(r);
				} else if (ch == ']') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(25));
					newMap.setTile(x, y, t);
				} else if (ch == '_') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(28));
					newMap.setTile(x, y, t);
				} else if (ch == ';') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(26));
					newMap.setTile(x, y, t);
				} else if (ch == ':') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(27));
					newMap.setTile(x, y, t);
				} else if (ch == '5') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(3));
					newMap.setTile(x, y, t);
				} else if (ch == '3') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(4));
					newMap.setTile(x, y, t);
				} else if (ch == '6') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(5));
					newMap.setTile(x, y, t);
				} else if (ch == '4') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(10));
					newMap.setTile(x, y, t);
				} else if (ch == '2') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(86));
					newMap.setTile(x, y, t);
				} else if (ch == '{') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(81));
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
					newMap.creatures().add(new RedShell(pixelX, pixelY, newMap, soundManager, true,true));
				} else if(ch == 'C') {
					newMap.creatures().add(new Coin(pixelX, pixelY));
				} else if(ch == 'P') {
					Platform p = new Platform(pixelX, pixelY);
					newMap.creatures().add(p);
				} else if(ch == '<') {
					Platform p = new Platform(pixelX, pixelY,togglePlatform_velocity?0.05f:-0.05f,0,false);
					newMap.creatures().add(p);
					togglePlatform_velocity=!togglePlatform_velocity;
				} else if(ch == '>') {
					Platform p = new Platform(pixelX, pixelY,0,0.05f,false);
					newMap.creatures().add(p);
					//togglePlatform_velocity=!togglePlatform_velocity;
				} else if(ch == '^') {
					Spring s = new Spring(pixelX, pixelY, newMap);
					newMap.creatures().add(s);
				} else if (ch=='T') {
					Tree t = new Tree(pixelX, pixelY);
					newMap.setTile(x, y, t);
					newMap.animatedTiles().add(t);
				} else if (ch=='}') {
					Bush t = new Bush(pixelX, pixelY);
					newMap.setTile(x, y, t);
					newMap.animatedTiles().add(t);
				} else if (ch == 'M') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(82));
					t.setIsCollidable(false);
					newMap.setTile(x, y, t);
					newMap.addBookMark(new  Point(x,y));
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
					GameTile t = new GameTile(pixelX, pixelY, plain.get(pipeOffsetIndex)); //pipe top left
					newMap.setTile(x, y, t);
				} else if(ch == 'u') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(pipeOffsetIndex+1)); //pipe top middle
					newMap.setTile(x, y, t);
				} else if(ch == 'v') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(pipeOffsetIndex+2)); //pipe top right
					newMap.setTile(x, y, t);
				} else if(ch == 'w') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(pipeOffsetIndex+3)); //pipe base left
					newMap.setTile(x, y, t);
				} else if(ch == 'x') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(pipeOffsetIndex+4)); //pipe base middle
					newMap.setTile(x, y, t);
				} else if(ch == 'y') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(pipeOffsetIndex+5)); //pipe base right
					newMap.setTile(x, y, t);
				} else if(ch == 'z') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(75)); //tree stem
					newMap.setTile(x, y, t);
				} else if(ch == 'i') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(42)); //
					newMap.setTile(x, y, t);
				} else if(ch == 'j') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(43)); //
					newMap.setTile(x, y, t);
				} else if(ch == 'k') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(44)); //tree stem
					newMap.setTile(x, y, t);
				} else if(ch == 'g') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(9));
					newMap.setTile(x, y, t);
				} else if(ch == 'h') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(11));
					newMap.setTile(x, y, t);
				} else if(ch == '%') {
					GameTile t = new GameTile(pixelX, pixelY,plain.get(36)); //BlueRock 
					newMap.setTile(x, y, t);
				} else if(ch == '|') {
					GameTile t = new GameTile(pixelX, pixelY,plain.get(37)); //Yellowrock
					newMap.setTile(x, y, t);
				} else if(ch == '$') {
					GameTile t = new GameTile(pixelX, pixelY,plain.get(38)); //greyrock
					newMap.setTile(x, y, t);
				} else if(ch == '-') {
					GameTile t = new GameTile(pixelX, pixelY,plain.get(40)); // 
					newMap.setTile(x, y, t);
				} else if(ch == '+') {
					GameTile t = new GameTile(pixelX, pixelY,plain.get(46)); //
					newMap.setTile(x, y, t);
				} else if(ch == '~') {
					GameTile t = new GameTile(pixelX, pixelY,plain.get(89)); //watertile
					newMap.setTile(x, y, t);
				}
			}
		}
		for (Rect r:rects){
			newMap.addWaterZone(r);
		}
		Creature.map=newMap;
		return newMap;	
	}

	/**
	 * ReLoads map from offset point
	 * @param map map to be reloaded
	 * @param filename path of file wrt asset folder
	 * @param soundManager
	 * @param beginX Starting x (benchmark of player)
	 * @throws IOException
	 */
	public void reLoadMap(TileMap map,String filename, MarioSoundManager soundManager, int beginX) throws IOException {
		// lines is a list of strings, each element is a row of the map
		ArrayList<String> lines = new ArrayList<String>();
		int width = 0;
		int height = 0;
		int pipeOffsetIndex=(Settings.level==2)?30:96;
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
		
		if (width<=beginX || beginX<0)return;
		width=Math.min(map.getWidth(),width);
		height=Math.min(map.getHeight(),height);
		map.creaturesToAdd().clear();
		map.platforms().clear();
		
		for(int i = 0; i < map.creatures().size(); i++) { 
			if (map.creatures().get(i).getX()>GameRenderer.tilesToPixels(beginX) ) {
				if(!(map.creatures().get(i) instanceof CoinEmitter) && !(map.creatures().get(i) instanceof Latiku) && !(map.creatures().get(i) instanceof CreatureEmitter)){
					map.creatures().remove(i);	 
					i--;
   				}else{
   					map.creatures().get(i).wakeUp(true);
   				}
            } 
		}
			
		for (int y=0; y < height; y++) {
			String line = lines.get(y);
			for (int x=beginX; x < line.length(); x++) {
				char ch = line.charAt(x);
				int pixelX = GameRenderer.tilesToPixels(x);
				int pixelY = GameRenderer.tilesToPixels(y);
				// enumerate the possible tiles...
				if (ch == 'G') {
					map.creatures().add(new Goomba(pixelX, pixelY, soundManager));
				} else if (ch == 'K') {
					map.creatures().add(new RedKoopa(pixelX, pixelY, soundManager,false));
				} else if (ch == 'L') {
					map.creatures().add(new RedKoopa(pixelX, pixelY, soundManager,true));
				} else if (ch == 'I') {
					FireDisc f = new FireDisc(pixelX, pixelY, soundManager);
					map.creatures().add(f);
				} else if (ch == 'H') {
					map.creatures().add(new Thorny(pixelX, pixelY, soundManager));
				} else if (ch == 'F') {
					map.creatures().add(new FlyRedKoopa(pixelX, pixelY, soundManager));
				} else if (ch == 'O') {
					map.creatures().add(new FlyGoomba(pixelX, pixelY, soundManager));
				} else if (ch == 'N') {
					map.creatures().add(new Bowser(pixelX, pixelY, soundManager));
				} else if (ch == 'V') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(56));
					map.setTile(x, y, t);
				} else if (ch == 'J') {
					Piranha t = new Piranha(pixelX, pixelY,soundManager);
					map.creatures().add(t);
				} else if (ch == '?') {
					Virus t = new Virus(pixelX, pixelY);
					map.creatures().add(t);
				} else if (ch == '&') {
					//no need to add latiku since it is always relevent
					//Latiku t = new Latiku(pixelX, pixelY,soundManager,map);
					//map.creatures().add(t);
				} else if (ch == '(') {
					RedFish t = new RedFish(pixelX, pixelY,soundManager);
					map.creatures().add(t);
				} else if (ch == ')') {
					BlueFish t = new BlueFish(pixelX, pixelY,soundManager);
					map.creatures().add(t);
				} else if (ch == ',') {
					JumpingFish t = new JumpingFish(pixelX, pixelY,Math.random()>0.7f,soundManager);
					map.creatures().add(t);
				} else if (ch == 'l') {
					LevelComplete l= new LevelComplete(pixelX, pixelY);
					map.creatures().add(l);
				} else if (ch=='B') {
					Brick b = new Brick(pixelX, pixelY,map, plain.get(77),soundManager,10*(int)(Math.random()*1.1),false);
					map.setTile(x, y, b);
					map.animatedTiles().add(b);	
				} else if (ch == 'R') {
					RotatingBlock r = new RotatingBlock(pixelX, pixelY);
					map.setTile(x, y, r);
					map.animatedTiles().add(r);
				} else if (ch == '[') {
					FireTile r = new FireTile(pixelX, pixelY);
					map.setTile(x, y, r);
					map.animatedTiles().add(r);
				} else if (ch == ']') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(25));
					map.setTile(x, y, t);
				} else if (ch == '_') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(28));
					map.setTile(x, y, t);
				} else if (ch == ';') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(26));
					map.setTile(x, y, t);
				} else if (ch == ':') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(27));
					map.setTile(x, y, t);
				} else if (ch == '5') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(3));
					map.setTile(x, y, t);
				} else if (ch == '3') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(4));
					map.setTile(x, y, t);
				} else if (ch == '6') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(5));
					map.setTile(x, y, t);
				} else if (ch == '4') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(10));
					map.setTile(x, y, t);
				} else if (ch == '2') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(86));
					map.setTile(x, y, t);
				} else if (ch == '{') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(81));
					map.setTile(x, y, t);
				} else if (ch == 'Q') {
					QuestionBlock q = new QuestionBlock(pixelX, pixelY, map, soundManager, true, false);
					map.setTile(x, y, q);
					map.animatedTiles().add(q);
				} else if (ch == 'W') {
					QuestionBlock q = new QuestionBlock(pixelX, pixelY, map, soundManager, false, true);
					map.setTile(x, y, q);
					map.animatedTiles().add(q);
				} else if (ch == 'S') {
					map.creatures().add(new RedShell(pixelX, pixelY, map, soundManager, true,true));
				} else if(ch == 'C') {
					map.creatures().add(new Coin(pixelX, pixelY));
				} else if(ch == 'P') {
					Platform p = new Platform(pixelX, pixelY);
					map.creatures().add(p);
				} else if(ch == '<') {
					Platform p = new Platform(pixelX, pixelY,togglePlatform_velocity?0.05f:-0.05f,0,false);
					map.creatures().add(p);
					togglePlatform_velocity=!togglePlatform_velocity;
				} else if(ch == '>') {
					Platform p = new Platform(pixelX, pixelY,0,0.05f,false);
					map.creatures().add(p);
					//togglePlatform_velocity=!togglePlatform_velocity;
				} else if(ch == '^') {
					Spring s = new Spring(pixelX, pixelY,map);
					map.creatures().add(s);
				} else if (ch=='T') {
					Tree t = new Tree(pixelX, pixelY);
					map.setTile(x, y, t);
					map.animatedTiles().add(t);
				} else if (ch=='}') {
					Bush t = new Bush(pixelX, pixelY);
					map.setTile(x, y, t);
					map.animatedTiles().add(t);
				} else if (ch == 'M') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(82));
					t.setIsCollidable(false);
					map.setTile(x, y, t);
					map.addBookMark(new  Point(x,y));
				}else if(ch == '9') {
					SlopedTile t = new SlopedTile(pixelX, pixelY, sloped_image, true);
					map.setTile(x, y, t);
					map.slopedTiles().add(t);
				} else if(ch == '8') {
					GameTile t = new GameTile(pixelX, pixelY, grass_edge);
					map.setTile(x, y, t);
				} else if(ch == '7') {
					GameTile t = new GameTile(pixelX, pixelY, grass_center);
					map.setTile(x, y, t);
				} else if(ch == 't') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(pipeOffsetIndex)); //pipe top left
					map.setTile(x, y, t);
				} else if(ch == 'u') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(pipeOffsetIndex+1)); //pipe top middle
					map.setTile(x, y, t);
				} else if(ch == 'v') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(pipeOffsetIndex+2)); //pipe top right
					map.setTile(x, y, t);
				} else if(ch == 'w') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(pipeOffsetIndex+3)); //pipe base left
					map.setTile(x, y, t);
				} else if(ch == 'x') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(pipeOffsetIndex+4)); //pipe base middle
					map.setTile(x, y, t);
				} else if(ch == 'y') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(pipeOffsetIndex+5)); //pipe base right
					map.setTile(x, y, t);
				} else if(ch == 'z') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(75)); //tree stem
					map.setTile(x, y, t);
				} else if(ch == 'i') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(42)); //
					map.setTile(x, y, t);
				} else if(ch == 'j') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(43)); //
					map.setTile(x, y, t);
				} else if(ch == 'k') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(44)); //tree stem
					map.setTile(x, y, t);
				} else if(ch == 'g') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(9));
					map.setTile(x, y, t);
				} else if(ch == 'h') {
					GameTile t = new GameTile(pixelX, pixelY, plain.get(11));
				    map.setTile(x, y, t);
				} else if(ch == '%') {
					GameTile t = new GameTile(pixelX, pixelY,plain.get(36)); //BlueRock 
					map.setTile(x, y, t);
				} else if(ch == '|') {
					GameTile t = new GameTile(pixelX, pixelY,plain.get(37)); //Yellowrock
					map.setTile(x, y, t);
				} else if(ch == '$') {
					GameTile t = new GameTile(pixelX, pixelY,plain.get(38)); //greyrock
					map.setTile(x, y, t);
				} else if(ch == '-') {
					GameTile t = new GameTile(pixelX, pixelY,plain.get(40)); // sandtop
					map.setTile(x, y, t);
				} else if(ch == '+') {
					GameTile t = new GameTile(pixelX, pixelY,plain.get(46)); //sandbottom
					map.setTile(x, y, t);
				} else if(ch == '~') {
					GameTile t = new GameTile(pixelX, pixelY,plain.get(89)); //watertile
					map.setTile(x, y, t);
				}
			}
		}
		Creature.map=map;
	}

	
	public int getBackGroundImageIndex() {
		return backGroundImageIndex;
	}


	public void setBackGroundImageIndex(int backGroundImageIndex) {
		if (backGroundImageIndex<0 ||backGroundImageIndex>10)return;
		this.backGroundImageIndex = backGroundImageIndex;
	}
	
}
