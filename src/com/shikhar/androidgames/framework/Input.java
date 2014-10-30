package com.shikhar.androidgames.framework;

import java.util.List;

public interface Input {
	public static class KeyEvent {
		public static final int KEY_DOWN = 0;
		public static final int KEY_UP = 1;
		public int type;
		public int keyCode;
		public char keyChar;
	}

	public static class TouchEvent {
		public static final int TOUCH_DOWN = 0;
		public static final int TOUCH_UP = 1;
		public static final int TOUCH_DRAGGED = 2;
		public int type;
		public int x, y;
		public int pointer;
	}

	public boolean isKeyPressed(int keyCode);

	public boolean isTouchDown(int pointer);

	public int getTouchX(int pointer);

	public int getTouchY(int pointer);

	public float getAccelX();

	public float getAccelY();

	public float getAccelZ();

	public List<KeyEvent> getKeyEvents();

	public List<TouchEvent> getTouchEvents();
}