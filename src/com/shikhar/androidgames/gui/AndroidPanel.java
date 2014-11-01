package com.shikhar.androidgames.gui;

import java.util.ArrayList;

import com.shikhar.androidgames.framework.Input.TouchEvent;

import android.R.color;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Rect;

public class AndroidPanel extends Component{
	ArrayList<Component>components;
	protected int titleBarheight=40;
	protected int titleBarColor=Color.rgb(255, 127, 39);
	
	public int getTitleBarheight() {
		return titleBarheight;
	}

	public void setTitleBarheight(int titleBarheight) {
		if(titleBarheight<height)this.titleBarheight = titleBarheight;
	}

	public int getTitleBarColor() {
		return titleBarColor;
	}

	public void setTitleBarColor(int titleColor) {
		this.titleBarColor = titleColor;
	}

	public AndroidPanel(String title, int x, int y, int w, int h) {
		super(x, y, w, h);
		components=new ArrayList<Component>();
		this.text=title;
		this.bgColorNormal=Color.rgb(153, 217, 234);
	}
	
	public void addComponent(Component c){
		components.add(c);
		c.setParent(this);
	}
	
	public void removeComponent(Component c){
		components.remove(c);
		c.setParent(null);
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
		int n=components.size();
		/*
		Paint paint =new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL_AND_STROKE);
		paint.setStrokeJoin(Join.MITER);		
		paint.setColor(titleBarColor);
		Rect r=new Rect(X+x,Y+y,X+x+width,Y+y+titleBarheight);
		g.drawRect(r, paint);
		r.set(X+x,Y+y+titleBarheight,X+x+width,Y+y+height);
		paint.setColor(bgColorNormal);
		//g.drawRect(r, paint);
		
		
		// titleText 
		paint.setStyle(Style.FILL_AND_STROKE);
		paint.setStrokeWidth(2);
		paint.setTextAlign(Align.CENTER);
		paint.setColor(foreColor);
		paint.setTextSize(titleBarheight/2+1);
		g.drawText(text, X+x+width/2 , Y+y +  3*titleBarheight / 4, paint);
		
		// border
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(3);
		paint.setStrokeJoin(Join.MITER);		
		paint.setColor(borderColor);
		g.drawRect(r, paint);
		r.set(X+x,Y+y,X+x+width,Y+y+titleBarheight);
		g.drawRect(r, paint);
		*/
		// draw children components of this panel
		for (int i=0; i<n;i++){
			components.get(i).draw(g, X+x,Y+y);
		}
	}

	
}
