package me.javawarriors.reverend.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

public class Bullet extends Entity {

	public static final int Speed = 850;
	private static Texture texture;
	private static TextureRegion textureRegion;
	float x, y;
	double xkatsayisi, ykatsayisi, magnitude;
	float Rot;
	float secondsElapsed;

	private boolean remove = false;

	// collision with map
	private TiledMapTileLayer collisionLayer;

	public Bullet(float playerX, float playerY, TiledMapTileLayer collisionLayer) {
		this.collisionLayer = collisionLayer;
		x = playerX + 50;
		y = playerY + 50;
		ykatsayisi = -(50 + 540 - 1080 + Gdx.input.getY());
		xkatsayisi = -(50 + 960 - Gdx.input.getX());
		Rot =  45 + (float) Math.toDegrees(- Math.atan2(xkatsayisi, ykatsayisi));

		magnitude = Math.abs(Math.sqrt((xkatsayisi * xkatsayisi) + (ykatsayisi * ykatsayisi)));
		ykatsayisi = ykatsayisi / magnitude;
		xkatsayisi = xkatsayisi / magnitude;

		if (texture == null) {
			texture = new Texture("Pew.png");
			textureRegion = new TextureRegion(texture);
		}

	}

	public void update(float delta) {
		boolean collision = false;
		move();
		collision = isCellBlocked(x, y);
		if (collision) {
			remove = true;
		}
		secondsElapsed += delta;

		if (secondsElapsed > 2) {
			remove = true;
		}

	}

	public boolean shouldRemove() {
		return remove;
	}

	private boolean isCellBlocked(float x, float y) {
		Cell cell = collisionLayer.getCell((int) (x / (collisionLayer.getTileWidth() * 4)),
				(int) (y / (collisionLayer.getTileHeight() * 4)));
		return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey("blocked");
	}

	@Override
	public void move() {
		x += xkatsayisi * Speed * Gdx.graphics.getDeltaTime();
		y += ykatsayisi * Speed * Gdx.graphics.getDeltaTime();
		setPosition(x, y);
	}

	public void render(SpriteBatch batch) {
		batch.draw(textureRegion, x, y, 4.5f, 4.5f, 9f, 9f, 3, 3, Rot);
	}
}
