package com.psychokitty.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Scaling;


/**
 * Created by steven on 24.07.15.
 */
public class MenuScreen implements Screen {

    final PsychoKittyGame game;
    public com.psychokitty.game.AdMob.AdsController adcont;
    private Stage stage = new Stage();
    private Music menuMusic = Gdx.audio.newMusic(Gdx.files.internal(com.psychokitty.game.Utils.Constants.musicMenu));
    private Texture texture = new Texture(Gdx.files.internal(com.psychokitty.game.Utils.Constants.backgroundMenu));
    private Image menuBackground = new Image(texture);
    private com.psychokitty.game.Utils.Highscore highscore;
    private Group scoreItems;
    private Group menuItems;

    private TextureAtlas buttonsAtlas;
    private NinePatch buttonUpNine;
    private NinePatch buttonDownNine;
    private TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();

    private Skin skin = new Skin();
    private Skin skin2 = new Skin(Gdx.files.internal(com.psychokitty.game.Utils.Constants.defaultJson));

    public MenuScreen(final PsychoKittyGame gam, com.psychokitty.game.AdMob.AdsController adsController) {
        game = gam;
        adcont = adsController;
        highscore = new com.psychokitty.game.Utils.Highscore();
        highscore.config();
    }

    private void setupBackground(){
        menuBackground.setHeight(Gdx.graphics.getHeight());
        menuBackground.setScaling(Scaling.fillY);
        menuBackground.setPosition(Gdx.graphics.getWidth() / 2 - menuBackground.getWidth() / 2, Gdx.graphics.getHeight() / 2 - menuBackground.getHeight() / 2);
        stage.addActor(menuBackground);
    }

    private void setupMusic(){
        menuMusic.setVolume(1);
        menuMusic.setLooping(true);
        menuMusic.play();
    }

    private void createButtons(){
        buttonsAtlas = new TextureAtlas("Buttons/Buttons.pack");
        buttonUpNine = buttonsAtlas.createPatch("ButtonUp");
        buttonDownNine = buttonsAtlas.createPatch("ButtonDown");

        textButtonStyle.up = new NinePatchDrawable(buttonUpNine);
        textButtonStyle.down = new NinePatchDrawable(buttonDownNine);
        textButtonStyle.over = new NinePatchDrawable(buttonDownNine);
    }

    private void createBasicFont() {
        //Create a font
        BitmapFont font = new BitmapFont();
        skin.add("default", font);

        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 255, 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        menuItems = new Group();
        scoreItems = new Group();

        setupBackground();
        setupMusic();
        createButtons();
        createBasicFont();

        Gdx.input.setInputProcessor(stage);// Make the stage consume events

        final TextButton newGameButton = new TextButton("New game", skin); // Use the initialized skin
        final TextButton newHighscoreButton = new TextButton("Show Highscore", skin);
        final TextButton newExitButton = new TextButton("Exit", skin);

        final TextButton resetScoreButton = new TextButton("Reset Highscore", skin);
        final TextButton backButton = new TextButton("Back", skin);

        final Label scoreLabel = new Label("Score: " + Integer.toString(com.psychokitty.game.Utils.Highscore.getHighScore()), skin2);
        final Label scoreDate = new Label("Score Date: " + com.psychokitty.game.Utils.Highscore.getCurrentDate(), skin2);
        final Label Titel = new Label("Highscore", skin2);

        newGameButton.setPosition(Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2);
        newHighscoreButton.setPosition(Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2 - 100);
        newExitButton.setPosition(Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2 - 200);

        Titel.setPosition(Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2 + 200);
        scoreDate.setPosition(Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2 + 150);
        scoreLabel.setPosition(Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2 + 100);
        resetScoreButton.setPosition(Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2);
        backButton.setPosition(Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2 - 100);

        //make a group for score Items
        scoreItems.addActor(Titel);
        scoreItems.addActor(scoreDate);
        scoreItems.addActor(scoreLabel);
        scoreItems.addActor(resetScoreButton);
        scoreItems.addActor(backButton);

        //Make a group for menu items
        menuItems.addActor(newExitButton);
        menuItems.addActor(newHighscoreButton);
        menuItems.addActor(newGameButton);

        stage.addActor(menuItems);

        newGameButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                adcont.hideBannerAd();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(game, adcont));
            }
        });
        newHighscoreButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                menuItems.remove();
                stage.addActor(scoreItems);
            }
        });
        newExitButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                stage.addActor(menuItems);
                scoreItems.remove();
            }
        });
        resetScoreButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                com.psychokitty.game.Utils.Highscore.resetScore();
                scoreLabel.setText("Score: " + Integer.toString(com.psychokitty.game.Utils.Highscore.getHighScore()));
                scoreDate.setText("Score Date: " + com.psychokitty.game.Utils.Highscore.getCurrentDate());
            }
        });
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        menuMusic.dispose();
        buttonsAtlas.dispose();
    }
}