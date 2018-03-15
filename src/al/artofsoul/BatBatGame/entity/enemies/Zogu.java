package al.artofsoul.BatBatGame.entity.enemies;

import java.awt.image.BufferedImage;

import al.artofsoul.BatBatGame.entity.Enemy;
import al.artofsoul.BatBatGame.handlers.Content;
import al.artofsoul.BatBatGame.tileMap.TileMap;

/**
 * @author ArtOfSoul
 *
 */

public class Zogu extends Enemy {
	
	private BufferedImage[] idleSprites;
	
	private int tick;
	private double a;
	private double b;
	
	public Zogu(TileMap tm) {
		
		super(tm);

		setInitialHealth(2, 2);
		setDimension(39, 20, 25, 15);
		setEntityStats(1, 5, 0, 0);
		
		idleSprites = Content.ZOGU[0];
        setAnim(idleSprites, 4);
		
		tick = 0;
		a = Math.random() * 0.06 + 0.07;
		b = Math.random() * 0.06 + 0.07;
		
	}
	
	public void update() {

        super.update();

		tick++;
		x = Math.sin(a * tick) + x;
		y = Math.sin(b * tick) + y;
		
		// update animation
		animation.update();
		
	}
}
