package game.mygame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpaceShooter extends Game {

    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        GameManager.getInstance().init();
        setScreen(new GameScreen(this));
    }
    @Override
    public void dispose() {
        batch.dispose();
        super.dispose();
    }
}
