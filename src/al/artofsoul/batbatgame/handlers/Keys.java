package al.artofsoul.batbatgame.handlers;

import java.awt.event.KeyEvent;

/**
 * @author ArtOfSoul
 *
 */

// this class contains a boolean array of current and previous key states
// for the 10 keys that are used for this game.
// a key k is down when keyState[k] is true.

public class Keys {
	
	public static final int NUM_KEYS = 16;
	
	private static boolean[] keyState = new boolean[NUM_KEYS];
	private static boolean[] prevKeyState = new boolean[NUM_KEYS];
	
	public static final int UP = 0;
	public static final int LEFT = 1;
	public static final int DOWN = 2;
	public static final int RIGHT = 3;
	public static final int BUTTON1 = 4;
	public static final int BUTTON2 = 5;
	public static final int BUTTON3 = 6;
	public static final int BUTTON4 = 7;
	public static final int ENTER = 8;
	public static final int ESCAPE = 9;


	private Keys() {
		throw new IllegalStateException("Utility class");
	}

	public static boolean getKeyState(int key){
	    return keyState[key];
    }

	public static void keySet(int i, boolean b) {
		if(i == KeyEvent.VK_UP) keyState[UP] = b;
		else if(i == KeyEvent.VK_LEFT) keyState[LEFT] = b;
		else if(i == KeyEvent.VK_DOWN) keyState[DOWN] = b;
		else if(i == KeyEvent.VK_RIGHT) keyState[RIGHT] = b;
		else if(i == KeyEvent.VK_W) keyState[BUTTON1] = b;
		else if(i == KeyEvent.VK_E) keyState[BUTTON2] = b;
		else if(i == KeyEvent.VK_R) keyState[BUTTON3] = b;
		else if(i == KeyEvent.VK_F) keyState[BUTTON4] = b;
		else if(i == KeyEvent.VK_ENTER) keyState[ENTER] = b;
		else if(i == KeyEvent.VK_ESCAPE) keyState[ESCAPE] = b;
	}
	
	public static void update() {
		for(int i = 0; i < NUM_KEYS; i++) {
			prevKeyState[i] = keyState[i];
		}
	}
	
	public static boolean isPressed(int i) {
		return keyState[i] && !prevKeyState[i];
	}
	
	public static boolean anyKeyPress() {
		for(int i = 0; i < NUM_KEYS; i++) {
			if(keyState[i]) return true;
		}
		return false;
	}
	
}
