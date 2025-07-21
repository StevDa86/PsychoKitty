package com.psychokitty.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.psychokitty.game.GameObjects.Enemies;
import com.psychokitty.game.GameObjects.Items;
import com.psychokitty.game.GameObjects.Player;
import com.psychokitty.game.GameObjects.PowerItem;
import com.psychokitty.game.PsychoKittyGame;
import com.psychokitty.game.Utils.Assets;
import com.psychokitty.game.Utils.Constants;
import com.psychokitty.game.Utils.CustomDialog;
import com.psychokitty.game.Utils.Highscore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

/**
 * Created by steven on 25.07.15.
 */
public class GameScreen implements Screen, InputProcessor {

    public enum State {
        PAUSE,
        GAMEOVER,
        RUN,
        INTRO,
    }

    final PsychoKittyGame game;

    public com.psychokitty.game.AdMob.AdsController adcont;

    Player CatPlayer;
    Items CatFood;
    PowerItem PowerItems;
    Enemies Dog;
    float totalTime = 4; //starting at 3 seconds
    // Graphics components
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;

    // UI elements
    private BitmapFont font;
    private Skin skin2;

    // Audio
    private Sound catSound, catHiss, beepHigh, beepLow;
    private Music rainMusic, psychoMusic;

    // Game state
    private State state = State.INTRO;
    private Highscore highscore;
    private int score = 0;
    private String scorename;
    private int lives = 3;
    private long startTime, time;
    private boolean ads_Active = false;
    private boolean crazymode = false;

    // Game objects and properties
    private float crazyModeTimer = 0f;
    private static final float CRAZY_MODE_DURATION = 10f; // 10 seconds of crazy mode

    private int crazySpeed, backgroundSpeed;
    private int HeartSize = 30, SoundCounter = 0;
    private int treeWidth, treeHeight;
    private Texture Hearts, Number3, Number2, Number1;
    private Texture background, foreground, tree, crazyBackground, tabicon;

    int heartX = Constants.NATIVE_WIDTH - 200;
    int heartY = Constants.NATIVE_HEIGHT - HeartSize - 20;

    float timeState;

    private Assets assets = new Assets();

    public GameScreen(final PsychoKittyGame gam, com.psychokitty.game.AdMob.AdsController adsController) {
        this.game = gam;
        this.adcont = adsController;

        if(this.adcont == null) {
            Gdx.app.log("GameScreen", "AdMob controller is null, ads will not be shown.");
        }


        assets.load();
        assets.manager.finishLoading();

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false);

        viewport = new FitViewport(Constants.NATIVE_WIDTH, Constants.NATIVE_HEIGHT, camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
        stage = new Stage(viewport, batch);

        highscore = new com.psychokitty.game.Utils.Highscore();
        highscore.config();

        Gdx.input.setInputProcessor(this);

        // load the drop sound effect and the rain background "music"
        catSound = assets.manager.get(Assets.sSoundMiau);
        catHiss = assets.manager.get(Assets.sCatHiss);
        rainMusic = assets.manager.get(Assets.sMusicDream);
        psychoMusic = assets.manager.get(Assets.sMusicPsycho);
        beepHigh = assets.manager.get(Assets.sBeepHigh);
        beepLow = assets.manager.get(Assets.sBeepLow);

        rainMusic.setLooping(true);

        Number3 = assets.manager.get(Assets.Count3);
        Number2 = assets.manager.get(Assets.Count2);
        Number1 = assets.manager.get(Assets.Count1);

        // Now initialize skin2 after assets are loaded
        skin2 = new Skin(Gdx.files.internal(Constants.defaultJson));

        CatPlayer = new Player();
        CatPlayer.createPlayer();

        CatFood = new Items();
        CatFood.createItems();

        PowerItems = new PowerItem();
        PowerItems.createItems();

        Dog = new Enemies();
        Dog.createEnemies();

        //Text definition
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/OpenSans-Light.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1;
        font = generator.generateFont(parameter);
        generator.dispose();

        scorename = "Score:" + score;

        //Background to Black!
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tabicon = assets.manager.get(Assets.tabIcon);

        background = assets.manager.get(Assets.BackgroundImage);
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        crazyBackground = assets.manager.get(Assets.crazyBackground);
        crazyBackground.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        foreground = assets.manager.get(Assets.ForegroundImage);
        foreground.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        tree = assets.manager.get(Assets.TreeImage);
        treeWidth = tree.getWidth();
        treeHeight = tree.getHeight();
        Hearts = assets.manager.get(Assets.Hearts);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }

