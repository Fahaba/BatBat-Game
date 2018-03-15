package al.artofsoul.batbatgame.entity.enemies;

import java.awt.image.BufferedImage;

import al.artofsoul.batbatgame.entity.Enemy;
import al.artofsoul.batbatgame.handlers.Content;
import al.artofsoul.batbatgame.tileMap.TileMap;

/**
 * @author ArtOfSoul
 *
 */

public class RedEnergy extends Enemy {

    private BufferedImage[] startSprites;
    private BufferedImage[] sprites;

    private boolean start;
    private boolean permanent;

    private int type = 0;
    public static final int VECTOR = 0;
    public static final int GRAVITY = 1;
    public static final int BOUNCE = 2;

    private int bounceCount = 0;

    public RedEnergy(TileMap tm) {

        super(tm);

        setInitialHealth(1, 1);
        setDimension(20, 20, 12, 12);
        setEntityStats(1, 5, 0, 0);

        startSprites = Content.REDENERGY[0];
        sprites = Content.REDENERGY[1];
        setAnim(startSprites, 2);

        start = true;
        flinching = true;
        permanent = false;

    }

    public void setType(int i) {
        type = i;
    }

    public void setPermanent(boolean b) {
        permanent = b;
    }

    @Override
    public void update() {

        super.update();

        if (start && animation.hasPlayedOnce()) {
            animation.setFrames(sprites);
            animation.setNumFrames(3);
            animation.setDelay(2);
            start = false;
        }

        switch (type) {
            case VECTOR:
                x += dx;
                y += dy;
                break;
            case GRAVITY:
                dy += 0.2;
                x += dx;
                y += dy;
                break;
            case BOUNCE:
                double dx2 = dx;
                double dy2 = dy;
                checkTileMapCollision();
                if (dx == 0) {
                    dx = -dx2;
                    bounceCount++;
                }
                if (dy == 0) {
                    dy = -dy2;
                    bounceCount++;
                }
                x += dx;
                y += dy;
                break;
            default:
                break;
        }

        // update animation
        animation.update();

        if (!permanent) {
            if (x < 0 || x > tileMap.getWidth() || y < 0 || y > tileMap.getHeight()) {
                remove = true;
            }
            if (bounceCount == 3) {
                remove = true;
            }
        }

    }
}
