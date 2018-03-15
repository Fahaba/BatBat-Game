package al.artofsoul.BatBatGame.gamestate;

import al.artofsoul.BatBatGame.tileMap.Background;
import al.artofsoul.BatBatGame.tileMap.TileMap;
import al.artofsoul.BatBatGame.audio.JukeBox;
import al.artofsoul.BatBatGame.entity.*;

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

    public static final int[][] LEVELTITLES = {
            {0, 0, 178, 19},
            {0, 0, 178, 20},
            {0, 0, 178, 20}
    };

    public static final int[][] LEVELSUBTITLES = {
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
    public boolean blockInput;
    public int eventCount = 0;
    public boolean eventStart;
    public ArrayList<Rectangle> tb;
    public boolean eventFinish;
    public boolean eventDead;
    public boolean eventQuake;
    public boolean eventPortal;
    public boolean flash;
    public boolean eventBossDead;

    // backgrounds
    protected Background sky = new Background("/Backgrounds/qielli.gif", 0);
    protected Background clouds = new Background("/Backgrounds/rete.gif", 0.1);
    protected Background mountains = new Background("/Backgrounds/mali.gif", 0.2);

    protected Background perendimi = new Background("/Backgrounds/perendimi.gif", 0.5, 0);
    protected Background mountains2 = new Background("/Backgrounds/mali2.gif", 0.2);

    protected Background temple = new Background("/Backgrounds/temple.gif", 0.5, 0);

    protected Portal portal;


    public GameState(GameStateManager gsm, int level) {
		this.gsm = gsm;
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
                e.printStackTrace();
            }
        }

        // sfx
        JukeBox.load("/SFX/teleport.mp3", "teleport");
        JukeBox.load("/SFX/explode.mp3", "explode");
        JukeBox.load("/SFX/enemyhit.mp3", "enemyhit");

    }
	public abstract void update();
	public abstract void draw(Graphics2D g);
	public abstract void handleInput();


}
