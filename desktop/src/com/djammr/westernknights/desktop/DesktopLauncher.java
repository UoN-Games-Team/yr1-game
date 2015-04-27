package com.djammr.westernknights.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.djammr.westernknights.WKGame;


public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Manus-Terra: Legacy of Alathos";
		cfg.useGL30 = false;
		cfg.width = WKGame.SCREEN_WIDTH;
		cfg.height = WKGame.SCREEN_HEIGHT;
		cfg.fullscreen = false;
		cfg.addIcon("images/game_logo.png", Files.FileType.Internal);

		/* Borderless Window
		System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
		cfg.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
		cfg.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
		cfg.fullscreen = false;*/

		new LwjglApplication(new WKGame(), cfg);
	}
}
