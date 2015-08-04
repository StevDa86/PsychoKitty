package com.psychokitty.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;


/**
 * Created by steven on 03.08.15.
 */
public class ScorePopup extends Dialog{

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
        text("Exit ?");
        button("YES");
        button("NO");
    }

    @Override
    protected void result(Object object) {

    }

    public void dispose(){

    }
}
