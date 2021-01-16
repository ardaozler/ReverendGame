package me.javawarriors.reverend.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import me.javawarriors.reverend.ReverendGame;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.vSyncEnabled = true;
		config.useGL30 = true;
		config.title = "Reverend";
		config.height = ReverendGame.getHeight();
		config.width = ReverendGame.getWidth();
		config.fullscreen = false;

		new LwjglApplication(new ReverendGame(), config);
	}
}