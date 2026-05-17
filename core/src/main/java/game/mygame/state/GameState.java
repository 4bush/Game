package game.mygame.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import game.mygame.Assets;
import game.mygame.GameManager;
import game.mygame.entities.*;
import game.mygame.factory.EnemyFactory;
import game.mygame.observer.GameEvent;
import game.mygame.observer.GameEventListener;
import game.mygame.weapon.WeaponStrategy;
import game.mygame.weapon.SingleShotStrategy;
import game.mygame.weapon.DoubleShotStrategy;
import game.mygame.weapon.PierceStrategy;
import game.mygame.weapon.SplashStrategy;

import java.util.ArrayList;
import java.util.List;

public class GameState implements State, GameEventListener {

    private Player player;
    private final List<Enemy> enemies = new ArrayList<>();
    private final List<PowerUp> powerUps = new ArrayList<>();
    private EnemyFactory enemyFactory;

    private float spawnTimer = 0f;
    private float spawnInterval = 2.0f;

    private BitmapFont font;
    private String statusMessage = "";
    private boolean isActive = false;

    private boolean bossActive = false;
    private int nextBossScore = 1000;

    // Доступные стратегии вооружения
    private final WeaponStrategy[] strategies = new WeaponStrategy[4];
    private String weaponSwitchMessage = "";
    private float weaponSwitchMessageTimer = 0f;

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
        isActive = true;

        // Инициализировать стратегии вооружения
        strategies[0] = new SingleShotStrategy();
        strategies[1] = new DoubleShotStrategy();
        strategies[2] = new PierceStrategy();
        strategies[3] = new SplashStrategy();

        // Установить первую стратегию (SingleShot)
        player.setWeaponStrategy(strategies[0]);

