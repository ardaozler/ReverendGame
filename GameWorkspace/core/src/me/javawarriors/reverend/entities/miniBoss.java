package me.javawarriors.reverend.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

import me.javawarriors.reverend.screens.GameScreen;

public class miniBoss extends Entity {

	GameScreen screen;
	boolean active = true;
	boolean isDamaged = false;
	boolean isDead = false;
	// char properties
	String MobName;
	float charX = 600;
	float charY = 1800;
	int charWidthInPixels = 26;
	int charHeightInPixels = 34;
	float charWidth = charWidthInPixels * 4;
	float charHeight = charHeightInPixels * 4;
	float speed = 500;// the speed that was given in constructor
	float Speed = 500;// used in move
	int dx = 0, dy = 0;
	int HP, HPtemp;

	int dirx = 0;
	int diry = 0;
	double xkatsayisi, ykatsayisi, magnitude, ykatsayisiShoot, xkatsayisiShoot;
	int moveChange = 0;
	float shootTime = 0;
	int bulletSpeed = 800;

	// char Animation properties
	Animation<TextureRegion>[] bossHealthBar;
	Animation<TextureRegion>[] walk;
	int bossHealthBarFrameNoTemp, bossHealthBarFrameNo;
	private static final float charAnimationSpeed = 0.15f;
	boolean showHealthBar = false;
	float stateTime;

	boolean inVicinity;
	Sound dead = Gdx.audio.newSound(Gdx.files.internal("calebblemum.mp3"));
	Sound alert = Gdx.audio.newSound(Gdx.files.internal("alert.mp3"));
	boolean alerted = false;

	// collision
	private TiledMapTileLayer collisionLayer;
	private TiledMapTileLayer mobcollisionLayer;

	// bullet
	private ArrayList<Bullet> bullets = new ArrayList<Bullet>();

	public miniBoss(TiledMapTileLayer collisionLayer, TiledMapTileLayer MobcollisionLayer, GameScreen screen,
			String MobName, int x, int y, int bulletSpeed, int walkingSpeed) {
		this.charX = x;
		this.charY = y;
		this.speed = walkingSpeed;
		this.Speed = walkingSpeed;
		this.bulletSpeed = bulletSpeed;
		this.MobName = MobName;
		this.collisionLayer = collisionLayer;
		this.screen = screen;
		this.mobcollisionLayer = MobcollisionLayer;
		walk = new Animation[6];

		TextureRegion[][] walkSpriteSheet = TextureRegion.split(new Texture("RedBoi-sheet.png"), charWidthInPixels,
				charHeightInPixels);

		walk[0] = new Animation<>(charAnimationSpeed, walkSpriteSheet[0]);

		bossHealthBar = new Animation[11];
		TextureRegion[][] healthBarSpriteSheet = TextureRegion.split(new Texture("bossHealth.png"), 41, 7);
		bossHealthBar[0] = new Animation<>(0, healthBarSpriteSheet[0]);
		bossHealthBar[1] = new Animation<>(0, healthBarSpriteSheet[1]);
		bossHealthBar[2] = new Animation<>(0, healthBarSpriteSheet[2]);
		bossHealthBar[3] = new Animation<>(0, healthBarSpriteSheet[3]);
		bossHealthBar[4] = new Animation<>(0, healthBarSpriteSheet[4]);
		bossHealthBar[5] = new Animation<>(0, healthBarSpriteSheet[5]);
		bossHealthBar[6] = new Animation<>(0, healthBarSpriteSheet[6]);
		bossHealthBar[7] = new Animation<>(0, healthBarSpriteSheet[7]);
		bossHealthBar[8] = new Animation<>(0, healthBarSpriteSheet[8]);
		bossHealthBar[9] = new Animation<>(0, healthBarSpriteSheet[9]);
		bossHealthBar[10] = new Animation<>(0, healthBarSpriteSheet[10]);

		HP = 300;
		screen.getMiniBosses().add(this);
	}

	public void Update(float delta) {
		shootTime += delta;
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
				isDamaged = false;
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
		if (Math.abs(charX - screen.getPlayer().charX) < 1500 && Math.abs(charY - screen.getPlayer().charY) < 1500) {
			active = true;
		}

		if (Math.abs(charX - screen.getPlayer().charX) < 700 && Math.abs(charY - screen.getPlayer().charY) < 700) {
			if (!alerted) {
				alert.play();
				alerted = true;
			}
			if (HP > 200) {
				shoot1(charX, charY, collisionLayer);
				Speed = speed / 2;
			} else if (HP <= 200 && HP > 100) {
				// shoot2
			} else {
				// shoot3
			}
			inVicinity = true;
			if (Math.abs(charX - screen.getPlayer().charX) < 200 && Math.abs(charY - screen.getPlayer().charY) < 200) {
				inVicinity = false;
			}
		}

	}

	public boolean HitScan() {

		for (Bullet bullet : screen.getPbullets()) {

			float x = bullet.getX() + bullet.getWidth();
			float y = bullet.getY() + bullet.getHeight();
			if (x > charX && x < charX + charWidth && y > charY && y < charY + charHeight && !bullet.isCollided()) {
				if (bullet.secondsElapsed > 0.15 && !isDamaged) {
					bullet.setCollided(true);
					bullet.setRemove(true);
					HP -= 5;
					System.out.println("mob " + HP);
					isDamaged = true;
				}
				return true;
			}
		}
		return false;
	}

