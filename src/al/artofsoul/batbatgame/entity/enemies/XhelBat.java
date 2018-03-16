package al.artofsoul.batbatgame.entity.enemies;

import java.awt.image.BufferedImage;

import al.artofsoul.batbatgame.entity.Enemy;
import al.artofsoul.batbatgame.entity.Player;
import al.artofsoul.batbatgame.handlers.Content;
import al.artofsoul.batbatgame.main.GamePanel;
import al.artofsoul.batbatgame.tilemap.TileMap;

/**
 * @author ArtOfSoul
 *
 */

public class XhelBat extends Enemy {
	
	private BufferedImage[] sprites;
	private Player player;
	private boolean active;
	
	public XhelBat(TileMap tm, Player p) {
		
		super(tm);
		player = p;

		setInitialHealth(1, 1);
		setDimension(25, 25, 20, 18);
		setEntityStats(1, 0.8, 0.15, 4.0);

		jumpStart = -5;
		sprites = Content.XHELBAT[0];
		setAnim(sprites, 4);
		
		left = true;
		facingRight = false;
		
	}

	@Override
	public void update() {

        super.update();

		if(!active) {
			if(Math.abs(player.getx() - x) < GamePanel.WIDTH) active = true;
			return;
		}
		
		getNextPosition();
		checkTileMapCollision();
		calculateCorners(x, ydest + 1);
		if(!bottomLeft) {
			left = false;
			right = facingRight = true;
		}
		if(!bottomRight) {
			left = true;
			right = facingRight = false;
		}
		setPosition(xtemp, ytemp);
		
		if(dx == 0) {
			left = !left;
			right = !right;
			facingRight = !facingRight;
		}
		
		// update animation
		animation.update();
		
	}
}
