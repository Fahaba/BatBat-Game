package al.artofsoul.batbatgame.entity;

import java.awt.image.BufferedImage;

import al.artofsoul.batbatgame.handlers.Content;
import al.artofsoul.batbatgame.tilemap.TileMap;

/**
 * @author ArtOfSoul
 *
 */

public class EnergyParticle extends MapObject {
	
	private int count;
	private boolean remove;
	
	private BufferedImage[] sprites;
	
	public static final int DIR_UP = 0;
	public static final int DIR_LEFT = 1;
	public static final int DIR_DOWN = 2;
	public static final int DIR_RIGHT = 3;
	
	public EnergyParticle(TileMap tm, double x, double y, int dir) {
		super(tm);
		this.x = x;
		this.y = y;
		double d1 = Math.random() * 2.5 - 1.25;
		double d2 = -Math.random() - 0.8;

		switch (dir) {
            case DIR_UP:
			    dx = d1;
			    dy = d2;
			    break;
            case DIR_LEFT:
			    dx = d2;
			    dy = d1;
			    break;
            case DIR_DOWN:
			    dx = d1;
			    dy = -d2;
			    break;
            default:
			    dx = -d2;
			    dy = d1;
			    break;
		}
		
		count = 0;
		sprites = Content.ENERGYPARTICLE[0];
		animation.setFrames(sprites);
		animation.setDelay(-1);
	}
	
	public void update() {
		x += dx;
		y += dy;
		count++;
		if(count == 60) remove = true;
	}
	
	public boolean shouldRemove() { return remove; }

}
