package game.mygame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import game.mygame.Assets;
import game.mygame.weapon.WeaponStrategy;
import game.mygame.weapon.SingleShotStrategy;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private float x, y;
    private final float speed = 300f;
    private final Sprite sprite;

    private final Texture bulletTexture;

    private float shootCooldown = 0f;

    private final List<Bullet> bullets = new ArrayList<>();

    private WeaponStrategy currentStrategy;
    private boolean shieldActive = false;
    private float fireRateMultiplier = 1f;
    private float fireRateTimer = 0f;

    public Player(float startX, float startY) {
        this.x = startX;
        this.y = startY;

        this.sprite = new Sprite(Assets.playerShip);
        this.sprite.setSize(70, 80);
        this.sprite.setPosition(x, y);

        this.bulletTexture = Assets.manager.get("laserBlue01.png", com.badlogic.gdx.graphics.Texture.class);

        // Инициализировать стратегию по умолчанию
        this.currentStrategy = new SingleShotStrategy();
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
        if (fireRateTimer > 0f) {
            fireRateTimer -= delta;
            if (fireRateTimer <= 0f) {
                fireRateMultiplier = 1f;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && shootCooldown <= 0) {
            // Использовать текущую стратегию для стрельбы
            List<Bullet> newBullets = currentStrategy.shoot(x, y, sprite.getWidth(), sprite.getHeight(), bulletTexture);
            bullets.addAll(newBullets);
            shootCooldown = getEffectiveShootDelay();
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

    /**
     * Установить новую стратегию вооружения.
     */
    public void setWeaponStrategy(WeaponStrategy strategy) {
        if (strategy != null) {
            this.currentStrategy = strategy;
            this.shootCooldown = 0; // Сбросить кулдаун при смене стратегии
        }
    }

    public boolean hasShield() {
        return shieldActive;
    }

    public void activateShield() {
        shieldActive = true;
    }

    public void consumeShield() {
        shieldActive = false;
    }

    public void applyFireRateBoost(float multiplier, float duration) {
        fireRateMultiplier = Math.min(fireRateMultiplier, multiplier);
        fireRateTimer = Math.max(fireRateTimer, duration);
    }

    private float getEffectiveShootDelay() {
        return Math.max(0.05f, currentStrategy.getShootDelay() * fireRateMultiplier);
    }

    /**
     * Получить текущую стратегию вооружения.
     */
    public WeaponStrategy getCurrentStrategy() {
        return currentStrategy;
    }
}
