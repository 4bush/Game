package game.mygame.factory;

import com.badlogic.gdx.graphics.Texture;
import game.mygame.entities.Enemy;
import game.mygame.entities.ZigzagEnemy;

public class EnemyFactory {
    public enum EnemyType {
        SLOW,
        FAST,
        TANK,
        ZIGZAG
    }

    private final Texture slowTexture;
    private final Texture fastTexture;
    private final Texture tankTexture;
    private final Texture zigzagTexture;

    public EnemyFactory(Texture slowTexture, Texture fastTexture, Texture tankTexture, Texture zigzagTexture) {
        this.slowTexture = slowTexture;
        this.fastTexture = fastTexture;
        this.tankTexture = tankTexture;
        this.zigzagTexture = zigzagTexture;
    }

    public Enemy create(EnemyType type, float x, float y) {
        switch (type) {
            case FAST:
                return new Enemy(x, y, 220f, 1, 50,  fastTexture);
            case TANK:
                return new Enemy(x, y,  60f, 5, 200, tankTexture);
            case ZIGZAG:
                return new ZigzagEnemy(x, y, 100f, 2, 150, zigzagTexture);
            case SLOW:
            default:
                return new Enemy(x, y, 120f, 2, 100, slowTexture);
        }
    }
}
