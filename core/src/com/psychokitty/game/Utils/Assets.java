//Asset Manager zum laden aller Ressourcen beim Start der App.

package com.psychokitty.game.Utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Assets {

    public AssetManager manager = new AssetManager();

    //All Images
    public static final String Count1 = Constants.Number1Image;
    public static final String Count2 = Constants.Number2Image;
    public static final String Count3 = Constants.Number3Image;
    public static final String BackgroundImage = Constants.backgroundImage;
    public static final String ForegroundImage = Constants.foregroundImage;
    public static final String Hearts = Constants.heartImage;

    //All Sounds
    // load the drop sound effect and the rain background "music"
    public static final String sSoundMiau = Constants.soundMiau;
    public static final String sCatHiss = Constants.catHiss;
    public static final String sMusicDream = Constants.musicDream;
    public static final String sBeepHigh = Constants.beepHigh;
    public static final String sBeepLow = Constants.beepLow;

    public void load(){
        manager.load(Constants.Number1Image, Texture.class);
        manager.load(Count2, Texture.class);
        manager.load(Count3, Texture.class);
        manager.load(BackgroundImage, Texture.class);
        manager.load(ForegroundImage, Texture.class);
        manager.load(sBeepHigh, Sound.class);
        manager.load(sBeepLow, Sound.class);
        manager.load(sCatHiss, Sound.class);
        manager.load(sSoundMiau, Sound.class);
        manager.load(sMusicDream, Music.class);
        manager.load(Hearts,Texture.class);
        manager.finishLoading();
    }

    public void dispose(){
        manager.clear();
        manager.dispose();
    }
}
