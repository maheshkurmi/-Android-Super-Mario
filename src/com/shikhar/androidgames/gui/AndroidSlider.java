package com.shikhar.androidgames.gui;

import com.shikhar.androidgames.framework.Input.TouchEvent;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Rect;
import android.graphics.Paint.Style;

public class AndroidSlider extends Component {
	protected int controlX , controlY, controlW = 20, controlH = 30;
	private int touchX = 0;
	private int val=0;
	private boolean isSelected = false;
	private SliderChangeListener listener;
	public int barBeforeControlColor = Color.rgb(255, 137, 59),
			barAfterControlColor = Color.rgb(200, 200, 200);

	public AndroidSlider(int x, int y, int w, int h, int controlX) {
		super(x, y, w, h);
		this.controlX = controlX;
		this.controlH = 3 * h/2;
		this.controlY = y - ((controlH - h) / 2);
		setVal((int) Math.round((100.0*(controlX-this.x))/(width-controlW)));
	}

	@Override
	public void processEvent(TouchEvent event) {
		if (event.type == TouchEvent.TOUCH_UP) {
			focused = false;
			isSelected = false;
		}
		if (event.type == TouchEvent.TOUCH_DOWN) {
			touchX = (int) event.x;
			if (inBoundControl(event)) { // for control
				isSelected = true;
				focused = true;
			}
			if (inBounds(event)) { // for seek bar
				if ((int) event.x > x && (int) event.x < x + width - controlW) {
					isSelected = true;
					focused = true;
					controlX = (int) event.x;
				}
			}
		}
		if (event.type == TouchEvent.TOUCH_DRAGGED) {
			int dx = 0;
			dx = (int) event.x - touchX;
			if (isSelected) {
				if (controlX + dx > this.x + this.width - controlW) {
					controlX = this.x + this.width - controlW;
				} else if (controlX + dx < this.x) {
					controlX = this.x;
				} else if (controlX + dx > this.x
						&& controlX + dx < this.x + this.width - controlW) {
					controlX = controlX + dx;
				}
			}
			touchX = (int) event.x;
		}
		
		setVal((int) Math.round((100.0*(controlX-this.x))/(width-controlW)));
		System.out.println(val);
	}

	public void addListener(SliderChangeListener l){
		listener=l;
	}
	
	public int getValue(){
		return val;
	}
	
	public void setVal(int value){
		if (value!=val && value>=0 && value<=100){
			//controlX= this.x + (int)(val/100.0*(width-controlW));
			this.val=value;
			if (listener!=null) listener.onChange(this, val);
		}
	}
	
	public boolean inBoundControl(TouchEvent event) {
		int x = controlX;
		int y = controlY;
		if (getParent() != null) {
			x += getParent().x;
			y += getParent().y;
		}
		if (event.x > x-5 && event.x < x + controlW +5 && event.y > y-5
				&& event.y < y + controlH +5)
			return true;
		else
			return false;
	}

	@Override
	public void draw(Canvas g, int X, int Y) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);

		Paint paintB = new Paint();
		paintB.setAntiAlias(true);
		paintB.setColor(borderColor);
		paintB.setStyle(Style.STROKE);
		paintB.setStrokeWidth(3);
		paintB.setStrokeJoin(Join.MITER);
		

		paint.setColor(barBeforeControlColor);
		Rect barBeforeControl = new Rect(X + x, Y + y, X + controlX, Y + y
				+ height);
		g.drawRect(barBeforeControl, paint);
		g.drawRect(barBeforeControl, paintB);

		paint.setColor(barAfterControlColor);
		Rect barAfterControl = new Rect(X + controlX + controlW, Y + y, X + x
				+ width, Y + y + height);
		g.drawRect(barAfterControl, paint);
		g.drawRect(barAfterControl, paintB);
		
		bgColorNormal = Color.rgb(237, 88, 66);
		bgColorFocused = Color.rgb(217, 38, 26);
		paint.setColor(focused ? bgColorFocused : bgColorNormal);
		Rect control = new Rect(X + controlX, Y + controlY, X + controlX
				+ controlW, Y + controlY + controlH);
		g.drawRect(control, paint);		
		g.drawRect(control, paintB);
		g.drawText(val+"%", this.x-30, this.y+this.height, paint);
	}

	public int getControlX() {
		return controlX;
	}
	public int getControlW() {
		return controlW;
	}

}
