package me.javawarriors.reverend.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import me.javawarriors.reverend.ReverendGame;

public class MainMenuScreen implements Screen {

	private static final int ButtonWidth = 300;
	private static final int ButtonHeight = 100;
	private static final int PlayButtonY = 300;
	private static final int ExitButtonY = 100;
	private static final int ButtonX = (ReverendGame.getWidth() / 2) - (ButtonWidth / 2);

	ReverendGame game;

	Texture exitButtonActive, exitButtonInactive, playButtonActive, playButtonInactive, reverendLogo;

	public MainMenuScreen(ReverendGame game) {
		this.game = game;
		reverendLogo = new Texture("BEGINGAME-sheet.png");
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
		Gdx.gl.glClearColor(0.2f, 0.3f, 0.35f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.getBatch().begin();
		game.getBatch().draw(reverendLogo, 0 , 0, 1920, 1080);
		if (mouseInput() == 1) {
			game.getBatch().draw(playButtonInactive, ButtonX, PlayButtonY, ButtonWidth, ButtonHeight);
			game.getBatch().draw(exitButtonActive, ButtonX, ExitButtonY, ButtonWidth, ButtonHeight);
		} else if (mouseInput() == 2) {
			game.getBatch().draw(playButtonActive, ButtonX, PlayButtonY, ButtonWidth, ButtonHeight);
			game.getBatch().draw(exitButtonInactive, ButtonX, ExitButtonY, ButtonWidth, ButtonHeight);
		} else {
			game.getBatch().draw(playButtonInactive, ButtonX, PlayButtonY, ButtonWidth, ButtonHeight);
			game.getBatch().draw(exitButtonInactive, ButtonX, ExitButtonY, ButtonWidth, ButtonHeight);
		}
		game.getBatch().end();

	}

	public int mouseInput() {
		//ExitButtonHover
		if (1080 - Gdx.input.getY() > ((ExitButtonY)) && 1080 - Gdx.input.getY() < ((ExitButtonY) + ButtonHeight)
				&& Gdx.input.getX() > ButtonX && Gdx.input.getX() < ButtonX + ButtonWidth) {
			if(Gdx.input.isTouched()) {
				Gdx.app.exit();
			}
			return 1;
			//PlayButtonHover
		} else if (1080 - Gdx.input.getY() > ((PlayButtonY)) && 1080 - Gdx.input.getY() < ((PlayButtonY) + ButtonHeight)
				&& Gdx.input.getX() > ButtonX && Gdx.input.getX() < ButtonX + ButtonWidth) {
			if(Gdx.input.isTouched()) {
				this.dispose();
				game.setScreen(new GameScreen(game));
			}
			return 2;
		} else
			return 0;
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
