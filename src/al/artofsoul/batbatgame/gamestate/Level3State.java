package al.artofsoul.batbatgame.gamestate;

import al.artofsoul.batbatgame.audio.JukeBox;
import al.artofsoul.batbatgame.entity.*;
import al.artofsoul.batbatgame.entity.enemies.Ufo;
import al.artofsoul.batbatgame.entity.enemies.XhelBat;
import al.artofsoul.batbatgame.entity.enemies.Zogu;
import al.artofsoul.batbatgame.handlers.Keys;
import al.artofsoul.batbatgame.main.GamePanel;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author ArtOfSoul
 */

public class Level3State extends GameState {

    private int[][] xelBatPos = {
            {750, 100},
            {900, 150},
            {1320, 250},
            {1570, 160},
            {1590, 160},
            {2600, 370},
            {2620, 370},
            {2640, 370}
    };

    private int[][] zoguPos = {
            {904, 130},
            {1080, 270},
            {1200, 270},
            {1704, 300}
    };

    private int[][] ufoPos = {
            {1900, 580},
            {2330, 550},
            {2400, 490},
            {2457, 430}
    };

    public Level3State(GameStateManager gsm) {
        super(gsm, LEVEL3STATE);
        init();
    }

    public void init() {

        populateEnemies();

        // start event
        eventStart = true;
        tb = new ArrayList<Rectangle>();
        eventStart();

        // music
        JukeBox.load("/Music/level1v2.mp3", "level2");
        JukeBox.loop("level2", 600, JukeBox.getFrames("level2") - 2200);

    }

    private void populateEnemies() {
        enemies.clear();
        XhelBat gp;
        Zogu g;
        Ufo t;

        for (int i = 0; i < xelBatPos.length; i++) {
            gp = new XhelBat(tileMap, player);
            gp.setPosition(xelBatPos[i][0], xelBatPos[i][1]);
            enemies.add(gp);
        }

        for (int i = 0; i < zoguPos.length; i++) {
            g = new Zogu(tileMap);
            g.setPosition(zoguPos[i][0], zoguPos[i][1]);
            enemies.add(g);
        }

        for (int i = 0; i < ufoPos.length; i++) {
            t = new Ufo(tileMap, player, enemies);
            t.setPosition(ufoPos[i][0], ufoPos[i][1]);
            enemies.add(t);
        }
    }

    public void update() {
        super.update();
        // check if quake event should start
        if (player.getx() > 2175 && !tileMap.isShaking()) {
            eventQuake = blockInput = true;
        }

        if (eventQuake) eventQuake();
        if (eventDead) eventDead();

        // move backgrounds
        temple.setPosition(tileMap.getx(), tileMap.gety());
    }

    public void draw(Graphics2D g) {
        // draw background
        temple.draw(g);

        super.draw(g);

    }

///////////////////////////////////////////////////////
//////////////////// EVENTS
///////////////////////////////////////////////////////

    // reset level
    private void reset() {
        player.loseLife();
        player.reset();
        player.setPosition(300, 131);
        populateEnemies();
        blockInput = true;
        eventCount = 0;
        tileMap.setShaking(false, 0);
        eventStart = true;
        eventStart();
        title = new Title(batBatStart.getSubimage(0, 0, 178, 20));
        title.sety(60);
        subtitle = new Title(batBatStart.getSubimage(0, 33, 91, 13));
        subtitle.sety(85);
    }

    // player has died
    private void eventDead() {
        eventCount++;
        if (eventCount == 1) player.setDead();
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
                reset();
            }
        }
    }

    // earthquake
    private void eventQuake() {
        eventCount++;
        if (eventCount == 1) {
            player.stop();
            player.setPosition(2175, player.gety());
        }
        if (eventCount == 60) {
            player.setEmote(Player.EMOTE_CONFUSED);
        }
        if (eventCount == 120) player.setEmote(Player.EMOTE_NONE);
        if (eventCount == 150) tileMap.setShaking(true, 10);
        if (eventCount == 180) player.setEmote(Player.EMOTE_SURPRISED);
        if (eventCount == 300) {
            player.setEmote(Player.EMOTE_NONE);
            eventQuake = blockInput = false;
            eventCount = 0;
        }
    }
}
