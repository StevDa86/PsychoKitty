package com.psychokitty.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import com.psychokitty.game.PsychoKittyGame;


public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 800;
		config.height = 480;
		config.title= "Psycho Kitty";
		//new LwjglApplication(new PsychoKittyGame(), config);
        new LwjglApplication(new PsychoKittyGame(null), config);

	}
}
