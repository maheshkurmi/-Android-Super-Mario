package com.shikhar.androidgames.gui;

import com.shikhar.androidgames.framework.Input.TouchEvent;

import android.graphics.Canvas;
import android.graphics.Color;

public abstract class Component {
	protected int x = 0, y = 0, width = 1, height = 1;
	protected boolean focused = false;
	protected int borderColor = Color.BLACK, bgColorNormal = Color.LTGRAY,
			      bgColorFocused = Color.DKGRAY, foreColor = Color.BLACK;
	protected int borderWidth = 2;
	protected ComponentClickListener clickListener = null;
	protected String text = "";

	public abstract void draw(Canvas g, int X, int Y);

	protected int offsetX = borderWidth + 1;
	protected int offsetY = borderWidth + 1;

	private Component parent=null;
	
	private String command="";
	
	public Component(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
	}

	public void addListener(ComponentClickListener clickListener) {
		this.clickListener = clickListener;
	}

	public void onTouchDown() {
		focused = true;
	}
	public void onTouchUp() {
		focused = false;
		if (this.clickListener != null)
			this.clickListener.onClick(this);
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public abstract void processEvent(TouchEvent event) ;

	public void setoffsets(int x, int y){
		this.offsetX = x;
		this.offsetY = y;
	}
	
	public boolean inBounds(TouchEvent event) {
		int x=this.x;
		int y=this.y;
		if (parent!=null){
			x+=parent.x;
			y+=parent.y;
		}
		if (event.x > x && event.x < x + width - 1 && event.y > y
				&& event.y < y + height - 1)
			return true;
		else
			return false;
	}

	public Component getParent() {
		return parent;
	}

	public void setParent(Component parent) {
		this.parent = parent;
	}
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setBorderColor(int borderColor) {
		this.borderColor = borderColor;
	}

	public void setBgColorNormal(int bgColorNormal) {
		this.bgColorNormal = bgColorNormal;
	}

	public void setBgColorFocused(int bgColorFocused) {
		this.bgColorFocused = bgColorFocused;
	}

	public void setForeColor(int foreColor) {
		this.foreColor = foreColor;
	}

	public boolean isFocused() {
		return focused;
	}


	public ComponentClickListener getClickListener() {
		return clickListener;
	}


	public String getText() {
		return text;
	}
}
