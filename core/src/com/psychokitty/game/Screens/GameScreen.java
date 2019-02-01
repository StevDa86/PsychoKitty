package com.psychokitty.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
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
    Enemies Dog;
    float totalTime = 3; //starting at 3 seconds
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture background, foreground;
    private Sound catSound, catHiss, beepHigh, beepLow;
    private Music rainMusic;
    private OrthographicCamera camera;
    private Viewport viewport;
    private Highscore highscore;
    private int score = 0, backgroundSpeed, lives = 3,HeartSize = 25, SoundCounter = 0;
    private String scorename, lives_text;
    private Texture Hearts, Number3, Number2, Number1;
    private long startTime, time;
    private Skin skin2 = new Skin(Gdx.files.internal(Constants.defaultJson));
    private Stage stage = new Stage();
    private State state = State.INTRO;
    private float HeartPlace;

    public GameScreen(final PsychoKittyGame gam, com.psychokitty.game.AdMob.AdsController adsController) {
        this.game = gam;
        adcont = adsController;
        batch = new SpriteBatch();
        highscore = new com.psychokitty.game.Utils.Highscore();
        highscore.config();

        Gdx.input.setInputProcessor(this);

        // load the drop sound effect and the rain background "music"
        catSound = Gdx.audio.newSound(Gdx.files.internal(com.psychokitty.game.Utils.Constants.soundMiau));
        catHiss = Gdx.audio.newSound(Gdx.files.internal(Constants.catHiss));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal(com.psychokitty.game.Utils.Constants.musicDream));
        beepHigh = Gdx.audio.newSound(Gdx.files.internal(Constants.beepHigh));
        beepLow = Gdx.audio.newSound(Gdx.files.internal(Constants.beepLow));

        rainMusic.setLooping(true);
        rainMusic.play();

        Number3 = new Texture(Assets.Count3);
        Number2 = new Texture(Assets.Count2);
        Number1 = new Texture(Assets.Count1);

        CatPlayer = new Player();
        CatPlayer.createPlayer();

        CatFood = new Items();
        CatFood.createItems();

        Dog = new Enemies();
        Dog.CreateEnemies();

        //Text definition
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/OpenSans-Light.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) (22 * Gdx.graphics.getDensity());
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1;
        font = generator.generateFont(parameter);
        generator.dispose();


        scorename = "Score:" + score;
        lives_text = "Lives:" + lives;

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        camera.translate(camera.viewportWidth / 2, camera.viewportHeight / 2);

        background = new Texture(Gdx.files.internal(com.psychokitty.game.Utils.Constants.backgroundImage));
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        foreground = new Texture(Gdx.files.internal(com.psychokitty.game.Utils.Constants.foregroundImage));

        Hearts = new Texture(Gdx.files.internal(Constants.heartImage));
        HeartPlace = HeartSize*2 * Gdx.graphics.getDensity();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }


    @Override
    public void render(float delta) {
        camera.update();
        batch.setProjectionMatrix(stage.getCamera().combined);

        float deltaTime = Gdx.graphics.getDeltaTime(); //You might prefer getRawDeltaTime()
        totalTime -= deltaTime; //if counting down
        int seconds = ((int)totalTime) % 60;

        // begin a new batch and draw
        batch.begin();

        backgroundSpeed -= 1;
        batch.draw(background, 0, 0, 0, backgroundSpeed, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(foreground, 0, 0, Gdx.graphics.getWidth(), 200);

        CatPlayer.renderPlayer(batch);

        //lebensanzeige als Herzen
        if (lives == 3) {
            batch.draw(Hearts, Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 60, HeartPlace, HeartPlace);
            batch.draw(Hearts, Gdx.graphics.getWidth() - 150, Gdx.graphics.getHeight() - 60, HeartPlace, HeartPlace);
            batch.draw(Hearts, Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() - 60, HeartPlace, HeartPlace);
        } else if (lives == 2) {
            batch.draw(Hearts, Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 60, HeartPlace, HeartPlace);
            batch.draw(Hearts, Gdx.graphics.getWidth() - 150, Gdx.graphics.getHeight() - 60, HeartPlace, HeartPlace);
        } else {
            batch.draw(Hearts, Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 60, HeartPlace, HeartPlace);
        }


        //If abfrage für 3 Sekunden Zeitanzeige
        if(seconds < totalTime) {
            //font.draw(batch, seconds+1 + " Sekunden", 500, 500);
            if(seconds == 2) {
                batch.draw(Number3, (Gdx.graphics.getWidth() / 2) - 150, (Gdx.graphics.getHeight() / 2) - 150, 300, 300);
                if (SoundCounter == 0) {
                    beepLow.play(0.3f);
                    SoundCounter++;
                }

            }
            if(seconds == 1) {
                batch.draw(Number2, (Gdx.graphics.getWidth() / 2) - 150, (Gdx.graphics.getHeight() / 2) - 150, 300, 300);
                if (SoundCounter == 1) {
                    beepLow.play(0.3f);
                    SoundCounter++;
                }
            }
            if(seconds == 0) {
                batch.draw(Number1, (Gdx.graphics.getWidth() / 2) - 150, (Gdx.graphics.getHeight() / 2) - 150, 300, 300);
                if (SoundCounter == 2) {
                    beepHigh.play(0.3f);
                    SoundCounter++;
                }
            }
        }
        // if abfrage wenn 3 Sekunden vergangen sind, zeichne spiel
        else {

            CatFood.renderItems(batch);
            Dog.RenderEnemies(batch);

            font.draw(batch, scorename, 20, Gdx.graphics.getHeight() - 20);
            //font.draw(batch, lives_text, Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 20);
        }
        batch.end();

        switch (state) {
            //Intro counter
            case INTRO: {
                this.state = State.RUN;
                break;
            }

            case RUN: {

                //Drop icons
                if (time - startTime <= 4000) { //5 sekunde warten bis erster Drop.
                    time = TimeUtils.millis();
                } else if (TimeUtils.nanoTime() - CatFood.getLastDropTime() > 800000000)
                    CatFood.spawnItems();
                Iterator<Rectangle> iter = CatFood.getArray().iterator();
                while (iter.hasNext()) {
                    Rectangle Items = iter.next();
                    Items.y -= (300 + score * 5) * Gdx.graphics.getDeltaTime(); //geschwindigkeit
                    if (Items.y + 64 < 0) iter.remove();
                    if (Items.overlaps(CatPlayer.getRectangle())) {
                        catSound.play();
                        score++;
                        scorename = "Score: " + score;
                        iter.remove();
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
                    Items2.y -= (350 + score * 5) * Gdx.graphics.getDeltaTime();
                    if (Items2.y + 50 < 0) iter2.remove();
                    if (Items2.overlaps(CatPlayer.getRectangle())) {
                        catHiss.play();
                        Gdx.input.vibrate(100);
                        iter2.remove();
                        lives--;
                        lives_text = "Lives:" + lives;
                        if (lives == 0) {
                            GameOverState();
                        }
                    }
                }
                break;
            }

            case PAUSE: {
                stage.act(delta);//update all actors
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
        rainMusic.dispose();
        CatPlayer.disposePlayer();
        CatFood.disposeItems();
        Dog.DisposeEnemies();
        catSound.dispose();
        background.dispose();
        foreground.dispose();
        stage.dispose();
        Hearts.dispose();
        Number3.dispose();
        beepHigh.dispose();
        beepLow.dispose();
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
        if (adcont.isWifiConnected()) {
            adcont.showBannerAd();
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
                        resume();
                        return false;
                    }
                }).show(stage);
    }

    public void GameOver() {

        Gdx.input.setInputProcessor(stage);
        if (adcont.isWifiConnected()) {
            adcont.showBannerAd();
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
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public enum State {
        PAUSE,
        GAMEOVER,
        RUN,
        INTRO,
    }
}
