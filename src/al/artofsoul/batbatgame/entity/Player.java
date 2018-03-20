package al.artofsoul.batbatgame.entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import al.artofsoul.batbatgame.audio.JukeBox;
import al.artofsoul.batbatgame.main.Game;
import al.artofsoul.batbatgame.tilemap.TileMap;

/**
 * @author ArtOfSoul
 *
 */

public class Player extends MapObject {
	
	// references
	private List<Enemy> enemies;
	
	// player stuff
	private int lives;
	private int health;
	private int maxHealth;
	private int damage;
	private int chargeDamage;
	private boolean knockback;
	private boolean flinching;
	private long flinchCount;
	private int score;
	private boolean doubleJump;
	private boolean alreadyDoubleJump;
	private double doubleJumpStart;
	private List<EnergyParticle> energyParticles;
	private long time;
	
	// actions
	private boolean dashing;
	private boolean attacking;
	private boolean upattacking;
	private boolean charging;
	private int chargingTick;
	private boolean teleporting;
	
	// animations
	private ArrayList<BufferedImage[]> sprites;
	private static final int[] NUMFRAMES = {
		1, 8, 5, 3, 3, 5, 3, 8, 2, 1, 3
	};
	private static final int[] FRAMEWIDTHS = {
		40, 40, 80, 40, 40, 40, 80, 40, 40, 40, 40
	};
	private static final int[] FRAMEHEIGHTS = {
		40, 40, 40, 40, 40, 80, 40, 40, 40, 40, 40
	};
	private static final int[] SPRITEDELAYS = {
		-1, 3, 2, 6, 5, 2, 2, 2, 1, -1, 1
	};
	
	private Rectangle ar;
	private Rectangle aur;
	private Rectangle cr;
	
	// animation actions
	private static final int ANIM_IDLE = 0;
	private static final int ANIM_WALKING = 1;
	private static final int ANIM_ATTACKING = 2;
	private static final int ANIM_JUMPING = 3;
	private static final int ANIM_FALLING = 4;
	private static final int ANIM_UPATTACKING = 5;
	private static final int ANIM_CHARGING = 6;
	private static final int ANIM_DASHING = 7;
	private static final int ANIM_KNOCKBACK = 8;
	private static final int ANIM_DEAD = 9;
	private static final int ANIM_TELEPORTING = 10;
	
	// emotes
	private BufferedImage confused;
	private BufferedImage surprised;
	public static final int EMOTE_NONE = 0;
	public static final int EMOTE_CONFUSED = 1;
	public static final int EMOTE_SURPRISED = 2;
	private int emote = EMOTE_NONE;
	
	public Player(TileMap tm) {
		
		super(tm);
		
		ar = new Rectangle(0, 0, 0, 0);
		ar.width = 30;
		ar.height = 20;
		aur = new Rectangle((int)x - 15, (int)y - 45, 30, 30);
		cr = new Rectangle(0, 0, 0, 0);
		cr.width = 50;
		cr.height = 40;
		
		width = 30;
		height = 30;
		cwidth = 15;
		cheight = 38;
		
		moveSpeed = 1.6;
		maxSpeed = 1.6;
		stopSpeed = 1.6;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		doubleJumpStart = -3;
		
		damage = 2;
		chargeDamage = 1;
		
		facingRight = true;
		
		lives = 3;
		health = maxHealth = 5;
		
		// load sprites
		try {
			
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Player/BatterySpirtes.gif"
				)
			);
			
			int count = 0;
			sprites = new ArrayList<>();
			for(int i = 0; i < NUMFRAMES.length; i++) {
				BufferedImage[] bi = new BufferedImage[NUMFRAMES[i]];
				for(int j = 0; j < NUMFRAMES[i]; j++) {
					bi[j] = spritesheet.getSubimage(
						j * FRAMEWIDTHS[i],
						count,
						FRAMEWIDTHS[i],
						FRAMEHEIGHTS[i]
					);
				}
				sprites.add(bi);
				count += FRAMEHEIGHTS[i];
			}
			
