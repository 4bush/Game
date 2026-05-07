package game.mygame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private float x, y;
    private final float speed = 300f;
    private final float width, height;
    private final Texture texture;
    private final Texture bulletTexture;

    private float shootCooldown = 0f;
    private static final float SHOOT_DELAY = 0.3f;

    private final List<Bullet> bullets = new ArrayList<>();

    public Player(float startX, float startY, Texture texture, Texture bulletTexture) {
        this.x = startX;
        this.y = startY;
        this.texture = texture;
        this.bulletTexture = bulletTexture;
        this.width = texture.getWidth();
        this.height = texture.getHeight();
    }

    public void update(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            x -= speed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            x += speed * delta;
        }

        x = Math.max(0, Math.min(Gdx.graphics.getWidth() - width, x));


        shootCooldown -= delta;
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && shootCooldown <= 0) {
            bullets.add(new Bullet(x + width / 2 - 4, y + height, 500f, bulletTexture));
            shootCooldown = SHOOT_DELAY;
        }

        for (Bullet b : bullets) b.update(delta);
        bullets.removeIf(b -> !b.isAlive());
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
        for (Bullet b : bullets) b.draw(batch);
    }

    public Rectangle getBounds() { return new Rectangle(x, y, width, height); }
    public List<Bullet> getBullets() { return bullets; }
    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
}
