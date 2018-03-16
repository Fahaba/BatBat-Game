package al.artofsoul.batbatgame.entity;

import al.artofsoul.batbatgame.audio.JukeBox;
import al.artofsoul.batbatgame.tilemap.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author ArtOfSoul
 *
 */

public class Enemy extends MapObject {
	
	protected int health;
	protected int maxHealth;
	protected boolean dead;
	protected int damage;
	protected boolean remove;
	
	protected boolean flinching;
	protected long flinchCount;
	
	public Enemy(TileMap tm) {
		super(tm);
		remove = false;
	}
	
	public boolean isDead() { return dead; }
	public boolean shouldRemove() { return remove; }
	
	public int getDamage() { return damage; }
	
	public void hit(int damage) {
		if(dead || flinching) return;
		JukeBox.play("enemyhit");
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		if(dead) remove = true;
		flinching = true;
		flinchCount = 0;
	}

	public void setDimension(int width, int height, int cwidth, int cheight) {
		this.width = width;
		this.height = height;
		this.cwidth = cwidth;
		this.cheight = cheight;
	}

	public void setInitialHealth(int health, int maxHealth) {
		this.health = health;
		this.maxHealth = maxHealth;
	}

	public void setEntityStats(int damage, double moveSpeed, double fallSpeed, double maxFallSpeed) {
		this.damage = damage;
		this.moveSpeed = moveSpeed;
		this.fallSpeed = fallSpeed;
		this.maxFallSpeed = maxFallSpeed;
	}

	public void setAnim(BufferedImage[] sprites, int delay) {
	    animation.setFrames(sprites);
	    animation.setDelay(delay);
    }

    public void getNextPosition() {
        if(left) dx = -moveSpeed;
        else if(right) dx = moveSpeed;
        else dx = 0;
        if(falling) {
            dy += fallSpeed;
            if(dy > maxFallSpeed) dy = maxFallSpeed;
        }
        if(jumping && !falling) {
            dy = jumpStart;
        }
    }

    @Override
    public void draw(Graphics2D g) {

        if(flinching && (flinchCount == 0 || flinchCount == 2))
            return;

        super.draw(g);

    }

    public void update() {

        // check if done flinching
        if(flinching) {
            flinchCount++;
            if(flinchCount == 6) flinching = false;
        }
    }
}














