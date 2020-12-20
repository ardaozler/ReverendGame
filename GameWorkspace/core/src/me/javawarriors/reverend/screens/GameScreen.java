package me.javawarriors.reverend.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import me.javawarriors.reverend.ReverendGame;

public class GameScreen implements Screen {


	float stateTime;
	
	// char properties
	int frameNo;
	float charX;
	float charY;
	int charWidthInPixels = 21;
	int charHeightInPixels = 27;
	float charWidth = charWidthInPixels * 5;
	float charHeight = charHeightInPixels * 5;
	private static float speed = 600;
	
	//char Animation properties
	Animation<TextureRegion>[] walk;
	public static final float charAnimationSpeed = 0.1f;
	
	
	ReverendGame game;

	public GameScreen(ReverendGame game) {
		this.game = game;

		frameNo = 0;
		walk = new Animation[5];
		TextureRegion[][] walkSpriteSheet = TextureRegion.split(new Texture("charAnim.png"),charWidthInPixels, charHeightInPixels );
		
		walk[frameNo] = new Animation(charAnimationSpeed, walkSpriteSheet[frameNo]);
	}

	@Override
	public void show() {

	}

	public void move() {
		if (Gdx.input.isKeyPressed(Keys.UP) || (Gdx.input.isKeyPressed(Keys.W))) {
			charY += speed * Gdx.graphics.getDeltaTime();
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT) || (Gdx.input.isKeyPressed(Keys.D))) {
			charX += speed * Gdx.graphics.getDeltaTime();
		} else if (Gdx.input.isKeyPressed(Keys.DOWN) || (Gdx.input.isKeyPressed(Keys.S))) {
			charY -= speed * Gdx.graphics.getDeltaTime();
		} else if (Gdx.input.isKeyPressed(Keys.LEFT) || (Gdx.input.isKeyPressed(Keys.A))) {
			charX -= speed * Gdx.graphics.getDeltaTime();
		}
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0.5f, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stateTime += delta;

		move();

		game.getBatch().begin();
		game.getBatch().draw(walk[frameNo].getKeyFrame(stateTime, true), charX, charY, charWidth, charHeight);
		game.getBatch().end();

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
