//Asset Manager zum laden aller Ressourcen beim Start der App.

package com.psychokitty.game.Utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;


public class Assets {

    public static final AssetManager manager = new AssetManager();

    public static final String Count1 = Constants.Number1Image;
    public static final String Count2 = Constants.Number2Image;
    public static final String Count3 = Constants.Number3Image;


    public static void load(){
        manager.load(Count1, Texture.class);
        manager.load(Count2, Texture.class);
        manager.load(Count3, Texture.class);
    }

    public static void dispose(){
        manager.dispose();
    }
}
