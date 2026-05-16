package game.mygame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.mygame.observer.GameEvent;
import game.mygame.observer.GameEventListener;
import game.mygame.state.GameOverState;
import game.mygame.state.GameState;
import game.mygame.state.MainMenuState;
import game.mygame.state.PauseState;
import game.mygame.state.State;
import game.mygame.state.StateManager;

public class GameScreen implements Screen, GameEventListener {

    private final SpaceShooter game;
    private final SpriteBatch batch;
    private final OrthographicCamera camera;

    private final StateManager stateManager;
    private final MainMenuState mainMenuState;
    private final GameState gameState;
    private final PauseState pauseState;
    private final GameOverState gameOverState;

    public GameScreen(SpaceShooter game) {
        this.game = game;
        this.batch = game.batch;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Assets.load();

        // Initialize state manager and states
        stateManager = new StateManager();
        mainMenuState = new MainMenuState();
        gameState = new GameState();
        pauseState = new PauseState();
        gameOverState = new GameOverState();

        // Start with main menu state
        stateManager.changeState(mainMenuState);

        GameManager.getInstance().addListener(this);
    }

    @Override
    public void render(float delta) {
        try {
            // Handle input for state transitions
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                State current = stateManager.getCurrentState();
                if (current instanceof GameState) {
                    stateManager.changeState(pauseState);
                } else if (current instanceof PauseState || current instanceof GameOverState) {
                    Gdx.app.exit();
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                State current = stateManager.getCurrentState();
                if (current instanceof MainMenuState) {
                    GameManager.getInstance().init();
                    stateManager.changeState(gameState);
                } else if (current instanceof PauseState) {
                    stateManager.changeState(gameState);
                } else if (current instanceof GameOverState) {
                    GameManager.getInstance().init();
                    stateManager.changeState(mainMenuState);
                }
            }

            // Update and render
            camera.update();
            batch.setProjectionMatrix(camera.combined);
            batch.begin();

            stateManager.update(delta);
            stateManager.render(batch);

            batch.end();
        } catch (Exception e) {
            System.err.println("Error in GameScreen render: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onGameEvent(GameEvent event) {
        if (event == GameEvent.GAME_OVER) {
            stateManager.changeState(gameOverState);
        }
    }

    @Override
    public void dispose() {
        GameManager.getInstance().removeListener(this);
        Assets.dispose();
        stateManager.dispose();
    }

    @Override public void show() {}

    @Override
    public void resize(int w, int h) {
    }

    @Override public void pause() {
        GameManager.getInstance().setPaused(true);
    }

    @Override public void resume() {}
    @Override public void hide() {}
}
