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

/**
 * Created by steven on 24.07.15.
 */

public class SplashScreen implements Screen {

    private Texture texture = new Texture(Gdx.files.internal(com.psychokitty.game.Utils.Constants.splashImage));
    private Image splashImage = new Image(texture);
    private Stage stage = new Stage();
    private Music splashSound = Gdx.audio.newMusic(Gdx.files.internal(com.psychokitty.game.Utils.Constants.soundSplash));

    final PsychoKittyGame game;
    public com.psychokitty.game.AdMob.AdsController adcont;


    public SplashScreen(final PsychoKittyGame gam, com.psychokitty.game.AdMob.AdsController adsController) {
        game = gam;
        adcont = adsController;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }


    @Override
    public void show() {
        splashSound.setVolume(1);
        splashSound.play();

        splashImage.setPosition(Gdx.graphics.getWidth() / 2 - splashImage.getWidth() / 2, Gdx.graphics.getHeight() / 2 - splashImage.getHeight() / 2);
        stage.addActor(splashImage);

        splashImage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(2.0f),
                Actions.delay(3), Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new MenuScreen(game,adcont));
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
        splashSound.dispose();
    }

}
