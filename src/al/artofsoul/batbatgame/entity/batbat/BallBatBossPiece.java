package al.artofsoul.batbatgame.entity.batbat;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import al.artofsoul.batbatgame.entity.MapObject;
import al.artofsoul.batbatgame.main.Game;
import al.artofsoul.batbatgame.tilemap.TileMap;

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
            Game.LOGGER.log(e.getMessage());
        }
    }

    public void update() {
        x += dx;
        y += dy;
        animation.update();
    }
}
