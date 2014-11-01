package com.shikhar.androidgames.mario.objects.creatures;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.shikhar.androidgames.mario.core.MarioResourceManager;
import com.shikhar.androidgames.mario.core.MarioSoundManager;
import com.shikhar.androidgames.mario.core.animation.Animation;
import com.shikhar.androidgames.mario.core.tile.TileMap;
import com.shikhar.androidgames.mario.objects.base.Creature;


public class CreatureEmitter extends Creature {


	private Animation steady, fire,flip;;
	private static Bitmap fd_1, fd_2,fd_3,fd_4, fd_5;;
	private static boolean initialized = false;
    private float t;
	public boolean readytoFire=false;
	//private TileMap map;
	private int A=120;
	private int dormantTime=-1;
	private int initY;
	private int enemyCount=0;
    private Random random;
	public CreatureEmitter(int x, int y, MarioSoundManager soundManager) {

		super(x, y, soundManager);
		if (!initialized) {
			Bitmap[] images = MarioResourceManager.Latiku_Fire;
			fd_1 = images[0];
			fd_2 = images[1];
			images = MarioResourceManager.Latiku;
			fd_3 = images[0];
			fd_4 = images[1];
			fd_5 = images[2];
			initialized = true;
		}
		steady = new Animation(250).addFrame(fd_3).addFrame(fd_4);

		final class FireAnimation extends Animation {
			public void endOfAnimationAction() {
				setAnimation(steady);
			}
		}
		fire = new FireAnimation().setDAL(250).addFrame(fd_3).addFrame(fd_2)
				.addFrame(fd_1).addFrame(fd_2).addFrame(fd_4);
	
		flip = new Animation().addFrame(fd_5).addFrame(fd_5);		
		///flip.addFrame(fd_5, 100);
		//flip.addFrame(fd_5, 1200);
		setAnimation(steady);
		setIsItem(true);
		setGravityFactor(0);
		//setIsCollidable(false);
		setIsAlwaysRelevant(true);
		setIsItem(true);
		initY=-2*getHeight();
		this.y=initY-getHeight();
		//this.x=x;
		random=new Random();
		//setOffsetY(-getHeight());
	}

	@Override
	public void xCollide(Point p) {

	}
	
	public void creatureXCollide() {
		
	}
	
	public void wakeUp(boolean isleft) {
		super.wakeUp();
		if (x<map.getWidth()*12)x=map.getPlayer().getX()+A/2;
		y=initY-getHeight();
		dx=-2;
	}

	@Override
	public void update(int time) {
		super.update(time);
		t++;
		if (t>70-enemyCount/50){
			readytoFire=true;
			t=0;
		}
	}
	
	@Override
	public void updateCreature(TileMap map, int time) {
		update(time);
		if(this.currentAnimation()==flip && dormantTime<0){
			if (y>=map.getHeight()*16-16){
				dormantTime=400;
				setIsInvisible(true);
			}else{
				y=y+10*GRAVITY*time*time;
			}
			//if (y>mp.getHeight())
			return;
		}else if (dormantTime>0){
			dormantTime--;
			return;
		}else if (dormantTime==0){
			setAnimation(steady);
			soundManager.playItemSprout();
			setIsCollidable(true);
			setIsFlipped(false);
			setIsInvisible(false);
			x=map.getPlayer().getX()+A;
			y=initY-getHeight();
			dx=-2;
			wakeUp();
			dormantTime=-1;
			readytoFire=false;
			return;
		}
		
		
		double x1=x-map.getPlayer().getX();
		if(Math.abs(x1)<A){
			dx=Math.signum(dx)*100f;
		}else if (x1>A){
			dx=(float) (dx-(x1-A)/10.0);
		}else if (x1<-A){
			dx=(float) (dx+(-x1-A)/10.0);
		}
		//double v1=-x1/5;// -Math.signum(x1)*Math.abs(x1+100)/5;//-Math.signum(x1)*A*(x1*x1));
		//v1=Math.signum(v1)*(Math.abs(v1)+10);
		x=(float) (x+dx*time/600.0);
			x=map.getWidth()*8;
	
		y=initY-getHeight();
		//super.updateCreature(map,time);
		if (readytoFire && currentAnimation()!=fire){
			setAnimation(fire);
			//Latiku.this.map.creaturesToAdd().add(new Fire_Thorny((int)getX(),(int)getY()+15,Latiku.this.soundManager));
			//soundManager.playKick();
			///map.creaturesToAdd().add(new BoomRang((int) (dx<0?x:x+getWidth()),(int)(y+getHeight()*0.13f),dx<0?-2:2,soundManager));
			//readytoFire=false;
		}
		if(readytoFire && currentAnimation()==fire){
			if (currentAnimation().getImage()==fd_1){
				fireRandomCreature(map);
				readytoFire=false;
			}
		}
	}
	
	public void jumpedOn() {

	}
     
	private void fireRandomCreature(TileMap map){
		enemyCount++;
		Creature c;
		int i=random.nextInt(19);
		int x=(int) (map.getWidth()/2-10+Math.random()*(20))*16;
		switch (i){
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
			c= new Goomba(x,(int)getY(),soundManager);
			break;
		case 6:
		case 7:
		case 8:
			c=   new RedKoopa(x,(int)getY(),soundManager,true);
			break;
		case 9:
		case 10:
			c=   new RedKoopa(x,(int)getY(),soundManager,false);
			break;
		case 11:
			c=   new Thorny(x,(int)getY(),soundManager);
			break;
		case 12:
			c=   new Mushroom(x,(int)getY(),true);
			break;
		case 13:
			c=  new Mushroom(x,(int)getY(),false);
			break;
		case 14:
			//map.creaturesToAdd().add(new FlyRedKoopa(x,(int)getY(),soundManager));
		default:	
			c=  new Goomba(x,(int)getY(),soundManager);
			
		}
		c.setIsAlwaysRelevant(true);
		map.creaturesToAdd().add(c);
	}

}
