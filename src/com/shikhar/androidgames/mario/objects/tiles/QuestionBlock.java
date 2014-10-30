package com.shikhar.androidgames.mario.objects.tiles;

import java.util.Random;

import android.graphics.Bitmap;

import com.shikhar.androidgames.mario.core.MarioResourceManager;
import com.shikhar.androidgames.mario.core.MarioSoundManager;
import com.shikhar.androidgames.mario.core.animation.Animation;
import com.shikhar.androidgames.mario.core.tile.GameTile;
import com.shikhar.androidgames.mario.core.tile.TileMap;
import com.shikhar.androidgames.mario.objects.creatures.Coin;
import com.shikhar.androidgames.mario.objects.creatures.Mushroom;
import com.shikhar.androidgames.mario.objects.creatures.Score;


public class QuestionBlock extends GameTile {

	private MarioSoundManager soundManager;
	private TileMap map;
	
	private Animation active;
	private Animation dead;
	private boolean isActive;
	private boolean hasCoin;
	private boolean hasMushroom;
	
	public QuestionBlock(int pixelX, int pixelY, TileMap map, MarioSoundManager soundManager, boolean hasCoin,
			boolean hasMushroom) {
		
		// int pixelX, int pixelY, Animation anim, Image img
		super(pixelX, pixelY, null, null);
		
		setIsSloped(false);
		isActive = true;
		this.hasCoin = hasCoin;
		this.hasMushroom = hasMushroom;
		this.soundManager = soundManager;
		this.map = map;

		Bitmap q[] = { MarioResourceManager.Question_Block_0, MarioResourceManager.Question_Block_1,
		MarioResourceManager.Question_Block_2, MarioResourceManager.Question_Block_3,
		MarioResourceManager.Question_Block_Dead};
		
		Random r = new Random();
		active = new Animation(r.nextInt(20) + 140).addFrame(q[0]).addFrame(q[1]).addFrame(q[2]).addFrame(q[3]);
		dead = new Animation(2000).addFrame(q[4]);
		setAnimation(active);
	}
	
	public void update(int time) {
		super.update(time);
		if(getOffsetY() != 0) { setOffsetY(getOffsetY() + 2); }
	}
	
	public void doAction() {
		if(isActive) {
			if(hasCoin) {
				setOffsetY(-10);
				soundManager.playCoin();
				Coin newCoin = new Coin(getPixelX(), getPixelY());
				Score score = new Score(getPixelX(), getPixelY());
				map.creaturesToAdd().add(newCoin);
				map.creaturesToAdd().add(score);
				newCoin.shoot();
			} else if(hasMushroom) {
				setOffsetY(-10);
				soundManager.playItemSprout();
				Mushroom shroom = new Mushroom(getPixelX(), getPixelY()-26);
				map.creaturesToAdd().add(shroom);
			}
			setAnimation(dead);
			isActive = false;
		}
	}
}