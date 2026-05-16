package game.mygame.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import game.mygame.Assets;
import game.mygame.GameManager;

public class GameOverState implements State {

    private final Stage stage;
    private final Skin skin;
    private final Runnable onRestart;
    private final Runnable onQuit;
    private Label scoreLabel;

    public GameOverState(SpriteBatch batch, Runnable onRestart, Runnable onQuit) {
        this.onRestart = onRestart;
        this.onQuit = onQuit;
        this.stage = new Stage(new ScreenViewport(), batch);
        this.skin = createSkin();
        initUI();
    }

    private void initUI() {
        Table table = new Table();
        table.setFillParent(true);
        table.setSkin(skin);
        stage.addActor(table);

        scoreLabel = new Label("FINAL SCORE: 0", skin);

        TextButton restartButton = new TextButton("RESTART", skin);
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (onRestart != null) onRestart.run();
            }
        });

        TextButton quitButton = new TextButton("MAIN MENU", skin);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (onQuit != null) onQuit.run();
            }
        });

        table.add("GAME OVER").padBottom(15).row();
        table.add(scoreLabel).padBottom(40).row();
        table.add(restartButton).width(220).height(50).padBottom(15).row();
        table.add(quitButton).width(220).height(50);
    }

    private Skin createSkin() {
        Skin skin = new Skin();
        BitmapFont font = new BitmapFont();
        skin.add("default", font);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        pixmap.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default");
        skin.add("default", labelStyle);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.GRAY);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);

        return skin;
    }

    @Override
    public void enter() {
        Gdx.input.setInputProcessor(stage);
        // Обновляем текст счета актуальными данными из GameManager
        scoreLabel.setText("FINAL SCORE: " + GameManager.getInstance().getScore());
    }

    @Override
    public void update(float delta) {
        stage.act(Math.min(delta, 1 / 30f));
    }

    @Override
    public void render(SpriteBatch batch) {
        if (Assets.background != null) {
            batch.draw(Assets.background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        batch.end();
        stage.draw();
        batch.begin();
    }

    @Override
    public void exit() {
        if (Gdx.input.getInputProcessor() == stage) {
            Gdx.input.setInputProcessor(null);
        }
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
