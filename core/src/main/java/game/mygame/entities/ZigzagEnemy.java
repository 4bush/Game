package game.mygame.entities;

import com.badlogic.gdx.graphics.Texture;

public class ZigzagEnemy extends Enemy {
    private float horizontalSpeed;
    private float direction = 1f;
    private float zigzagAmplitude = 100f;
    private float startX;

    public ZigzagEnemy(float x, float y, float speed, int health, int scoreValue, Texture texture) {
        super(x, y, speed, health, scoreValue, texture);
        this.startX = x;
        this.horizontalSpeed = 80f;
    }

    @Override
    public void update(float delta) {
        // Vertical movement (downward)
        y -= speed * delta;

        // Horizontal zigzag movement
        x += horizontalSpeed * direction * delta;

        // Reverse direction when exceeding amplitude
        if (Math.abs(x - startX) > zigzagAmplitude) {
            direction *= -1;
        }

        // Mark as dead if off-screen
        if (y + height < 0) alive = false;
    }

    @Override
    public EnemyPrototype clone() {
        return new ZigzagEnemy(x, y, speed, (int) health, scoreValue, texture);
    }
}
