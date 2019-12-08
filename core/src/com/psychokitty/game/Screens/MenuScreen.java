package com.psychokitty.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.psychokitty.game.PsychoKittyGame;
import com.psychokitty.game.Utils.Constants;
import com.psychokitty.game.Utils.CustomDialog;

/**
 * Created by steven on 24.07.15.
 */
public class MenuScreen implements Screen {

    final PsychoKittyGame game;
    public com.psychokitty.game.AdMob.AdsController adcont;
    private Stage stage;
    private Music menuMusic = Gdx.audio.newMusic(Gdx.files.internal(com.psychokitty.game.Utils.Constants.musicMenu));
    private Texture texture = new Texture(Gdx.files.internal(com.psychokitty.game.Utils.Constants.backgroundMenu));
    private Image menuBackground = new Image(texture);
    private com.psychokitty.game.Utils.Highscore highscore;
    private Group scoreItems, menuItems;

    private TextureAtlas buttonsAtlas;
    private NinePatch buttonUpNine, buttonDownNine;
    private TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
    private int buttonwidth, buttonheight;
    private ImageButton startButton, scoreButton, exitButton;

    private Skin skin = new Skin();
    private Skin skin2 = new Skin(Gdx.files.internal(Constants.defaultJson));

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;

    public MenuScreen(final PsychoKittyGame gam, com.psychokitty.game.AdMob.AdsController adsController) {
        game = gam;
        adcont = adsController;
        highscore = new com.psychokitty.game.Utils.Highscore();
        highscore.config();

        buttonwidth = 200;
        buttonheight = 70;

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.NATIVE_WIDTH, Constants.NATIVE_HEIGHT, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport, batch);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
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

        final TextButton newHighscoreButton = new TextButton("Show Highscore", skin);
        final TextButton newExitButton = new TextButton("Exit", skin);

        final TextButton resetScoreButton = new TextButton("Reset Highscore", skin);
        final TextButton backButton = new TextButton("Back", skin);

        final Label scoreLabel = new Label("Score: " + Integer.toString(com.psychokitty.game.Utils.Highscore.getHighScore()), skin2);
        final Label scoreDate = new Label("Score Date: " + com.psychokitty.game.Utils.Highscore.getCurrentDate(), skin2);



        resetScoreButton.setSize(buttonwidth, buttonheight);
        backButton.setSize(buttonwidth, buttonheight);

        //Start Button als Images
        Texture startBtnTexture = new Texture(Gdx.files.internal(Constants.startButton));
        Texture startBtnPressedTexture = new Texture(Gdx.files.internal(Constants.startButtonPressed));
        startButton = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(startBtnTexture)),
                new TextureRegionDrawable(new TextureRegion(startBtnPressedTexture))
        );
        startButton.setSize(buttonwidth,buttonheight);
        startButton.setPosition(Constants.NATIVE_WIDTH / 2 - buttonwidth / 2, Constants.NATIVE_HEIGHT / 2);  //hikeButton is an ImageButton

        //Score Button als Images
        Texture scoreBtnTexture = new Texture(Gdx.files.internal(Constants.scoreButton));
        Texture scoreBtnPressedTexture = new Texture(Gdx.files.internal(Constants.scoreButtonPressed));
        scoreButton = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(scoreBtnTexture)),
                new TextureRegionDrawable(new TextureRegion(scoreBtnPressedTexture))
        );
        scoreButton.setSize(buttonwidth,buttonheight);
        scoreButton.setPosition(Constants.NATIVE_WIDTH / 2 - buttonwidth / 2, Constants.NATIVE_HEIGHT / 2 - 100);  //hikeButton is an ImageButton

        //Exit Button als Images
        Texture exitBtnTexture = new Texture(Gdx.files.internal(Constants.exitButton));
        Texture exitBtnPressedTexture = new Texture(Gdx.files.internal(Constants.exitButtonPressed));
        exitButton = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(exitBtnTexture)),
                new TextureRegionDrawable(new TextureRegion(exitBtnPressedTexture))
        );
        exitButton.setSize(buttonwidth,buttonheight);
        exitButton.setPosition(Constants.NATIVE_WIDTH / 2 - buttonwidth / 2, Constants.NATIVE_HEIGHT / 2 - 200);  //hikeButton is an ImageButton


        scoreDate.setPosition(Constants.NATIVE_WIDTH / 2 - scoreDate.getWidth() / 2, Constants.NATIVE_HEIGHT / 2 + 50);
        scoreLabel.setPosition(Constants.NATIVE_WIDTH / 2 - scoreLabel.getWidth() / 2, Constants.NATIVE_HEIGHT / 2 + 0);
        resetScoreButton.setPosition(Constants.NATIVE_WIDTH / 2 - resetScoreButton.getWidth() / 2, Constants.NATIVE_HEIGHT / 2 - 100);
        backButton.setPosition(Constants.NATIVE_WIDTH / 2 - backButton.getWidth() / 2, Constants.NATIVE_HEIGHT / 2 - 200);

        //make a group for score Items
        scoreItems.addActor(scoreDate);
        scoreItems.addActor(scoreLabel);
        scoreItems.addActor(resetScoreButton);
        scoreItems.addActor(backButton);

        final Table scoreTable = new Table(skin2);
        scoreTable.setColor(Color.BLACK);
        scoreTable.add(scoreItems);
        scoreTable.row();
        scoreTable.add(scoreItems);

        menuItems.addActor(startButton);
        menuItems.addActor(scoreButton);
        menuItems.addActor(exitButton);
        stage.addActor(menuItems);

        startButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                adcont.hideBannerAd();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(game, adcont));
            }
        });
        scoreButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                menuItems.remove();
                stage.addActor(scoreTable);
            }
        });
        exitButton.addListener(new ClickListener() {
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
        batch.dispose();
    }


    private void setupBackground() {
        menuBackground.setHeight(Constants.NATIVE_HEIGHT);
        menuBackground.setWidth(Constants.NATIVE_WIDTH);
        menuBackground.setScaling(Scaling.fillX);
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

    public void ExitDialog() {
        new CustomDialog("Exit game", skin2).text("Exit game?")
                .button("Yes", new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        Gdx.app.exit();
                        return false;
                    }
                }).button("No").show(stage);
    }
}