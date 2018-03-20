package al.artofsoul.batbatgame.gamestate;

import al.artofsoul.batbatgame.audio.JukeBox;
import al.artofsoul.batbatgame.entity.PlayerSave;
import al.artofsoul.batbatgame.handlers.Keys;
import java.awt.*;

/**
 * @author ArtOfSoul
 *
 */

public class MenuState extends GameState {

	private String[] options = { "Play", "Options", "Quit" };

	public MenuState(GameStateManager gsm) {
		super(gsm);
	}

	public void init() {
	    // empty
    }

    @Override
	public void update() {
		// check keys
		handleInput();
	}

	@Override
	public void draw(Graphics2D g) {
		// draw bg
		g.drawImage(bg, 0, 0, null);
		// draw menu options
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString("Play", 140, 133);
		g.drawString("Options", 140, 148);
		g.drawString("Quit", 140, 163);
		// draw floating head
		if (currentChoice == 0)
			g.drawImage(head, 120, 123, null);
		else if (currentChoice == 1)
			g.drawImage(head, 120, 138, null);
		else if (currentChoice == 2)
			g.drawImage(head, 120, 153, null);
		// other
		g.setFont(font2);
		g.drawString("2017 � toni kolaba", 10, 232);
	}

	private void select() {
		switch (currentChoice) {
		case 0:
			JukeBox.play(MENUSELECTFX);
			PlayerSave.init();
			gsm.setState(GameStateManager.LEVEL1STATE); /// start this level entrance
            break;
		case 1:
			gsm.setState(GameStateManager.OPTIONSSTATE);
			break;
		case 2:
			System.exit(0);
			break;
		default:
			break;
		}

	}

	@Override
	public void handleInput() {
		if (Keys.isPressed(Keys.ENTER))
			select();
		if (Keys.isPressed(Keys.UP)) {
			if (currentChoice > 0) {
				JukeBox.play(MENUOPTIONFX, 0);
				currentChoice--;
			}
		}
		if (Keys.isPressed(Keys.DOWN)) {
			if (currentChoice < options.length - 1) {
				JukeBox.play(MENUOPTIONFX, 0);
				currentChoice++;
			}
		}
	}
}
