package com.shikhar.androidgames.mario.objects.tiles;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import com.shikhar.androidgames.mario.core.GameRenderer;
import com.shikhar.androidgames.mario.core.tile.GameTile;
public class InfoPanel extends GameTile {
	protected int borderColor = Color.BLACK, bgColorNormal = Color.LTGRAY;
	protected int bgColorFocused = Color.DKGRAY, foreColor = Color.BLACK;
	protected int borderWidth = 2;

    private String info[];
    private int width=0, height;
	
    public InfoPanel(int pixelX, int pixelY, String info) {
		super(pixelX, pixelY,null,null);
    	info="Mahesh Kurmi;Android Mario V1.0";
    		this.info=info.split(";");
		setIsCollidable(false);
		height=24;//backPanel.getHeight();
		for (String s:this.info){
			if (width<s.length()*8)width=s.length()*8;
		}
		width=width+16;
		height=this.info.length*12+8;
		
	}
	
	@Override
	public void update(int time) {
	
	}
	
	@Override
	public void draw(Canvas g, int x, int y) {
		//g.drawBitmap(currentAnimation().getImage(),x+getOffsetX() , y+getOffsetY(),x +getWidth()+getOffsetX(),y+getOffsetY() +  getHeight(),0,0,getWidth(),getHeight(),null);
		//GameRenderer.drawStringDropShadow(g, info, x+getOffsetX()+6, y+getOffsetY()+5);
		//draw( g,  x,  y, 0, 0);
	}
	
	public void draw(Canvas g, int x, int y, int offsetX, int offsetY) {
		x+=offsetX;
		y+=offsetY;
		RectF r = new RectF(x, y, x+width, y+height);
		
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		bgColorNormal = Color.rgb(99, 214, 0);
		paint.setTextAlign(Align.CENTER);
			paint.setStrokeWidth(2);
		paint.setStyle(Style.FILL_AND_STROKE);

		paint.setShader(null);
		paint.setColor(Color.WHITE);
		paint.setShadowLayer(1.5f, 1.5f, 1.5f, Color.GRAY);
		g.drawCircle(x+5, y-10, 2, paint);
		g.drawCircle(x+width-5, y-10, 2, paint);
		paint.setStyle(Style.STROKE);

		g.drawRoundRect(r, 3, 3, paint);//drawRect(r, paint);
		//g.drawLine(x+5, y+height, x+5, g.getHeight(), paint);
		//g.drawLine(x+width-5, y+height, x+width-5, g.getHeight(), paint);
		g.drawLine(x+5, y, x+5, y-7, paint);
		g.drawLine(x+width-5, y, x+width-5, y-7, paint);
	
		for (int i=0; i<info.length;i++){
			GameRenderer.drawStringDropShadow(g, info[i], x+6, y+6+12*i,1,1);
		}
	}
	
	public String[] getInfo() {
		return info;
	}
	
	public void setInfo(String info) {
		this.info=info.split(";");
		width=16;
		for (String s : this.info) {
			if (width < s.length() * 8)
				width = s.length() * 8;
		}
		width = width + 16;
		height = this.info.length * 12 + 8;

	}
	
	@Override
	public int getWidth() {
		return width;
	}
	
	@Override
	public int getHeight() {
		return height;
	}

	
}
