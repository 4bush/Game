package game.mygame.state;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StateManager {
    private State currentState;

    public StateManager() {
        // Start with null state
    }

    public void changeState(State newState) {
        try {
            if (currentState != null) {
                currentState.exit();
            }
            currentState = newState;
            if (currentState != null) {
                currentState.enter();
            }
        } catch (Exception e) {
            System.err.println("Error changing state: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void update(float delta) {
        try {
            if (currentState != null) {
                currentState.update(delta);
            }
        } catch (Exception e) {
            System.err.println("Error updating state: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void render(SpriteBatch batch) {
        try {
            if (currentState != null && batch != null) {
                currentState.render(batch);
            }
        } catch (Exception e) {
            System.err.println("Error rendering state: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void dispose() {
        try {
            if (currentState != null) {
                currentState.exit();
                currentState = null;
            }
        } catch (Exception e) {
            System.err.println("Error disposing state: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public State getCurrentState() {
        return currentState;
    }
}
