package com.shikhar.androidgames.framework;

/**
 * A Screen interface for displaying/changing game screens.
 * This is an abstract class (not really an interface, which you 
 * cannot use to create objects with (although you can subclass it).. Some of these methods will be familiar, such as paint and update (they are named as they were in Units 2 and 3). They now have a parameter called deltaTime (which takes into account how much time passed 
 * since the last time the method was called) that we can use to create framerate independent movement (more on this later)
 * @author mahesh
 *
 */
public abstract class Screen {
	protected final Game game;

	/**
	 * <pre>
	 * The constructor receives the Game instance and stores it in a final
	 * member that’s accessible to all subclasses. Via this mechanism, we can
	 * achieve two things: 
	 * 
	 * 1. We can get access to the low-level modules of the
	 * Game interface to play back audio, draw to the screen, get user input,
	 * and read and write files. 
	 * 
	 * 2.  We can set a new current Screen by invoking
	 * Game.setScreen() when appropriate (for example, when a button is pressed
	 * that triggers a transition to a new screen).
	 * </pre>
	 * 
	 * @param game
	 */
	public Screen(Game game) {
		this.game = game;
	}

	/**
	 * updates screen, The Game instance will call it once in every iteration of
	 * the main loop.
	 * 
	 * @param deltaTime takes into account how much time passed since the last time the method was called) 
	 * that we can use to create framerate independent movement (more on this later)
	 */
	public abstract void update(float deltaTime);

	/**
	 * present screen (renders)The Game instance will call it once in every
	 * iteration of the main loop.
	 * 
	 * @param deltaTime deltaTime takes into account how much time passed since the last time the method was called) 
	 * that we can use to create framerate independent movement (more on this later)
	 */
	public abstract void paint(float deltaTime);

	/**
	 * THIS methods will be called when the game is paused o
	 */
	public abstract void pause();

	/**
	 * THIS methods will be called when the game is resumed
	 */
	public abstract void resume();

	/**
	 * method will be called by the Game instance in case Game.setScreen() is
	 * called. The Game instance will dispose of the current Screen via this
	 * method and thereby give the Screen an opportunity to release all its
	 * system resources (for example, graphical assets stored in Pixmaps) to
	 * make room for the new screen’s resources in memory. The call to the
	 * Screen.dispose() method is also the last opportunity for a screen to make
	 * sure that any information that needs persistence is saved.
	 */
	public abstract void dispose();
	
	
	/**
	 * Action performed on pressing back key when this screen is active (set as Current Screen of game)
	 */
	 public abstract void onBackPressed();
}
