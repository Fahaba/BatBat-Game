package al.artofsoul.batbatgame.gamestate;

import al.artofsoul.batbatgame.handlers.Keys;
import al.artofsoul.batbatgame.main.Game;
import al.artofsoul.batbatgame.main.GamePanel;
import al.artofsoul.batbatgame.tileMap.Background;
import al.artofsoul.batbatgame.tileMap.TileMap;
import al.artofsoul.batbatgame.audio.JukeBox;
import al.artofsoul.batbatgame.entity.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


/**
 * @author ArtOfSoul
 *
 */

public abstract class GameState {

    public static final int LEVEL1STATE = 0;
    public static final int LEVEL2STATE = 1;
    public static final int LEVEL3STATE = 2;
    public static final int LEVEL4STATE = 3;

    protected static final int[][] LEVELTITLES = {
            {0, 0, 178, 19},
            {0, 0, 178, 20},
            {0, 0, 178, 20}
    };

    protected static final int[][] LEVELSUBTITLES = {
            {0, 20, 82, 13},
            {0, 33, 91, 13},
            {0, 33, 91, 13}
    };

    protected HUD hud;
    protected BufferedImage batBatStart;
    protected Title title;
    protected Title subtitle;
    protected Teleport teleport;

	protected GameStateManager gsm;
	protected Player player;
	protected TileMap tileMap;
	protected ArrayList<Enemy> enemies;
	protected ArrayList<EnergyParticle> energyParticles;
	protected ArrayList<Explosion> explosions;
	protected ArrayList<EnemyProjectile> eprojectiles;

    // events
    protected boolean blockInput;
    protected int eventCount = 0;
    protected boolean eventStart;
    protected ArrayList<Rectangle> tb;
    protected boolean eventFinish;
    protected boolean eventDead;
    protected boolean eventQuake;
    protected boolean eventPortal;
    protected boolean flash;
    protected boolean eventBossDead;

    // backgrounds
    protected Background sky = new Background("/Backgrounds/qielli.gif", 0);
    protected Background clouds = new Background("/Backgrounds/rete.gif", 0.1);
    protected Background mountains = new Background("/Backgrounds/mali.gif", 0.2);

    protected Background perendimi = new Background("/Backgrounds/perendimi.gif", 0.5, 0);
    protected Background mountains2 = new Background("/Backgrounds/mali2.gif", 0.2);

    protected Background temple = new Background("/Backgrounds/temple.gif", 0.5, 0);

    protected Portal portal;

    private int level;

    public GameState(GameStateManager gsm, int level) {
		this.gsm = gsm;
		this.level = level;
        blockInput = false;
        eventCount = 0;
		init(level);
	}

    public GameState(GameStateManager gsm) {
        this.gsm = gsm;
    }

