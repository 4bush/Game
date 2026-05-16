package game.mygame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Пуля со взрывом.
 * При столкновении создает взрыв в радиусе.
 */
public class SplashBullet extends Bullet {

    private float splashRadius = 120f; // Большой радиус взрыва
    private boolean exploded = false;

    public SplashBullet(float x, float y, float speed, Texture texture) {
        super(x, y, speed, texture);
    }

    /**
     * Получить радиус взрыва.
     */
    public float getSplashRadius() {
        return splashRadius;
    }

    /**
     * Вызвать взрыв.
     */
    public void explode() {
        if (!exploded) {
            exploded = true;
            destroy();
        }
    }

    public boolean hasExploded() {
        return exploded;
    }
}

