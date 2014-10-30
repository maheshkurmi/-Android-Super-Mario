package com.shikhar.androidgames.framework;

import android.graphics.Paint;

public interface Graphics {
	public static enum ImageFormat {
		ARGB8888, ARGB4444, RGB565
	}

	/**
	 * will load an image given in either JPEG or PNG format. We specify a
	 * desired format for the resulting Pixmap, which is a hint for the loading
	 * mechanism. The resulting Pixmap might have a different format. We do this
	 * so that we can somewhat control the memory footprint of our loaded images
	 * (for example, by loading RGB888 or ARGB8888 images as RGB565 or ARGB4444
	 * images). The filename specifies an asset in our application’s APK file.
	 * 
	 * @param fileName
	 * @param format
	 * @return
	 */
	public Image newImage(String fileName, ImageFormat format);

	/**
	 * clears the complete framebuffer with the given color. All colors in our
	 * little framework will be specified as 32-bit ARGB8888 values (Pixmaps
	 * might, of course, have a different format).
	 * 
	 * @param color
	 */
	public void clearScreen(int color);

	/**
	 * will set the pixel at (x,y) in the framebuffer to the given color.
	 * Coordinates outside the screen will be ignored. This is called clipping.
	 * 
	 * @param x
	 * @param y
	 * @param color
	 */
	public void drawPixel(int x, int y, int color);

	/**
	 * Fill the entire canvas' bitmap (restricted to the current clip) with the specified 
	 * ARGB color, using srcover porterduff mode
	 * @param a
	 * @param r
	 * @param g
	 * @param b
	 */
	public void drawARGB(int a, int r, int g, int b);
	/**
	 * The Graphics.drawLine() method is analogous to the Graphics.drawPixel()
	 * method. We specify the start point and endpoint of the line, along with a
	 * color. Any portion of the line that is outside the framebuffer’s raster
	 * will be ignored.
	 * 
	 * @param x
	 * @param y
	 * @param x2
	 * @param y2
	 * @param color
	 */
	public void drawLine(int x, int y, int x2, int y2, int color);

	/**
	 * The Graphics.drawRect() method draws a rectangle to the framebuffer. The
	 * (x,y) specifies the position of the rectangle’s top-left corner in the
	 * framebuffer. The arguments width and height specify the number of pixels
	 * in x and y, and the rectangle will fill starting from (x,y). We fill
	 * downward in y. The color argument is the color that is used to fill the
	 * rectangle.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 */
	public void drawRect(int x, int y, int width, int height, int color);

	/**
	 * The Graphics.drawPixmap() method draws rectangular portions of a Pixmap
	 * to the framebuffer. The (x,y) coordinates specify the top-left corner’s
	 * position of the Pixmap’s target location in the framebuffer. The
	 * arguments srcX and srcY specify the corresponding top-left corner of the
	 * rectangular region that is used from the Pixmap, given in the Pixmap’s
	 * own coordinate system. Finally, srcWidth and srcHeight specify the size
	 * of the portion that we take from the Pixmap.
	 * 
	 * @param pixmap
	 * @param x
	 * @param y
	 * @param srcX
	 * @param srcY
	 * @param srcWidth
	 * @param srcHeight
	 */

	/**
	 * The first two arguments specify the coordinates of the center of the
	 * circle, the next argument specifies the radius in pixels,
	 * 
	 * @param centerX
	 * @param centerY
	 * @param radius
	 * @param color
	 */
	public void drawCircle(float centerX, float centerY, float radius, int color);

	public void drawImage(Image pixmap, int x, int y, int srcX, int srcY,
			int srcWidth, int srcHeight);

	/**
	 * The Graphics.drawPixmap() method draws rectangular portions of a Pixmap
	 * to the framebuffer. The (x,y) coordinates specify the top-left corner’s
	 * position of the Pixmap’s target location in the framebuffer \n. Note that
	 * when we refer to the framebuffer, we actually mean the virtual
	 * framebuffer of the UI component to which we draw. We just pretend that we
	 * directly draw to the real framebuffer
	 * 
	 * @param pixmap
	 * @param x
	 * @param y
	 */
	public void drawImage(Image pixmap, int x, int y);

	
	public void drawString(String text, int x, int y, Paint paint);

	/**
	 * returns the width of the framebuffer in pixels.
	 * 
	 * @return
	 */
	public int getWidth();

	/**
	 * returns the height of the framebuffer in pixels.
	 * 
	 * @return
	 */
	public int getHeight();
}
