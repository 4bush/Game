package game.mygame.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Enemy implements EnemyPrototype {
        protected float x, y;
        protected float speed;
        protected int health;
        protected int scoreValue;
        protected final float width, height;
        protected final Texture texture;
        protected boolean alive = true;

        public Enemy(float x, float y, float speed, int health, int scoreValue, Texture texture) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.health = health;
            this.scoreValue = scoreValue;
            this.texture = texture;
            this.width = texture.getWidth();
            this.height = texture.getHeight();
        }

        public void update(float delta) {
            y -= speed * delta;
            if (y + height < 0) alive = false;
        }

        public void draw(SpriteBatch batch) {
            if (alive) batch.draw(texture, x, y, width, height);
        }

        public void hit(int damage) {
            health -= damage;
            if (health <= 0) alive = false;
        }

        public Rectangle getBounds()  { return new Rectangle(x, y, width, height); }
        public boolean isAlive()      { return alive; }
        public int getScoreValue()    { return scoreValue; }

        // EnemyPrototype implementation
        @Override
        public EnemyPrototype clone() {
            return new Enemy(x, y, speed, health, scoreValue, texture);
        }

        @Override
        public float getX() { return x; }

        @Override
        public float getY() { return y; }

        @Override
        public float getSpeed() { return speed; }

        @Override
        public int getHealth() { return health; }

        @Override
        public Texture getTexture() { return texture; }

        @Override
        public float getWidth() { return width; }

        @Override
        public float getHeight() { return height; }
}
