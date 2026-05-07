package game.mygame.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Enemy {
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


}
