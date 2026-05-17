package game.mygame.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import game.mygame.Assets;
import game.mygame.GameManager;

public class PowerUp {

    public enum Type {
        HEALTH("Health", Color.GREEN),
        SHIELD("Shield", Color.CYAN),
        FIRE_RATE("Fire Rate", Color.ORANGE);

        private final String displayName;
        private final Color tint;

        Type(String displayName, Color tint) {
            this.displayName = displayName;
            this.tint = tint;
        }

        public String getDisplayName() {
            return displayName;
        }

        public Color getTint() {
            return tint;
        }
    }

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
        Color previousColor = batch.getColor().cpy();
        batch.setColor(type.getTint());
        batch.draw(texture, x, y, size, size);
        batch.setColor(previousColor);
    }

    public Rectangle getBounds() { return new Rectangle(x, y, size, size); }
    public boolean isAlive() { return alive; }
    public Type getType() { return type; }

    public String collect(Player player) {
        alive = false;
        GameManager gm = GameManager.getInstance();
        gm.addScore(25);

        switch (type) {
            case HEALTH:
                gm.addLife();
                return "+1 Life!";
            case SHIELD:
                player.activateShield();
                return "Shield absorbed one hit!";
            case FIRE_RATE:
                player.applyFireRateBoost(0.7f, 5f);
                return "Fire rate boosted!";
            default:
                return "Power-up collected!";
        }
    }
}