        GameManager.getInstance().addListener(this);
    }

    @Override
    public void update(float delta) {
        if (!isActive) return;

        player.update(delta);

        if (!bossActive) {
            spawnEnemies(delta);
            checkBossSpawnCondition();
        }

        // Update enemies; provide player position to Boss so it can aim
        for (Enemy e : enemies) {
            if (e instanceof Boss) {
                Boss b = (Boss) e;
                b.update(delta, player.getX() + player.getWidth() / 2f, player.getY() + player.getHeight() / 2f);
            } else {
                e.update(delta);
            }
        }

        // Update power-ups
        for (PowerUp p : powerUps) p.update(delta);
        powerUps.removeIf(p -> !p.isAlive());

        checkCollisions();
        enemies.removeIf(e -> !e.isAlive());

        // Обработать смену стратегии вооружения
        handleWeaponSwitch();

        // Обновить таймер сообщения о смене оружия
        weaponSwitchMessageTimer -= delta;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (player == null) return;

        GameManager gm = GameManager.getInstance();

        // Draw background
        batch.draw(Assets.background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Draw player and enemies
        player.draw(batch);
        for (Enemy e : enemies) {
            e.draw(batch);
        }

        // Draw boss bullets (if any)
        for (Enemy e : enemies) {
            if (e instanceof game.mygame.entities.Boss) {
                game.mygame.entities.Boss b = (game.mygame.entities.Boss) e;
                for (game.mygame.entities.BossBullet bb : b.getBullets()) {
                    bb.draw(batch);
                }
            }
        }

        // Draw power-ups
        for (PowerUp p : powerUps) {
            p.draw(batch);
        }

        // Draw HUD
        font.draw(batch, "Score: " + gm.getScore(), 10, Gdx.graphics.getHeight() - 10);
        font.draw(batch, "Lives: " + gm.getLives(), 10, Gdx.graphics.getHeight() - 35);

        // Отобразить текущую стратегию вооружения с полной информацией
        String weaponInfo = "Weapon: " + player.getCurrentStrategy().getDisplayName() +
            " | Dmg:" + String.format("%.1f", player.getCurrentStrategy().getDamage());
        font.draw(batch, weaponInfo, 10, Gdx.graphics.getHeight() - 60);

        // Отобразить подсказку по переключению оружия
        font.setColor(Color.YELLOW);
        font.getData().setScale(1f);
        font.draw(batch, "Press 1-4 to switch weapons", 10, 30);
        font.setColor(Color.WHITE);
        font.getData().setScale(1.5f);

        // Draw status message
        if (!statusMessage.isEmpty()) {
            font.draw(batch, statusMessage, Gdx.graphics.getWidth() / 2f - 60,
                Gdx.graphics.getHeight() / 2f + 80);
        }

        // Draw weapon switch message
        if (weaponSwitchMessageTimer > 0) {
            font.setColor(Color.LIME);
            font.getData().setScale(1.2f);
            font.draw(batch, weaponSwitchMessage, Gdx.graphics.getWidth() / 2f - 100, Gdx.graphics.getHeight() / 2f + 150);
            font.setColor(Color.WHITE);
            font.getData().setScale(1.5f);
        }
    }

    @Override
    public void exit() {
        GameManager.getInstance().removeListener(this);
        if (font != null) {
            font.dispose();
        }
        enemies.clear();
        isActive = false;
    }

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

            // Small chance to spawn a power-up at the same X
            if (MathUtils.random() < 0.08f) {
                PowerUp.Type pt = PowerUp.Type.values()[MathUtils.random(0, PowerUp.Type.values().length - 1)];
                powerUps.add(new PowerUp(x, MathUtils.random(Gdx.graphics.getHeight() + 10f, Gdx.graphics.getHeight() + 80f), pt));
            }

            spawnInterval = Math.max(0.5f, spawnInterval - 0.05f);
        }
    }

    private void checkCollisions() {
        GameManager gm = GameManager.getInstance();

        // Create a copy of enemies list to avoid ConcurrentModificationException
        List<Enemy> enemiesCopy = new ArrayList<>(enemies);

        for (Enemy enemy : enemiesCopy) {
            if (!enemy.isAlive()) continue;

            // Bullet-Enemy collision
            // Create a copy to avoid ConcurrentModificationException
            List<Bullet> bulletsCopy = new ArrayList<>(player.getBullets());
            for (Bullet bullet : bulletsCopy) {
                if (!bullet.isAlive()) continue;
                if (bullet.getBounds().overlaps(enemy.getBounds())) {
                    // Получить урон из текущей стратегии
                    float damage = player.getCurrentStrategy().getDamage();

                    // Обработать специальные типы пуль
                    if (bullet instanceof PierceBullet) {
                        // Пробивающая пуля - не уничтожается, но наносит урон
                        PierceBullet pierceBullet = (PierceBullet) bullet;
                        enemy.hit(damage); // Урон зависит от стратегии (float)
                        pierceBullet.onEnemyHit();
                        if (!enemy.isAlive()) {
                            gm.addScore(enemy.getScoreValue());
                            gm.notify(GameEvent.ENEMY_KILLED);
                        }
                    } else if (bullet instanceof SplashBullet) {
                        // Взрывная пуля - при попадании вызывает взрыв
                        SplashBullet splashBullet = (SplashBullet) bullet;
                        splashBullet.explode();
                        // Урон всем врагам в радиусе
                        damageSurroundingEnemies(splashBullet, gm, enemiesCopy, damage);
                    } else {
                        // Обычная пуля
                        bullet.destroy();
                        enemy.hit(damage); // Урон зависит от стратегии (float)
                        if (!enemy.isAlive()) {
                            gm.addScore(enemy.getScoreValue());
                            gm.notify(GameEvent.ENEMY_KILLED);
                        }
                    }
                }
            }

            // Boss bullets hit player
            if (enemy instanceof game.mygame.entities.Boss) {
                game.mygame.entities.Boss b = (game.mygame.entities.Boss) enemy;
                for (game.mygame.entities.BossBullet bb : b.getBullets()) {
                    if (!bb.isAlive()) continue;
                    if (bb.getBounds().overlaps(player.getBounds())) {
                        bb.destroy();
                        gm.loseLife();
                    }
                }
            }

            // Player-Enemy collision
            if (enemy.getBounds().overlaps(player.getBounds())) {
                enemy.hit(999);
                gm.loseLife();
            }
        }

        // PowerUp - Player collision
        List<PowerUp> powerCopy = new ArrayList<>(powerUps);
        for (PowerUp p : powerCopy) {
            if (!p.isAlive()) continue;
            if (p.getBounds().overlaps(player.getBounds())) {
                p.collect();
                powerUps.remove(p);
                GameManager.getInstance().notify(game.mygame.observer.GameEvent.POWERUP_COLLECTED);
            }
        }
    }

    /**
     * Нанести урон всем врагам в радиусе взрыва.
     */
    private void damageSurroundingEnemies(SplashBullet splashBullet, GameManager gm, List<Enemy> enemies, float damage) {
        float splashRadius = splashBullet.getSplashRadius();
        float bulletX = splashBullet.getBounds().x;
        float bulletY = splashBullet.getBounds().y;

        for (Enemy enemy : enemies) {
            if (!enemy.isAlive()) continue;

            float enemyX = enemy.getBounds().x + enemy.getBounds().width / 2;
            float enemyY = enemy.getBounds().y + enemy.getBounds().height / 2;

            float distance = (float) Math.sqrt((bulletX - enemyX) * (bulletX - enemyX) +
                (bulletY - enemyY) * (bulletY - enemyY));

            if (distance <= splashRadius) {
                // Урон зависит от стратегии (Splash вернёт 0.25)
                enemy.hit(damage);
                if (!enemy.isAlive()) {
                    gm.addScore(enemy.getScoreValue());
                    gm.notify(GameEvent.ENEMY_KILLED);
                }
            }
        }
    }

    @Override
    public void onGameEvent(GameEvent event) {
        if (!isActive) return;  // Ignore events if state is not active

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

    /**
     * Обработать переключение стратегии вооружения по клавишам 1-4.
     */
    private void handleWeaponSwitch() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            switchWeapon(0);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            switchWeapon(1);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            switchWeapon(2);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            switchWeapon(3);
        }
    }

    /**
     * Переключить на стратегию с указанным индексом.
     */
    private void switchWeapon(int index) {
        if (index >= 0 && index < strategies.length && strategies[index] != null) {
            player.setWeaponStrategy(strategies[index]);
            weaponSwitchMessage = "Weapon: " + strategies[index].getDisplayName();
            weaponSwitchMessageTimer = 1.5f; // Показать сообщение на 1.5 секунды
        }
    }
}
