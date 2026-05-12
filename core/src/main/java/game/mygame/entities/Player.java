package game.mygame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import game.mygame.Assets;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private float x, y;
    private final float speed = 300f;
    private final Sprite sprite;

    private final Texture bulletTexture;

    private float shootCooldown = 0f;
    private static final float SHOOT_DELAY = 0.25f;

    private final List<Bullet> bullets = new ArrayList<>();

    public Player(float startX, float startY) {
        this.x = startX;
        this.y = startY;

        this.sprite = new Sprite(Assets.playerShip);
        this.sprite.setSize(70, 80);
        this.sprite.setPosition(x, y);

        this.bulletTexture = Assets.manager.get("laserBlue01.png", com.badlogic.gdx.graphics.Texture.class);
    }

    public void update(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            x -= speed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            x += speed * delta;
        }

        x = Math.max(0, Math.min(Gdx.graphics.getWidth() - sprite.getWidth(), x));

        sprite.setPosition(x, y);

        shootCooldown -= delta;
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && shootCooldown <= 0) {
            bullets.add(new Bullet(x + sprite.getWidth() / 2 - 4, y + sprite.getHeight(), 500f, bulletTexture));
            shootCooldown = SHOOT_DELAY;
        }

        for (Bullet b : bullets) b.update(delta);
        bullets.removeIf(b -> !b.isAlive());
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
        for (Bullet b : bullets) b.draw(batch);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, sprite.getWidth(), sprite.getHeight());
    }

    public List<Bullet> getBullets() { return bullets; }
    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return sprite.getWidth(); }
    public float getHeight() { return sprite.getHeight(); }
}