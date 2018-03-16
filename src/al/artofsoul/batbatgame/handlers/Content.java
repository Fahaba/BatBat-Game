package al.artofsoul.batbatgame.handlers;

import al.artofsoul.batbatgame.main.Game;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * @author ArtOfSoul
 *
 */

// this class loads resources on boot.
// spritesheets are taken from here.

public class Content {

	public static final BufferedImage[][] ENERGYPARTICLE = load("/Sprites/Player/ENERGYPARTICLE.gif", 5, 5);
	public static final BufferedImage[][] EXPLOSION = load("/Sprites/Enemies/ExplosionRed.gif", 30, 30);

	public static final BufferedImage[][] ZOGU = load("/Sprites/Enemies/ZOGU.gif", 39, 20);
	public static final BufferedImage[][] UFO = load("/Sprites/Enemies/UFO.gif", 30, 30);
	public static final BufferedImage[][] XHELBAT = load("/Sprites/Enemies/XHELBAT.gif", 25, 25);
	public static final BufferedImage[][] REDENERGY = load("/Sprites/Enemies/REDENERGY.gif", 20, 20);

	private Content() {
		throw new IllegalStateException("Utility class");
	}

	public static BufferedImage[][] load(String s, int w, int h) {
		BufferedImage[][] ret;
		try {
			BufferedImage spritesheet = ImageIO.read(Content.class.getResourceAsStream(s));
			int width = spritesheet.getWidth() / w;
			int height = spritesheet.getHeight() / h;
			ret = new BufferedImage[height][width];
			for(int i = 0; i < height; i++) {
				for(int j = 0; j < width; j++) {
					ret[i][j] = spritesheet.getSubimage(j * w, i * h, w, h);
				}
			}
			return ret;
		}
		catch(Exception e) {
			Game.LOGGER.log(e.getMessage());
			Game.LOGGER.log("Error loading graphics.");
			System.exit(0);
		}
		return new BufferedImage[0][0];
	}
	
}
