package game.mygame.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Boss {
    protected float x, y;
    protected float speed;
    protected float health;
    protected float maxHealth;
    protected int scoreValue;
    protected final float width, height;
    protected final Texture texture;
    protected boolean alive = true;
    protected BossPhase currentPhase;

    public enum BossPhase {
        ENTRY,
        PHASE_1,
        PHASE_2,
        PHASE_3,
        DEFEATED
    }

    public Boss(float x, float y, int health, int scoreValue, Texture texture) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.maxHealth = health;
        this.scoreValue = scoreValue;
        this.texture = texture;
        this.width = texture.getWidth();
        this.height = texture.getHeight();
        this.speed = 50f;
        this.currentPhase = BossPhase.ENTRY;
    }

    public void update(float delta) {
        // Entry phase: move to center of screen
        if (currentPhase == BossPhase.ENTRY) {
            y -= speed * delta;
            // Placeholder: transition to PHASE_1 when positioned
        }

        // Additional phase logic will be implemented later
    }

    public void draw(SpriteBatch batch) {
        if (alive) {
            batch.draw(texture, x, y, width, height);
        }
    }

    public void hit(float damage) {
        health -= damage;
        if (health <= 0) {
            alive = false;
            currentPhase = BossPhase.DEFEATED;
        }
        // Phase transitions based on health percentage can be added later
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isAlive() {
        return alive;
    }

    public int getScoreValue() {
        return scoreValue;
    }

    public BossPhase getCurrentPhase() {
        return currentPhase;
    }

    public float getHealthPercentage() {
        return (float) health / maxHealth;
    }

    public void setPhase(BossPhase phase) {
        this.currentPhase = phase;
    }
}
