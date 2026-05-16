package game.mygame.weapon;

import com.badlogic.gdx.graphics.Texture;
import game.mygame.entities.Bullet;

import java.util.List;

/**
 * Двойной выстрел.
 * Два выстрела одновременно с расширенным расстоянием между ними.
 * Немного медленнее одиночного, но может попасть по врагам на большей площади.
 * Каждая пуля наносит меньше урона для балансировки.
 * - Задержка: 0.25s (4.0 урама в секунду при 0.5 на пулю = 2.0 в сек)
 * - Урам за пулю: 0.5 (всего 1.0 за выстрел, но шире атака)
 */
public class DoubleShotStrategy extends AbstractWeaponStrategy {

    public DoubleShotStrategy() {
        super(0.25f, 0.5f); // Быстрее чем сейчас, хороший баланс для широкой атаки
    }

    @Override
    public List<Bullet> shoot(float playerX, float playerY, float playerWidth, float playerHeight, Texture bulletTexture) {
        return createDoubleBullets(playerX, playerY, playerWidth, playerHeight, bulletTexture);
    }

    @Override
    public String getDisplayName() {
        return "Double Shot";
    }
}

