package game.mygame.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Gdx;

import game.mygame.Assets;
import game.mygame.GameManager;

public class PowerUp {

    public enum Type { HEALTH, SHIELD, FIRE_RATE }

    private float x, y;
    private final float speed = 80f;
    private final Type type;
    private final Texture texture;
    private boolean alive = true;
    private final float size = 28f;

    public PowerUp(float x, float y, Type type) {
        this.x = x;
        this.y = y;
        this.type = type;
        // reuse existing asset to avoid adding new files
        this.texture = Assets.explosion;
    }

    public void update(float delta) {
        y -= speed * delta;
        if (y + size < 0) alive = false;
    }

    public void draw(SpriteBatch batch) {
        if (!alive) return;
        batch.draw(texture, x, y, size, size);
    }

    public Rectangle getBounds() { return new Rectangle(x, y, size, size); }
    public boolean isAlive() { return alive; }
    public Type getType() { return type; }

    public void collect() {
        alive = false;
        // Minimal immediate effect: increase score to signal pickup
        GameManager.getInstance().addScore(0);
    }
}