    @Override
    public void render(float delta) {

        // Clear the screen with black color every frame to fix letterbox corruption
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (assets == null || !assets.manager.update()) {
            return;
        }

        // Add this check to ensure skin2 is initialized
        if (skin2 == null) {
            skin2 = new Skin(Gdx.files.internal(Constants.defaultJson));
        }

        float deltaTime = Gdx.graphics.getDeltaTime(); //You might prefer getRawDeltaTime()
        totalTime -= deltaTime; //if counting down
        int seconds = ((int) totalTime) % 60;

        batch.setProjectionMatrix(camera.combined);

        // begin a new batch and draw
        batch.begin();

        backgroundSpeed += 1;
        crazySpeed += 3;


        //Test crazy mode for music long
        if (crazymode) {
            crazyModeTimer += Gdx.graphics.getDeltaTime();

            if (crazyModeTimer < CRAZY_MODE_DURATION) {
                // Crazy mode is active
                batch.draw(crazyBackground, 0, 0, crazySpeed, 0, Constants.NATIVE_WIDTH, Constants.NATIVE_HEIGHT);
                batch.draw(tree, Constants.NATIVE_WIDTH - 250, 30, 0, 0, treeWidth, treeHeight);
                batch.draw(foreground, 0, 0, crazySpeed, 0, Constants.NATIVE_WIDTH, 32);

                if (!psychoMusic.isPlaying()) {
                    rainMusic.pause();
                    psychoMusic.setLooping(true);
                    psychoMusic.play();
                }
            } else {
                // Crazy mode duration exceeded, turn it off
                crazymode = false;
                crazyModeTimer = 0f;
                psychoMusic.stop();
                if (!rainMusic.isPlaying()) {
                    rainMusic.play();
                }
            }
        } else {
            // Normal mode
            batch.draw(background, 0, 0, backgroundSpeed, 0, Constants.NATIVE_WIDTH, Constants.NATIVE_HEIGHT);
            batch.draw(tree, Constants.NATIVE_WIDTH - 250, 30, 0, 0, treeWidth, treeHeight);
            batch.draw(foreground, 0, 0, 0, 0, Constants.NATIVE_WIDTH, 32);

            if (!rainMusic.isPlaying()) {
                rainMusic.play();
            }
        }


        //Draw Tab Icons
        batch.draw(tabicon, 10, 10);
        batch.draw(tabicon, Constants.NATIVE_WIDTH - 60, 10);

        CatPlayer.renderPlayer(batch, camera);

        //lebensanzeige als Herzen
        for (int i = 0; i < lives; i++) {
            batch.draw(Hearts, heartX + i * 50, heartY, HeartSize, HeartSize);
        }


        //If abfrage für 3 Sekunden Zeitanzeige
        if (seconds < totalTime) {
            //font.draw(batch, seconds+1 + " Sekunden", 500, 500);
            if (seconds == 2) {
                batch.draw(Number3, (Constants.NATIVE_WIDTH / 2) - 100, (Constants.NATIVE_HEIGHT / 2) - 100, 200, 200);
                if (SoundCounter == 0) {
                    beepLow.play(0.3f);
                    SoundCounter++;
                }

            }
            if (seconds == 1) {
                batch.draw(Number2, (Constants.NATIVE_WIDTH / 2) - 100, (Constants.NATIVE_HEIGHT / 2) - 100, 200, 200);
                if (SoundCounter == 1) {
                    beepLow.play(0.3f);
                    SoundCounter++;
                }
            }
            if (seconds == 0) {
                batch.draw(Number1, (Constants.NATIVE_WIDTH / 2) - 100, (Constants.NATIVE_HEIGHT / 2) - 100, 200, 200);
                if (SoundCounter == 2) {
                    beepHigh.play(0.3f);
                    SoundCounter++;
                }
            }
        }
        // if abfrage wenn 3 Sekunden vergangen sind, zeichne spiel
        else {
            CatFood.renderItems(batch);
            PowerItems.renderItems(batch);
            Dog.renderEnemies(batch);
            font.draw(batch, scorename, 20, Constants.NATIVE_HEIGHT - 20);

        }
        batch.end();

        switch (state) {
            //Intro counter
            case INTRO: {
                this.state = State.RUN;
                break;
            }

            case RUN: {
                //Drop Burger
                if (time - startTime <= 4000) { //5 sekunde warten bis erster Drop.
                    time = TimeUtils.millis();
                } else if (TimeUtils.nanoTime() - CatFood.getLastDropTime() > 700000000)
                    CatFood.spawnItems();
                Iterator<Rectangle> iter = CatFood.getArray().iterator();
                while (iter.hasNext()) {
                    Rectangle Items = iter.next();
                    Items.y -= (200 + score * 5) * Gdx.graphics.getDeltaTime(); //geschwindigkeit
                    if (Items.y + 30 < 0) iter.remove();
                    if (Items.overlaps(CatPlayer.getRectangle())) {
                        catSound.play();
                        score++;
                        scorename = "Score: " + score;
                        iter.remove();
                    }
                }

                //Drop PowerItem
                if (time - startTime <= 4000) { //5 sekunde warten bis erster Drop.
                    time = TimeUtils.millis();
                } else if (TimeUtils.nanoTime() - PowerItems.getLastDropTime() > 20000000000f ) //anzahl der drops
                    PowerItems.spawnItems();
                Iterator<Rectangle> iter3 = PowerItems.getArray().iterator();
                while (iter3.hasNext()) {
                    Rectangle Items = iter3.next();
                    Items.y -= (200 + score * 5) * Gdx.graphics.getDeltaTime(); //geschwindigkeit
                    if (Items.y + 30 < 0) iter3.remove();
                    if (Items.overlaps(CatPlayer.getRectangle())) {
                        catSound.play();
                        score = score +10;
                        scorename = "Score: " + score;
                        iter3.remove();
                        crazymode = true;
                    }
                }


                //DropDogs
                if (time - startTime <= 4000) {
                    time = TimeUtils.millis();
                } else if (TimeUtils.nanoTime() - Dog.getLastDropTime() > 1000000000)
                    Dog.spawnDog();
                Iterator<Rectangle> iter2 = Dog.getArray().iterator();
                while (iter2.hasNext()) {
                    Rectangle Items2 = iter2.next();
                    Items2.y -= (250 + score * 5) * Gdx.graphics.getDeltaTime();
                    if (Items2.y + 50 < 0) iter2.remove();
                    if (Items2.overlaps(CatPlayer.getRectangle())) {
                        Gdx.input.vibrate(100);
                        iter2.remove();
                        //only remove hearts wihtout crazy mode
                        if(crazymode == false) {
                            catHiss.play();
                            lives--;
                        }
                        if (lives == 0) {
                            GameOverState();
                        }

                    }
                }
                break;
            }

            case PAUSE: {
                stage.draw();
                ExitGame();
                break;
            }

            case GAMEOVER: {
                stage.act(delta);//update all actors
                stage.draw();
                GameOver();
                break;
            }
        }
    }

