package com.psychokitty.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.psychokitty.game.PsychoKittyGame;
import com.psychokitty.game.Utils.Constants;
import com.psychokitty.game.AdMob.AdsController;

public class SplashScreen implements Screen {

    private final PsychoKittyGame game;
    private final AdsController adcont;
    private final Stage stage;
    private final Music splashSound;

    public SplashScreen(final PsychoKittyGame gam, AdsController adsController) {
        game = gam;
        adcont = adsController;
        stage = new Stage();
        splashSound = Gdx.audio.newMusic(Gdx.files.internal(Constants.soundSplash));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Handle resizing if needed.
    }

    @Override
    public void show() {
        splashSound.setVolume(1);
        splashSound.play();

        Texture texture = new Texture(Gdx.files.internal(Constants.splashImage));
        Image splashImage = new Image(texture);

        float imageX = Gdx.graphics.getWidth() / 2f - splashImage.getWidth() / 2f;
        float imageY = Gdx.graphics.getHeight() / 2f - splashImage.getHeight() / 2f;

        splashImage.setPosition(imageX, imageY);
        stage.addActor(splashImage);

        splashImage.addAction(
                Actions.sequence(
                        Actions.alpha(0),
                        Actions.fadeIn(2.0f),
                        Actions.delay(3),
                        Actions.run(() -> ((Game) Gdx.app.getApplicationListener()).setScreen(new MenuScreen(game, adcont)))
                )
        );
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void pause() {
        // Handle pause if needed.
    }

    @Override
    public void resume() {
        // Handle resume if needed.
    }

    @Override
    public void dispose() {
        stage.dispose();
        splashSound.dispose();
    }
}
