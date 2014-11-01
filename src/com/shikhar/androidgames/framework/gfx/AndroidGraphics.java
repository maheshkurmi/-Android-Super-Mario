package com.shikhar.androidgames.framework.gfx;

import java.io.IOException;
import java.io.InputStream;

import com.shikhar.androidgames.framework.Graphics;
import com.shikhar.androidgames.framework.Image;
import com.shikhar.androidgames.mario.core.MarioResourceManager;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

public class AndroidGraphics implements Graphics {
	AssetManager assets;
	Bitmap frameBuffer;
	Canvas canvas;
	Paint paint;
	Rect srcRect = new Rect();
	Rect dstRect = new Rect();

	public AndroidGraphics(AssetManager assets, Bitmap frameBuffer) {
		this.assets = assets;
		this.frameBuffer = frameBuffer;
		this.canvas = new Canvas(frameBuffer);
		this.paint = new Paint();
	}

	public Image newImage(String fileName, ImageFormat  format) {
		Config config = null;
		if (format == ImageFormat .RGB565)
			config = Config.RGB_565;
		else if (format == ImageFormat .ARGB4444)
			config = Config.ARGB_4444;
		else
			config = Config.ARGB_8888;
		Options options = new Options();
		options.inPreferredConfig = config;
		InputStream in = null;
		Bitmap bitmap = null;
		try {
			in = assets.open(fileName);
			bitmap = BitmapFactory.decodeStream(in);
			if (bitmap == null)
				throw new RuntimeException("Couldn't load bitmap from asset '"
						+ fileName + "'");
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load bitmap from asset '"
					+ fileName + "'");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		if (bitmap.getConfig() == Config.RGB_565)
			format = ImageFormat.RGB565;
		else if (bitmap.getConfig() == Config.ARGB_4444)
			format = ImageFormat.ARGB4444;
		else
			format = ImageFormat.ARGB8888;
		return new AndroidImage(bitmap, format);
	}

	
    public Canvas getCanvas() {
        return canvas;
    }
	
	@Override
    public void clearScreen(int color) {
        canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8,
                (color & 0xff));
    }

	public void drawPixel(int x, int y, int color) {
		paint.setColor(color);
		canvas.drawPoint(x, y, paint);
	}
	  
	@Override
	public void drawARGB(int a, int r, int g, int b) {
		canvas.drawARGB(a, r, g, b);
		/*
		 * paint.setStyle(Style.FILL); paint.setColor(Color.argb(a,r,g,b));
		 * canvas.drawRect(canvas.getClipBounds(), paint);
		 */
	}
	    
	@Override
	public void drawString(String text, int x, int y, Paint paint){
	     canvas.drawText(text, x, y, paint);
    }
	

	/**
	 * Draws Bitmap Font
	 * @param font bitmap representing fonts
	 * @param text
	 * @param x
	 * @param y
	 */
	public  void drawBitmapFont(Bitmap[] font, String text, int x, int y,int charSize) {
		char[] ch = text.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			canvas.drawBitmap(font[ch[i] - 32], x + i	* charSize, y, null);
		}
	}

	
	public void drawLine(int x, int y, int x2, int y2, int color) {
		paint.setColor(color);
		canvas.drawLine(x, y, x2, y2, paint);
	}

	public void drawRect(int x, int y, int width, int height, int color) {
		paint.setColor(color);
		paint.setStyle(Style.FILL);
		canvas.drawRect(x, y, x + width - 1, y + width - 1, paint);
	}

	@Override
	public void drawCircle(float centerX, float centerY, float radius, int color) {
		paint.setColor(color);
		paint.setStyle(Style.FILL);
		canvas.drawCircle(centerX, centerY, radius, paint);
	}
	
	 public void drawImage(Image Image, int x, int y, int srcX, int srcY,
	            int srcWidth, int srcHeight) {
	        srcRect.left = srcX;
	        srcRect.top = srcY;
	        srcRect.right = srcX + srcWidth;
	        srcRect.bottom = srcY + srcHeight;
	        
	        dstRect.left = x;
	        dstRect.top = y;
	        dstRect.right = x + srcWidth;
	        dstRect.bottom = y + srcHeight;

	        canvas.drawBitmap(((AndroidImage) Image).bitmap, srcRect, dstRect,
	                null);
	    }
	

	 @Override
	    public void drawImage(Image Image, int x, int y) {
	        canvas.drawBitmap(((AndroidImage)Image).bitmap, x, y, null);
	    }
	 public void drawScaledImage(Image Image, int x, int y, int width, int height, int srcX, int srcY, int srcWidth, int srcHeight){
	        
	        
	     srcRect.left = srcX;
	        srcRect.top = srcY;
	        srcRect.right = srcX + srcWidth;
	        srcRect.bottom = srcY + srcHeight;
	        
	        
	        dstRect.left = x;
	        dstRect.top = y;
	        dstRect.right = x + width;
	        dstRect.bottom = y + height;
	        
	        
	        canvas.drawBitmap(((AndroidImage) Image).bitmap, srcRect, dstRect, null);
	        
	    }
	public int getWidth() {
		return frameBuffer.getWidth();
	}

	public int getHeight() {
		return frameBuffer.getHeight();
	}

	

}