			// emotes
			spritesheet = ImageIO.read(getClass().getResourceAsStream(
				"/HUD/Emotes.gif"
			));
			confused = spritesheet.getSubimage(
				0, 0, 14, 17
			);
			surprised = spritesheet.getSubimage(
				14, 0, 14, 17
			);
			
		}
		catch(Exception e) {
            Game.LOGGER.log(e.getMessage());
		}
		
		energyParticles = new ArrayList<>();
		
		setAnimation(ANIM_IDLE);
		
		JukeBox.load("/SFX/playerjump.mp3", "playerjump");
		JukeBox.load("/SFX/playerlands.mp3", "playerlands");
		JukeBox.load("/SFX/playerattack.mp3", "playerattack");
		JukeBox.load("/SFX/playerhit.mp3", "playerhit");
		JukeBox.load("/SFX/playercharge.mp3", "playercharge");
		
	}
	
	public void init(
		List<Enemy> enemies,
		List<EnergyParticle> energyParticles) {
		this.enemies = enemies;
		this.energyParticles = energyParticles;
	}
	
	public int getHealth() { return health; }
	public int getMaxHealth() { return maxHealth; }
	
	public void setEmote(int i) {
		emote = i;
	}
	public void setTeleporting(boolean b) { teleporting = b; }

	@Override
	public void setJumping(boolean b) {
		if(knockback) return;
		if(b && !jumping && falling && !alreadyDoubleJump) {
			doubleJump = true;
		}
		jumping = b;
	}

	public void setAttacking() {
		if(knockback) return;
		if(charging) return;
		if(up && !attacking) upattacking = true;
		else attacking = true;
	}

	public void setCharging() {
		if(knockback) return;
		if(!attacking && !upattacking && !charging) {
			charging = true;
			JukeBox.play("playercharge");
			chargingTick = 0;
		}
	}

	public void setDashing(boolean b) {
		if(!b) dashing = false;
		else if(!falling) {
			dashing = true;
		}
	}
	public boolean isDashing() { return dashing; }
	
	public void setDead() {
		health = 0;
		stop();
	}
	
	public String getTimeToString() {
		int minutes = (int) (time / 3600);
		int seconds = (int) ((time % 3600) / 60);
		return seconds < 10 ? minutes + ":0" + seconds : minutes + ":" + seconds;
	}
	public long getTime() { return time; }
	public void setTime(long t) { time = t; }
	public void setHealth(int i) { health = i; }
	public void setLives(int i) { lives = i; }
	public void gainLife() { lives++; }
	public void loseLife() { lives--; }
	public int getLives() { return lives; }
	
	public void increaseScore(int score) {
		this.score += score; 
	}
	
	public int getScore() { return score; }
	
	public void hit(int damage) {
		if(flinching) return;
		JukeBox.play("playerhit");
		stop();
		health -= damage;
		if(health < 0) health = 0;
		flinching = true;
		flinchCount = 0;
		if(facingRight) dx = -1;
		else dx = 1;
		dy = -3;
		knockback = true;
		falling = true;
		jumping = false;
	}
	
	public void reset() {
		health = maxHealth;
		facingRight = true;
		currentAction = -1;
		stop();
	}
	
	public void stop() {
		left = right = up = down = flinching = 
			dashing = jumping = attacking = upattacking = charging = false;
	}

	private void setMovementPosition() {
		if(left) {
			dx -= tempSpeed;
			if(dx < -tempSpeed) {
				dx = -tempSpeed;
			}
		}
		else if(right) {
			dx += tempSpeed;
			if(dx > tempSpeed) {
				dx = tempSpeed;
			}
		}
		else {
			if(dx > 0) {
				dx -= stopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			}
			else if(dx < 0) {
				dx += stopSpeed;
				if(dx > 0) {
					dx = 0;
				}
			}
		}
	}

	private void setNoMoveOnAttackPosition() {
        if((attacking || upattacking || charging) &&
                !(jumping || falling)) {
            dx = 0;
        }
    }

    private void setChargingPosition() {
        if(charging) {
            chargingTick++;
            if(facingRight) dx = tempSpeed * (3 - chargingTick * 0.07);
            else dx = -tempSpeed * (3 - chargingTick * 0.07);
        }
    }

    private void setJumpingPosition() {
        if(jumping && !falling) {
            dy = jumpStart;
            falling = true;
            JukeBox.play("playerjump");
        }
    }

    private void handleDoubleJump() {
        if(doubleJump) {
            dy = doubleJumpStart;
            alreadyDoubleJump = true;
            doubleJump = false;
            JukeBox.play("playerjump");
            for(int i = 0; i < 6; i++) {
                energyParticles.add(
                        new EnergyParticle(
                                tileMap,
                                x,
                                y + (double)cheight / 4,
                                EnergyParticle.DIR_DOWN));
            }
        }

        if(!falling) alreadyDoubleJump = false;
    }

    private void handleFalling() {
        if(falling) {
            dy += fallSpeed;
            if(dy < 0 && !jumping) dy += stopJumpSpeed;
            if(dy > maxFallSpeed) dy = maxFallSpeed;
        }
    }

	private void getNextPosition() {
		
		if(knockback) {
			dy += fallSpeed * 2;
			if(!falling) knockback = false;
			return;
		}
		
		tempSpeed = this.maxSpeed;
		if(dashing) tempSpeed *= 1.75;
		
		// movement
		setMovementPosition();

		
		// cannot move while attacking, except in air
		setNoMoveOnAttackPosition();
		
		// charging
		setChargingPosition();

		// jumping
		setJumpingPosition();
		
		// double jump
        handleDoubleJump();
		
		// falling
		handleFalling();
		
	}
	
	private void setAnimation(int i) {
	    if (currentAction == i && currentAction != ANIM_IDLE)
	        return;

		currentAction = i;
		animation.setFrames(sprites.get(currentAction));
		animation.setDelay(SPRITEDELAYS[currentAction]);
		width = FRAMEWIDTHS[currentAction];
		height = FRAMEHEIGHTS[currentAction];
	}

	private void checkTeleporting() {
        // check teleporting
        if(teleporting) {
            energyParticles.add(
                    new EnergyParticle(tileMap, x, y, EnergyParticle.DIR_UP)
            );
        }
    }

    private void updatePosition() {
        // update position
        boolean isFalling = falling;
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
        if(isFalling && !falling) {
            JukeBox.play("playerlands");
        }
        if(dx == 0) x = (int)x;
    }

    private void checkDoneFlinching() {
        // check done flinching
        if(flinching) {
            flinchCount++;
            if(flinchCount > 120) {
                flinching = false;
            }
        }
    }

    private void updateEnergyParticles() {
        // energy particles
        for(int i = 0; i < energyParticles.size(); i++) {
            energyParticles.get(i).update();
            if(energyParticles.get(i).shouldRemove()) {
                energyParticles.remove(i);
                i--;
            }
        }
    }

    private void checkAttackFinished() {
        // check attack finished
        if((currentAction == ANIM_ATTACKING || currentAction == ANIM_UPATTACKING)
                && animation.hasPlayedOnce()) {
            attacking = false;
            upattacking = false;
        }
        if(currentAction == ANIM_CHARGING) {
            if(animation.hasPlayed(5)) {
                charging = false;
            }
            cr.y = (int)y - 20;
            if(facingRight) cr.x = (int)x - 15;
            else cr.x = (int)x - 35;
            if(facingRight)
                energyParticles.add(
                        new EnergyParticle(
                                tileMap,
                                x + 30,
                                y,
                                EnergyParticle.DIR_RIGHT));
            else
                energyParticles.add(
                        new EnergyParticle(
                                tileMap,
                                x - 30,
                                y,
                                EnergyParticle.DIR_LEFT));
        }
    }

    private void checkEnemyInteraction() {
        // check enemy interaction
        for(int i = 0; i < enemies.size(); i++) {

            Enemy e = enemies.get(i);

            // check attack
            if(currentAction == ANIM_ATTACKING && animation.getFrame() == 3
                    && animation.getCount() == 0 && e.intersects(ar)) {
                e.hit(damage);
            }

            // check upward attack
            if(currentAction == ANIM_UPATTACKING && animation.getFrame() == 3
                    && animation.getCount() == 0 && e.intersects(aur)) {
                e.hit(damage);
            }

            // check charging attack
            if(currentAction == ANIM_CHARGING && animation.getCount() == 0 && e.intersects(cr)) {
                e.hit(chargeDamage);
            }

            // collision with enemy
            if(!e.isDead() && intersects(e) && !charging) {
                hit(e.getDamage());
            }

            if(e.isDead()) {
                JukeBox.play("explode", 2000);
            }

        }
    }

    private void setAnimationByPriority() {
        // set animation, ordered by priority
        if(teleporting) {
            setAnimation(ANIM_TELEPORTING);
        }
        else if(knockback) {
            setAnimation(ANIM_KNOCKBACK);
        }
        else if(health == 0) {
            setAnimation(ANIM_DEAD);
        }
        else if(upattacking) {
            setAnimation(ANIM_UPATTACKING);

            if(currentAction != ANIM_UPATTACKING) {
                JukeBox.play("playerattack");
                aur.x = (int)x - 15;
                aur.y = (int)y - 50;
            }
            else {
                if(animation.getFrame() == 4 && animation.getCount() == 0) {
                    for(int c = 0; c < 3; c++) {
                        energyParticles.add(
                                new EnergyParticle(
                                        tileMap,
                                        aur.x + aur.width / 2,
                                        aur.y + 5,
                                        EnergyParticle.DIR_UP));
                    }
                }
            }
        }
        else if(attacking) {
            setAnimation(ANIM_ATTACKING);
            if(currentAction != ANIM_ATTACKING) {
                JukeBox.play("playerattack");
                ar.y = (int)y - 6;
                if(facingRight) ar.x = (int)x + 10;
                else ar.x = (int)x - 40;
            }
            else {
                if(animation.getFrame() == 4 && animation.getCount() == 0) {
                    for(int c = 0; c < 3; c++) {
                        if(facingRight)
                            energyParticles.add(
                                    new EnergyParticle(
                                            tileMap,
                                            ar.x + ar.width - 4,
                                            ar.y + ar.height / 2,
                                            EnergyParticle.DIR_RIGHT));
                        else
                            energyParticles.add(
                                    new EnergyParticle(
                                            tileMap,
                                            ar.x + 4,
                                            ar.y + ar.height / 2,
                                            EnergyParticle.DIR_LEFT));
                    }}
            }
        }
        else if(charging) {
            setAnimation(ANIM_CHARGING);
        }
        else if(dy < 0) {
            setAnimation(ANIM_JUMPING);
        }
        else if(dy > 0) {
            setAnimation(ANIM_FALLING);
        }
        else if(dashing && (left || right)) {
            setAnimation(ANIM_DASHING);
        }
        else if(left || right) {
            setAnimation(ANIM_WALKING);
        }
        else {
            setAnimation(ANIM_IDLE);
        }

        animation.update();
    }

	public void update() {
		
		time++;

		checkTeleporting();

		updatePosition();
		
		checkDoneFlinching();
		
		updateEnergyParticles();
		
		checkAttackFinished();
		
		checkEnemyInteraction();

        setAnimationByPriority();
		
		// set direction
		if(!attacking && !upattacking && !charging && !knockback) {
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
		
	}

	@Override
	public void draw(Graphics2D g) {
		
		// draw emote
		if(emote == EMOTE_CONFUSED) {
			g.drawImage(confused, (int)(x + xmap - (double)cwidth / 2), (int)(y + ymap - 40), null);
		}
		else if(emote == EMOTE_SURPRISED) {
			g.drawImage(surprised, (int)(x + xmap - (double)cwidth / 2), (int)(y + ymap - 40), null);
		}
		
		// draw energy particles
		for(int i = 0; i < energyParticles.size(); i++) {
			energyParticles.get(i).draw(g);
		}
		
		// flinch
		if(flinching && !knockback && (flinchCount % 10 < 5)) return;
		
		super.draw(g);
		
	}
	
}