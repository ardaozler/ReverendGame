package me.javawarriors.reverend.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

public class Player extends Entity {

	// char properties
	float charX = 540;
	float charY = 960;
	int charWidthInPixels = 25;
	int charHeightInPixels = 29;
	float charWidth = charWidthInPixels * 4;
	float charHeight = charHeightInPixels * 4;
	float speed = 600;
	int dx = 0, dy = 0;

	// char Animation properties
	Animation<TextureRegion>[] walk;
	private static final float charAnimationSpeed = 0.15f;
	int frameNo;
	float stateTime;

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
		collision = isCellBlocked(getX(), getY() + getHeight() * 1 / 3);

		// bot left
		if (!collision) {
			collision = isCellBlocked(getX(), getY());
		}
		// top right
		if (!collision) {
			collision = isCellBlocked(getX() + getWidth() * 1 / 2, getY() + getHeight() * 1 / 3);
		}
		// bot right
		if (!collision) {
			collision = isCellBlocked(getX() + getWidth() * 1 / 2, getY());
		}

		if (collision) {

			charX = oldX;
			charY = oldY;

		}

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

	
	private void collisionCheck() {
		if (isCellBlocked(charX - 10, charY)) {
			System.out.println("sol alt sol bok");
			dx = 0;
		}
		if (isCellBlocked(charX, charY - 10)) {
			System.out.println("sol alt alt bok");
			dy = 0;
		}
		if (isCellBlocked(charX - 10, charY + getHeight() * 1 / 3)) {
			System.out.println("sol üst sol bok");
			dx = 0;
		}
		if (isCellBlocked(charX, charY + 10 + getHeight() * 1 / 3)) {
			System.out.println("sol üst üst bok");
			dy = 0;
		}
		if (isCellBlocked(charX + getWidth() * 1 / 2 + 10, charY)) {
			System.out.println("sağ alt sağ bok");
			dx = 0;
		}
		if (isCellBlocked(charX + getWidth() * 1 / 2, charY - 10)) {
			System.out.println("sağ alt alt bok");
			dy = 0;
		}
		if (isCellBlocked(charX + getWidth() * 1 / 2 + 10, charY + getHeight() * 1 / 3)) {
			System.out.println("sağ üst sağ bok");
			dx = 0;
		}
		if (isCellBlocked(charX + getWidth() * 1 / 2, charY + 10 + getHeight() * 1 / 3)) {
			System.out.println("sağ üst üst bok");
			dy = 0;
		}
	}

