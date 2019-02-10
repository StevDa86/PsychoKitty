package com.psychokitty.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.psychokitty.game.PsychoKittyGame;
import com.psychokitty.game.Utils.Assets;
import com.psychokitty.game.Utils.Constants;
import com.psychokitty.game.Utils.CustomDialog;


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
    private Skin skin2 = new Skin(Gdx.files.internal(Constants.defaultJson));

    private float screenwidth, screenheight;

    public MenuScreen(final PsychoKittyGame gam, com.psychokitty.game.AdMob.AdsController adsController) {
        game = gam;
        adcont = adsController;
        highscore = new com.psychokitty.game.Utils.Highscore();
        highscore.config();
        screenwidth = Gdx.graphics.getWidth();
        screenheight = Gdx.graphics.getHeight();
    }

    private void setupBackground() {
        menuBackground.setHeight(screenheight);
        menuBackground.setWidth(screenwidth);
        menuBackground.setScaling(Scaling.fillX);
        menuBackground.setPosition(screenwidth / 2 - menuBackground.getWidth() / 2, screenheight / 2 - menuBackground.getHeight() / 2);
        stage.addActor(menuBackground);
    }

    private void setupMusic() {
        menuMusic.setVolume(1);
        menuMusic.setLooping(true);
        menuMusic.play();
    }

    private void createButtons() {
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

        newGameButton.setPosition(screenwidth / 2 - newGameButton.getWidth() / 2, screenheight / 2);
        newHighscoreButton.setPosition(screenwidth / 2 - newHighscoreButton.getWidth() / 2, screenheight / 2 - 100);
        newExitButton.setPosition(screenwidth / 2 - newExitButton.getWidth() / 2, screenheight / 2 - 200);

        Titel.setPosition(screenwidth / 2 - Titel.getWidth() / 2, screenheight / 2 + 100);
        scoreDate.setPosition(screenwidth / 2 - scoreDate.getWidth() / 2, screenheight / 2 + 50);
        scoreLabel.setPosition(screenwidth / 2 - scoreLabel.getWidth() / 2, screenheight / 2 + 0);
        resetScoreButton.setPosition(screenwidth / 2 - resetScoreButton.getWidth() / 2, screenheight / 2 - 100);
        backButton.setPosition(screenwidth / 2 - backButton.getWidth() / 2, screenheight / 2 - 200);

        //make a group for score Items
        scoreItems.addActor(Titel);
        scoreItems.addActor(scoreDate);
        scoreItems.addActor(scoreLabel);
        scoreItems.addActor(resetScoreButton);
        scoreItems.addActor(backButton);

        final Table scoreTable = new Table(skin2);
        scoreTable.setColor(Color.BLACK);
        scoreTable.add(scoreItems);
        scoreTable.row();
        scoreTable.add(scoreItems);

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
                stage.addActor(scoreTable);
            }
        });
        newExitButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                ExitDialog();
            }
        });
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                stage.addActor(menuItems);
                scoreTable.remove();
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

    public void ExitDialog() {
        new CustomDialog("Exit game", skin2).text("Exit game?")
                .button("Yes", new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        Assets.dispose();
                        Gdx.app.exit();
                        return false;
                    }
                }).button("No").show(stage);
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