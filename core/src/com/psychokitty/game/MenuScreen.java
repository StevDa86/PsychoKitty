package com.psychokitty.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;

/**
 * Created by steven on 24.07.15.
 */
public class MenuScreen implements Screen {

    private Stage stage = new Stage();
    private Music menuMusic = Gdx.audio.newMusic(Gdx.files.internal(Constants.musicMenu));

    private Texture texture = new Texture(Gdx.files.internal(Constants.backgroundMenu));
    private Image menuBackground = new Image(texture);


    private Skin skin;

    final PsychoKittyGame game;
    public AdsController adcont;

    private void createBasicSkin() {
        //Create a font
        BitmapFont font = new BitmapFont();
        skin = new Skin();
        skin.add("default", font);


        //Create a texture
        Pixmap pixmap = new Pixmap((int) Gdx.graphics.getWidth() / 2, (int) Gdx.graphics.getHeight() / 10, Pixmap.Format.RGB888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("Buttons", new Texture(pixmap));

        //Create a button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("Buttons", Color.GRAY);
        textButtonStyle.down = skin.newDrawable("Buttons", Color.DARK_GRAY);
        textButtonStyle.over = skin.newDrawable("Buttons", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);

    }

    public MenuScreen(final PsychoKittyGame gam, AdsController adsController) {
        game = gam;
        adcont = adsController;

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
        menuMusic.setLooping(true);
        menuMusic.play();

        menuBackground.setHeight(Gdx.graphics.getHeight());
        menuBackground.setScaling(Scaling.fillY);
        menuBackground.setPosition(Gdx.graphics.getWidth() / 2 - menuBackground.getWidth() / 2, Gdx.graphics.getHeight() / 2 - menuBackground.getHeight() / 2);
        stage.addActor(menuBackground);

        Gdx.input.setInputProcessor(stage);// Make the stage consume events

        createBasicSkin();
        TextButton newGameButton = new TextButton("New game", skin); // Use the initialized skin
        TextButton newDonateButton = new TextButton("Donate Paypal", skin);
        TextButton newExitButton = new TextButton("Exit", skin);

        newGameButton.setPosition(Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2 + 100);
        newDonateButton.setPosition(Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2);
        newExitButton.setPosition(Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2 - 100);

        stage.addActor(newGameButton);
        stage.addActor(newDonateButton);
        stage.addActor(newExitButton);

        newGameButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                adcont.hideBannerAd();
                // Do something interesting here...
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen(game, adcont));
            }
        });
        newDonateButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                // Do something interesting here...
                Gdx.net.openURI("https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=steven%2edanz%40t%2donline%2ede&lc=DE&item_name=Psycho%20Kitty&no_note=0&currency_code=EUR&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHostedGuest");
            }
        });
        newExitButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                // Do something interesting here...
                Gdx.app.exit();
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
    }
}