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

    public static void load() {

        manager.load("sprites/player/playerShip_blue.png", Texture.class);
        manager.load("background.png", Texture.class);
        manager.load("laserBlue01.png", Texture.class);
        manager.load("enemyRed1.png", Texture.class);
        manager.load("enemyBlack1.png", Texture.class);
        manager.load("enemyGreen1.png", Texture.class);

        manager.finishLoading();

        playerShip = manager.get("sprites/player/playerShip_blue.png", Texture.class);
        background = manager.get("background.png", Texture.class);
        laserBlue = manager.get("laserBlue01.png", Texture.class);
        enemyRed = manager.get("enemyRed1.png", Texture.class);
        enemyBlack = manager.get("enemyBlack1.png", Texture.class);
        enemyGreen = manager.get("enemyGreen1.png", Texture.class);
    }

    public static void dispose() {
        manager.dispose();
    }
}