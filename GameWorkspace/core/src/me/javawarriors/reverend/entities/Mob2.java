package me.javawarriors.reverend.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

import me.javawarriors.reverend.screens.GameScreen;

public class Mob2 extends Entity {

	GameScreen screen;
	boolean active = false;

	// char properties
	String MobName;
	float charX = 600;
	float charY = 1800;
	int charWidthInPixels = 11;// constructora eklenmeli belki?
	int charHeightInPixels = 12;// constructora eklenmeli belki?
	float charWidth = charWidthInPixels * 4;
	float charHeight = charHeightInPixels * 4;
	int dx = 0, dy = 0;
	int HP;
	int speed = 500;
	int damage = 0;
	int dirx = 0;
	int diry = 0;
	double xkatsayisi, ykatsayisi, magnitude;
	int moveChange = 0;

	// char Animation properties
	Animation<TextureRegion>[] walk;
	private static final float charAnimationSpeed = 0.15f;
	float stateTime;
	boolean inVicinity;

	// collision
	private TiledMapTileLayer collisionLayer;
	private TiledMapTileLayer mobcollisionLayer;

	public Mob2(TiledMapTileLayer collisionLayer, TiledMapTileLayer MobcollisionLayer, GameScreen screen,
			String MobName, int x, int y, int speed, int health, int damage, String TextureName) {
		this.speed = speed;
		this.damage = damage;
		this.MobName = MobName;
		this.collisionLayer = collisionLayer;
		this.screen = screen;
		this.mobcollisionLayer = MobcollisionLayer;
		walk = new Animation[6];
		TextureRegion[][] walkSpriteSheet = TextureRegion.split(new Texture(TextureName), charWidthInPixels,
				charHeightInPixels);

		walk[0] = new Animation<>(charAnimationSpeed, walkSpriteSheet[0]);
		HP = health;
		screen.getMob2s().add(this);
	}

	public void Update(float delta) {
		float oldX = charX, oldY = charY;
		boolean collision = false;
		if (active) {
			move();
		}
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
		HitScan();

		Vicinity();
	}

	public boolean HitScan() {

		for (Bullet bullet : screen.getPbullets()) {

			float x = bullet.getX() + bullet.getWidth();
			float y = bullet.getY() + bullet.getHeight();

			if (x > charX && x < charX + charWidth && y > charY && y < charY + charHeight && !bullet.isCollided()) {
				if (bullet.secondsElapsed > 0.15) {
					bullet.setCollided(true);
					HP -= 5;
					System.out.println("mob " + HP);
					bullet.setRemove(true);
				}
				return true;
			}
		}
		return false;
	}

	public boolean isDead() {

		if (HP < 0) {
			return true;
		}
		return false;
	}

	public void Vicinity() {
		if (Math.abs(charX - screen.getPlayer().charX) < 1000 && Math.abs(charY - screen.getPlayer().charY) < 1000) {
			active = true;
		}
		if (Math.abs(charX - screen.getPlayer().charX) < 300 && Math.abs(charY - screen.getPlayer().charY) < 300) {
			inVicinity = true;
		}
		if (Math.abs(charX - screen.getPlayer().charX) < 30 && Math.abs(charY - screen.getPlayer().charY) < 30) {
			BlowUp();
		}
	}

	public void BlowUp() {
		HP = -1;
		screen.getPlayer().setHP(screen.getPlayer().getHP() - damage);

	}

	public ArrayList<Bullet> getMBullets() {
		return screen.getBullets();
	}

	private boolean isCellBlocked(float x, float y) {
		Cell cell = collisionLayer.getCell((int) (x / (collisionLayer.getTileWidth() * 4)),
				(int) (y / (collisionLayer.getTileHeight() * 4)));
		Cell boschcell = mobcollisionLayer.getCell((int) (x / (mobcollisionLayer.getTileWidth() * 4)),
				(int) (y / (mobcollisionLayer.getTileHeight() * 4)));

		return (cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey("blocked"))
				|| (boschcell != null && boschcell.getTile() != null
						&& boschcell.getTile().getProperties().containsKey("blocked"));
	}

	@Override
	public void move() {
		stateTime += Gdx.graphics.getDeltaTime();
		if (inVicinity) {

			dirx = 1;
			diry = 1;
			xkatsayisi = screen.getPlayer().getX() - charX;
			ykatsayisi = screen.getPlayer().getY() - charY;

			magnitude = Math.abs(Math.sqrt((xkatsayisi * xkatsayisi) + (ykatsayisi * ykatsayisi)));
			ykatsayisi = ykatsayisi / magnitude;
			xkatsayisi = xkatsayisi / magnitude;
		}
		charX += dirx * xkatsayisi * speed * Gdx.graphics.getDeltaTime();
		charY += diry * ykatsayisi * speed * Gdx.graphics.getDeltaTime();

		setPosition(charX, charY);

	}

	public void setInactive() {
		stateTime = 0;
		active = false;
		charX = -100;
		charY = -100;
		setPosition(charX, charY);
	}

	public String getMobName() {
		return MobName;
	}

	public void setMobName(String mobName) {
		MobName = mobName;
	}

	public GameScreen getScreen() {
		return screen;
	}

	public void setScreen(GameScreen screen) {
		this.screen = screen;
	}

	public TiledMapTileLayer getCollisionLayer() {
		return collisionLayer;
	}

	public void setCollisionLayer(TiledMapTileLayer collisionLayer) {
		this.collisionLayer = collisionLayer;
	}

	public TextureRegion GetFrame() {
		if (inVicinity) {
			return (walk[0].getKeyFrame(stateTime, true));
		} else {
			return (walk[0].getKeyFrame(0, true));
		}
	}

	public float getWidth() {
		return charWidth;
	}

	public float getHeight() {
		return charHeight;
	}

	public void render(SpriteBatch batch) {
		batch.draw(GetFrame(), getX(), getY(), getWidth(), getHeight());
	}

}