	public void init(int level){

        // backgrounds
        sky = new Background("/Backgrounds/qielli.gif", 0);
        clouds = new Background("/Backgrounds/rete.gif", 0.1);
        mountains = new Background("/Backgrounds/mali.gif", 0.2);

        perendimi = new Background("/Backgrounds/perendimi.gif", 0.5, 0);
        mountains2 = new Background("/Backgrounds/mali2.gif", 0.2);

        temple = new Background("/Backgrounds/temple.gif", 0.5, 0);

        // tilemap
        tileMap = new TileMap(30);
        tileMap.loadTiles("/Tilesets/ruinstileset.gif");

        player = new Player(tileMap);
        player.setHealth(PlayerSave.getHealth());
        player.setLives(PlayerSave.getLives());
        player.setTime(PlayerSave.getTime());

        switch (level) {
            case 0:
                tileMap.loadMap("/Maps/level1.map");
                tileMap.setPosition(0, 120);
                tileMap.setBounds(
                        tileMap.getWidth() - 1 * tileMap.getTileSize(),
                        tileMap.getHeight() - 2 * tileMap.getTileSize(),
                        0, 0
                );
                player.setPosition(140, 191);

                // teleport
                teleport = new Teleport(tileMap);
                teleport.setPosition(3700, 131);

                break;
            case 1:
                tileMap.loadMap("/Maps/level2.map");
                tileMap.setPosition(140, 0);
                tileMap.setBounds(tileMap.getWidth() - 1 * tileMap.getTileSize(),
                        tileMap.getHeight() - 2 * tileMap.getTileSize(), 0, 0);
                player.setPosition(300, 161);

                // teleport
                teleport = new Teleport(tileMap);
                teleport.setPosition(3700, 131);

                break;
            case 2:
                tileMap.loadMap("/Maps/level3.map");
                tileMap.setPosition(140, 0);
                player.setPosition(300, 131);

                // teleport
                teleport = new Teleport(tileMap);
                teleport.setPosition(2850, 371);

                break;
            case 3:
                tileMap.loadMap("/Maps/level4.map");
                tileMap.setPosition(140, 0);
                player.setPosition(50, 190);

                // portal
                portal = new Portal(tileMap);
                portal.setPosition(160, 154);

                break;

            default:
                break;
        }
        tileMap.setTween(1);

        // enemies
        enemies = new ArrayList<>();
        eprojectiles = new ArrayList<>();

        // energy particle
        energyParticles = new ArrayList<>();

        // init player
        player.init(enemies, energyParticles);

        // explosions
        explosions = new ArrayList<>();

        // hud
        hud = new HUD(player);

        if (level != LEVEL4STATE) {
            // title and subtitle
            try {
                batBatStart = ImageIO.read(
                        getClass().getResourceAsStream("/HUD/batbat.gif")
                );
                title = new Title(batBatStart.getSubimage(LEVELTITLES[level][0],
                        LEVELTITLES[level][1], LEVELTITLES[level][2], LEVELTITLES[level][3]));
                title.sety(60);
                subtitle = new Title(batBatStart.getSubimage(LEVELSUBTITLES[level][0],
                        LEVELSUBTITLES[level][1], LEVELSUBTITLES[level][2], LEVELSUBTITLES[level][3]));
                subtitle.sety(85);
            } catch (Exception e) {
                Game.logger.log(e.getMessage());
            }
        }

        // sfx
        JukeBox.load("/SFX/teleport.mp3", "teleport");
        JukeBox.load("/SFX/explode.mp3", "explode");
        JukeBox.load("/SFX/enemyhit.mp3", "enemyhit");

    }
	public void update() {
        // check keys
        handleInput();

        // check if end of level event should start
        if (teleport != null && teleport.contains(player)) {
            eventFinish = blockInput = true;
        }

        // play events
        if (eventStart) eventStart();
        if (eventFinish) eventFinish();

        // move title and subtitle
        if (title != null) {
            title.update();
            if (title.shouldRemove()) title = null;
        }
        if (subtitle != null) {
            subtitle.update();
            if (subtitle.shouldRemove()) subtitle = null;
        }

        // update player
        player.update();
        if (player.getHealth() == 0 || player.gety() > tileMap.getHeight()) {
            eventDead = blockInput = true;
        }

        // update tilemap
        tileMap.setPosition(
                (double) GamePanel.WIDTH / 2 - player.getx(),
                (double)GamePanel.HEIGHT / 2 - player.gety()
        );
        tileMap.update();
        tileMap.fixBounds();

        // update enemies
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            e.update();
            if (e.isDead()) {
                enemies.remove(i);
                i--;
                explosions.add(new Explosion(tileMap, e.getx(), e.gety()));
            }
        }

        // update enemy projectiles
        for (int i = 0; i < eprojectiles.size(); i++) {
            EnemyProjectile ep = eprojectiles.get(i);
            ep.update();
            if (ep.shouldRemove()) {
                eprojectiles.remove(i);
                i--;
            }
        }

        // update explosions
        for (int i = 0; i < explosions.size(); i++) {
            explosions.get(i).update();
            if (explosions.get(i).shouldRemove()) {
                explosions.remove(i);
                i--;
            }
        }

