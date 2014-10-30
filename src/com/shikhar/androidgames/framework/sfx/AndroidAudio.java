package com.shikhar.androidgames.framework.sfx;

import java.io.IOException;

import com.shikhar.androidgames.framework.Audio;
import com.shikhar.androidgames.framework.Music;
import com.shikhar.androidgames.framework.Sound;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

public class AndroidAudio implements Audio {
	AssetManager assets;
	/**soundpool is used to manage audio resources from memory or from the file system.*/
	SoundPool soundPool;

	/**
	 * There are two reasons why we pass our game’s Activity in the constructor:
	 * it allows us to set the volume control of the media stream (we always
	 * want to do that), and it gives us an AssetManager instance, which we will
	 * happily store in the corresponding class member. The SoundPool is
	 * configured to play back 20 sound effects in parallel, which is adequate
	 * for our needs.
	 */
	public AndroidAudio(Activity activity) {
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		this.assets = activity.getAssets();
		this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
	}

	/**
	 * The newMusic() method creates a new AndroidMusic instance. The
	 * constructor of that class takes an AssetFileDescriptor, which it uses to
	 * create an internal MediaPlayer (more on that later). The
	 * AssetManager.openFd() method throws an IOException in case something goes
	 * wrong.Why not hand the IOException to the caller? First, it would clutter
	 * the calling code considerably, so we would rather throw a
	 * RuntimeException that does not have to be caught explicitly. Second, we
	 * load the music from an asset file. It will only fail if we actually
	 * forget to add the music file to the assets/directory, or if our music
	 * file contains false bytes. False bytes constitute unrecoverable errors
	 * since we need that Music instance for our game to function properly. To
	 * avoid such an occurrence, we throw RuntimeExceptions instead of checked
	 * exceptions in a few more places in the framework of our game.
	 */
	public Music createMusic(String filename) {
		try {
			AssetFileDescriptor assetDescriptor = assets.openFd(filename);
			return new AndroidMusic(assetDescriptor);
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load music '" + filename + "'");
		}
	}

	/**
	 * the newSound() method loads a sound effect from an asset into the
	 * SoundPool and returns an AndroidSound instance. The constructor of that
	 * instance takes a SoundPool and the ID of the sound effect assigned to it
	 * by the SoundPool. Again, we catch any IOException and rethrow it as an
	 * unchecked RuntimeException.
	 * 
	 */
	public Sound createSound(String filename) {
		try {
			AssetFileDescriptor assetDescriptor = assets.openFd(filename);
			int soundId = soundPool.load(assetDescriptor, 0);
			return new AndroidSound(soundPool, soundId);
		} catch (IOException e) {
			throw new RuntimeException("Couldn't load sound '" + filename + "'");
		}
	}

	/*
	 * We do not release the SoundPool in any of the methods. The reason for
	 * this is that there will always be a single Game instance holding a single
	 * Audio instance that holds a single SoundPool instance. The SoundPool
	 * instance will, thus, be alive as long as the activity (and with it our
	 * game) is alive. It will be destroyed automatically as soon as the
	 * activity ends.
	 */
}
