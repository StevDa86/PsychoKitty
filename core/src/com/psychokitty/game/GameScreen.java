package com.psychokitty.game;

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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.psychokitty.game.Utils.Constants;
import com.psychokitty.game.Utils.CustomDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

/**
 * Created by steven on 25.07.15.
 */
public class GameScreen implements Screen, InputProcessor {

    final PsychoKittyGame game;
    public com.psychokitty.game.AdMob.AdsController adcont;
    public Array<Rectangle> catfood;
    public Array<Rectangle> dog;
    Vector2 touchPos;
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture dropImage;
    private Texture catImage;
    private Texture dogImage;
    private Sound catSound;
    private Sound catHiss;
    private Music rainMusic;
    private OrthographicCamera camera;
    private Viewport viewport;
    private com.psychokitty.game.Utils.Highscore highscore;
    private Rectangle cat;
    private float deltaTime;
    private int score;
    private String scorename;
    private Texture background;
    private Texture foreground;
    private int backgroundSpeed;
    private long lastDropTime;
    private Sprite catSprite;

    private int direction = 0;

    private Skin skin2 = new Skin(Gdx.files.internal(Constants.defaultJson));
    private Stage stage = new Stage();

    public enum State {
        PAUSE,
        RUN,
    }

    private State state = State.RUN;

    public GameScreen(final PsychoKittyGame gam, com.psychokitty.game.AdMob.AdsController adsController) {
        this.game = gam;
        adcont = adsController;
        batch = new SpriteBatch();
        highscore = new com.psychokitty.game.Utils.Highscore();
        highscore.config();

        Gdx.input.setInputProcessor(this);

        //Text definition
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/OpenSans-Light.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) (22 * Gdx.graphics.getDensity());
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1;
        font = generator.generateFont(parameter);
        generator.dispose();

        // load the images for the droplet and the cat, 64x64 pixels each
        dropImage = new Texture(Constants.catnipImage);
        catImage = new Texture(Constants.playerImage);
        dogImage = new Texture(Constants.dogImage);

        catSprite = new Sprite(catImage);

        // load the drop sound effect and the rain background "music"
        catSound = Gdx.audio.newSound(Gdx.files.internal(com.psychokitty.game.Utils.Constants.soundMiau));
        catHiss = Gdx.audio.newSound(Gdx.files.internal(Constants.catHiss));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal(com.psychokitty.game.Utils.Constants.musicDream));

        touchPos = new Vector2(Gdx.graphics.getWidth() / 2 - com.psychokitty.game.Utils.Constants.catsize / 2, 0);

        score = 0;
        scorename = "Score:" + 0;

        rainMusic.setLooping(true);
        rainMusic.play();

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        camera.translate(camera.viewportWidth / 2, camera.viewportHeight / 2);

        background = new Texture(Gdx.files.internal(com.psychokitty.game.Utils.Constants.backgroundImage));
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        foreground = new Texture(Gdx.files.internal(com.psychokitty.game.Utils.Constants.foregroundImage));

        catfood = new Array<Rectangle>();
        dog = new Array<Rectangle>();

        cat = new Rectangle();
        cat.x = Gdx.graphics.getWidth() / 2 - com.psychokitty.game.Utils.Constants.catsize / 2;
        cat.y = 20;
        cat.width = com.psychokitty.game.Utils.Constants.catsize;
        cat.height = com.psychokitty.game.Utils.Constants.catsize;


    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public void spawnItems() {
        Rectangle Items = new Rectangle();
        Items.x = MathUtils.random(0, Gdx.graphics.getWidth() - 64);
        Items.y = Gdx.graphics.getHeight();
        Items.width = 64;
        Items.height = 64;

        Rectangle Items2 = new Rectangle();
        Items2.x = MathUtils.random(0, Gdx.graphics.getWidth() - 100);
        Items2.y = Gdx.graphics.getHeight();
        Items2.width = 100;
        Items2.height = 100;


        catfood.add(Items);
        dog.add(Items2);
        lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(float delta) {
        deltaTime = Gdx.graphics.getDeltaTime();

        camera.update();
        batch.setProjectionMatrix(stage.getCamera().combined);

        // begin a new batch and draw
        batch.begin();
        backgroundSpeed -= 1;
        batch.draw(background, 0, 0, 0, backgroundSpeed, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(foreground, 0, 0, Gdx.graphics.getWidth(), 300);
        font.draw(batch, scorename, 20, Gdx.graphics.getHeight() - 20);
        batch.draw(catSprite, cat.x, cat.y, com.psychokitty.game.Utils.Constants.catsize, com.psychokitty.game.Utils.Constants.catsize);

        for (Rectangle Items : catfood) {
            batch.draw(dropImage, Items.x, Items.y, 80, 80);

        }
        for (Rectangle Items2 : dog){batch.draw(dogImage, Items2.x, Items2.y, 100, 100);}
        batch.end();


        switch (state) {
            case RUN: {
                //setup user interaction
                if (Gdx.input.isTouched()) {
                    touchPos.set(Gdx.input.getX() - 32, Gdx.input.getY());
                }
                //Move right
                if (touchPos.x > cat.x) {

                    cat.x += com.psychokitty.game.Utils.Constants.catspeed * deltaTime;
                    if (direction == 1) {
                        direction = 0;
                        catSprite.flip(true, false);
                    }
                }
                //move left
                else if (touchPos.x < cat.x) {

                    cat.x -= com.psychokitty.game.Utils.Constants.catspeed * deltaTime;
                    if (direction == 0) {
                        direction = 1;
                        catSprite.flip(true, false);
                    }
                }

                if (Math.abs(touchPos.x - cat.x) < 5)
                    cat.x = touchPos.x;
                //Katze am rand aufhalten
                if (cat.x > Gdx.graphics.getWidth() - 100)
                    cat.x = Gdx.graphics.getWidth() - 100;
                if (cat.x < 0)
                    cat.x = 0;

                //Drop icons
                if (TimeUtils.nanoTime() - lastDropTime > 800000000) spawnItems();

                Iterator<Rectangle> iter = catfood.iterator();
                while (iter.hasNext()) {
                    Rectangle Items = iter.next();
                    Items.y -= 300 * Gdx.graphics.getDeltaTime();
                    if (Items.y + 64 < 0) iter.remove();
                    if (Items.overlaps(cat)) {
                        catSound.play();
                        score++;
                        scorename = "Score: " + score;
                        iter.remove();
                    }
                }

                Iterator<Rectangle> iter2 = dog.iterator();
                while (iter2.hasNext()) {
                    Rectangle Items2 = iter2.next();
                    Items2.y -= 350 * Gdx.graphics.getDeltaTime();
                    if (Items2.y + 100 < 0) iter2.remove();
                    if (Items2.overlaps(cat)) {
                        catHiss.play();
                        iter2.remove();
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
        }
    }

    @Override
    public void show() {
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
        dropImage.dispose();
        dogImage.dispose();
        catImage.dispose();
        catSound.dispose();
        background.dispose();
        foreground.dispose();
        stage.dispose();
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
                            Calendar currentDate = Calendar.getInstance(); //Get the current date
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MMM/dd"); //format it as per your requirement
                            String dateNow = formatter.format(currentDate.getTime());
                            com.psychokitty.game.Utils.Highscore.setCurrentDate(dateNow);
                        }
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new MenuScreen(game, adcont));
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
}
