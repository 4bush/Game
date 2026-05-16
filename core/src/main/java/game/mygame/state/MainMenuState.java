package game.mygame.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import game.mygame.Assets;

public class MainMenuState implements State {

    private final Stage stage;
    private final Skin skin;
    private final Runnable onPlay;
    private final Runnable onCredits;
    private final Runnable onExit;

    public MainMenuState(SpriteBatch batch, Runnable onPlay, Runnable onCredits, Runnable onExit) {
        this.onPlay = onPlay;
        this.onCredits = onCredits;
        this.onExit = onExit;
        this.stage = new Stage(new ScreenViewport(), batch);
        this.skin = createSkin();
        initUI();
    }

    private void initUI() {
        Table table = new Table();
        table.setFillParent(true);
        table.setSkin(skin);
        stage.addActor(table);

        TextButton playButton = new TextButton("PLAY", skin);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (onPlay != null) onPlay.run();
            }
        });

        TextButton creditsButton = new TextButton("CREDITS", skin);
        creditsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (onCredits != null) onCredits.run();
            }
        });

        TextButton exitButton = new TextButton("EXIT", skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (onExit != null) onExit.run();
            }
        });

        // Сборка UI (заголовок и вертикальный стек кнопок)
        table.add("SPACE SHOOTER").padBottom(60).row();
        table.add(playButton).width(220).height(50).padBottom(15).row();
        table.add(creditsButton).width(220).height(50).padBottom(15).row();
        table.add(exitButton).width(220).height(50);
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

        com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle labelStyle = new com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle();
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
        Gdx.input.setInputProcessor(stage); // Захватываем ввод для меню
    }

    @Override
    public void update(float delta) {
        stage.act(Math.min(delta, 1 / 30f));
    }

    @Override
    public void render(SpriteBatch batch) {
        // Отрисовка космического фона из Assets
        if (Assets.background != null) {
            batch.draw(Assets.background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        // Временно закрываем batch игры, так как у Stage свой цикл отрисовки
        batch.end();
        stage.draw();
        batch.begin(); // Возвращаем batch в исходное состояние для GameScreen
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
