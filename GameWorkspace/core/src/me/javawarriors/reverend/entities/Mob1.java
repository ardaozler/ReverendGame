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

public class Mob1 extends Entity {

	GameScreen screen;
	boolean active = false;
	// char properties
	String MobName;
	float charX = 1852;
	float charY = 2600;
	int charWidthInPixels = 25;
	int charHeightInPixels = 29;
	float charWidth = charWidthInPixels * 4;
	float charHeight = charHeightInPixels * 4;
	float speed = 600;
	int dx = 0, dy = 0;
	int HP;
	int Speed = 500;
	int dirx = 0;
	int diry = 0;
	double xkatsayisi, ykatsayisi, magnitude;
	int moveChange = 0;
	// char Animation properties
	Animation<TextureRegion>[] walk;
	private static final float charAnimationSpeed = 0.15f;
	int frameNo;
	float stateTime;
	boolean inVicinity;

	// collision
	private TiledMapTileLayer collisionLayer;

	// bullet
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();

	public Mob1(TiledMapTileLayer collisionLayer, GameScreen screen, String MobName) {
		this.MobName = MobName;
		this.collisionLayer = collisionLayer;
		this.screen = screen;
		frameNo = 0;
		walk = new Animation[6];
		TextureRegion[][] walkSpriteSheet = TextureRegion.split(new Texture("charAnimOld2.png"), charWidthInPixels,
				charHeightInPixels);

		walk[0] = new Animation<>(charAnimationSpeed, walkSpriteSheet[0]);
		walk[1] = new Animation<>(charAnimationSpeed, walkSpriteSheet[1]);
		HP = 100;
		screen.getMob1s().add(this);
	}

	public boolean HitScan() {

		for (Bullet bullet : screen.getPbullets()) {

			float x = bullet.getX() + bullet.getWidth();
			float y = bullet.getY() + bullet.getHeight();

			if (x > charX && x < charX + charWidth && y > charY && y < charY + charHeight&& !bullet.isCollided()) {
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

	public void Update(float delta) {
		float oldX = charX, oldY = charY;
		boolean collision = false;
		if (active) {
			move();
		}


		// concurrent modification exception olmaması için ikinci array açıp looplama
		// bittikten sorna siliyoruz
		ArrayList<Bullet> bulletsToRemove = new ArrayList<Bullet>();
		for (Bullet bullet : screen.getMbullets()) {

			bullet.update(Gdx.graphics.getDeltaTime());

			if (bullet.shouldRemove()) {
				bulletsToRemove.add(bullet);
			}
		}
		screen.getBullets().removeAll(bulletsToRemove);

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

	public void Vicinity() {
		if (Math.abs(charX - screen.getPlayer().charX) < 1000 && Math.abs(charY - screen.getPlayer().charY) < 1000) {
			active = true;
		}

		if (Math.abs(charX - screen.getPlayer().charX) < 600 && Math.abs(charY - screen.getPlayer().charY) < 600) {
			shoot(charX, charY, collisionLayer);
			inVicinity = true;
		}
	}

	private void shoot(float playerX, float playerY, TiledMapTileLayer collisionLayer) {
		if (screen.getMbullets().size() == 0
				|| screen.getMbullets().get(screen.getMbullets().size() - 1).secondsElapsed > 0.15) {
			bullets.add(new Bullet(playerX, playerY, collisionLayer, screen.getPlayer().charX, screen.getPlayer().charY,
					MobName, this.screen));
			screen.getMbullets().add(new Bullet(playerX, playerY, collisionLayer, screen.getPlayer().charX,
					screen.getPlayer().charY, MobName, this.screen));
		}

	}

	public ArrayList<Bullet> getMBullets() {
		return screen.getBullets();
	}

	private boolean isCellBlocked(float x, float y) {
		Cell cell = collisionLayer.getCell((int) (x / (collisionLayer.getTileWidth() * 4)),
				(int) (y / (collisionLayer.getTileHeight() * 4)));
		return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey("blocked");
	}

	@Override
	public void move() {
		stateTime += Gdx.graphics.getDeltaTime();
		moveChange++;
		frameNo = 1;
		if (moveChange > 15) {
			moveChange = 0;

			xkatsayisi = Math.random();
			ykatsayisi = Math.random();
			 //magnitude = Math.abs(Math.sqrt((xkatsayisi * xkatsayisi) + (ykatsayisi *
			 //ykatsayisi)));
			 //ykatsayisi = ykatsayisi / magnitude;
			// xkatsayisi = xkatsayisi / magnitude;

			double random = Math.random();
			if (random < 0.14) {
				dirx = -1;
				diry = -1;
			} else if (random > 0.14 && random < 0.28) {
				dirx = -1;
				diry = 1;
			} else if (random > 0.42 && random < 0.56) {
				dirx = 1;
				diry = -1;
			} else if (random > 0.56 && random < 0.7) {
				dirx = 1;
				diry = 1;
			} else {
				if (inVicinity) {
					dirx = 1;
					diry = 1;
					xkatsayisi = screen.getPlayer().getX() - charX;
					ykatsayisi = screen.getPlayer().getY() - charY;
				}
			}
			magnitude = Math.abs(Math.sqrt((xkatsayisi * xkatsayisi) + (ykatsayisi * ykatsayisi)));
			ykatsayisi = ykatsayisi / magnitude;
			xkatsayisi = xkatsayisi / magnitude;
		}
		charX += dirx * xkatsayisi * Speed * Gdx.graphics.getDeltaTime();
		charY += diry * ykatsayisi * Speed * Gdx.graphics.getDeltaTime();

		setPosition(charX, charY);

	}

	public void setInactive() {
		frameNo = 0;
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
		return (walk[frameNo].getKeyFrame(stateTime, true));
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
