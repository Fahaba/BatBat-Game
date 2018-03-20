package al.artofsoul.batbatgame.gamestate;

import al.artofsoul.batbatgame.audio.JukeBox;
import al.artofsoul.batbatgame.handlers.Keys;
import al.artofsoul.batbatgame.main.Game;
import al.artofsoul.batbatgame.main.GamePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author ArtOfSoul
 */

public class OptionsState extends GameState {

    final ImageIcon howTo = new ImageIcon(getClass().getResource("/Backgrounds/howTo.gif"));

    private String[] options = {
            "HowTo Play",
            "Back"
    };

    public OptionsState(GameStateManager gsm) {
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

        g.drawString("HowTo Play", 140, 133);
        g.drawString("Back", 140, 148);

        // draw floating head
        if (currentChoice == 0) g.drawImage(head, 120, 123, null);
        else if (currentChoice == 1) g.drawImage(head, 120, 138, null);


        // other
        g.setFont(font2);
        g.drawString("2017 � toni kolaba", 10, 232);

    }


    public void siLuhet() {
        JOptionPane.showMessageDialog(null, "", "HowTo Paly?", JOptionPane.INFORMATION_MESSAGE, howTo);
    }


    private void select() {
        switch (currentChoice) {
            case 0:
                JukeBox.play(MENUSELECTFX);
                siLuhet();
                break;
            case 1:
                gsm.setState(GameStateManager.MENUSTATE);
                break;
            default:
                break;
        }
    }

    @Override
    public void handleInput() {
        if (Keys.isPressed(Keys.ENTER)) select();
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










