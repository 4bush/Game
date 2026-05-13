package game.mygame.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import game.mygame.Assets;
import game.mygame.GameManager;
import game.mygame.entities.Bullet;
import game.mygame.entities.Enemy;
import game.mygame.entities.Player;
import game.mygame.factory.EnemyFactory;
import game.mygame.observer.GameEvent;
import game.mygame.observer.GameEventListener;

import java.util.ArrayList;
import java.util.List;

public class PlayingState implements State, GameEventListener {

    private Player player;
    private final List<Enemy> enemies = new ArrayList<>();
    private EnemyFactory enemyFactory;

    private float spawnTimer = 0f;
    private float spawnInterval = 2.0f;

    private BitmapFont font;
    private String statusMessage = "";

    @Override
    public void enter() {
        player = new Player(
            Gdx.graphics.getWidth() / 2f - 35,
            60f
        );

        enemyFactory = new EnemyFactory(
            Assets.enemyGreen,
            Assets.enemyRed,
            Assets.enemyBlack,
            Assets.enemyGreen
        );

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(1.5f);

        spawnTimer = 0f;
        spawnInterval = 2.0f;
        statusMessage = "";

        GameManager.getInstance().addListener(this);
    }

    @Override
    public void update(float delta) {
        player.update(delta);
        spawnEnemies(delta);

        for (Enemy e : enemies) {
            e.update(delta);
        }

        checkCollisions();
        enemies.removeIf(e -> !e.isAlive());
    }

    @Override
    public void render(SpriteBatch batch) {
        GameManager gm = GameManager.getInstance();

        // Draw background
        batch.draw(Assets.background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Draw player and enemies
        player.draw(batch);
        for (Enemy e : enemies) {
            e.draw(batch);
        }

        // Draw HUD
        font.draw(batch, "Score: " + gm.getScore(), 10, Gdx.graphics.getHeight() - 10);
        font.draw(batch, "Lives: " + gm.getLives(), 10, Gdx.graphics.getHeight() - 35);

        // Draw status message
        if (!statusMessage.isEmpty()) {
            font.draw(batch, statusMessage, Gdx.graphics.getWidth() / 2f - 60,
                Gdx.graphics.getHeight() / 2f + 80);
        }
    }

    @Override
    public void exit() {
        GameManager.getInstance().removeListener(this);
        if (font != null) {
            font.dispose();
        }
        enemies.clear();
    }

    private void spawnEnemies(float delta) {
        spawnTimer += delta;
        if (spawnTimer >= spawnInterval) {
            spawnTimer = 0f;
            float x = MathUtils.random(20f, Gdx.graphics.getWidth() - 80f);
            float y = Gdx.graphics.getHeight() + 10f;

            int roll = MathUtils.random(3);
            EnemyFactory.EnemyType type;
            if (roll == 0)      type = EnemyFactory.EnemyType.FAST;
            else if (roll == 1) type = EnemyFactory.EnemyType.TANK;
            else if (roll == 2) type = EnemyFactory.EnemyType.ZIGZAG;
            else                type = EnemyFactory.EnemyType.SLOW;

            enemies.add(enemyFactory.create(type, x, y));

            spawnInterval = Math.max(0.5f, spawnInterval - 0.05f);
        }
    }

    private void checkCollisions() {
        GameManager gm = GameManager.getInstance();

        for (Enemy enemy : enemies) {
            if (!enemy.isAlive()) continue;

            // Bullet-Enemy collision
            for (Bullet bullet : player.getBullets()) {
                if (!bullet.isAlive()) continue;
                if (bullet.getBounds().overlaps(enemy.getBounds())) {
                    bullet.destroy();
                    enemy.hit(1);
                    if (!enemy.isAlive()) {
                        gm.addScore(enemy.getScoreValue());
                        gm.notify(GameEvent.ENEMY_KILLED);
                    }
                }
            }

            // Player-Enemy collision
            if (enemy.getBounds().overlaps(player.getBounds())) {
                enemy.hit(999);
                gm.loseLife();
            }
        }
    }

    @Override
    public void onGameEvent(GameEvent event) {
        switch (event) {
            case ENEMY_KILLED:
                statusMessage = "+Score!";
                break;
            case LIFE_LOST:
                statusMessage = "Ouch! Lives left: " + GameManager.getInstance().getLives();
                break;
            case GAME_OVER:
                statusMessage = "";
                break;
            default:
                break;
        }
    }
}
