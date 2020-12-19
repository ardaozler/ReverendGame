package me.javawarriors.reverend;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import me.javawarriors.reverend.screens.MainMenuScreen;

public class ReverendGame extends Game {

	private static final int Width = 1920;
	private static final int Height= 1080;
	private SpriteBatch batch;

	@Override
	public void create() {
		batch = new SpriteBatch();
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		batch.dispose();

	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public void setBatch(SpriteBatch batch) {
		this.batch = batch;
	}

	public static int getWidth() {
		return Width;
	}

	public static int getHeight() {
		return Height;
	}
	
}
