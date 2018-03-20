package al.artofsoul.batbatgame.gamestate;

import al.artofsoul.batbatgame.audio.JukeBox;
import al.artofsoul.batbatgame.entity.*;
import al.artofsoul.batbatgame.entity.enemies.XhelBat;
import al.artofsoul.batbatgame.entity.enemies.Zogu;
import al.artofsoul.batbatgame.main.GamePanel;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author ArtOfSoul
 */

public class Level1State extends GameState {

    private int[][] xelBatPos = {
        {1300, 100},
        {1320, 100},
        {1340, 100},
        {1660, 100},
        {1680, 100},
        {1700, 100},
        {2177, 100},
        {2960, 100},
        {2980, 100},
        {3000, 100}
    };

    private int[][] zoguPos = {
            {2300, 100},
            {3500, 100}
    };

    private int[][] ufoPos = {
        // not used
    };

    public Level1State(GameStateManager gsm) {
        super(gsm, LEVEL1STATE);
        init();
    }

    public void init() {

        populateEnemies();

        // start event
        eventStart = true;
        tb = new ArrayList<>();
        eventStart();

        // music
        JukeBox.load("/Music/level1.mp3", LEVEL1MUSIC);
        JukeBox.loop(LEVEL1MUSIC, 600, JukeBox.getFrames(LEVEL1MUSIC) - 2200);
    }

    private void populateEnemies() {
        populateCommonEnemies(xelBatPos, zoguPos, ufoPos);
    }

    @Override
    public void update() {

        super.update();
        // move backgrounds
        clouds.setPosition(tileMap.getx(), tileMap.gety());
        mountains2.setPosition(tileMap.getx(), tileMap.gety());

        if (eventDead) eventDead();

    }

    @Override
    public void draw(Graphics2D g) {

        // draw background
        sky.draw(g);
        clouds.draw(g);
        mountains.draw(g);

        super.draw(g);

    }

///////////////////////////////////////////////////////
//////////////////// EVENTS
///////////////////////////////////////////////////////

    // player has died
    private void eventDead() {
        eventCount++;
        if (eventCount == 1) {
            player.setDead();
            player.stop();
        }
        if (eventCount == 60) {
            tb.clear();
            tb.add(new Rectangle(
                    GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2, 0, 0));
        } else if (eventCount > 60) {
            tb.get(0).x -= 6;
            tb.get(0).y -= 4;
            tb.get(0).width += 12;
            tb.get(0).height += 8;
        }
        if (eventCount >= 120) {
            if (player.getLives() == 0) {
                gsm.setState(GameStateManager.MENUSTATE);
            } else {
                eventDead = blockInput = false;
                eventCount = 0;
                player.loseLife();
                reset();
            }
        }
    }

    // reset level
    private void reset() {
        super.reset(140, 191);
        populateEnemies();
    }
}
