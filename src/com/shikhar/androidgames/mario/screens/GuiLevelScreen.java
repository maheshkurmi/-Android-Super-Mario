package com.shikhar.androidgames.mario.screens;

import java.util.List;

import com.shikhar.androidgames.framework.Game;
import com.shikhar.androidgames.framework.Input.TouchEvent;
import com.shikhar.androidgames.framework.Screen;
import com.shikhar.androidgames.framework.gfx.AndroidGraphics;
import com.shikhar.androidgames.gui.AndroidPanel;
import com.shikhar.androidgames.gui.AndroidPic;
import com.shikhar.androidgames.gui.Component;
import com.shikhar.androidgames.gui.ComponentClickListener;
import com.shikhar.androidgames.mario.core.MarioResourceManager;
import com.shikhar.androidgames.mario.core.Settings;

import android.graphics.Canvas;
import android.graphics.Color;


public class GuiLevelScreen extends Screen  {


	private int row = 3, col = 4;
	private int screenW = 360, screenH = 240;
	private int imageW = 39, imageH = 50; // image for level icons

	private int gridGapX = (screenW - imageW * col) / (col + 1),
			gridGapY = (((3 * screenH) / 4) - imageH * row) / (row + 1);

	private int latest_Level=Settings.getLevelsUnlocked();
	private int level_stars[] = new int[row*col];
	private AndroidPic[][] levels;
	AndroidPanel panel;

	public GuiLevelScreen(Game game) {
		super(game);
		screenW = game.getScreenWidth();		
		panel = new AndroidPanel("SELECT LEVEL", 0, 0, screenW, screenH);
		panel.setTitleBarheight(30);
	//	panel.setBgColorNormal(Color.rgb(40, 120, 90)); // dark green
		screenH -= panel.getTitleBarheight();
		gridGapX = (screenW - imageW * col) / (col + 1);
		gridGapY = (screenH - imageH * row) / (row + 1);
		
		for (int i=0 ; i< Settings.getLevelsUnlocked() ;i++){
			level_stars[i]=3;
		}
		
		latest_Level=Settings.getLevelsUnlocked();
		levels = new AndroidPic[row][col];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {

				if ((i * col + j) < latest_Level) {
					switch (level_stars[i * col + j]) {
					case 0:
						levels[i][j] = new AndroidPic(
								MarioResourceManager.level, ""+
										+ (i * col + j + 1), gridGapX
										+ (gridGapX + imageW) * j,
								panel.getTitleBarheight() + +gridGapY
										+ (gridGapY + imageH) * i,
										MarioResourceManager.level.getWidth(),
										MarioResourceManager.level.getHeight());
						break;
					case 1:
						levels[i][j] = new AndroidPic(
								MarioResourceManager.level_1star,
								"" + (i * col + j + 1), gridGapX
										+ (gridGapX + imageW) * j, panel
										.getTitleBarheight()
										+ +gridGapY
										+ (gridGapY + imageH) * i,
										MarioResourceManager.level_1star.getWidth(),
										MarioResourceManager.level_1star.getHeight());
						break;
					case 2:
						levels[i][j] = new AndroidPic(
								MarioResourceManager.level_2star,
								"" + (i * col + j + 1), gridGapX
										+ (gridGapX + imageW) * j, panel
										.getTitleBarheight()
										+ +gridGapY
										+ (gridGapY + imageH) * i,
										MarioResourceManager.level_2star.getWidth(),
										MarioResourceManager.level_2star.getHeight());
						break;
					case 3:
						levels[i][j] = new AndroidPic(
								MarioResourceManager.level_3star,
								"" + (i * col + j + 1), gridGapX
										+ (gridGapX + imageW) * j, panel
										.getTitleBarheight()
										+ +gridGapY
										+ (gridGapY + imageH) * i,
										MarioResourceManager.level_3star.getWidth(),
										MarioResourceManager.level_3star.getHeight());
						break;
					}

				} else {
					levels[i][j] = new AndroidPic(
							MarioResourceManager.level_locked,
							"", gridGapX + (gridGapX + imageW) * j,
							panel.getTitleBarheight() + +gridGapY
									+ (gridGapY + imageH) * i,
									MarioResourceManager.level_locked.getWidth(),
									MarioResourceManager.level_locked.getHeight());
				}
				levels[i][j].setForeColor(Color.WHITE);
				panel.addComponent(levels[i][j]);
			}
		}
		
		for (int r = 0; r < row; r++) {
			for (int c = 0; c < col; c++) {
				if (r * col + c < latest_Level) {
					levels[r][c].addListener(new ComponentClickListener() {
						@Override
						public void onClick(Component c) {
							gotoLevel(Integer.parseInt(c.getText()));
						}
					});
				}
			}
		}
	}

	public void gotoLevel(int level) {
		Settings.level=level;
		game.setScreen(new GameScreen(game));
	}	
	
	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			for (int r = 0; r < row; r++) {
				for (int c = 0; c < col; c++) {
					if (r * col + c < latest_Level) {
						levels[r][c].processEvent(event);
					}
				}
			}
		}
	}
	
	@Override
	public void paint(float deltaTime) {

		// game.getGraphics().drawARGB(155, 100, 100,100);
		Canvas g = ((AndroidGraphics) (game.getGraphics())).getCanvas();
		g.drawRGB((Color.BLACK & 0xff0000) >> 16, (Color.BLACK & 0xff00) >> 8,
				(Color.BLACK & 0xff));
		
		panel.setForeColor(Color.WHITE);
		panel.draw(g, 0, 0);
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
		game.setScreen(new GuiMenuScreen(game));
	}


}
