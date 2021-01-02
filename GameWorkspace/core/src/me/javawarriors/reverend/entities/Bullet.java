package me.javawarriors.reverend.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

import me.javawarriors.reverend.screens.GameScreen;

public class Bullet extends Entity {

	private int Speed = 800;
	private static Texture texture;
	private static TextureRegion textureRegion;
	float x, y;
	double xkatsayisi, ykatsayisi, magnitude;
	float Rot;
	float secondsElapsed;
	String ShootersName;
	GameScreen screen;
	boolean collided=false;
	private boolean remove = false;

	// collision with map
	private TiledMapTileLayer collisionLayer;

	public Bullet(float playerX, float playerY, TiledMapTileLayer collisionLayer, float targetX, float targetY,
			String ShootersName, GameScreen GameScreen, int Speed) {
		this.screen = GameScreen;
		this.Speed = Speed;
		this.collisionLayer = collisionLayer;
		this.ShootersName = ShootersName;
		x = playerX + 50;
		y = playerY + 50;
		ykatsayisi = targetY - playerY;
		xkatsayisi = targetX - playerX;
		Rot = 45 + (float) Math.toDegrees(-Math.atan2(xkatsayisi, ykatsayisi));

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
		if (collision && secondsElapsed>0.05) {
			collided=true;
			remove = true;
		}
		secondsElapsed += delta;

		if (secondsElapsed > 5) {
			remove = true;
		}

	}

	public boolean shouldRemove() {
		return remove;
	}

	public void setRemove(boolean remove) {
		this.remove = remove;
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
		batch.draw(textureRegion, x, y, 0f, 0f, 9f, 9f, 3, 3, Rot);
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

	public double getXkatsayisi() {
		return xkatsayisi;
	}

	public void setXkatsayisi(double xkatsayisi) {
		this.xkatsayisi = xkatsayisi;
	}

	public double getYkatsayisi() {
		return ykatsayisi;
	}

	public void setYkatsayisi(double ykatsayisi) {
		this.ykatsayisi = ykatsayisi;
	}

	public double getMagnitude() {
		return magnitude;
	}

	public void setMagnitude(double magnitude) {
		this.magnitude = magnitude;
	}

	public float getRot() {
		return Rot;
	}

	public void setRot(float rot) {
		Rot = rot;
	}

	public float getSecondsElapsed() {
		return secondsElapsed;
	}

	public void setSecondsElapsed(float secondsElapsed) {
		this.secondsElapsed = secondsElapsed;
	}

	public String getShootersName() {
		return ShootersName;
	}

	public void setShootersName(String shootersName) {
		ShootersName = shootersName;
	}

	public TiledMapTileLayer getCollisionLayer() {
		return collisionLayer;
	}

	public void setCollisionLayer(TiledMapTileLayer collisionLayer) {
		this.collisionLayer = collisionLayer;
	}

	public int getSpeed() {
		return Speed;
	}

	public boolean isRemove() {
		return remove;
	}

	public boolean isCollided() {
		return collided;
	}

	public void setCollided(boolean collided) {
		this.collided = collided;
	}

}
