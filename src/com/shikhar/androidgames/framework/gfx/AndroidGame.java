package com.shikhar.androidgames.framework.gfx;

import com.shikhar.androidgames.framework.Audio;
import com.shikhar.androidgames.framework.FileIO;
import com.shikhar.androidgames.framework.Game;
import com.shikhar.androidgames.framework.Graphics;
import com.shikhar.androidgames.framework.Input;
import com.shikhar.androidgames.framework.Screen;
import com.shikhar.androidgames.framework.fileio.AndroidFileIO;
import com.shikhar.androidgames.framework.input.AndroidInput;
import com.shikhar.androidgames.framework.sfx.AndroidAudio;
import com.shikhar.androidgames.mario.core.Settings;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

/**
 * 
 * <li>Perform window management. In our context, this means setting up an
 * activity and an AndroidFastRenderView, and handling the activity life cycle
 * in a clean way.</li> <li>Use and manage a WakeLock so that the screen does
 * not dim. Instantiate and hand out references to Graphics, Audio, FileIO, and
 * Input to interested parties.</li> <li>Manage Screens and integrate them with
 * the activity life cycle.</li>
 * 
 * @author mahesh
 * 
 */
public abstract class AndroidGame extends Activity implements Game {
	protected GameView renderView;
	protected Graphics graphics;
	protected Audio audio;
	protected Input input;
	protected FileIO fileIO;
	protected Screen screen;
	protected WakeLock wakeLock;
	protected Bitmap frameBuffer;
	protected int WIDTH;
	protected int HEIGHT=240;
	private boolean switchScreen =false;
	protected Screen nextScreen=null;
	protected boolean screenTransitionActive=false;;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//check the current orientation of the device and set the Width and Height of our Game. 
		boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		float h=metrics.heightPixels;
		float w=metrics.widthPixels;
		Log.v("mario"," window width & height (in pixels): " + w + ", " + h);
		float aspectRaio=w/h;
		//adjust width according to the aspect ratio, using this we can deal with any resolution.
		WIDTH=(int) (HEIGHT*aspectRaio);
		
		int frameBufferWidth = isLandscape ? WIDTH : HEIGHT;
		int frameBufferHeight = isLandscape ? HEIGHT : WIDTH;
		frameBuffer = Bitmap.createBitmap(frameBufferWidth,
				frameBufferHeight, Config.RGB_565);
		// proceed by creating floats to scale and adjust everything to the device's aspect ratio.
		float scaleX = (float) frameBufferWidth	/ w;
		float scaleY = (float) frameBufferHeight/ h;
		screen = getStartScreen();
		renderView = new GameView(this, frameBuffer);
		  // set up a new game
		graphics = new AndroidGraphics(getAssets(), frameBuffer);
		fileIO = new AndroidFileIO(this);
		audio = new AndroidAudio(this);
		input = new AndroidInput(this, renderView, scaleX, scaleY);
		
		setContentView(renderView);
		//we also use the PowerManager to define the wakeLock variable and we acquire and 
		//release wakelock in the onResume and onPause methods, respectively. 
		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,
				"GLGame");
	}

	@Override
	public void onBackPressed() {
		if (!screenTransitionActive)getCurrentScreen().onBackPressed();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		wakeLock.acquire();
		screen.resume();
		renderView.getThread().unpause();
		Settings.loadPreferences(this);
		((AndroidInput) input).registerAccListener();
	}

	@Override
	public void onPause() {
		super.onPause();
		wakeLock.release();
		renderView.getThread().pause();
		screen.pause();
		if (isFinishing())
			screen.dispose();
		((AndroidInput) input).unRegisterAccListener();
	}

	public Input getInput() {
		return input;
	}

	public FileIO getFileIO() {
		return fileIO;
	}

	public Graphics getGraphics() {
		return graphics;
	}

	public Audio getAudio() {
		return audio;
	}

	public void setScreen(Screen screen) {
		if (screen == null)
			throw new IllegalArgumentException("Screen must not be null");
		this.screen.dispose();
		this.screen = screen;
		//screen.resume();
		screen.update(0);
		this.nextScreen=null;
		getInput().getKeyEvents().clear();
		getInput().getTouchEvents().clear();
	}

	public void setScreenWithFade(Screen screen) {
		if (screenTransitionActive) return;
		if (screen == null)
			throw new IllegalArgumentException("Screen must not be null");
		this.nextScreen=screen;
		setSwitchScreen(true);
		setScreenTransitionActive(true);
	}

	public Screen getCurrentScreen() {
		return screen;
	}
	
	/**
	 * returns Buffer Bitmap of FastRendererView associated with game
	 * @return
	 */
	public Bitmap getBuffer(){
		return frameBuffer;
	}
	
	public void setBuffer(Bitmap buffer){
		frameBuffer=buffer;
		renderView.setBuffer(frameBuffer);

	}
	
	public int getScreenWidth(){
		return WIDTH;
	}
	
	public int getScreenHeight(){
		return HEIGHT;
	}

	public boolean isSwitchingScreen() {
		return switchScreen;
	}

	public void setSwitchScreen(boolean switchScreen) {
		this.switchScreen = switchScreen;
	}
	
	public Screen getNextScreen(){
		return nextScreen;
		/*
		if (nextScreen!=null){
			return nextScreen;
		}else{
			return screen;
		}
		*/
	}

	public boolean isScreenTransitionActive() {
		return screenTransitionActive;
	}

	public void setScreenTransitionActive(boolean screenTransitionActive) {
		this.screenTransitionActive = screenTransitionActive;
	}
}