package game.mygame.weapon;

import com.badlogic.gdx.graphics.Texture;
import game.mygame.entities.Bullet;
import game.mygame.entities.SplashBullet;

import java.util.ArrayList;
import java.util.List;

/**
 * Взрывной выстрел (Splash).
 * Пуля взрывается при столкновении или выходе за экран, поражая врагов в большом радиусе.
 * Самая медленная стрельба, но может поразить много врагов одновременно.
 * - Задержка: 0.45s
 * - Урам за врага в радиусе: 0.3 (может поразить много врагов = много дамага)
 * - Радиус взрыва: 120 пикселей (большой покрой)
 */
public class SplashStrategy extends AbstractWeaponStrategy {

    public SplashStrategy() {
        super(0.45f, 0.3f); // Хороший баланс для площадной атаки
    }

    @Override
    public List<Bullet> shoot(float playerX, float playerY, float playerWidth, float playerHeight, Texture bulletTexture) {
        List<Bullet> bullets = new ArrayList<>();
        float bulletX = playerX + playerWidth / 2 - 4;
        float bulletY = playerY + playerHeight;

        SplashBullet splashBullet = new SplashBullet(bulletX, bulletY, 450f, bulletTexture);
        bullets.add(splashBullet);

        return bullets;
    }

    @Override
    public String getDisplayName() {
        return "Splash";
    }
}

