package game.mygame.entities;

import com.badlogic.gdx.graphics.Texture;

public interface EnemyPrototype {
    EnemyPrototype clone();

    float getX();
    float getY();
    float getSpeed();
    int getHealth();
    int getScoreValue();
    Texture getTexture();
    boolean isAlive();
    float getWidth();
    float getHeight();
}
