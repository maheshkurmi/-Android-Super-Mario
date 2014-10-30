package com.shikhar.androidgames.framework;

import com.shikhar.androidgames.framework.Graphics.ImageFormat;

public interface Image {
	/**
	 * return the width of the Pixmap in pixels.
	 * @return
	 */
	public int getWidth();

	/**
	 * return the height of the Pixmap in pixels.
	 * @return
	 */
	public int getHeight();

	/**
	 * returns the PixelFormat that the Pixmap is stored with in RAM.
	 * @return
	 */
	public ImageFormat getFormat();

	/**
	 * Pixmap instances use up memory and potentially other system resources. If
	 * we no longer need them, we should dispose of them with this method.
	 */
	public void dispose();
}