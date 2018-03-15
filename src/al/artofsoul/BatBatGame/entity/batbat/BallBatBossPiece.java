package al.artofsoul.BatBatGame.entity.batbat;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import al.artofsoul.BatBatGame.entity.MapObject;
import al.artofsoul.BatBatGame.tileMap.TileMap;

/**
 * @author ArtOfSoul
 *
 */

public class BallBatBossPiece extends MapObject {

    private BufferedImage[] sprites;

    public BallBatBossPiece(TileMap tm, int x, int y, int w, int h) {
        super(tm);
        try {
            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream("/Sprites/Other/ballBatBoss.gif")
            );
            sprites = new BufferedImage[1];
            width = height = 4;
            sprites[0] = spritesheet.getSubimage(x, y, w, h);
            animation.setFrames(sprites);
            animation.setDelay(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        x += dx;
        y += dy;
        animation.update();
    }
}
