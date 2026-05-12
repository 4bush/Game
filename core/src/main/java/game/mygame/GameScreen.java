package game.mygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import game.mygame.entities.Bullet;
import game.mygame.entities.Enemy;
import game.mygame.entities.Player;
import game.mygame.factory.EnemyFactory;
import game.mygame.observer.GameEvent;
import game.mygame.observer.GameEventListener;

import java.util.ArrayList;
import java.util.List;

import static game.mygame.observer.GameEvent.GAME_OVER;

public class GameScreen implements Screen, GameEventListener {

    private final SpaceShooter game;
    private final SpriteBatch batch;
    private final OrthographicCamera camera;

    private Player player;
    private final List<Enemy> enemies = new ArrayList<>();

    private EnemyFactory enemyFactory;

    private BitmapFont font;

    private float spawnTimer = 0f;
    private float spawnInterval = 2.0f;

    private String statusMessage = "";

    public GameScreen(SpaceShooter game) {
        this.game = game;
        this.batch = game.batch;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Assets.load();

        player = new Player(
                Gdx.graphics.getWidth() / 2f - 35,
                60f
        );

        enemyFactory = new EnemyFactory();

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(1.5f);

        GameManager.getInstance().addListener(this);
    }

    @Override
    public void render(float delta) {
        GameManager gm = GameManager.getInstance();
        if (!gm.isGameOver()) update(delta);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(Assets.background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        player.draw(batch);
        for (Enemy e : enemies) e.draw(batch);

        font.draw(batch, "Score: " + gm.getScore(), 10, Gdx.graphics.getHeight() - 10);
        font.draw(batch, "Lives: " + gm.getLives(),  10, Gdx.graphics.getHeight() - 35);

        if (gm.isGameOver()) {
            font.getData().setScale(3f);
            font.draw(batch, "GAME OVER",
                    Gdx.graphics.getWidth() / 2f - 100, Gdx.graphics.getHeight() / 2f);
            font.getData().setScale(1.5f);
            font.draw(batch, "Final Score: " + gm.getScore(),
                Gdx.graphics.getWidth() / 2f - 80, Gdx.graphics.getHeight() / 2f - 50);
        }
        if (!statusMessage.isEmpty()) {
            font.draw(batch, statusMessage, Gdx.graphics.getWidth() / 2f - 60,
                Gdx.graphics.getHeight() / 2f + 80);
        }

        batch.end();
    }

    private void update(float delta) {
        player.update(delta);
        spawnEnemies(delta);
        for (Enemy e : enemies) e.update(delta);
        checkCollisions();
        enemies.removeIf(e -> !e.isAlive());
    }

    private void spawnEnemies(float delta) {
        spawnTimer += delta;
        if (spawnTimer >= spawnInterval) {
            spawnTimer = 0f;
            float x = MathUtils.random(20f, Gdx.graphics.getWidth() - 80f);
            float y = Gdx.graphics.getHeight() + 10f;

            int roll = MathUtils.random(2);
            EnemyFactory.EnemyType type;
            if (roll == 0)      type = EnemyFactory.EnemyType.FAST;
            else if (roll == 1) type = EnemyFactory.EnemyType.TANK;
            else                type = EnemyFactory.EnemyType.SLOW;

            enemies.add(enemyFactory.create(type, x, y));

            spawnInterval = Math.max(0.5f, spawnInterval - 0.05f);
        }
    }

    private void checkCollisions() {
        GameManager gm = GameManager.getInstance();

        for (Enemy enemy : enemies) {
            if (!enemy.isAlive()) continue;

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

    @Override
    public void dispose() {
        GameManager.getInstance().removeListener(this);
        Assets.dispose();      // ← Теперь всё чистим через Assets
        font.dispose();
    }

    @Override public void show() {}
    @Override public void resize(int w, int h) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}