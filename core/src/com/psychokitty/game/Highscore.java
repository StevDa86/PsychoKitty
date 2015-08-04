package com.psychokitty.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

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

        if (!prefs.contains("Date")) {
            prefs.putString("Date", "no Score recorded");
        }
    }

    public static void resetScore(){
        prefs.putInteger("highScore", 0);
        prefs.putString("Date", "no Score recorded");
        prefs.flush();
    }

    public static void setHighScore(int val) {

        prefs.putInteger("highScore", val);
        prefs.flush();
    }

    public static int getHighScore() {
        return prefs.getInteger("highScore");
    }

    public static void setCurrentDate(String val) {

        prefs.putString("Date", val);
        prefs.flush();
    }

    public static String getCurrentDate() {
        return prefs.getString("Date");
    }
}
