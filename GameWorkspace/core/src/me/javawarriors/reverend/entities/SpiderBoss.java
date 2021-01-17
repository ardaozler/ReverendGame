package me.javawarriors.reverend.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import me.javawarriors.reverend.screens.GameScreen;

public class SpiderBoss extends Entity {

	GameScreen screen;
	boolean active = true;
	boolean isDamaged = false;
	boolean isDead = false;
	// char properties
	String MobName;
	float charX = 600;
	float charY = 1800;
	int charWidthInPixels = 47;
	int charHeightInPixels = 32;
	float charWidth = charWidthInPixels * 4;
	float charHeight = charHeightInPixels * 4;
	float speed = 500;// the speed that was given in constructor
	float Speed = 500;// used in move
	int dx = 0, dy = 0;
	int HP, HPtemp;
	int shootAngle = 0;
	int shootX = 9000;
	int shootY = 9000;
	int shoot1X = -9000;
	int shoot1Y = -9000;
	int shoot2X = 9000;
	int shoot2Y = -9000;
	int shoot3X = -9000;
	int shoot3Y = 9000;
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
	private static final float charAnimationSpeed = 0.1f;
	boolean showHealthBar = false;
	float stateTime;
	int frameNo = 0;

	boolean inVicinity;
	Sound dead = Gdx.audio.newSound(Gdx.files.internal("calebblemum.mp3"));
	Music bossmusic = Gdx.audio.newMusic(Gdx.files.internal("back2back.mp3"));
	
	boolean alerted = false;

	// lil bebies
	private Mob2 mob2a;
	private float babyTime = 0;

	// collision
	private TiledMapTileLayer collisionLayer;
	private TiledMapTileLayer mobcollisionLayer;

	// bullet
	private ArrayList<sBullet> bullets = new ArrayList<sBullet>();

	public SpiderBoss(TiledMapTileLayer collisionLayer, TiledMapTileLayer MobcollisionLayer, GameScreen screen,
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
		bossmusic.setVolume(0.2f);

		walk = new Animation[2];
		TextureRegion[][] walkSpriteSheet = TextureRegion.split(new Texture("SpiderBossFinal-sheet.png"),
				charWidthInPixels, charHeightInPixels);
		walk[0] = new Animation<>(charAnimationSpeed, walkSpriteSheet[0]);
		walk[1] = new Animation<>(charAnimationSpeed, walkSpriteSheet[1]);

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

		HP = 600;

		screen.getSpiderBosses().add(this);
		setPosition(charX, charY);
	}

	public void Update(float delta) {
		stateTime += Gdx.graphics.getDeltaTime();
		shootTime += delta;
		babyTime += delta;

		// concurrent modification exception olmaması için ikinci array açıp looplama
		// bittikten sorna siliyoruz
		ArrayList<sBullet> bulletsToRemove = new ArrayList<sBullet>();
		for (sBullet bullet : screen.getSbullets()) {

			bullet.update(Gdx.graphics.getDeltaTime());

			if (bullet.shouldRemove()) {
				bulletsToRemove.add(bullet);
				isDamaged = false;
			}
		}
		screen.getSbullets().removeAll(bulletsToRemove);

		HitScan();

		Vicinity();
	}

	public void Vicinity() {
		if (Math.abs(charX - screen.getPlayer().charX) < 1500 && Math.abs(charY - screen.getPlayer().charY) < 1500) {
			active = true;
		}

		if (Math.abs(charX - screen.getPlayer().charX) < 1500 && Math.abs(charY - screen.getPlayer().charY) < 1500) {
			if (!alerted) {
				screen.music.stop();
				bossmusic.play();
				alerted = true;
				showHealthBar = true;
			}
			if (HP > 400) {
				shoot2(charX, charY, collisionLayer);
				if (babyTime > 5) {
					babyTime = 0;
					makeBabies(1);
				}
			} else if (HP <= 400 && HP > 200) {
				shoot1(charX, charY, collisionLayer);
				if (babyTime > 4) {
					babyTime = 0;
					makeBabies(1);
				}
			} else if (HP <= 200) {
				shoot2(charX, charY, collisionLayer);
				if (babyTime > 3) {
					babyTime = 0;
					makeBabies(1);
				}
			}
			inVicinity = true;

		}

	}

	public boolean HitScan() {

		for (Bullet bullet : screen.getPbullets()) {

			float x = bullet.getX() + bullet.getWidth();
			float y = bullet.getY() + bullet.getHeight();
			if (x > charX && x < charX + charWidth && y > charY && y < charY + charHeight && !bullet.isCollided()) {
				if (bullet.secondsElapsed > 0.15) {
					bullet.setCollided(true);
					bullet.setRemove(true);
					HP -= 5;
					isDamaged = true;
				}
				return true;
			}
		}
		return false;
	}

