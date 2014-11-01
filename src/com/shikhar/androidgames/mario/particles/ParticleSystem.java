package com.shikhar.androidgames.mario.particles;

import com.shikhar.androidgames.mario.core.MarioResourceManager;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class ParticleSystem {
	private Particle particles[];
	private Bitmap bitmap;
	public final int numParticles = 6;
	public boolean respawnParticle = false;
	public boolean isBursting = true;
    private boolean active;
    private int count=0;
    
	public ParticleSystem(int startXPos, int startYPos,	int numParticles) {
		bitmap=MarioResourceManager.brick_particle;
		this.active=true;
		particles = new Particle[numParticles];
		float speed=0.2f;
		float dx,dy;
		double theta;
		for (int i = 0; i < particles.length; i++) {
			float v=0.13f+(float) (0.4*speed*Math.random());
			theta=(0.15+0.7*i/particles.length)*Math.PI;
			dx=0.6f*v*(float) Math.cos(theta);
			dy=-v*(float) Math.sin(theta);
			particles[i] = new Particle(startXPos, startYPos,dx,dy, bitmap);
		}
	}

	public void doDraw(Canvas canvas,int offsetX,int offsetY) {
		//if (count>2000) return;
		for (int i = 0; i < particles.length; i++) {
			particles[i].doDraw(canvas,offsetX,offsetY);
		}
	}

	public void updatePhysics(int time) {
		//if (count>2000) return;
		//count++;
		//if (count % 10!=0) return;
		for (int i = 0; i < particles.length; i++) {
			//Particle particle = particles[i];
			particles[i].updatePhysics(time);
		}
	}
	
	
	public boolean isActive(){
		return active;
	}
}