    @Override
    public void show() {
        startTime = TimeUtils.millis();
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
        this.state = State.PAUSE;
    }

    @Override
    public void resume() {
        Gdx.input.setInputProcessor(this);
        stage.clear();
        this.state = State.RUN;
    }

    @Override
    public void dispose() {
        font.dispose();
        batch.dispose();
        CatPlayer.disposePlayer();
        CatFood.disposeItems();
        PowerItems.disposeItems();
        Dog.disposeEnemies();
        stage.dispose();
        Hearts.dispose();
        rainMusic.dispose();
        assets.dispose();
        psychoMusic.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACK) {
            pause();
        } else if (keycode == Input.Keys.ESCAPE) {
            pause();
        }
        return false;
    }

    public void GameOverState() {
        this.state = State.GAMEOVER;
    }

    public void ExitGame() {
        if (skin2 != null && adcont != null) {
            Gdx.input.setInputProcessor(stage);

            if (adcont.isWifiConnected() && !ads_Active) {
                adcont.showBannerAd();
                ads_Active = true;
            }

            new CustomDialog("Exit game", skin2).text("Exit game?")
                    .button("Yes", new InputListener() {
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            // Store final variables for use in anonymous class
                            final int finalScore = score;
                            final com.psychokitty.game.AdMob.AdsController finalAdcont = adcont;

                            // Highscore setzen und datum setzen
                            if (finalScore > com.psychokitty.game.Utils.Highscore.getHighScore()) {
                                com.psychokitty.game.Utils.Highscore.setHighScore(finalScore);
                                Calendar currentDate = Calendar.getInstance();
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MMM/dd");
                                String dateNow = formatter.format(currentDate.getTime());
                                com.psychokitty.game.Utils.Highscore.setCurrentDate(dateNow);
                            }

                            // Properly exit the application
                            if (finalAdcont != null) {
                                finalAdcont.hideBannerAd();
                            }

                            // For desktop version, exit the application completely
                            Gdx.app.exit();
                            return false;
                        }
                    })
                    .button("No", new InputListener() {
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            // Store final reference for anonymous class
                            final com.psychokitty.game.AdMob.AdsController finalAdcont = adcont;

                            if (finalAdcont != null) {
                                finalAdcont.hideBannerAd();
                            }
                            ads_Active = false;
                            resume();
                            return false;
                        }
                    }).show(stage);
        }
    }

    public void GameOver() {
        if (adcont != null && skin2 != null) {
            Gdx.input.setInputProcessor(stage);

            if (adcont.isWifiConnected() && !ads_Active) {
                adcont.showBannerAd();
                ads_Active = true;
            }

            String gameOverMessage = "Game Over";
            String scoreText = Constants.scoreTXT + score;

            new CustomDialog(gameOverMessage, skin2)
                    .text(scoreText)
                    .button("EXIT", new InputListener() {
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            // Store final variables for use in anonymous class
                            final int finalScore = score;
                            final PsychoKittyGame finalGame = game;
                            final com.psychokitty.game.AdMob.AdsController finalAdcont = adcont;

                            // Update high score and current date
                            if (finalScore > com.psychokitty.game.Utils.Highscore.getHighScore()) {
                                com.psychokitty.game.Utils.Highscore.setHighScore(finalScore);
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MMM/dd");
                                String dateNow = formatter.format(Calendar.getInstance().getTime());
                                com.psychokitty.game.Utils.Highscore.setCurrentDate(dateNow);
                            }

                            // Stop all music before transitioning
                            if (rainMusic != null && rainMusic.isPlaying()) {
                                rainMusic.stop();
                            }
                            if (psychoMusic != null && psychoMusic.isPlaying()) {
                                psychoMusic.stop();
                            }

                            if (finalAdcont != null) {
                                finalAdcont.hideBannerAd();
                            }

                            // Don't call dispose() here - let LibGDX handle it
                            if (finalGame != null && finalAdcont != null) {
                                ((Game) Gdx.app.getApplicationListener()).setScreen(new com.psychokitty.game.Screens.MenuScreen(finalGame, finalAdcont));
                            }
                            return false;
                        }
                    })
                    .show(stage);
        }
    }

@Override
public boolean keyUp(int keycode) {
    return false;
}

@Override
public boolean keyTyped(char character) {
    return false;
}

@Override
public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    return false;
}

@Override
public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    return false;
}

@Override
public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
    return false;
}

@Override
public boolean touchDragged(int screenX, int screenY, int pointer) {
    return false;
}

@Override
public boolean mouseMoved(int screenX, int screenY) {
    return false;
}

@Override
public boolean scrolled(float amountX, float amountY) {
    return false;
}
}
