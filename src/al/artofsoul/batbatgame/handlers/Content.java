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
