package game.mygame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import game.mygame.GameManager;
import game.mygame.observer.GameEvent;

public class Boss extends Enemy {
    protected int maxHealth;
    protected BossPhase currentPhase;
    private float movementTimer = 0f;
    private float attackTimer = 0f;
    private boolean movingRight = true;
    private final float targetY;

    private final float width;
    private final float height;

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
        this.targetY = Gdx.graphics.getHeight() - 180f;
        this.width = texture.getWidth();
        this.height = texture.getHeight();
    }

    @Override
    public void update(float delta) {
        if (!alive) return;

        movementTimer += delta;

        switch (currentPhase) {
            case ENTRY:
                if (y > targetY) {
                    y -= 80f * delta;
                } else {
                    y = targetY;
                    currentPhase = BossPhase.PHASE_1;
                }
                break;

            case PHASE_1:
                moveHorizontally(delta, 120f);
                break;

            case PHASE_2:
                moveHorizontally(delta, 200f);
                y = targetY + MathUtils.sin(movementTimer * 5f) * 40f;
                break;

            case PHASE_3:
                moveHorizontally(delta, 280f);
                attackTimer += delta;
                if (attackTimer > 1.5f) {
                    y -= 350f * delta;
                    if (y < 150f) {
                        y = targetY; // Reset position back to top after swoop complete
                        attackTimer = 0f;
                    }
                }
                break;

            case DEFEATED:
                alive = false;
                break;
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
    public void hit(int damage) {
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
}
