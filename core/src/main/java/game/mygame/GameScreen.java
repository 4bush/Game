package game.mygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.mygame.observer.GameEvent;
import game.mygame.observer.GameEventListener;
import game.mygame.state.GameOverState;
import game.mygame.state.MainMenuState;
import game.mygame.state.PlayingState;
import game.mygame.state.State;

public class GameScreen implements Screen, GameEventListener {

    private final SpaceShooter game;
    private final SpriteBatch batch;
    private final OrthographicCamera camera;

    private State currentState;
    private final MainMenuState mainMenuState;
    private final PlayingState playingState;
    private final GameOverState gameOverState;

    private final PauseOverlay pauseOverlay;
    private final InputMultiplexer gameplayInputMultiplexer;

    public GameScreen(SpaceShooter game) {
        this.game = game;
        this.batch = game.batch;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Assets.load();

        // 1. Сначала создаем базовое игровое состояние
        playingState = new PlayingState();
        gameplayInputMultiplexer = new InputMultiplexer(); // Сюда подключаются процессоры ввода из playingState

        // 2. Создаем Главное Меню (ошибка исчезнет, так как оно инициализируется раньше паузы)
        mainMenuState = new MainMenuState(batch,
            () -> {
                GameManager.getInstance().init(); // Сброс очков и жизней перед стартом
                switchState(playingState);
            },
            () -> System.out.println("Credits clicked!"),
            () -> Gdx.app.exit()
        );

        // 3. Создаем Экран Game Over
        gameOverState = new GameOverState(batch,
            () -> {
                GameManager.getInstance().init(); // Сброс очков для перезапуска
                switchState(playingState);
            },
            () -> switchState(mainMenuState) // Возврат в главное меню
        );

        // 4. Теперь создаем Паузу (теперь mainMenuState уже определен и лямбда сработает корректно)
        pauseOverlay = new PauseOverlay(batch,
            () -> GameManager.getInstance().setPaused(false), // Кнопка Resume
            () -> {
                GameManager.getInstance().setPaused(false);   // Кнопка Quit (снимаем паузу и в меню)
                switchState(mainMenuState);
            }
        );

        // Старт игры начинается с Главного Меню
        currentState = mainMenuState;
        currentState.enter();

        GameManager.getInstance().addListener(this);
    }

    @Override
    public void render(float delta) {
        GameManager gm = GameManager.getInstance();

        // Пауза по кнопке ESC работает только во время живого геймплея
        if (currentState == playingState && Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            gm.setPaused(!gm.isPaused());
        }

        // Обновляем логику игры, только если нет паузы
        if (!gm.isPaused()) {
            currentState.update(delta);
        }

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        currentState.render(batch);

        batch.end();

        // Отрисовка затемненного оверлея поверх кадра игры при паузе
        if (gm.isPaused() && currentState == playingState) {
            pauseOverlay.render();
        }
    }

    @Override
    public void onGameEvent(GameEvent event) {
        if (event == GameEvent.GAME_OVER) {
            switchState(gameOverState);
        } else if (event == GameEvent.GAME_PAUSED) {
            // Переключаем фокус ввода на оверлей паузы, чтобы кнопки нажимались
            Gdx.input.setInputProcessor(pauseOverlay.getStage());
        } else if (event == GameEvent.GAME_RESUMED) {
            // Возвращаем управление обратно игровому процессу
            if (currentState == playingState) {
                Gdx.input.setInputProcessor(gameplayInputMultiplexer);
            }
        }
    }

    private void switchState(State newState) {
        if (currentState != null) {
            currentState.exit();
        }
        currentState = newState;
        currentState.enter();

        // Если перешли в состояние игры — активируем игровой мультиплексор ввода
        if (newState == playingState) {
            Gdx.input.setInputProcessor(gameplayInputMultiplexer);
        }
    }

    @Override
    public void dispose() {
        GameManager.getInstance().removeListener(this);
        if (currentState != null) {
            currentState.exit();
        }
        if (mainMenuState != null) mainMenuState.dispose();
        if (gameOverState != null) gameOverState.dispose();
        Assets.dispose();
        pauseOverlay.dispose();
    }

    @Override public void show() {}

    @Override
    public void resize(int w, int h) {
        pauseOverlay.resize(w, h);
        if (mainMenuState != null) mainMenuState.resize(w, h);
        if (gameOverState != null) gameOverState.resize(w, h);
    }

    @Override public void pause() {
        if (currentState == playingState) {
            GameManager.getInstance().setPaused(true);
        }
    }

    @Override public void resume() {}
    @Override public void hide() {}
}