        // update teleport
        if (teleport != null)
            teleport.update();

    }

    public void eventStart() {
        eventCount++;
        if (eventCount == 1) {
            tb.clear();
            tb.add(new Rectangle(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
            tb.add(new Rectangle(0, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));
            tb.add(new Rectangle(0, GamePanel.HEIGHT / 2, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
            tb.add(new Rectangle(GamePanel.WIDTH / 2, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));

            if (portal != null && !portal.isOpened()) {
                tileMap.setShaking(true, 10);
                JukeBox.stop("level1");
            }
        }
        if (eventCount > 1 && eventCount < 60) {
            tb.get(0).height -= 4;
            tb.get(1).width -= 6;
            tb.get(2).y += 4;
            tb.get(3).x += 6;
        }

        if (eventCount == 30 && title != null) title.begin();
        if (eventCount == 60) {
            eventStart = blockInput = false;
            eventCount = 0;
            if (level == LEVEL4STATE)
                eventPortal = blockInput = true;

            if (subtitle != null)
                subtitle.begin();

            tb.clear();
        }
    }

    // finished level
    private void eventFinish() {
        eventCount++;
        if (eventCount == 1) {
            JukeBox.play("teleport");
            player.setTeleporting(true);
            player.stop();
        } else if (eventCount == 120) {
            tb.clear();
            tb.add(new Rectangle(
                    GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2, 0, 0));
        } else if (eventCount > 120) {
            tb.get(0).x -= 6;
            tb.get(0).y -= 4;
            tb.get(0).width += 12;
            tb.get(0).height += 8;
            JukeBox.stop("teleport");
        }
        if (eventCount == 180) {
            PlayerSave.setHealth(player.getHealth());
            PlayerSave.setLives(player.getLives());
            PlayerSave.setTime(player.getTime());

            switch (level){
                case LEVEL1STATE:
                    gsm.setState(GameStateManager.LEVEL3STATE);
                    break;
                case LEVEL2STATE:
                    gsm.setState(GameStateManager.LEVEL4STATE);
                    break;
                case LEVEL3STATE:
                    gsm.setState(GameStateManager.LEVEL2STATE);
                    break;
                case LEVEL4STATE:
                    gsm.setState(GameStateManager.ACIDSTATE);
                    break;
                default:
                    break;
            }
        }

    }

	public void draw(Graphics2D g) {

        // draw tilemap
        tileMap.draw(g);

        // draw enemies
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).draw(g);
        }

        if (level != LEVEL4STATE) {
            // draw enemy projectiles
            for (int i = 0; i < eprojectiles.size(); i++) {
                eprojectiles.get(i).draw(g);
            }
        }
        // draw explosions
        for (int i = 0; i < explosions.size(); i++) {
            explosions.get(i).draw(g);
        }

        // draw player
        player.draw(g);

        // draw teleport
        if (teleport != null)
            teleport.draw(g);

        // draw hud
        hud.draw(g);

        // draw title
        if (title != null) title.draw(g);
        if (subtitle != null) subtitle.draw(g);

        // draw transition boxes
        g.setColor(java.awt.Color.BLACK);
        for (int i = 0; i < tb.size(); i++) {
            g.fill(tb.get(i));
        }

    }

    public void handleInput() {
        if (Keys.isPressed(Keys.ESCAPE)) gsm.setPaused(true);
        if (blockInput || player.getHealth() == 0) return;
        player.setUp(Keys.getKeyState(Keys.UP));
        player.setLeft(Keys.getKeyState(Keys.LEFT));
        player.setDown(Keys.getKeyState(Keys.DOWN));
        player.setRight(Keys.getKeyState(Keys.RIGHT));
        player.setJumping(Keys.getKeyState(Keys.BUTTON1));
        player.setDashing(Keys.getKeyState(Keys.BUTTON2));
        if (Keys.isPressed(Keys.BUTTON3)) player.setAttacking();
        if (Keys.isPressed(Keys.BUTTON4)) player.setCharging();
    }


}
