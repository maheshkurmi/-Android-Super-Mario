package com.shikhar.androidgames.framework;

/**
 * An Audio Interface that relies on  a Music interface (for large audio files) and a Sound interface (for small audio files)
 * @author mahesh
 *
 */
public interface Audio {
	public Music createMusic(String filename);

	public Sound createSound(String filename);
}