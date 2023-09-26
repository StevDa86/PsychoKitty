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

    final PsychoKittyGame game;

    public com.psychokitty.game.AdMob.AdsController adcont;

    Player CatPlayer;
    Items CatFood;
    PowerItem PowerItems;
    Enemies Dog;
    float totalTime = 4; //starting at 3 seconds
    private SpriteBatch batch;
    private BitmapFont font;
    private Sound catSound, catHiss, beepHigh, beepLow;
    private Music rainMusic, psychoMusic;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Highscore highscore;
    private int score = 0, crazySpeed, backgroundSpeed, lives = 3, HeartSize = 30, SoundCounter = 0, treeWidth, treeHeight, count;
    private String scorename;
    private Texture Hearts, Number3, Number2, Number1, background, foreground, tree, crazyBackground, tabicon;
    private long startTime, time;
    private final Skin skin2 = new Skin(Gdx.files.internal(Constants.defaultJson));
    private Stage stage;
    private State state = State.INTRO;
    private Boolean ads_Active = false;
    private Boolean crazymode = false;

    float timeState;

    private Assets assets = new Assets();

    public GameScreen(final PsychoKittyGame gam, com.psychokitty.game.AdMob.AdsController adsController) {
        this.game = gam;
        adcont = adsController;

        count = 1;

        assets.load();

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

        CatPlayer = new Player();
        CatPlayer.createPlayer();

        CatFood = new Items();
        CatFood.createItems();

        PowerItems = new PowerItem();
        PowerItems.createItems();

        Dog = new Enemies();
        Dog.CreateEnemies();

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
        Gdx.gl.glClearColor(0, 0, 0, 0);
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

        if (assets == null || !assets.manager.update()) {
            //maybe draw a load screen image that's not a texture that's being managed
            //by the assetManager. You could even play an animation. Otherwise,
            //you can leave the screen blank.
            return;
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
        if (crazymode && count < 800) {
            timeState += Gdx.graphics.getDeltaTime();
            if (timeState <= 2f) {
                crazymode = true;

                batch.draw(crazyBackground, 0, 0, crazySpeed, 0, Constants.NATIVE_WIDTH, Constants.NATIVE_HEIGHT);
                batch.draw(tree, Constants.NATIVE_WIDTH - 250, 30, 0, 0, treeWidth, treeHeight);

                batch.draw(foreground, 0, 0, crazySpeed, 0, Constants.NATIVE_WIDTH, 32);
                rainMusic.pause();
                psychoMusic.setLooping(true);
                psychoMusic.play();

                timeState = 0f;
                count++;
                Gdx.app.log("count", "Count:" + count);
            }
        } else {
            if (count == 800 ){count = 1;}
            crazymode = false;
            psychoMusic.stop();
            rainMusic.play();
            batch.draw(background, 0, 0, backgroundSpeed, 0, Constants.NATIVE_WIDTH, Constants.NATIVE_HEIGHT);
            batch.draw(tree, Constants.NATIVE_WIDTH - 250, 30, 0, 0, treeWidth, treeHeight);

            batch.draw(foreground, 0, 0, 0, 0, Constants.NATIVE_WIDTH, 32);
        }


        //Draw Tab Icons
        batch.draw(tabicon, 10, 10);
        batch.draw(tabicon, Constants.NATIVE_WIDTH - 60, 10);

        CatPlayer.renderPlayer(batch, camera);

        //lebensanzeige als Herzen
        if (lives == 3) {
            batch.draw(Hearts, Constants.NATIVE_WIDTH - 200, Constants.NATIVE_HEIGHT - HeartSize - 20, HeartSize, HeartSize);
            batch.draw(Hearts, Constants.NATIVE_WIDTH - 150, Constants.NATIVE_HEIGHT - HeartSize - 20, HeartSize, HeartSize);
            batch.draw(Hearts, Constants.NATIVE_WIDTH - 100, Constants.NATIVE_HEIGHT - HeartSize - 20, HeartSize, HeartSize);
        } else if (lives == 2) {
            batch.draw(Hearts, Constants.NATIVE_WIDTH - 200, Constants.NATIVE_HEIGHT - HeartSize - 20, HeartSize, HeartSize);
            batch.draw(Hearts, Constants.NATIVE_WIDTH - 150, Constants.NATIVE_HEIGHT - HeartSize - 20, HeartSize, HeartSize);
        } else {
            batch.draw(Hearts, Constants.NATIVE_WIDTH - 200, Constants.NATIVE_HEIGHT - HeartSize - 20, HeartSize, HeartSize);
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
            Dog.RenderEnemies(batch);
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
        Dog.DisposeEnemies();
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
        Gdx.input.setInputProcessor(stage);
        if (adcont.isWifiConnected() && !ads_Active) {
            adcont.showBannerAd();
            ads_Active = true;
        }
        new CustomDialog("Exit game", skin2).text("Exit game?")
                .button("Yes", new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        dispose();

                        //highscore setzen und datum setzen
                        if (score > com.psychokitty.game.Utils.Highscore.getHighScore()) {
                            com.psychokitty.game.Utils.Highscore.setHighScore(score);
                            Calendar currentDate = Calendar.getInstance(); //Get the current dat
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MMM/dd"); //format it as per your requirement
                            String dateNow = formatter.format(currentDate.getTime());
                            com.psychokitty.game.Utils.Highscore.setCurrentDate(dateNow);
                        }
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new com.psychokitty.game.Screens.MenuScreen(game, adcont));
                        return false;
                    }
                })
                .button("No", new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        adcont.hideBannerAd();
                        ads_Active = false;
                        resume();
                        return false;
                    }
                }).show(stage);
    }

    public void GameOver() {

        Gdx.input.setInputProcessor(stage);
        if (adcont.isWifiConnected() && !ads_Active) {
            adcont.showBannerAd();
            ads_Active = true;
        }
        new CustomDialog("Game Over", skin2).text(Constants.scoreTXT + score)
                .button("EXIT", new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        dispose();

                        //highscore setzen und datum setzen
                        if (score > com.psychokitty.game.Utils.Highscore.getHighScore()) {
                            com.psychokitty.game.Utils.Highscore.setHighScore(score);
                            Calendar currentDate = Calendar.getInstance(); //Get the current date
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MMM/dd"); //format it as per your requirement
                            String dateNow = formatter.format(currentDate.getTime());
                            com.psychokitty.game.Utils.Highscore.setCurrentDate(dateNow);
                        }
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new com.psychokitty.game.Screens.MenuScreen(game, adcont));
                        return false;
                    }
                })
                .show(stage);
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

    public enum State {
        PAUSE,
        GAMEOVER,
        RUN,
        INTRO,
    }
}