	public void makeBabies(int i) {
		for (; i > 0; i--) {
			mob2a = new Mob2((TiledMapTileLayer) screen.getMap().getLayers().get(3),
					(TiledMapTileLayer) screen.getMap().getLayers().get(0), screen, "Mob2a", (int) this.getX(),
					(int) this.getY(), 250, 20, 40, "orumcekebebe.png", 10, 16);
		}
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

		if (screen.getSbullets().size() == 0 || shootTime > 0.16) {
			shootTime = 0;

			bullets.add(new sBullet(playerX, playerY, collisionLayer, screen.getPlayer().charX,
					screen.getPlayer().charY, MobName, this.screen, bulletSpeed));
			screen.getSbullets().add(new sBullet(playerX, playerY, collisionLayer, screen.getPlayer().charX,
					screen.getPlayer().charY, MobName, this.screen, bulletSpeed));

			bullets.add(new sBullet(playerX, playerY, collisionLayer, (float) (screen.getPlayer().charX + 200),
					(float) (screen.getPlayer().charY + 200), MobName, this.screen, bulletSpeed));
			screen.getSbullets()
					.add(new sBullet(playerX, playerY, collisionLayer, (float) (screen.getPlayer().charX + 200),
							(float) (screen.getPlayer().charY + 200), MobName, this.screen, bulletSpeed));

			bullets.add(new sBullet(playerX, playerY, collisionLayer, (float) (screen.getPlayer().charX - 200),
					(float) (screen.getPlayer().charY - 200), MobName, this.screen, bulletSpeed));
			screen.getSbullets()
					.add(new sBullet(playerX, playerY, collisionLayer, (float) (screen.getPlayer().charX - 200),
							(float) (screen.getPlayer().charY - 200), MobName, this.screen, bulletSpeed));

		}

	}

	private void shoot(float playerX, float playerY, TiledMapTileLayer collisionLayer) {
		if (screen.getSbullets().size() == 0 || shootTime > 0.16) {
			shootTime = 0;
			bullets.add(new sBullet(playerX, playerY, collisionLayer, screen.getPlayer().charX,
					screen.getPlayer().charY, MobName, this.screen, bulletSpeed));
			screen.getSbullets().add(new sBullet(playerX, playerY, collisionLayer, screen.getPlayer().charX,
					screen.getPlayer().charY, MobName, this.screen, bulletSpeed));
		}

	}

	private void shoot2(float playerX, float playerY, TiledMapTileLayer collisionLayer) {

		shootAngle++;
		if (shootAngle < 90) {
			shootX -= 200;
			shoot1X += 200;
			shoot2Y += 200;
			shoot3Y -= 200;
		} else if (shootAngle < 180 && shootAngle >= 90) {
			shootY -= 200;
			shoot1Y += 200;
			shoot2X -= 200;
			shoot3X += 200;
		} else if (shootAngle < 270 && shootAngle >= 180) {
			shootX += 200;
			shoot1X -= 200;
			shoot2Y -= 200;
			shoot3Y += 200;
		} else if (shootAngle >= 270 && shootAngle < 360) {
			shootY += 200;
			shoot1Y -= 200;
			shoot2X += 200;
			shoot3X -= 200;

		} else {
			shootAngle = 0;
		}

		if (screen.getSbullets().size() == 0 || shootTime > 0.05) {
			frameNo = 1;
			shootTime = 0;
			bullets.add(new sBullet(playerX, playerY, collisionLayer, charX + shootX, charY + shootY, MobName,
					this.screen, bulletSpeed));

			screen.getSbullets().add(new sBullet(playerX, playerY, collisionLayer, charX + shootX, charY + shootY,
					MobName, this.screen, bulletSpeed));

			bullets.add(new sBullet(playerX, playerY, collisionLayer, charX + shoot1X, charY + shoot1Y, MobName,
					this.screen, bulletSpeed));
			screen.getSbullets().add(new sBullet(playerX, playerY, collisionLayer, charX + shoot1X, charY + shoot1Y,
					MobName, this.screen, bulletSpeed));
			
			bullets.add(new sBullet(playerX, playerY, collisionLayer, charX + shoot2X, charY + shoot2Y, MobName,
					this.screen, bulletSpeed));
			screen.getSbullets().add(new sBullet(playerX, playerY, collisionLayer, charX + shoot2X, charY + shoot2Y,
					MobName, this.screen, bulletSpeed));
			
			bullets.add(new sBullet(playerX, playerY, collisionLayer, charX + shoot3X, charY + shoot3Y, MobName,
					this.screen, bulletSpeed));
			screen.getSbullets().add(new sBullet(playerX, playerY, collisionLayer, charX + shoot3X, charY + shoot3Y,
					MobName, this.screen, bulletSpeed));
		}

	}

	public void setInactive() {
		if (!isDead) {
			screen.getPlayer().setHP(1000);
			isDead = true;
			showHealthBar = false;
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

	public boolean isShowHealthBar() {
		return showHealthBar;
	}

	public void setShowHealthBar(boolean showHealthBar) {
		this.showHealthBar = showHealthBar;
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

	public TextureRegion GetBossHealthFrame() {
		HPtemp = HP;
		if (HP > 400) {
			bossHealthBarFrameNoTemp = 30 - (HPtemp / 20);
		} else if(HP<=400 && HP>200) {
			bossHealthBarFrameNoTemp = 20 - (HPtemp / 20);
		}else {
			bossHealthBarFrameNoTemp = 10 - (HPtemp / 20);
		}
		bossHealthBarFrameNo = bossHealthBarFrameNoTemp;
		if (bossHealthBarFrameNoTemp >= 20) {
			bossHealthBarFrameNo = 10;
		}
		return (bossHealthBar[bossHealthBarFrameNo].getKeyFrame(0));
	}

	public Music getBossmusic() {
		return bossmusic;
	}

	public void setBossmusic(Music bossmusic) {
		this.bossmusic = bossmusic;
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

	@Override
	public void move() {
		// TODO Auto-generated method stub
		// i cant mooooove
	}

}
