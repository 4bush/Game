package game.mygame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import game.mygame.GameManager;
import game.mygame.observer.GameEvent;

import java.util.ArrayList;
import java.util.List;

public class Boss extends Enemy {
    protected int maxHealth;
    protected BossPhase currentPhase;
    private float movementTimer = 0f;
    private float attackTimer = 0f;
    private boolean movingRight = true;
    private final float targetY;

    private final float width;
    private final float height;

    private final List<BossBullet> bullets = new ArrayList<>();

    public enum BossPhase {
        ENTRY,
        PHASE_1,
        PHASE_2,
        PHASE_3,
        DEFEATED
    }

    public Boss(float x, float y, int health, int scoreValue, Texture texture) {
        super(x, y, 100f, health, scoreValue, texture);
        this.maxHealth = health;
        this.currentPhase = BossPhase.ENTRY;
        // lower final position so boss is more visible on screen
        this.targetY = Gdx.graphics.getHeight() - 220f;
        // scale boss down a bit so it's not huge
        this.width = texture.getWidth() * 0.8f;
        this.height = texture.getHeight() * 0.8f;
    }

    @Override
    public void update(float delta) {
        // keep default behavior if called without player info
        update(delta, 0f, 0f);
    }

    // New update that accepts player position to aim bullets at the player
    public void update(float delta, float playerCenterX, float playerCenterY) {
        if (!alive) return;

        movementTimer += delta;
        attackTimer += delta;

        switch (currentPhase) {
            case ENTRY:
                if (y > targetY) {
                    y -= 60f * delta; // slower entry so it's visible
                } else {
                    y = targetY;
                    currentPhase = BossPhase.PHASE_1;
                }
                break;

            case PHASE_1:
                moveHorizontally(delta, 100f);
                // easy phase: slow, single big shot occasionally
                handleShooting(playerCenterX, playerCenterY, 2.5f, 180f, 36f, 1);
                break;

            case PHASE_2:
                moveHorizontally(delta, 160f);
                y = targetY + MathUtils.sin(movementTimer * 3f) * 40f;
                // medium: two-shot spread, faster rate
                handleShooting(playerCenterX, playerCenterY, 1.2f, 240f, 28f, 2);
                break;

            case PHASE_3:
                moveHorizontally(delta, 220f);
                // aggressive: triple spread, fast rate, occasional dive-and-reset behavior removed in favor of continuous shooting
                handleShooting(playerCenterX, playerCenterY, 0.6f, 320f, 22f, 3);
                break;

            case DEFEATED:
                alive = false;
                break;
        }

        // update boss bullets
        for (BossBullet b : bullets) b.update(delta);
        bullets.removeIf(b -> !b.isAlive());
    }

    private void handleShooting(float playerCenterX, float playerCenterY, float interval, float bulletSpeed, float size, int count) {
        if (attackTimer < interval) return;
        attackTimer = 0f;

        // spawn `count` bullets in a spread aimed roughly at the player
        float centerX = x + width / 2f;
        float centerY = y + height / 2f;

        float baseDx = playerCenterX - centerX;
        float baseDy = playerCenterY - centerY;
        float baseLen = (float) Math.sqrt(baseDx * baseDx + baseDy * baseDy);
        if (baseLen < 1f) baseLen = 1f;

        for (int i = 0; i < count; i++) {
            // spread angle
            float spread = (i - (count - 1) / 2f) * 0.18f; // radians offset
            float dirX = baseDx / baseLen;
            float dirY = baseDy / baseLen;
            // rotate by spread
            float cos = (float) Math.cos(spread);
            float sin = (float) Math.sin(spread);
            float rx = dirX * cos - dirY * sin;
            float ry = dirX * sin + dirY * cos;
            float vx = rx * bulletSpeed;
            float vy = ry * bulletSpeed;
            BossBullet bb = new BossBullet(centerX, centerY, vx, vy, size, com.badlogic.gdx.Gdx.app.getApplicationListener() == null ? null : null);
            // Use Assets texture directly to avoid extra dependency here; will set texture after creation if null
            // but safer to use Assets.explosion; use fully qualified access at runtime
            bullets.add(new BossBullet(centerX, centerY, vx, vy, size, game.mygame.Assets.explosion));
        }
    }

    private void moveHorizontally(float delta, float currentSpeed) {
        if (movingRight) {
            x += currentSpeed * delta;
            if (x >= Gdx.graphics.getWidth() - width) {
                x = Gdx.graphics.getWidth() - width;
                movingRight = false;
            }
        } else {
            x -= currentSpeed * delta;
            if (x <= 0) {
                x = 0;
                movingRight = true;
            }
        }
    }

    @Override
    public void hit(float damage) {
        if (!alive) return;

        health -= damage;
        float healthPct = getHealthPercentage();

        if (health <= 0) {
            alive = false;
            currentPhase = BossPhase.DEFEATED;

            GameManager.getInstance().notify(GameEvent.BOSS_DEFEATED);
        } else if (healthPct <= 0.35f && currentPhase != BossPhase.PHASE_3) {
            currentPhase = BossPhase.PHASE_3;
        } else if (healthPct <= 0.70f && currentPhase == BossPhase.PHASE_1) {
            currentPhase = BossPhase.PHASE_2;
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public float getHealthPercentage() {
        return Math.max(0f, (float) health / maxHealth);
    }

    public BossPhase getCurrentPhase() {
        return currentPhase;
    }

    public java.util.List<BossBullet> getBullets() { return bullets; }
}
