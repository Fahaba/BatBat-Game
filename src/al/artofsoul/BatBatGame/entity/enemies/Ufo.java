package al.artofsoul.BatBatGame.entity.enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import al.artofsoul.BatBatGame.entity.Enemy;
import al.artofsoul.BatBatGame.entity.Player;
import al.artofsoul.BatBatGame.Handlers.Content;
import al.artofsoul.BatBatGame.TileMap.TileMap;

/**
 * @author ArtOfSoul
 *
 */

public class Ufo extends Enemy {
	
	private Player player;
	private List<Enemy> enemies;
	
	private BufferedImage[] idleSprites;
	private BufferedImage[] jumpSprites;
	private BufferedImage[] attackSprites;

	private static final int IDLE = 0;
	private static final int JUMPING = 1;
	private static final int ATTACKING = 2;
	
	private int attackTick;
	private int attackDelay = 30;
	private int step;
	
	public Ufo(TileMap tm, Player p, List<Enemy> en) {
		
		super(tm);
		player = p;
		enemies = en;

		setInitialHealth(4, 4);
		setDimension(30, 30, 20, 26);
		setEntityStats(1, 1.5, 0.15, 4.0);
		jumpStart = -5;
		
		idleSprites = Content.UFO[0];
		jumpSprites = Content.UFO[1];
		attackSprites = Content.UFO[2];
        setAnim(idleSprites, -1);
		
		attackTick = 0;
		
	}

	public void update() {

        super.update();

		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// update animation
		animation.update();
		
		if(player.getx() < x) facingRight = false;
		else facingRight = true;

		switch (step) {
            // idle
            case 0: {
                if (currentAction != IDLE) {
                    currentAction = IDLE;
                    setAnim(idleSprites, -1);
                }
                attackTick++;
                if (attackTick >= attackDelay && Math.abs(player.getx() - x) < 60) {
                    step++;
                    attackTick = 0;
                }
            }
            // jump away
            case 1: {
                if (currentAction != JUMPING) {
                    currentAction = JUMPING;
                    setAnim(jumpSprites, -1);
                }
                jumping = true;
                if (facingRight) left = true;
                else right = true;
                if (falling) {
                    step++;
                }
            }
            // attack
            case 2: {
                if (dy > 0 && currentAction != ATTACKING) {
                    currentAction = ATTACKING;
                    setAnim(attackSprites, 3);
                    RedEnergy de = new RedEnergy(tileMap);
                    de.setPosition(x, y);
                    if (facingRight) de.setVector(3, 3);
                    else de.setVector(-3, 3);
                    enemies.add(de);
                }
                if (currentAction == ATTACKING && animation.hasPlayedOnce()) {
                    step++;
                    currentAction = JUMPING;
                    setAnim(jumpSprites, -1);
                }
            }
            // done attacking
            case 3: {
                if (dy == 0) step++;
            }
            // land
            case 4: {
                step = 0;
                left = right = jumping = false;
            }
        }
	}
}
