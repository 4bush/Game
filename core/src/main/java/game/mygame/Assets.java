package game.mygame;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Assets {

    public static AssetManager manager = new AssetManager();

    public static Texture playerShip;
    public static Texture background;
    public static Texture laserBlue;
    public static Texture enemyRed;
    public static Texture enemyBlack;
    public static Texture enemyGreen;
    public static Texture explosion;
    public static Texture bossTexture;
    public static void load() {

        manager.load("sprites/player/PlayerShip.png", Texture.class);
        manager.load("background.png", Texture.class);
        manager.load("laserBlue01.png", Texture.class);
        manager.load("sprites/enemies/enemy_tank.png", Texture.class);
        manager.load("sprites/enemies/enemy_fighter.png", Texture.class);
        manager.load("sprites/enemies/enemy_scout.png", Texture.class);
        manager.load("sprites/effects/explosion_1.png", Texture.class);
        manager.load("sprites/enemies/boss.png", Texture.class);
        manager.finishLoading();

        playerShip = manager.get("sprites/player/PlayerShip.png", Texture.class);
        background = manager.get("background.png", Texture.class);
        laserBlue = manager.get("laserBlue01.png", Texture.class);
        enemyRed = manager.get("sprites/enemies/enemy_tank.png", Texture.class);
        enemyBlack = manager.get("sprites/enemies/enemy_fighter.png", Texture.class);
        enemyGreen = manager.get("sprites/enemies/enemy_scout.png", Texture.class);
        explosion = manager.get("sprites/effects/explosion_1.png", Texture.class);
        bossTexture = manager.get("sprites/enemies/boss.png", Texture.class);
    }

    public static void dispose() {
        manager.dispose();
    }
}
