package com.shikhar.androidgames.mario.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.shikhar.androidgames.framework.FileIO;


public class Settings {
    public static boolean soundEnabled = true;
    public static boolean musicEnabled = true;
    
    public final static int[] highscores = new int[] { 100, 80, 50, 30, 10 };
    public final static String file = ".supermario";

    private static int score=0;
	private static int lives=0;
	
	public static final int COIN_BONUS=100;
	public static final int GOOMBA_BONUS=300;
	public static final int KOOPA_BONUS=500;
	public static final int LIFE_BONUS=1000;

    public static void load(FileIO files) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(files.readFile(file)));
            soundEnabled = Boolean.parseBoolean(in.readLine());
            musicEnabled = Boolean.parseBoolean(in.readLine());
          
            for(int i = 0; i < 5; i++) {
                highscores[i] = Integer.parseInt(in.readLine());
            }
        } catch (IOException e) {
            // :( It's ok we have defaults
        } catch (NumberFormatException e) {
            // :/ It's ok, defaults save our day
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
            }
        }
    }

    public static void save(FileIO files) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    files.writeFile(file)));
            out.write(Boolean.toString(soundEnabled));
            out.write("\n");
            out.write(Boolean.toString(musicEnabled));
            out.write("\n");
            
            for(int i = 0; i < 5; i++) {
                out.write(Integer.toString(highscores[i]));
                out.write("\n");
            }

        } catch (IOException e) {
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
            }
        }
    }

    public static int getLives() {
		return lives;
	}
    
    
    public static int getScore() {
		return score;
	}
    
	public static void addScore(int newScore) {
		score += newScore;
	}

	public static void addLife() {
		if (lives<3)lives ++;
	}

	public static void subtractLife() {
		if (lives>0)lives --;
	}
	
	public static void addHighScore(int newHighScore) {

		for (int i = 0; i < 5; i++) {
			if (highscores[i] < score) {
				for (int j = 4; j > i; j--)
					highscores[j] = highscores[j - 1];
				highscores[i] = score;
				break;
			}
		}

	}
    
   
}
