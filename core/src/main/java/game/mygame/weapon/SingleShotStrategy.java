package game.mygame.weapon;

import com.badlogic.gdx.graphics.Texture;
import game.mygame.entities.Bullet;

import java.util.ArrayList;
import java.util.List;

/**
 * Одиночный выстрел.
 * Быстрая стрельба, один выстрел за раз, урон 1.0.
 * Стандартное оружие с хорошей скоростью стрельбы - эталон баланса.
 * - Задержка: 0.15s (6.67 урама в секунду)
 * - Урон: 1.0
 */
public class SingleShotStrategy extends AbstractWeaponStrategy {

    public SingleShotStrategy() {
        super(0.15f, 1.0f); // Быстрая стрельба, стандартный урон - эталон
    }

    @Override
    public List<Bullet> shoot(float playerX, float playerY, float playerWidth, float playerHeight, Texture bulletTexture) {
        List<Bullet> bullets = new ArrayList<>();
        bullets.add(createBullet(playerX, playerY, playerWidth, playerHeight, bulletTexture));
        return bullets;
    }

    @Override
    public String getDisplayName() {
        return "Single Shot";
    }
}

