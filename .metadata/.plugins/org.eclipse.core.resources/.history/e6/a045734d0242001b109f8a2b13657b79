package me.javawarriors.reverend.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import me.javawarriors.reverend.ReverendGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = ReverendGame.getHeight();
		config.width = ReverendGame.getWidth();
		config.fullscreen = true;

		new LwjglApplication(new ReverendGame(), config);
	}
}