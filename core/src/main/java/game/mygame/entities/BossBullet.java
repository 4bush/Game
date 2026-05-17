package game.mygame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class BossBullet {

    private float x, y;
    private final float vx, vy;
    private final float size;
    private final Texture texture;
    private boolean alive = true;

    public BossBullet(float x, float y, float vx, float vy, float size, Texture texture) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.size = size;
        this.texture = texture;
    }

    public void update(float delta) {
        x += vx * delta;
        y += vy * delta;
        if (x < -size || x > Gdx.graphics.getWidth() + size || y < -size || y > Gdx.graphics.getHeight() + size) {
            alive = false;
        }
    }

    public void draw(SpriteBatch batch) {
        if (!alive) return;
        batch.draw(texture, x - size / 2f, y - size / 2f, size, size);
    }

    public void destroy() { alive = false; }
    public boolean isAlive() { return alive; }
    public Rectangle getBounds() { return new Rectangle(x - size / 2f, y - size / 2f, size, size); }
}
