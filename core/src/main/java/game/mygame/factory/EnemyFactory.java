package game.mygame.factory;

import com.badlogic.gdx.graphics.Texture;
import game.mygame.entities.Enemy;
import game.mygame.entities.Boss;
import game.mygame.entities.ZigzagEnemy;

public class EnemyFactory {
    public enum EnemyType {
        SLOW,
        FAST,
        TANK,
        ZIGZAG,
        BOSS
    }
    private final Texture bossTexture;
    private final Texture slowTexture;
    private final Texture fastTexture;
    private final Texture tankTexture;
    private final Texture zigzagTexture;

    public EnemyFactory(
        Texture slowTexture,
        Texture fastTexture,
        Texture tankTexture,
        Texture zigzagTexture,
        Texture bossTexture
    ) {
        this.slowTexture = slowTexture;
        this.fastTexture = fastTexture;
        this.tankTexture = tankTexture;
        this.zigzagTexture = zigzagTexture;
        this.bossTexture = bossTexture;
    }

    public Enemy create(EnemyType type, float x, float y) {
        switch (type) {
            case FAST:
                return new Enemy(x, y, 220f, 1, 50,  fastTexture);
            case TANK:
                return new Enemy(x, y,  60f, 5, 200, tankTexture);
            case ZIGZAG:
                return new ZigzagEnemy(x, y, 100f, 2, 150, zigzagTexture);
            case BOSS:
                return new Boss(x, y, 25, 400, bossTexture);
            case SLOW:
            default:
                return new Enemy(x, y, 120f, 2, 100, slowTexture);
        }
    }
}
