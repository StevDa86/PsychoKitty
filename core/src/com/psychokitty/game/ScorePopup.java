package com.psychokitty.game;

import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;


/**
 * Created by steven on 03.08.15.
 */
public class ScorePopup extends Dialog {


   private Highscore highscore;

    public ScorePopup(String title, Skin skin) {
        super(title, skin);
    }

    public ScorePopup(String title, Skin skin, String windowStyleName) {
        super(title, skin, windowStyleName);
    }

    public ScorePopup(String title, WindowStyle windowStyle) {
        super(title, windowStyle);
    }

    {
        highscore = new Highscore();
        highscore.config();

        text("Best Score:" + Integer.toString(Highscore.getHighScore()));
        text("Date:" + Highscore.getCurrentDate());
        button("Reset Score", "reset");
        button("Back", "abort");

    }

    @Override
    protected void result(Object object) {
        if (object == "abort"){hide();}
        if (object == "reset"){
            highscore.resetScore();
        }
    }



}
