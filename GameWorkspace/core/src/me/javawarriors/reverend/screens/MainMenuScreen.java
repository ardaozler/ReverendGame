package me.javawarriors.reverend.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import me.javawarriors.reverend.ReverendGame;

public class MainMenuScreen implements Screen {

	private static final int ButtonWidth = 300;
	private static final int ButtonHeight = 200;
	private static final int PlayButtonY = 300;
	private static final int ExitButtonY = 100;
	private static final int ButtonX = (ReverendGame.getWidth() / 2) - (ButtonWidth / 2);

	ReverendGame game;

	Texture exitButtonActive, exitButtonInactive, playButtonActive, playButtonInactive;

	public MainMenuScreen(ReverendGame game) {
		this.game = game;
		exitButtonActive = new Texture("ExitW.png");
		exitButtonInactive = new Texture("ExitR.png");
		playButtonActive = new Texture("PlayW.png");
		
		playButtonInactive = new Texture("PlayR.png");
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.getBatch().begin();
		if (mouseInput() != 0) {
			game.getBatch().draw(exitButtonInactive, ButtonX, ExitButtonY, ButtonWidth, ButtonHeight);
		} else {
			game.getBatch().draw(exitButtonActive, ButtonX, ExitButtonY, ButtonWidth, ButtonHeight);
		}
		game.getBatch().end();

	}

	public int mouseInput() {
		if ( Gdx.input.getY() > ((ExitButtonY))
				&& Gdx.input.getY() < ((ExitButtonY))) {
			return 0;
		} else
			return 1;
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
	}

}
