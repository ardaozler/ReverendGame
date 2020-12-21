package me.javawarriors.reverend.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends Entity {
	float stateTime;
	// char properties
	int frameNo;
	float charX = 960 ;
	float charY = 540;
	int charWidthInPixels = 21;
	int charHeightInPixels = 27;
	float charWidth = charWidthInPixels * 3;
	float charHeight = charHeightInPixels * 3;
	float speed = 600;

	// char Animation properties
	Animation<TextureRegion>[] walk;
	private static final float charAnimationSpeed = 0.1f;
	
	public Player(){
		
		frameNo = 0;
		walk = new Animation[5];
		TextureRegion[][] walkSpriteSheet = TextureRegion.split(new Texture("charAnim.png"), charWidthInPixels,
				charHeightInPixels);

		walk[frameNo] = new Animation(charAnimationSpeed, walkSpriteSheet[frameNo]);
		
	}
	
	public void Update(float delta) {
		move();
	}
	
	public void move() {
		
		if ((Gdx.input.isKeyPressed(Keys.RIGHT) || (Gdx.input.isKeyPressed(Keys.D)))
				&& (Gdx.input.isKeyPressed(Keys.UP) || (Gdx.input.isKeyPressed(Keys.W)))) {
			charY += speed * Gdx.graphics.getDeltaTime() * 0.7;
			charX += speed * Gdx.graphics.getDeltaTime() * 0.7;
			stateTime += Gdx.graphics.getDeltaTime();
		} else if ((Gdx.input.isKeyPressed(Keys.UP) || (Gdx.input.isKeyPressed(Keys.W)))
				&& (Gdx.input.isKeyPressed(Keys.LEFT) || (Gdx.input.isKeyPressed(Keys.A)))) {
			charY += speed * Gdx.graphics.getDeltaTime() * 0.7;
			charX -= speed * Gdx.graphics.getDeltaTime() * 0.7;
			stateTime += Gdx.graphics.getDeltaTime();
		} else if ((Gdx.input.isKeyPressed(Keys.DOWN) || (Gdx.input.isKeyPressed(Keys.S)))
				&& (Gdx.input.isKeyPressed(Keys.RIGHT) || (Gdx.input.isKeyPressed(Keys.D)))) {
			charY -= speed * Gdx.graphics.getDeltaTime() * 0.7;
			charX += speed * Gdx.graphics.getDeltaTime() * 0.7;
			stateTime += Gdx.graphics.getDeltaTime();
		} else if ((Gdx.input.isKeyPressed(Keys.DOWN) || (Gdx.input.isKeyPressed(Keys.S)))
				&& (Gdx.input.isKeyPressed(Keys.LEFT) || (Gdx.input.isKeyPressed(Keys.A)))) {
			charY -= speed * Gdx.graphics.getDeltaTime() * 0.7;
			charX -= speed * Gdx.graphics.getDeltaTime() * 0.7;
			stateTime += Gdx.graphics.getDeltaTime();
		} else if (Gdx.input.isKeyPressed(Keys.UP) || (Gdx.input.isKeyPressed(Keys.W))) {
			charY += speed * Gdx.graphics.getDeltaTime();
			stateTime += Gdx.graphics.getDeltaTime();
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT) || (Gdx.input.isKeyPressed(Keys.D))) {
			charX += speed * Gdx.graphics.getDeltaTime();
			stateTime += Gdx.graphics.getDeltaTime();
		} else if (Gdx.input.isKeyPressed(Keys.DOWN) || (Gdx.input.isKeyPressed(Keys.S))) {
			charY -= speed * Gdx.graphics.getDeltaTime();
			stateTime += Gdx.graphics.getDeltaTime();
		} else if (Gdx.input.isKeyPressed(Keys.LEFT) || (Gdx.input.isKeyPressed(Keys.A))) {
			charX -= speed * Gdx.graphics.getDeltaTime();
			stateTime += Gdx.graphics.getDeltaTime();
		}else {
			stateTime = 0;
		}
		setPosition(charX, charY);
	}
	
	public TextureRegion GetFrame() {
		return (walk[frameNo].getKeyFrame(stateTime, true));
	}
	
	public void draw(SpriteBatch spriteBatch) {
		Update(Gdx.graphics.getDeltaTime());
		super.draw(spriteBatch);
	}

	public float getWidth() {
		return charWidth;
	}
	public float getHeight() {
		return charHeight;
	}
}
