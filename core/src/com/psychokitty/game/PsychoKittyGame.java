package com.psychokitty.game;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Iterator;

public class PsychoKittyGame extends ApplicationAdapter {
    private SpriteBatch batch;
    private BitmapFont font;

    private Texture dropImage;
    private Texture catImage;

    private Sound dropSound;
    private Music rainMusic;

    private OrthographicCamera camera;
    private Viewport viewport;

    private Rectangle cat;

    private Array<Rectangle> raindrops;
    private long lastDropTime;

    private int score;
    private String scorename;

    private Texture background;
    private Texture foreground;
    private int srcy;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2);

        // load the images for the droplet and the cat, 64x64 pixels eachs
        dropImage = new Texture(Gdx.files.internal("Characters/droplet.png"));
        catImage = new Texture(Gdx.files.internal("Characters/cat.png"));

        // load the drop sound effect and the rain background "music"
        dropSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/cat.mp3"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("Music/dream.mp3"));

        score = 0;
        scorename = "Score:" + 0;

        rainMusic.setLooping(true);
        rainMusic.play();

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //camera.setToOrtho(false, 700, 480);
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        camera.translate(camera.viewportWidth / 2, camera.viewportHeight / 2);

        background = new Texture(Gdx.files.internal("Backgrounds/SC1BG.jpg"));
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        foreground = new Texture(Gdx.files.internal("Backgrounds/FGSC1.png"));




        cat = new Rectangle();
        cat.x = Gdx.graphics.getWidth() / 2 - 64 / 2;
        cat.y = 20;
        cat.width = 64;
        cat.height = 64;

        raindrops = new Array<Rectangle>();
        spawnRaindrop();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);

    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        font.dispose();
        batch.dispose();
        rainMusic.dispose();
        dropImage.dispose();
        catImage.dispose();
        dropSound.dispose();
        background.dispose();
        foreground.dispose();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();


        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        //Zeichne hintergründe
        batch.draw(background, 0, 0, 0, srcy, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(foreground,0,0,Gdx.graphics.getWidth(),300);

        srcy -= 1;
        font.draw(batch, scorename, 20, Gdx.graphics.getHeight() - 20);
        batch.draw(catImage, cat.x, cat.y,100,100);
        for (Rectangle raindrop : raindrops) {
            batch.draw(dropImage, raindrop.x, raindrop.y,80,80);
        }
        batch.end();

        //setup user interaction
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            cat.x = touchPos.x - 64 / 2;
        }

        //Raindrops

        if (TimeUtils.nanoTime() - lastDropTime > 800000000) spawnRaindrop();

        Iterator<Rectangle> iter = raindrops.iterator();
        while (iter.hasNext()) {
            Rectangle raindrop = iter.next();
            raindrop.y -= 300 * Gdx.graphics.getDeltaTime();
            if (raindrop.y + 64 < 0) iter.remove();
            if (raindrop.overlaps(cat)) {
                dropSound.play();
                score++;
                scorename = "Score: " + score;
                iter.remove();
            }

        }
    }

    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, Gdx.graphics.getWidth() - 64);
        raindrop.y = Gdx.graphics.getHeight();
        raindrop.width = 64;
        raindrop.height = 64;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }

}