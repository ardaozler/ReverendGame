package me.javawarriors.reverend.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import me.javawarriors.reverend.screens.GameScreen;

public class Blood {

	// Animations
	float x, y;

	
	GameScreen screen;

	
	private static Texture texture;
	private static TextureRegion textureRegion;

	public Blood( GameScreen GameScreen, float x, float y) {
		this.screen = GameScreen;
		this.x = x;
		this.y = y;

		if (texture == null) {
			texture = new Texture("Blood.png");
			textureRegion = new TextureRegion(texture);
		}
		
		screen.getBloods().add(this);

	}

	public void update(float delta) {

	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public void render(SpriteBatch batch) {
		batch.draw(textureRegion, x, y, 62, 33);
	}

}