	public void move() {

		if ((Gdx.input.isKeyPressed(Keys.RIGHT) || (Gdx.input.isKeyPressed(Keys.D)))
				&& (Gdx.input.isKeyPressed(Keys.UP) || (Gdx.input.isKeyPressed(Keys.W)))) {
			frameNo = 1;
			dx = 1;
			dy = 1;
			if (isCellBlocked(charX + getWidth() * 1 / 2 + 10, charY + getHeight() * 1 / 3)) {
				System.out.println("sağ üst sağ bok");
				dx = 0;
			}
			if (isCellBlocked(charX + getWidth() * 1 / 2, charY + 10 + getHeight() * 1 / 3)) {
				System.out.println("sağ üst üst bok");
				dy = 0;
			}
			if (isCellBlocked(charX + getWidth() * 1 / 2 + 10, charY)) {
				System.out.println("sağ alt sağ bok");
				dx = 0;
			}
			if (isCellBlocked(charX, charY + 10 + getHeight() * 1 / 3)) {
				System.out.println("sol üst üst bok");
				dy = 0;
			}
			// collisionChech();
			charX += speed * Gdx.graphics.getDeltaTime() * dx * 0.7;
			charY += speed * Gdx.graphics.getDeltaTime() * dy * 0.7;
			stateTime += Gdx.graphics.getDeltaTime();

		} else if ((Gdx.input.isKeyPressed(Keys.UP) || (Gdx.input.isKeyPressed(Keys.W)))
				&& (Gdx.input.isKeyPressed(Keys.LEFT) || (Gdx.input.isKeyPressed(Keys.A)))) {
			frameNo = 1;
			dx = -1;
			dy = 1;
			if (isCellBlocked(charX - 10, charY + getHeight() * 1 / 3)) {
				System.out.println("sol üst sol bok");
				dx = 0;
			}
			if (isCellBlocked(charX, charY + 10 + getHeight() * 1 / 3)) {
				System.out.println("sol üst üst bok");
				dy = 0;
			}
			if (isCellBlocked(charX - 10, charY)) {
				System.out.println("sol alt sol bok");
				dx = 0;
			}
			if (isCellBlocked(charX + getWidth() * 1 / 2, charY + 10 + getHeight() * 1 / 3)) {
				System.out.println("sağ üst üst bok");
				dy = 0;
			}
			// collisionCheck();
			charX += speed * Gdx.graphics.getDeltaTime() * dx * 0.7;
			charY += speed * Gdx.graphics.getDeltaTime() * dy * 0.7;
			stateTime += Gdx.graphics.getDeltaTime();

		} else if ((Gdx.input.isKeyPressed(Keys.DOWN) || (Gdx.input.isKeyPressed(Keys.S)))
				&& (Gdx.input.isKeyPressed(Keys.RIGHT) || (Gdx.input.isKeyPressed(Keys.D)))) {
			frameNo = 1;
			dx = 1;
			dy = -1;
			if (isCellBlocked(charX + getWidth() * 1 / 2 + 10, charY)) {
				System.out.println("sağ alt sağ bok");
				dx = 0;
			}
			if (isCellBlocked(charX + getWidth() * 1 / 2, charY - 10)) {
				System.out.println("sağ alt alt bok");
				dy = 0;
			}
			if (isCellBlocked(charX + getWidth() * 1 / 2 + 10, charY + getHeight() * 1 / 3)) {
				System.out.println("sağ üst sağ bok");
				dx = 0;
			}
			if (isCellBlocked(charX, charY - 10)) {
				System.out.println("sol alt alt bok");
				dy = 0;
			}
			// collisionCheck();
			charX += speed * Gdx.graphics.getDeltaTime() * dx * 0.7;
			charY += speed * Gdx.graphics.getDeltaTime() * dy * 0.7;
			stateTime += Gdx.graphics.getDeltaTime();

		} else if ((Gdx.input.isKeyPressed(Keys.DOWN) || (Gdx.input.isKeyPressed(Keys.S)))
				&& (Gdx.input.isKeyPressed(Keys.LEFT) || (Gdx.input.isKeyPressed(Keys.A)))) {
			frameNo = 1;
			dx = -1;
			dy = -1;
			if (isCellBlocked(charX - 10, charY)) {
				System.out.println("sol alt sol bok");
				dx = 0;
			}
			if (isCellBlocked(charX, charY - 10)) {
				System.out.println("sol alt alt bok");
				dy = 0;
			}
			if (isCellBlocked(charX - 10, charY + getHeight() * 1 / 3)) {
				System.out.println("sol üst sol bok");
				dx = 0;
			}
			if (isCellBlocked(charX + getWidth() * 1 / 2, charY - 10)) {
				System.out.println("sağ alt alt bok");
				dy = 0;
			}
			// collisionCheck();
			charX += speed * Gdx.graphics.getDeltaTime() * dx * 0.7;
			charY += speed * Gdx.graphics.getDeltaTime() * dy * 0.7;
			stateTime += Gdx.graphics.getDeltaTime();

		} else if (Gdx.input.isKeyPressed(Keys.UP) || (Gdx.input.isKeyPressed(Keys.W))) {
			frameNo = 1;
			dx = 0;
			dy = 1;
			if (isCellBlocked(charX, charY + 10 + getHeight() * 1 / 3)) {
				System.out.println("sol üst üst bok");
				dy = 0;
			}
			if (isCellBlocked(charX + getWidth() * 1 / 2, charY + 10 + getHeight() * 1 / 3)) {
				System.out.println("sağ üst üst bok");
				dy = 0;
			}
			// collisionCheck();
			charX += speed * Gdx.graphics.getDeltaTime() * dx;
			charY += speed * Gdx.graphics.getDeltaTime() * dy;
			stateTime += Gdx.graphics.getDeltaTime();

		} else if (Gdx.input.isKeyPressed(Keys.RIGHT) || (Gdx.input.isKeyPressed(Keys.D))) {
			frameNo = 1;
			dx = 1;
			dy = 0;
			if (isCellBlocked(charX + getWidth() * 1 / 2 + 10, charY)) {
				System.out.println("sağ alt sağ bok");
				dx = 0;
			}
			if (isCellBlocked(charX + getWidth() * 1 / 2 + 10, charY + getHeight() * 1 / 3)) {
				System.out.println("sağ üst sağ bok");
				dx = 0;
			}
			// collisionCheck();
			charX += speed * Gdx.graphics.getDeltaTime() * dx;
			charY += speed * Gdx.graphics.getDeltaTime() * dy;
			stateTime += Gdx.graphics.getDeltaTime();

		} else if (Gdx.input.isKeyPressed(Keys.DOWN) || (Gdx.input.isKeyPressed(Keys.S))) {
			frameNo = 1;
			dx = 0;
			dy = -1;
			if (isCellBlocked(charX, charY - 10)) {
				System.out.println("sol alt alt bok");
				dy = 0;
			}
			if (isCellBlocked(charX + getWidth() * 1 / 2, charY - 10)) {
				System.out.println("sağ alt alt bok");
				dy = 0;
			}
			// collisionCheck();
			charX += speed * Gdx.graphics.getDeltaTime() * dx;
			charY += speed * Gdx.graphics.getDeltaTime() * dy;
			stateTime += Gdx.graphics.getDeltaTime();

		} else if (Gdx.input.isKeyPressed(Keys.LEFT) || (Gdx.input.isKeyPressed(Keys.A))) {
			frameNo = 1;
			dx = -1;
			dy = 0;
			if (isCellBlocked(charX - 10, charY + getHeight() * 1 / 3)) {
				System.out.println("sol üst sol bok");
				dx = 0;
			}
			if (isCellBlocked(charX - 10, charY)) {
				System.out.println("sol alt sol bok");
				dx = 0;
			}
			// collisionCheck();
			charX += speed * Gdx.graphics.getDeltaTime() * dx;
			charY += speed * Gdx.graphics.getDeltaTime() * dy;
			stateTime += Gdx.graphics.getDeltaTime();
		} else {
			dx = 0;
			dy = 0;
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
