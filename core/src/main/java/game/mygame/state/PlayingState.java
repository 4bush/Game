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
import game.mygame.entities.Boss;
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

    private boolean bossActive = false;

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
            Assets.enemyGreen,
            Assets.bossTexture
        );

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(1.5f);

        spawnTimer = 0f;
        spawnInterval = 2.0f;
        statusMessage = "";
        bossActive = false;
        nextBossScore = 1000;
        GameManager.getInstance().addListener(this);
    }

    @Override
    public void update(float delta) {
        player.update(delta);

        if (!bossActive) {
            spawnEnemies(delta);
            checkBossSpawnCondition();
        }

        for (Enemy e : enemies) {
            e.update(delta);
        }

        checkCollisions();
        enemies.removeIf(e -> !e.isAlive());
    }

    private int nextBossScore = 1000;
    private void checkBossSpawnCondition() {
        int currentScore = GameManager.getInstance().getScore();

        if (!bossActive && currentScore >= nextBossScore) {

            System.out.println("BOSS SPAWNED AT SCORE: " + currentScore);

            nextBossScore += 1000;
            triggerBossFight();
        }
    }

    private void triggerBossFight() {
        bossActive = true;
        statusMessage = "WARNING: BOSS APPROACHING!";

        // Center boss at top boundary above active viewport layout
        float bossX = Gdx.graphics.getWidth() / 2f - 32f;
        float bossY = Gdx.graphics.getHeight() + 40f;

        Enemy boss = enemyFactory.create(EnemyFactory.EnemyType.BOSS, bossX, bossY);
        enemies.add(boss);

        GameManager.getInstance().notify(GameEvent.BOSS_SPAWNED);
    }

    @Override
    public void render(SpriteBatch batch) {
        GameManager gm = GameManager.getInstance();

        batch.draw(Assets.background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        player.draw(batch);
        for (Enemy e : enemies) {
            e.draw(batch);
        }

        font.draw(batch, "Score: " + gm.getScore(), 10, Gdx.graphics.getHeight() - 10);
        font.draw(batch, "Lives: " + gm.getLives(), 10, Gdx.graphics.getHeight() - 35);

        if (bossActive) {
            for (Enemy e : enemies) {
                if (e instanceof Boss) {
                    Boss b = (Boss) e;
                    font.draw(batch, "BOSS HP: " + (int)(b.getHealthPercentage() * 100) + "%",
                        Gdx.graphics.getWidth() - 160, Gdx.graphics.getHeight() - 10);
                }
            }
        }

        if (!statusMessage.isEmpty()) {
            font.draw(batch, statusMessage, Gdx.graphics.getWidth() / 2f - 120,
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
            case BOSS_DEFEATED:
                bossActive = false;
                statusMessage = "BOSS DEFEATED! Endless loop resumed.";
                break;
            default:
                break;
        }
    }
}
