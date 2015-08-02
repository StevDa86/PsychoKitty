package com.psychokitty.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;

/**
 * Created by steven on 02.08.15.
 */
public class Highscore{
    private static Preferences prefs;

    public void config() {

        prefs = Gdx.app.getPreferences("MyPrefs");

        if (!prefs.contains("highScore")) {
            prefs.putInteger("highScore", 0);
        }
    }

    public static void setHighScore(int val) {

        prefs.putInteger("highScore", val);
        prefs.flush();
    }

    public static int getHighScore() {
        return prefs.getInteger("highScore");
    }
}
