package me.javawarriors.reverend.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

import me.javawarriors.reverend.screens.GameScreen;

public class Shield extends Entity {


	private static Texture texture;
	private static TextureRegion textureRegion;
	float x, y;
	
	
	float secondsElapsed;
	String ShootersName;
	GameScreen screen;
	boolean collided=false;
	private boolean remove = false;

	// collision with map
	private TiledMapTileLayer collisionLayer;

	public Shield(float playerX, float playerY, GameScreen GameScreen) {
		this.screen = GameScreen;
		
		this.collisionLayer = collisionLayer;
		this.ShootersName = ShootersName;
		x = playerX;
		y = playerY;
		
		
		if (texture == null) {
			texture = new Texture("Shield.png");
			textureRegion = new TextureRegion(texture);
		}

	}

	public void update(float delta) {
		boolean collision = false;
		move();
		collision = isCellBlocked(x, y);
		
		if(secondsElapsed>3) {
			remove=true;
		}
		
		
		
		
		/*if (collision && secondsElapsed>0.05) {
			collided=true;
			remove = true;
		}
		secondsElapsed += delta;

		if (secondsElapsed > 5) {
			remove = true;
		}*/

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
		x =screen.getPlayer().getX();
		y =screen.getPlayer().getY();
		setPosition(x, y);
	}

	public void render(SpriteBatch batch) {
		batch.draw(textureRegion, x, y, 0f, 0f, 9f, 9f, 3, 3,0);
	}

	public float getX() {
		return x;
	}
}
