package com.shikhar.androidgames.gui;

import com.shikhar.androidgames.framework.Input.TouchEvent;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;

public class AndroidPic extends Component {
	Bitmap img;
	int textSize=30;
	public AndroidPic(Bitmap img, String txt, int x, int y, int w, int h) {
		super(x, y, w, h);
		this.img = img;
		this.text = txt;
		this.textSize = h/2;
	}
	@Override
	public void processEvent(TouchEvent event) {
		if (event.type == TouchEvent.TOUCH_UP) {
			focused=false;
			if (inBounds(event)) {
				onTouchUp();
			}

		}
		if (event.type == TouchEvent.TOUCH_DOWN) {

			if (inBounds(event)) {
				onTouchDown();
			}

		}
	}
	@Override
	public void draw(Canvas g, int X, int Y) {
		g.drawBitmap(img, X + x + offsetX, Y + y + offsetY, null);
		// Text
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStrokeWidth(2);
		paint.setTextAlign(Align.CENTER);
		paint.setColor(foreColor);
		paint.setTextSize(textSize);
		g.drawText(text, X + x + width / 2, Y + y + height-(height-textSize )/ 2, paint);
	}
	public int getTextSize() {
		return textSize;
	}

	public void setTextSize(int textSize) {
		if (textSize > 0)
			this.textSize = textSize;
	}
}
