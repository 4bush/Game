package game.mygame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Пуля, которая пробивает врагов.
 * Не уничтожается при попадании, может ударить нескольких врагов.
 */
public class PierceBullet extends Bullet {

    private int maxPierceCount = 3; // Максимум врагов, которых может пробить
    private int pierceCount = 0;

    public PierceBullet(float x, float y, float speed, Texture texture) {
        super(x, y, speed, texture);
    }

    /**
     * Пуля попала в врага - увеличить счетчик пробивания.
     */
    public void onEnemyHit() {
        pierceCount++;
        if (pierceCount >= maxPierceCount) {
            destroy();
        }
    }

    public int getPierceCount() {
        return pierceCount;
    }
}

