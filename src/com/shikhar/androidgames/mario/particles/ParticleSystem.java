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

		for (int i = 0; i < particles.length; i++) {
			particles[i] = new Particle(startXPos, startYPos, bitmap);
		}
	}

	public void doDraw(Canvas canvas,int offsetX,int offsetY) {
		if (count>500) return;
		for (int i = 0; i < particles.length; i++) {
			Particle particle = particles[i];
			particle.doDraw(canvas,offsetX,offsetY);
		}
	}

	public void updatePhysics(int altDelta) {
		if (count>500) return;
		count++;
		for (int i = 0; i < particles.length; i++) {
			Particle particle = particles[i];
			particle.updatePhysics(altDelta);

		}
	}
	
	
	public boolean isActive(){
		return active;
	}
}