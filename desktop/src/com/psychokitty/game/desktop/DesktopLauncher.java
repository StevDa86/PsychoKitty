package com.psychokitty.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.psychokitty.game.PsychoKittyGame;
import com.psychokitty.game.Utils.Constants;


public class DesktopLauncher {
	public static void main (String[] arg) {
		try {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Constants.NATIVE_WIDTH;
		config.height = Constants.NATIVE_HEIGHT;
		config.title= "Psycho Kitty";

		       // Performance settings
			   config.vSyncEnabled = true;
			   config.foregroundFPS = 60;
			   config.backgroundFPS = 30;
			   
	
		
		//new LwjglApplication(new PsychoKittyGame(), config);
        new LwjglApplication(new PsychoKittyGame(null), config);
		} catch (Exception e) {
			System.err.println("Application failed to start: " + e.getMessage());
            System.exit(1);
		}

	}
}
