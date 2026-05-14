package game.mygame.state;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StateManager {
    private State currentState;

    public StateManager() {
        // Start with MainMenuState or something, but will be set later
    }

    public void changeState(State newState) {
        if (currentState != null) {
            currentState.exit();
        }
        currentState = newState;
        if (currentState != null) {
            currentState.enter();
        }
    }

    public void update(float delta) {
        if (currentState != null) {
            currentState.update(delta);
        }
    }

    public void render(SpriteBatch batch) {
        if (currentState != null) {
            currentState.render(batch);
        }
    }

    public void dispose() {
        if (currentState != null) {
            currentState.exit();
        }
    }

    public State getCurrentState() {
        return currentState;
    }
}
