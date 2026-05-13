package game.mygame.state;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface State {
    void enter();
    void update(float delta);
    void render(SpriteBatch batch);
    void exit();
}
