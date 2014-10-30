package com.shikhar.androidgames.mario.screens;

import com.shikhar.androidgames.framework.Game;
import com.shikhar.androidgames.framework.Graphics;
import com.shikhar.androidgames.framework.Graphics.ImageFormat;
import com.shikhar.androidgames.framework.Screen;
import com.shikhar.androidgames.mario.core.Assets;
import com.shikhar.androidgames.mario.core.MarioGame;
import com.shikhar.androidgames.mario.core.MarioSoundManager;
import com.shikhar.androidgames.mario.core.Settings;

public class LoadingScreen extends Screen {
    public LoadingScreen(Game game) {
         super(game);
    }

    @Override
    public void update(float deltaTime) {
    	Graphics g = game.getGraphics();
        Settings.load(game.getFileIO());
        
        ((MarioGame)game).soundManager.loadResouces();
        MarioSoundManager.musicEnabled=Settings.musicEnabled;
        if(Settings.musicEnabled){
        	 ((MarioGame)game).soundManager.playMusic();
        }
        Assets.menu = g.newImage("menu/menu.png", ImageFormat.RGB565);
        ((MarioGame)game).resourceManager.loadResouces();
    	
    	//
        /*Assets.background = g.newImage("background.png", ImageFormat.RGB565);
        Assets.character = g.newImage("character.png", ImageFormat.ARGB4444);
        Assets.character2 = g.newImage("character2.png", ImageFormat.ARGB4444);
        Assets.character3  = g.newImage("character3.png", ImageFormat.ARGB4444);
        Assets.characterJump = g.newImage("jumped.png", ImageFormat.ARGB4444);
        Assets.characterDown = g.newImage("down.png", ImageFormat.ARGB4444);

        Assets.heliboy = g.newImage("heliboy.png", ImageFormat.ARGB4444);
        Assets.heliboy2 = g.newImage("heliboy2.png", ImageFormat.ARGB4444);
        Assets.heliboy3  = g.newImage("heliboy3.png", ImageFormat.ARGB4444);
        Assets.heliboy4  = g.newImage("heliboy4.png", ImageFormat.ARGB4444);
        Assets.heliboy5  = g.newImage("heliboy5.png", ImageFormat.ARGB4444);

        Assets.tiledirt = g.newImage("tiledirt.png", ImageFormat.RGB565);
        Assets.tilegrassTop = g.newImage("tilegrasstop.png", ImageFormat.RGB565);
        Assets.tilegrassBot = g.newImage("tilegrassbot.png", ImageFormat.RGB565);
        Assets.tilegrassLeft = g.newImage("tilegrassleft.png", ImageFormat.RGB565);
        Assets.tilegrassRight = g.newImage("tilegrassright.png", ImageFormat.RGB565);
        
        Assets.button = g.newImage("button.jpg", ImageFormat.RGB565);

        //This is how you would load a sound if you had one.
        //Assets.click = game.getAudio().createSound("explode.ogg");
        Assets.bump =  game.getAudio().createSound("sounds/bump.wav");
        Assets.kick = game.getAudio().createSound("sounds/kick.wav");
        Assets.coin = game.getAudio().createSound("sounds/coin.wav");
        Assets.jump = game.getAudio().createSound("sounds/jump.wav");
        Assets.pause = game.getAudio().createSound("sounds/pause.wav");
        Assets.itemSprout = game.getAudio().createSound("sounds/item_sprout.wav");
        Assets.bonusPoints = game.getAudio().createSound("sounds/veggie_throw.wav");
        Assets.healthUp = game.getAudio().createSound("sounds/power_up.wav");
        Assets.healthDown = game.getAudio().createSound("sounds/power_down.wav");
 		
        Assets.hurt1 = game.getAudio().createSound("sounds/mario_ooh.wav");
        Assets.hurt2 = game.getAudio().createSound("sounds/mario_oh.wav");
        Assets.yahoo1 = game.getAudio().createSound("sounds/mario_waha.wav");
        Assets.yahoo2 = game.getAudio().createSound("sounds/mario_woohoo.wav");
        */
       // MainMenuScreen mainMenuScreen=new MainMenuScreen(game);
        //mainMenuScreen.loadGameScreen();
        game.setScreen(new MainMenuScreen(game));//mainMenuScreen);
    }

    @Override
    public void paint(float deltaTime) {
        Graphics g = game.getGraphics();
        //g.drawARGB(200, , g, b)
        g.drawImage(Assets.splash,0, 0);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void onBackPressed() {

    }
}