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
import game.mygame.state.PlayingState;
import game.mygame.state.State;

public class GameScreen implements Screen, GameEventListener {

    private final SpaceShooter game;
    private final SpriteBatch batch;
    private final OrthographicCamera camera;

    private State currentState;
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

        // Initialize states
        playingState = new PlayingState();
        gameOverState = new GameOverState();

        // Start with playing state
        currentState = playingState;
        currentState.enter();

        pauseOverlay = new PauseOverlay(batch,
            () -> GameManager.getInstance().setPaused(false),
            () -> Gdx.app.exit()
        );

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(pauseOverlay.getStage());
        Gdx.input.setInputProcessor(inputMultiplexer);

        GameManager.getInstance().addListener(this);
    }

    @Override
    public void render(float delta) {
        GameManager gm = GameManager.getInstance();

        // Handle pause toggle
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            gm.setPaused(!gm.isPaused());
        }

        // Update current state if not paused
        if (!gm.isPaused()) {
            currentState.update(delta);
        }

        // Render
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        currentState.render(batch);

        batch.end();

        // Render pause overlay if paused
        if (gm.isPaused()) {
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
    }

    @Override
    public void dispose() {
        GameManager.getInstance().removeListener(this);
        if (currentState != null) {
            currentState.exit();
        }
        Assets.dispose();
        pauseOverlay.dispose();
    }

    @Override public void show() {}

    @Override
    public void resize(int w, int h) {
        pauseOverlay.resize(w, h);
    }

    @Override public void pause() {
        GameManager.getInstance().setPaused(true);
    }

    @Override public void resume() {}
    @Override public void hide() {}
}
