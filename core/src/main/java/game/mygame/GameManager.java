package game.mygame;

import game.mygame.observer.GameEvent;
import game.mygame.observer.GameEventListener;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private static GameManager instance;

    private int score;
    private int lives;
    private boolean gameOver;
    private boolean paused;
    private final List<GameEventListener> listeners = new ArrayList<>();
    private GameManager() {}

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void init() {
        score = 0;
        lives = 3;
        gameOver = false;
        paused = false;
    }

    public void addListener(GameEventListener listener) {
        listeners.add(listener);
    }

    public void removeListener(GameEventListener listener) {
        listeners.remove(listener);
    }

    public void notify(GameEvent event) {
        for (GameEventListener l : listeners) {
            l.onGameEvent(event);
        }
    }

    public void addScore(int points) {
        score += points;
        notify(GameEvent.SCORE_CHANGED);
    }

    public void loseLife() {
        lives--;
        if (lives <= 0) {
            gameOver = true;
            notify(GameEvent.GAME_OVER);
        } else {
            notify(GameEvent.LIFE_LOST);
        }
    }

    public int getScore()       { return score; }
    public int getLives()       { return lives; }
    public boolean isGameOver() { return gameOver; }
    public boolean isPaused()   { return paused; }

    public void setPaused(boolean paused) {
        if (this.gameOver) return;
        this.paused = paused;
        notify(paused ? GameEvent.GAME_PAUSED : GameEvent.GAME_RESUMED);
    }
}
