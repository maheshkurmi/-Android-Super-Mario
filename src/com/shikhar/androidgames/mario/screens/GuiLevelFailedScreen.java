package com.shikhar.androidgames.mario.screens;

import java.util.List;

import com.shikhar.androidgames.framework.Game;
import com.shikhar.androidgames.framework.Graphics;
import com.shikhar.androidgames.framework.Input.TouchEvent;
import com.shikhar.androidgames.framework.Screen;
import com.shikhar.androidgames.framework.gfx.AndroidGraphics;
import com.shikhar.androidgames.gui.AndroidButton;
import com.shikhar.androidgames.gui.AndroidPanel;
import com.shikhar.androidgames.gui.AndroidText;
import com.shikhar.androidgames.gui.Component;
import com.shikhar.androidgames.gui.ComponentClickListener;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class GuiLevelFailedScreen extends Screen {

	AndroidPanel panel;
	AndroidButton btnReplay, btnMenu;
	AndroidText txtScore, messageFail;
	private int width,height;
	private int panelX = 60, panelY = 50;
	int highscore = 1234560, score = 12345;

	public GuiLevelFailedScreen(Game game) {
		super(game);
		width = game.getScreenWidth();
		height = game.getScreenHeight();
		int h = height - 2 * panelY;
		int w = width - 2 * panelX;
		panel = new AndroidPanel("HIGHSCORE :" + highscore, panelX, panelY, w,
				h);
		panel.setTitleBarheight(30);
		int tbh = panel.getTitleBarheight();
		btnReplay = new AndroidButton("REPLAY", (w - 160) / 3, h - 40, 80, 30);
		btnReplay.setTextSize(16);
		btnMenu = new AndroidButton("MENU", 2 * (w - 160) / 3 + 80, h - 40, 80,
				30);
		btnMenu.setTextSize(16);
		messageFail = new AndroidText("Level Failed", 0, tbh ,
				panel.getWidth(), 40);
		messageFail.setTextSize(21);
		messageFail.setAlign(1);
		txtScore = new AndroidText("SCORE : " + score, 0, tbh + 30,
				panel.getWidth(), 30);
		txtScore.setTextSize(14);
		txtScore.setAlign(1);
		// txtScore.setStrokeWidth(1);
		panel.addComponent(btnReplay);
		panel.addComponent(btnMenu);
		panel.addComponent(txtScore);
		panel.addComponent(messageFail);
	}

	@Override
	public void update(float deltaTime) {
		Graphics g = game.getGraphics();
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			btnReplay.processEvent(event);
			btnMenu.processEvent(event);
			btnReplay.addListener(new ComponentClickListener() {
				@Override
				public void onClick(Component c) {
					//game.resetlevel();
					game.setScreen(new GameScreen(game));
				}
			});
			btnMenu.addListener(new ComponentClickListener() {
				@Override
				public void onClick(Component c) {
					game.setScreen(new GuiMenuScreen(game));
				}
			});
		}
	}

	@Override
	public void paint(float deltaTime) {

		// game.getGraphics().drawARGB(155, 100, 100,100);
		Canvas g = ((AndroidGraphics) (game.getGraphics())).getCanvas();
		//g.drawRGB((Color.BLACK & 0xff0000) >> 16, (Color.BLACK & 0xff00) >> 8,
				//(Color.BLACK & 0xff));
	//	g.drawARGB(225,10, 10, 10);

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