	public boolean isDead() {

		if (HP <= 0) {

			return true;
		}
		return false;
	}

	private void shoot1(float playerX, float playerY, TiledMapTileLayer collisionLayer) {

		double hipotenus = Math
				.sqrt(Math.abs((((screen.getPlayer().getY() - charY) * (screen.getPlayer().getY() - charY))
						+ ((screen.getPlayer().getX() - charX * screen.getPlayer().getX() - charY)))));

		xkatsayisiShoot = screen.getPlayer().getX() - charX;
		ykatsayisiShoot = screen.getPlayer().getY() - charY;
		ykatsayisiShoot = ykatsayisiShoot / hipotenus;
		xkatsayisiShoot = xkatsayisiShoot / hipotenus;
		
		if (screen.getMbullets().size() == 0 || shootTime > 0.16) {
			shootTime = 0;

			bullets.add(new Bullet(playerX, playerY, collisionLayer, screen.getPlayer().charX, screen.getPlayer().charY,
					MobName, this.screen, bulletSpeed));
			screen.getMbullets().add(new Bullet(playerX, playerY, collisionLayer, screen.getPlayer().charX,
					screen.getPlayer().charY, MobName, this.screen, bulletSpeed));
			// some retardness is in this but idk how to find it dinçer helb
			bullets.add(new Bullet(playerX, playerY, collisionLayer,
					(float) (screen.getPlayer().charX + hipotenus * Math.sin(0.1)),
					(float) (screen.getPlayer().charY + hipotenus * Math.tan(0.1)), MobName, this.screen, bulletSpeed));
			screen.getMbullets().add(new Bullet(playerX, playerY, collisionLayer,
					(float) (screen.getPlayer().charX + hipotenus * Math.sin(0.1)),
					(float) (screen.getPlayer().charY + hipotenus * Math.tan(0.1)), MobName, this.screen, bulletSpeed));

			bullets.add(new Bullet(playerX, playerY, collisionLayer,
					(float) (screen.getPlayer().charX - hipotenus * Math.sin(0.1)),
					(float) (screen.getPlayer().charY - hipotenus * Math.tan(0.1)), MobName, this.screen, bulletSpeed));
			screen.getMbullets().add(new Bullet(playerX, playerY, collisionLayer,
					(float) (screen.getPlayer().charX - hipotenus * Math.sin(0.1)),
					(float) (screen.getPlayer().charY - hipotenus * Math.tan(0.1)), MobName, this.screen, bulletSpeed));

		}

	}

	private void shoot(float playerX, float playerY, TiledMapTileLayer collisionLayer) {
		if (screen.getMbullets().size() == 0 || shootTime > 0.16) {
			shootTime = 0;
			bullets.add(new Bullet(playerX, playerY, collisionLayer, screen.getPlayer().charX, screen.getPlayer().charY,
					MobName, this.screen, bulletSpeed));
			screen.getMbullets().add(new Bullet(playerX, playerY, collisionLayer, screen.getPlayer().charX,
					screen.getPlayer().charY, MobName, this.screen, bulletSpeed));
		}

	}

	public ArrayList<Bullet> getMBullets() {
		return screen.getBullets();
	}

	private boolean isCellBlocked(float x, float y) {
		Cell cell = collisionLayer.getCell((int) (x / (collisionLayer.getTileWidth() * 4)),
				(int) (y / (collisionLayer.getTileHeight() * 4)));
		Cell bosCell = mobcollisionLayer.getCell((int) (x / (mobcollisionLayer.getTileWidth() * 4)),
				(int) (y / (mobcollisionLayer.getTileHeight() * 4)));

		return (cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey("blocked"))
				|| (bosCell != null && bosCell.getTile() != null
						&& bosCell.getTile().getProperties().containsKey("blocked"));
	}

	@Override
	public void move() {
		stateTime += Gdx.graphics.getDeltaTime();
		moveChange++;
		if (moveChange > 15) {
			moveChange = 0;

			xkatsayisi = Math.random();
			ykatsayisi = Math.random();
			// magnitude = Math.abs(Math.sqrt((xkatsayisi * xkatsayisi) + (ykatsayisi *
			// ykatsayisi)));
			// ykatsayisi = ykatsayisi / magnitude;
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
		if (!isDead) {
			isDead = true;
			dead.play();
		}
		stateTime = 0;
		active = false;
		charX = -10000;
		charY = -10000;
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
		return (walk[0].getKeyFrame(stateTime, true));
	}

	public TextureRegion GetBossHealthFrame() {
		HPtemp = HP;
		System.out.println(HPtemp);
		if (HP > 200) {
			bossHealthBarFrameNoTemp = 30 - (HPtemp / 10);
		} else if (HP > 100 && HP <= 200) {
			bossHealthBarFrameNoTemp = 20 - (HPtemp / 10);
		} else {
			bossHealthBarFrameNoTemp = 10 - (HPtemp / 10);
		}
		bossHealthBarFrameNo = bossHealthBarFrameNoTemp;
		if (bossHealthBarFrameNoTemp >= 30) {
			bossHealthBarFrameNo = 10;
		}
		System.out.println(bossHealthBarFrameNo);
		return (bossHealthBar[bossHealthBarFrameNo].getKeyFrame(0));
	}

	public boolean isInVicinity() {
		return inVicinity;
	}

	public void setInVicinity(boolean inVicinity) {
		this.inVicinity = inVicinity;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
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
