package com.psychokitty.game.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Highscore {
    private static final String PREFERENCES_NAME = "MyPrefs";
    private static final String HIGH_SCORE_KEY = "highScore";
    private static final String DATE_KEY = "Date";

    private static Preferences prefs;

    public void config() {
        prefs = Gdx.app.getPreferences(PREFERENCES_NAME);

        if (!prefs.contains(HIGH_SCORE_KEY)) {
            prefs.putInteger(HIGH_SCORE_KEY, 0);
        }

        if (!prefs.contains(DATE_KEY)) {
            prefs.putString(DATE_KEY, "no Score recorded");
        }
        prefs.flush();
    }

    public static void resetScore() {
        prefs.putInteger(HIGH_SCORE_KEY, 0);
        prefs.putString(DATE_KEY, "no Score recorded");
        prefs.flush();
    }

    public static void setHighScore(int val) {
        prefs.putInteger(HIGH_SCORE_KEY, val);
        prefs.flush();
    }

    public static int getHighScore() {
        return prefs.getInteger(HIGH_SCORE_KEY);
    }

    public static void setCurrentDate(String val) {
        prefs.putString(DATE_KEY, val);
        prefs.flush();
    }

    public static String getCurrentDate() {
        return prefs.getString(DATE_KEY);
    }
}
