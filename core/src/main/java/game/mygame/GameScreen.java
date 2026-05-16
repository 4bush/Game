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

    private PauseOverlay pauseOverlay;
    private InputMultiplexer inputMultiplexer;

    public GameScreen(SpaceShooter game) {
        this.game = game;
        this.batch = game.batch;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Assets.load();

        // Инициализация оверлея паузы
        pauseOverlay = new PauseOverlay(batch,
            () -> GameManager.getInstance().setPaused(false),
            () -> Gdx.app.exit()
        );

        // Настройка мультиплексора для геймплея
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(pauseOverlay.getStage());

        // Инициализация состояний
        playingState = new PlayingState();
        gameOverState = new GameOverState();

        // Передаем коллбеки для кнопок меню
        mainMenuState = new MainMenuState(batch,
            () -> switchState(playingState), // PLAY -> Запуск игры
            () -> System.out.println("Credits clicked!"), // CREDITS -> Лог/Экран
            () -> Gdx.app.exit() // EXIT -> Выход из игры
        );

        // Стартуем с Главного Меню
        currentState = mainMenuState;
        currentState.enter();

        GameManager.getInstance().addListener(this);
    }

    @Override
    public void render(float delta) {
        GameManager gm = GameManager.getInstance();

        // Пауза доступна только во время игры (не в меню)
        if (currentState == playingState && Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            gm.setPaused(!gm.isPaused());
        }

        // Обновляем состояние, если нет паузы
        if (!gm.isPaused()) {
            currentState.update(delta);
        }

        // Рендер текущего состояния
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        currentState.render(batch);

        batch.end();

        // Рендер экрана паузы поверх игры
        if (gm.isPaused() && currentState == playingState) {
            pauseOverlay.render();
        }
    }

    @Override
    public void onGameEvent(GameEvent event) {
        if (event == GameEvent.GAME_OVER) {
            switchState(gameOverState);
        }
    }

    private void switchState(State newState) {
        if (currentState != null) {
            currentState.exit();
        }
        currentState = newState;
        currentState.enter();

        // Если перешли в режим игры — активируем игровой мультиплексор ввода
        if (newState == playingState) {
            Gdx.input.setInputProcessor(inputMultiplexer);
        }
    }

    @Override
    public void dispose() {
        GameManager.getInstance().removeListener(this);
        if (currentState != null) {
            currentState.exit();
        }
        if (mainMenuState != null) {
            mainMenuState.dispose();
        }
        Assets.dispose();
        pauseOverlay.dispose();
    }

    @Override public void show() {}

    @Override
    public void resize(int w, int h) {
        pauseOverlay.resize(w, h);
        if (mainMenuState != null) {
            mainMenuState.resize(w, h);
        }
    }

    @Override public void pause() {
        if (currentState == playingState) {
            GameManager.getInstance().setPaused(true);
        }
    }

    @Override public void resume() {}
    @Override public void hide() {}
}
