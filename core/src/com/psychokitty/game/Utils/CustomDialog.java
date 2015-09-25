package com.psychokitty.game.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by sdanz on 18.08.2015.
 */
public class CustomDialog extends Dialog {

    public Skin skin = new Skin(Gdx.files.internal(Constants.defaultJson));

    public CustomDialog (String title, Skin skin) {
        super(title, skin);
        initialize();
    }
    private void initialize() {
        padTop(50); // set padding on top of the dialog title
        getButtonTable().defaults().height(100); // set buttons height
        getButtonTable().defaults().width(100);
        setModal(true);
        setMovable(false);
        setResizable(false);
    }

    @Override
    public CustomDialog text(String text) {
        super.text(new Label(text, skin, "default"));
        return this;
    }

    /**
     * Adds a text button to the button table.
     * @param listener the input listener that will be attached to the button.
     */
    public CustomDialog button(String buttonText, InputListener listener) {
        TextButton button = new TextButton(buttonText, skin);
        button.addListener(listener);
        button(button);
        return this;
    }

    @Override
    public float getPrefWidth() {
        // force dialog width
        return 480f;
    }

    @Override
    public float getPrefHeight() {
        // force dialog height
        return 200f;
    }
}
