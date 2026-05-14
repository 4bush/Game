package game.mygame.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.mygame.Assets;
import game.mygame.GameManager;

public class GameOverState implements State {

    private BitmapFont font;
    private BitmapFont largeFont;

    @Override
    public void enter() {
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(1.5f);

        largeFont = new BitmapFont();
        largeFont.setColor(Color.RED);
        largeFont.getData().setScale(3f);
    }

    @Override
    public void update(float delta) {
        // No update logic needed for game over screen
    }

    @Override
    public void render(SpriteBatch batch) {
        if (batch == null) return;

        GameManager gm = GameManager.getInstance();

        // Draw background safely
        if (Assets.background != null) {
            batch.draw(Assets.background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        // Draw GAME OVER text
        if (largeFont != null) {
            largeFont.draw(batch, "GAME OVER",
                Gdx.graphics.getWidth() / 2f - 100, Gdx.graphics.getHeight() / 2f);
        }

        // Draw final score
        if (font != null && gm != null) {
            font.draw(batch, "Final Score: " + gm.getScore(),
                Gdx.graphics.getWidth() / 2f - 80, Gdx.graphics.getHeight() / 2f - 50);
        }
    }

    @Override
    public void exit() {
        if (font != null) {
            font.dispose();
        }
        if (largeFont != null) {
            largeFont.dispose();
        }
    }
}
