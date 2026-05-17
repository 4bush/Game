package game.mygame.weapon;

import com.badlogic.gdx.graphics.Texture;
import game.mygame.entities.Bullet;

import java.util.List;

/**
 * Стратежия вооружения.
 * Определяет, как игрок стреляет.
 */
public interface WeaponStrategy {

    /**
     * Выполнить стрельбу при заданной позиции.
     *
     * @param playerX X позиция игрока
     * @param playerY Y позиция игрока
     * @param playerWidth Ширина спрайта игрока
     * @param playerHeight Вечина спрайта игрока
     * @param bulletTexture Текстура пули
     * @return Список созданных пуль
     */
    List<Bullet> shoot(float playerX, float playerY, float playerWidth, float playerHeight, Texture bulletTexture);

    /**
     * Получить задержку между выстрелами.
     */
    float getShootDelay();

    /**
     * Получить название стратегии для отображения.
     */
    String getDisplayName();

    /**
     * Получить базовый урон за выстрел.
     */
    float getDamage();
}

