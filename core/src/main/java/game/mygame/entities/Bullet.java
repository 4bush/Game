package game.mygame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Bullet {

    private float x, y;
    private final float speed;
    private final Texture texture;
    private boolean alive = true;

    private static final float W = 8f, H = 20f;

    public Bullet(float x, float y, float speed, Texture texture) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.texture = texture;
    }

    public void update(float delta) {
        y += speed * delta;
        if (y > Gdx.graphics.getHeight()) alive = false;
    }

    public void draw(SpriteBatch batch) {
        if (alive) batch.draw(texture, x, y, W, H);
    }

    public void destroy()        { alive = false; }
    public boolean isAlive()     { return alive; }
    public Rectangle getBounds() { return new Rectangle(x, y, W, H); }
}
