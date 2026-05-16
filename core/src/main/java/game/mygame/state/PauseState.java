package game.mygame.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input;

import game.mygame.Assets;

public class PauseState implements State {

    private BitmapFont titleFont;
    private BitmapFont menuFont;

    @Override
    public void enter() {
        titleFont = new BitmapFont();
        titleFont.setColor(Color.YELLOW);
        titleFont.getData().setScale(4f);

        menuFont = new BitmapFont();
        menuFont.setColor(Color.WHITE);
        menuFont.getData().setScale(2f);
    }

    @Override
    public void update(float delta) {
        // No update logic needed for pause screen
    }

    @Override
    public void render(SpriteBatch batch) {
        if (batch == null) return;

        // Draw background
        if (Assets.background != null) {
            batch.draw(Assets.background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        // Draw pause title
        if (titleFont != null) {
            titleFont.draw(batch, "PAUSED",
                Gdx.graphics.getWidth() / 2f - 100, Gdx.graphics.getHeight() / 2f + 100);
        }

        // Draw menu
        if (menuFont != null) {
            menuFont.draw(batch, "Press SPACE or ENTER to Resume",
                Gdx.graphics.getWidth() / 2f - 200, Gdx.graphics.getHeight() / 2f - 50);
            menuFont.draw(batch, "Press ESC to Exit",
                Gdx.graphics.getWidth() / 2f - 120, Gdx.graphics.getHeight() / 2f - 100);
        }
    }

    @Override
    public void exit() {
        if (titleFont != null) {
            titleFont.dispose();
        }
        if (menuFont != null) {
            menuFont.dispose();
        }
    }
}
