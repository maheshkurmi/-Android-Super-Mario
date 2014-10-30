package com.shikhar.androidgames.framework.gfx;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * extends SurfaceView to perform continuous rendering in a separate thread that
 * could also house our game’s main loop. It keeps a reference to a Game
 * instance from which it can get the active Screen. We constantly call the
 * Screen.update() and Screen.present() methods from within the FastRenderView
 * thread. It keeps track of the delta time between frames that is passed to
 * the active Screen. It takes the artificial framebuffer to which the
 * AndroidGraphics instance draws, and draws it to the SurfaceView, which is
 * scaled if necessary.
 * 
 * @author mahesh
 * 
 */
public class AndroidFastRenderView extends SurfaceView implements Runnable {
	AndroidGame game;
	Bitmap framebuffer;
	Thread renderThread = null;
	SurfaceHolder holder;
	volatile boolean running = false;

	public AndroidFastRenderView(AndroidGame game, Bitmap framebuffer) {
		super(game);
		this.game = game;
		this.framebuffer = framebuffer;
		this.holder = getHolder();
	}

	public void resume() {
		running = true;
		renderThread = new Thread(this);
		renderThread.start();
	}

	public void run() {
		Rect dstRect = new Rect();
		long startTime = System.nanoTime();
		while (running) {
			if (!holder.getSurface().isValid())
				continue;
			float deltaTime = (System.nanoTime() - startTime) / 1000000000.0f;
			startTime = System.nanoTime();
			if (deltaTime > 2.5){
	            deltaTime = (float) 2.5;
	        }
			game.getCurrentScreen().update(deltaTime);
			game.getCurrentScreen().paint(deltaTime);
			Canvas canvas = holder.lockCanvas();
			canvas.getClipBounds(dstRect);
			canvas.drawBitmap(framebuffer, null, dstRect, null);
			holder.unlockCanvasAndPost(canvas);
		}
	}

	public void pause() {
		running = false;
		while (true) {
			try {
				renderThread.join();
				return;
			} catch (InterruptedException e) {
				// retry
			}
		}
	}
}