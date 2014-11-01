package com.shikhar.androidgames.gui;

import com.shikhar.androidgames.framework.Input.TouchEvent;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;

public class AndroidText extends Component {
	protected int textSize = 18;
	protected int align=0;
	protected int color = Color.BLACK;
	protected int strokeWidth = 1;

	public AndroidText(String text, int x, int y, int w, int h) {
		super(x, y, w, h);
		this.text = text;

	}
	public int getTextSize() {
		return textSize;
	}

	public void setTextSize(int textSize) {
		if (textSize > 0)
			this.textSize = textSize;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getAlign() {
		return align;
	}
	/**
	 * sets alignment of the text
	 * 
	 * @param align
	 *            0=left, 1=centre, 2=right
	 */
	public void setAlign(int align) {
		this.align = align;
	}
	@Override
	public void processEvent(TouchEvent event) {
	
	}
	@Override
	public void draw(Canvas g, int X, int Y) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL_AND_STROKE);
		paint.setStrokeWidth(strokeWidth);
		paint.setTextAlign(Align.CENTER);
		paint.setColor(foreColor);
		paint.setTextSize(textSize);
		paint.setShadowLayer(2, 1, 1, Color.GRAY);
		switch (align) {
		case 0:
			paint.setTextAlign(Align.LEFT);
			g.drawText(text, X+x + offsetX, Y+y + height-(height - textSize)/2, paint);
			break;
		case 1:
			paint.setTextAlign(Align.CENTER);
			g.drawText(text, X+x + width / 2, Y+y +  3*height / 4, paint);
			break;
		case 2:
			paint.setTextAlign(Align.RIGHT);
			g.drawText(text, X+width - offsetX, Y+y + 3*height / 4, paint);
			break;
		}
	}
	public int getStrokeWidth() {
		return strokeWidth;
	}
	public void setStrokeWidth(int strokeWidth) {
		this.strokeWidth = strokeWidth;
	}

}
