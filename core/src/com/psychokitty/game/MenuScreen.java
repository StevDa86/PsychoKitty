package com.psychokitty.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by steven on 24.07.15.
 */
public class MenuScreen implements Screen {
    private Texture texture = new Texture(Gdx.files.internal(Constants.backgroundMenu));
    private Image splashImage = new Image(texture);
    private Stage stage = new Stage();
    private Music menuMusic = Gdx.audio.newMusic(Gdx.files.internal(Constants.musicMenu));

    final PsychoKittyGame game;

    public MenuScreen(final PsychoKittyGame gam) {
        game = gam;
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
        menuMusic.setVolume(1);
        menuMusic.play();
        splashImage.setPosition(Gdx.graphics.getWidth() / 2 - splashImage.getWidth() / 2, Gdx.graphics.getHeight() / 2 - splashImage.getHeight() / 2);
        stage.addActor(splashImage);

        splashImage.addAction(Actions.sequence(Actions.alpha(0)
                , Actions.fadeIn(1.0f), Actions.delay(2), Actions.run(new Runnable() {
            @Override
            public void run() {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(game));
            }
        })));
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
        texture.dispose();
        stage.dispose();
        menuMusic.dispose();
    }
}