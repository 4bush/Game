package game.mygame.weapon;

import com.badlogic.gdx.graphics.Texture;
import game.mygame.entities.Bullet;

import java.util.ArrayList;
import java.util.List;

/**
 * Базовый класс для стратегий вооружения.
 */
public abstract class AbstractWeaponStrategy implements WeaponStrategy {

    protected float shootDelay;
    protected float damage;

    public AbstractWeaponStrategy(float shootDelay, float damage) {
        this.shootDelay = shootDelay;
        this.damage = damage;
    }

    @Override
    public float getShootDelay() {
        return shootDelay;
    }

    @Override
    public float getDamage() {
        return damage;
    }

    /**
     * Создать одиночную пулю в центре игрока.
     */
    protected Bullet createBullet(float playerX, float playerY, float playerWidth, float playerHeight, Texture bulletTexture) {
        float bulletX = playerX + playerWidth / 2 - 4;
        float bulletY = playerY + playerHeight;
        return new Bullet(bulletX, bulletY, 500f, bulletTexture);
    }

    /**
     * Создать две пули с расширенным расстоянием для лучшего покрытия.
     */
    protected List<Bullet> createDoubleBullets(float playerX, float playerY, float playerWidth, float playerHeight, Texture bulletTexture) {
        List<Bullet> bullets = new ArrayList<>();
        float centerX = playerX + playerWidth / 2;
        float bulletY = playerY + playerHeight;

        // Левая пуля (расширенное расстояние для лучшего покрытия)
        bullets.add(new Bullet(centerX - 25, bulletY, 500f, bulletTexture));
        // Правая пуля
        bullets.add(new Bullet(centerX + 17, bulletY, 500f, bulletTexture));

        return bullets;
    }
}

