package game.mygame.factory;

import com.badlogic.gdx.graphics.Texture;
import game.mygame.entities.Enemy;

public class EnemyFactory {
    public enum EnemyType {
        SLOW,
        FAST,
        TANK
    }

    private final Texture slowTexture;
    private final Texture fastTexture;
    private final Texture tankTexture;

    public EnemyFactory(Texture slowTexture, Texture fastTexture, Texture tankTexture) {
        this.slowTexture = slowTexture;
        this.fastTexture = fastTexture;
        this.tankTexture = tankTexture;
    }

    public Enemy create(EnemyType type, float x, float y) {
        switch (type) {
            case FAST:
                return new Enemy(x, y, 220f, 1, 50,  fastTexture);
            case TANK:
                return new Enemy(x, y,  60f, 5, 200, tankTexture);
            case SLOW:
            default:
                return new Enemy(x, y, 120f, 2, 100, slowTexture);
        }
    }
}
