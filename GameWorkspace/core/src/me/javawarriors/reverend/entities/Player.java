package me.javawarriors.reverend.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

public class Player extends Entity {
	float stateTime;
	// char properties
	float charX = 540;
	float charY = 960;
	int charWidthInPixels = 25;
	int charHeightInPixels = 29;
	float charWidth = charWidthInPixels * 4;
	float charHeight = charHeightInPixels * 4;
	float speed = 600;
	boolean colTopRight = false, colTopLeft = false, colBotRight = false, colBotLeft = false, colBot = false,
			colLeft = false, colTop = false, colRight = false;
	// char Animation properties
	Animation<TextureRegion>[] walk;
	private static final float charAnimationSpeed = 0.15f;
	int frameNo;

	// collision
	private TiledMapTileLayer collisionLayer;

	// bullet
	private ArrayList<Bullet> bullets;

	public Player(TiledMapTileLayer collisionLayer) {
		this.collisionLayer = collisionLayer;
		frameNo = 0;
		walk = new Animation[6];
		TextureRegion[][] walkSpriteSheet = TextureRegion.split(new Texture("charAnim.png"), charWidthInPixels,
				charHeightInPixels);

		walk[0] = new Animation<>(charAnimationSpeed, walkSpriteSheet[0]);
		walk[1] = new Animation<>(charAnimationSpeed, walkSpriteSheet[1]);

		bullets = new ArrayList<Bullet>();

	}

	public void Update(float delta) {
		float oldX = charX, oldY = charY;
		boolean collision = false;
		move();

		if (Gdx.input.isTouched()) {
			shoot(charX, charY, collisionLayer);
		}

		// concurrent modification exception olmaması için ikinci array açıp looplama
		// bittikten sorna siliyoruz
		ArrayList<Bullet> bulletsToRemove = new ArrayList<Bullet>();
		for (Bullet bullet : bullets) {

			bullet.update(Gdx.graphics.getDeltaTime());

			if (bullet.shouldRemove()) {
				bulletsToRemove.add(bullet);
			}
		}
		bullets.removeAll(bulletsToRemove);

		// top left
		collision = isCellBlocked(getX(), getY() + getHeight());

		// bot left
		if (!collision) {
			collision = isCellBlocked(getX(), getY());
		}
		// top right
		if (!collision) {
			collision = isCellBlocked(getX() + getWidth(), getY() + getHeight());
		}
		// bot right
		if (!collision) {
			collision = isCellBlocked(getX() + getWidth(), getY());
		}

		if (collision) {
			charX = oldX;
			charY = oldY;
		}

		// kasiyo anlamadim
		/*
		 * if(collision) { if(colTopRight||colTopLeft||colBotRight||colBotLeft) {
		 * charX=oldX; charY=oldY; } else if(colTop||colBot) {
		 * 
		 * charY=oldY; } else if(colLeft||colRight) { charX=oldX;
		 * 
		 * }
		 * 
		 * }
		 */

	}

	private void shoot(float playerX, float playerY, TiledMapTileLayer collisionLayer) {
		if (bullets.size() == 0 || bullets.get(bullets.size() - 1).secondsElapsed > 0.2) {
			bullets.add(new Bullet(playerX, playerY, collisionLayer));
		}

	}

	public ArrayList<Bullet> getBullets() {
		return bullets;
	}

	private boolean isCellBlocked(float x, float y) {
		Cell cell = collisionLayer.getCell((int) (x / (collisionLayer.getTileWidth() * 4)),
				(int) (y / (collisionLayer.getTileHeight() * 4)));
		return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey("blocked");
	}

	public void move() {

		if ((Gdx.input.isKeyPressed(Keys.RIGHT) || (Gdx.input.isKeyPressed(Keys.D)))
				&& (Gdx.input.isKeyPressed(Keys.UP) || (Gdx.input.isKeyPressed(Keys.W))) && colTopRight == false) {
			frameNo = 1;
			charY += speed * Gdx.graphics.getDeltaTime() * 0.7;
			charX += speed * Gdx.graphics.getDeltaTime() * 0.7;
			stateTime += Gdx.graphics.getDeltaTime();

		} else if ((Gdx.input.isKeyPressed(Keys.UP) || (Gdx.input.isKeyPressed(Keys.W)))
				&& (Gdx.input.isKeyPressed(Keys.LEFT) || (Gdx.input.isKeyPressed(Keys.A))) && colTopLeft == false) {
			frameNo = 1;
			charY += speed * Gdx.graphics.getDeltaTime() * 0.7;
			charX -= speed * Gdx.graphics.getDeltaTime() * 0.7;
			stateTime += Gdx.graphics.getDeltaTime();

		} else if ((Gdx.input.isKeyPressed(Keys.DOWN) || (Gdx.input.isKeyPressed(Keys.S)))
				&& (Gdx.input.isKeyPressed(Keys.RIGHT) || (Gdx.input.isKeyPressed(Keys.D))) && colBotRight == false) {
			frameNo = 1;
			charY -= speed * Gdx.graphics.getDeltaTime() * 0.7;
			charX += speed * Gdx.graphics.getDeltaTime() * 0.7;
			stateTime += Gdx.graphics.getDeltaTime();
		} else if ((Gdx.input.isKeyPressed(Keys.DOWN) || (Gdx.input.isKeyPressed(Keys.S)))
				&& (Gdx.input.isKeyPressed(Keys.LEFT) || (Gdx.input.isKeyPressed(Keys.A))) && colBotLeft == false) {
			frameNo = 1;
			charY -= speed * Gdx.graphics.getDeltaTime() * 0.7;
			charX -= speed * Gdx.graphics.getDeltaTime() * 0.7;
			stateTime += Gdx.graphics.getDeltaTime();
		} else if (Gdx.input.isKeyPressed(Keys.UP) || (Gdx.input.isKeyPressed(Keys.W)) && colTop == false) {
			frameNo = 1;
			charY += speed * Gdx.graphics.getDeltaTime();
			stateTime += Gdx.graphics.getDeltaTime();
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT) || (Gdx.input.isKeyPressed(Keys.D)) && colRight == false) {
			frameNo = 1;
			charX += speed * Gdx.graphics.getDeltaTime();
			stateTime += Gdx.graphics.getDeltaTime();
		} else if (Gdx.input.isKeyPressed(Keys.DOWN) || (Gdx.input.isKeyPressed(Keys.S)) && colLeft == false) {
			frameNo = 1;
			charY -= speed * Gdx.graphics.getDeltaTime();
			stateTime += Gdx.graphics.getDeltaTime();
		} else if (Gdx.input.isKeyPressed(Keys.LEFT) || (Gdx.input.isKeyPressed(Keys.A)) && colBot == false) {
			frameNo = 1;
			charX -= speed * Gdx.graphics.getDeltaTime();
			stateTime += Gdx.graphics.getDeltaTime();
		} else {
			frameNo = 0;
			stateTime = 0;
		}
		setPosition(charX, charY);
	}

	public TextureRegion GetFrame() {
		return (walk[frameNo].getKeyFrame(stateTime, true));
	}

	public float getWidth() {
		return charWidth;
	}

	public float getHeight() {
		return charHeight;
	}
}
