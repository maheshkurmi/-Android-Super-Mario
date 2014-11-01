package com.shikhar.androidgames.gui;

import com.shikhar.androidgames.framework.Input.TouchEvent;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;

public class AndroidButton extends Component {
	protected int textSize = 18;

	public AndroidButton(String text, int x, int y, int w, int h) {
		super(x, y, w, h);
		this.text = text;
		borderColor=Color.rgb(200, 120, 100);
	};

	@Override
	public void draw(Canvas g, int X, int Y) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		int color;
		int i=focused ? 0 : -1;
		bgColorNormal = Color.rgb(99, 214, 0);
		bgColorFocused = Color.rgb(66, 170, 0);
		color=focused ? bgColorFocused : bgColorNormal;
		paint.setStyle(Style.FILL_AND_STROKE);
		paint.setShader(null);
		
		RectF r = new RectF(X+x, Y+y, X+x+width, Y+y+height);
		Shader textShader=new LinearGradient(r.left ,  r.top ,  r.left , r.bottom ,
	            new int[]{color,Color.rgb(66, 170, 0)},
	            new float[]{0, 1}, TileMode.CLAMP);
		paint.setShader(textShader);
		g.drawRect(r, paint);		

		// Text
	
		paint.setTextAlign(Align.CENTER);
			paint.setStrokeWidth(2);
		paint.setStyle(Style.FILL_AND_STROKE);
		textShader=new LinearGradient(X+x ,  Y+y ,  X+x ,  Y+y+height ,
		            new int[]{Color.rgb(230, 130, 41),Color.rgb(197, 0, 0)},
		            new float[]{0, 1}, TileMode.CLAMP);
		  paint.setShader(textShader);	
			paint.setTextSize(textSize);
			
		  g.drawText(text, X+x + width / 2, Y+y + height-(height-textSize)/2-2+i, paint);

			paint.setStrokeWidth(0.3f);
		//	paint.setColor(this.foreColor);
			paint.setStyle(Style.STROKE);
			paint.setColor(Color.rgb(10, 0, 0));
			paint.setShader(null);
	
		//paint.setShadowLayer(1, 1, 1, Color.WHITE);
			
		//g.drawText(text, X+x + width / 2, Y+y + height-(height-textSize)/2-2+i, paint);
		//
		// border
		//paint.setShadowLayer(0,0,0,0);
		paint.setColor(borderColor);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(3);
		paint.setStrokeJoin(Join.MITER);
		textShader=new LinearGradient(r.left ,  r.top ,  r.left , r.bottom ,
	            new int[]{Color.rgb(99, 214, 0),Color.rgb(66, 170, 0)},
	            new float[]{0, 1}, TileMode.CLAMP);
		//paint.setShadowLayer(null);//3, 3, 3, Color.GRAY);
		paint.setShader(null);
		paint.setColor(Color.WHITE);
		paint.setShadowLayer(2, 2, 2, Color.GRAY);
		g.drawRoundRect(r, 5, 5, paint);//drawRect(r, paint);
	}

	public int getTextSize() {
		return textSize;
	}

	public void setTextSize(int textSize) {
		if (textSize > 0)
			this.textSize = textSize;
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
}
