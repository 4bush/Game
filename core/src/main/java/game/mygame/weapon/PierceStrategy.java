package game.mygame.weapon;

import com.badlogic.gdx.graphics.Texture;
import game.mygame.entities.Bullet;
import game.mygame.entities.PierceBullet;

import java.util.ArrayList;
import java.util.List;

/**
 * Пробивающий выстрел.
 * Пуля пробивает врагов, может попасть в нескольких врагов подряд.
 * Медленная стрельба, но может нанести много дамага при пробивании нескольких врагов.
 * - Задержка: 0.40s
 * - Урам за попадание: 0.5 (пробивает максимум 3 врагов = 1.5 дамага)
 * Хорошо для ситуаций когда враги выстроены в ряд.
 */
public class PierceStrategy extends AbstractWeaponStrategy {

    public PierceStrategy() {
        super(0.40f, 0.5f); // Хороший урам для пробивания
    }

    @Override
    public List<Bullet> shoot(float playerX, float playerY, float playerWidth, float playerHeight, Texture bulletTexture) {
        List<Bullet> bullets = new ArrayList<>();
        float bulletX = playerX + playerWidth / 2 - 4;
        float bulletY = playerY + playerHeight;

        PierceBullet pierceBullet = new PierceBullet(bulletX, bulletY, 500f, bulletTexture);
        bullets.add(pierceBullet);

        return bullets;
    }

    @Override
    public String getDisplayName() {
        return "Pierce";
    }
}

