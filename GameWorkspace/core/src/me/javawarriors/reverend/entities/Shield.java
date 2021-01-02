package me.javawarriors.reverend.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import me.javawarriors.reverend.screens.GameScreen;

public class Shield extends Entity {


	private static Texture texture;
	private static TextureRegion textureRegion;
	float x, y;
	
	
	float secondsElapsed = 0;
	String ShieldOwner;
	GameScreen screen;
	boolean collided=false;
	private boolean remove = false;

	public Shield(float playerX, float playerY, GameScreen GameScreen , String ShieldOwner) {
		this.ShieldOwner = ShieldOwner;
		this.screen = GameScreen;
		x = playerX;
		y = playerY;
		
		if (texture == null) {
			texture = new Texture("Shield.png");
			textureRegion = new TextureRegion(texture);
		}

	}

	public void update(float delta) {
		move();
		secondsElapsed += delta;
		if(secondsElapsed>3) {
			remove=true;
		}

	}

	public boolean shouldRemove() {
		return remove;
	}

	public void setRemove(boolean remove) {
		this.remove = remove;
	}

	@Override
	public void move() {
		x =screen.getPlayer().getX();
		y =screen.getPlayer().getY();
		setPosition(x, y);
	}

	public void render(SpriteBatch batch) {
		batch.draw(textureRegion, x, y-8, 23*3, 32*3);
	}
}
