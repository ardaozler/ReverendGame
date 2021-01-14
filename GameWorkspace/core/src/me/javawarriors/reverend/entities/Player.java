package me.javawarriors.reverend.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

import me.javawarriors.reverend.screens.GameScreen;

public class Player extends Entity {

	GameScreen screen;
	boolean isDamaged = false;

	Shield Shield;

	// char properties
	float charX =-150;
	float charY = 2200;
	int charWidthInPixels = 25;
	int charHeightInPixels = 29;
	float charWidth = charWidthInPixels * 4;
	float charHeight = charHeightInPixels * 4;
	float speed = 600;
	int dx = 0, dy = 0;
	int HP;
	int bulletSpeed = 800;
	boolean isShieldOn = false;
	
	// char Animation properties
	Animation<TextureRegion>[] healthBar;
	Animation<TextureRegion>[] walk;
	Texture HealNotification;
	private static final float charAnimationSpeed = 0.15f;
	int frameNo;
	float stateTime;
	
	// collision
	private TiledMapTileLayer collisionLayer;

	// shield
	float shieldCooldown = 0;
	
	// dash
	int dash = 1;
	float dashCooldown = 0;
	float dashTimer = 0;
	Sound dashSfx = Gdx.audio.newSound(Gdx.files.internal("dash.ogg"));

	public Player(TiledMapTileLayer collisionLayer, GameScreen screen) {
		this.collisionLayer = collisionLayer;
		this.screen = screen;
		frameNo = 0;
		
		//Heal notification seyi cikcak boyle ekranin ortasinda falan assetti var
		
		walk = new Animation[6];
		TextureRegion[][] walkSpriteSheet = TextureRegion.split(new Texture("charAnimOld2.png"), charWidthInPixels,
				charHeightInPixels);
		walk[0] = new Animation<>(charAnimationSpeed, walkSpriteSheet[0]);
		walk[1] = new Animation<>(charAnimationSpeed, walkSpriteSheet[1]);
		walk[2] = new Animation<>(charAnimationSpeed, walkSpriteSheet[2]);

		healthBar = new Animation[11];
		TextureRegion[][] healthBarSpriteSheet = TextureRegion.split(new Texture("charHealth.png"), 41, 7);
		healthBar[0] = new Animation<>(0, healthBarSpriteSheet[0]);
		healthBar[1] = new Animation<>(0, healthBarSpriteSheet[1]);
		healthBar[2] = new Animation<>(0, healthBarSpriteSheet[2]);
		healthBar[3] = new Animation<>(0, healthBarSpriteSheet[3]);
		healthBar[4] = new Animation<>(0, healthBarSpriteSheet[4]);
		healthBar[5] = new Animation<>(0, healthBarSpriteSheet[5]);
		healthBar[6] = new Animation<>(0, healthBarSpriteSheet[6]);
		healthBar[7] = new Animation<>(0, healthBarSpriteSheet[7]);
		healthBar[8] = new Animation<>(0, healthBarSpriteSheet[8]);
		healthBar[9] = new Animation<>(0, healthBarSpriteSheet[9]);
		healthBar[10] = new Animation<>(0, healthBarSpriteSheet[10]);

		HP = 100;

	}

	public void Update(float delta) {
		// System.out.println("player loc x" +(int)(charX)+ " y "+(int)(charY));
		float oldX = charX, oldY = charY;
		boolean collision = false;
		stateTime += delta;
		shieldCooldown += delta;
		dashCooldown += delta;
		dashTimer += delta;
		move();

		if (Gdx.input.isTouched()) {
			shoot(charX + 27, charY + 37, collisionLayer);
		}
		if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			if (shieldCooldown > 8) {
				isShieldOn = true;
				Shield = new Shield(charX, charY, screen, "player");
				screen.getShields().add(Shield);
				shieldCooldown = 0;
			} else
				System.out.println("Liderim cooldown bekle aq " + (8 - (int) shieldCooldown) + " saniye var");

		}
		if (Gdx.input.isKeyJustPressed(Keys.SHIFT_LEFT)) {
			if (dashCooldown > 4) {
				
				dashTimer=0;
				dash();
				
				dashCooldown = 0;
			} else
				System.out.println("Dash cd=" + (4 - (int)dashCooldown));

		}
		
		
		if(dashTimer>0.2) {
			dash();
		}
		if (Shield != null) {
			Shield.update(delta);
			if (Shield.shouldRemove()) {
				isShieldOn = false;
				screen.getShields().remove(Shield);
			}
		}
		// concurrent modification exception olmaması için ikinci array açıp looplama
		// bittikten sorna siliyoruz
		ArrayList<Bullet> bulletsToRemove = new ArrayList<Bullet>();
		for (Bullet bullet : screen.getPbullets()) {

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
		if (udead()) {

		}
	}

	public boolean HitScan() {

		for (Bullet bullet : screen.getMbullets()) {

			float x = bullet.getX() + bullet.getWidth();
			float y = bullet.getY() + bullet.getHeight();
			if (x > charX && x < charX + charWidth && y > charY && y < charY + charHeight && !bullet.isCollided()) {
				if (bullet.secondsElapsed > 0.1 && !isDamaged) {
					bullet.setCollided(true);
					bullet.setRemove(true);
					if (!isShieldOn) {
						HP -= 5;
					}
					System.out.println("Player " + HP);
					isDamaged = true;
				}
				return true;
			}
		}
		return false;
	}

	public boolean udead() {

		if (HP < 0) {
			return true;
		}
		return false;
	}

	private void shoot(float playerX, float playerY, TiledMapTileLayer collisionLayer) {
		if (screen.getPbullets().size() == 0
				|| screen.getPbullets().get(screen.getPbullets().size() - 1).secondsElapsed > 0.2) {
			screen.getPbullets()
					.add(new Bullet(playerX, playerY, collisionLayer, -(50 + 960 - Gdx.input.getX()) + charX,
							-(50 + 540 - 1080 + Gdx.input.getY()) + charY, "Player", this.screen, bulletSpeed));
		}

	}

	public ArrayList<Bullet> getPBullets() {
		return screen.getPbullets();
	}

	private boolean isCellBlocked(float x, float y) {
		Cell cell = collisionLayer.getCell((int) (x / (collisionLayer.getTileWidth() * 4)),
				(int) (y / (collisionLayer.getTileHeight() * 4)));
		return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey("blocked");
	}

	private void collisionCheck() {
		if (isCellBlocked(charX - 10, charY)) {
			// System.out.println("sol alt sol bok");
			dx = 0;
		}
		if (isCellBlocked(charX, charY - 10)) {
			// System.out.println("sol alt alt bok");
			dy = 0;
		}
		if (isCellBlocked(charX - 10, charY + getHeight() * 1 / 3)) {
			// System.out.println("sol üst sol bok");
			dx = 0;
		}
		if (isCellBlocked(charX, charY + 10 + getHeight() * 1 / 3)) {
			// System.out.println("sol üst üst bok");
			dy = 0;
		}
		if (isCellBlocked(charX + getWidth() * 1 / 2 + 10, charY)) {
			// System.out.println("sağ alt sağ bok");
			dx = 0;
		}
		if (isCellBlocked(charX + getWidth() * 1 / 2, charY - 10)) {
			// System.out.println("sağ alt alt bok");
			dy = 0;
		}
		if (isCellBlocked(charX + getWidth() * 1 / 2 + 10, charY + getHeight() * 1 / 3)) {
			// System.out.println("sağ üst sağ bok");
			dx = 0;
		}
		if (isCellBlocked(charX + getWidth() * 1 / 2, charY + 10 + getHeight() * 1 / 3)) {
			// System.out.println("sağ üst üst bok");
			dy = 0;
		}
	}

	public void dash() {
		
		
		if(dashTimer < 0.2) {
			System.out.println("anan");
			dashSfx.play();
			dash = 5;
		}
		else {
			dash=1;
		}
	}

	public void move() {

		if ((Gdx.input.isKeyPressed(Keys.RIGHT) || (Gdx.input.isKeyPressed(Keys.D)))
				&& (Gdx.input.isKeyPressed(Keys.UP) || (Gdx.input.isKeyPressed(Keys.W)))) {
			frameNo = 1;
			dx = 1;
			dy = 1;
			if (isCellBlocked(charX + getWidth() * 1 / 2 + 10, charY + getHeight() * 1 / 3)) {
				// System.out.println("sağ üst sağ bok");
				dx = 0;
			}
			if (isCellBlocked(charX + getWidth() * 1 / 2, charY + 10 + getHeight() * 1 / 3)) {
				// System.out.println("sağ üst üst bok");
				dy = 0;
			}
			if (isCellBlocked(charX + getWidth() * 1 / 2 + 10, charY)) {
				// System.out.println("sağ alt sağ bok");
				dx = 0;
			}
			if (isCellBlocked(charX, charY + 10 + getHeight() * 1 / 3)) {
				// System.out.println("sol üst üst bok");
				dy = 0;
			}
			// collisionChech();
			charX += speed * dash * Gdx.graphics.getDeltaTime() * dx * 0.7;
			charY += speed * dash * Gdx.graphics.getDeltaTime() * dy * 0.7;

		} else if ((Gdx.input.isKeyPressed(Keys.UP) || (Gdx.input.isKeyPressed(Keys.W)))
				&& (Gdx.input.isKeyPressed(Keys.LEFT) || (Gdx.input.isKeyPressed(Keys.A)))) {
			frameNo = 2;
			dx = -1;
			dy = 1;
			if (isCellBlocked(charX - 10, charY + getHeight() * 1 / 3)) {
				// System.out.println("sol üst sol bok");
				dx = 0;
			}
			if (isCellBlocked(charX, charY + 10 + getHeight() * 1 / 3)) {
				// System.out.println("sol üst üst bok");
				dy = 0;
			}
			if (isCellBlocked(charX - 10, charY)) {
				// System.out.println("sol alt sol bok");
				dx = 0;
			}
			if (isCellBlocked(charX + getWidth() * 1 / 2, charY + 10 + getHeight() * 1 / 3)) {
				// System.out.println("sağ üst üst bok");
				dy = 0;
			}
			// collisionCheck();
			charX += speed * dash * Gdx.graphics.getDeltaTime() * dx * 0.7;
			charY += speed * dash * Gdx.graphics.getDeltaTime() * dy * 0.7;

		} else if ((Gdx.input.isKeyPressed(Keys.DOWN) || (Gdx.input.isKeyPressed(Keys.S)))
				&& (Gdx.input.isKeyPressed(Keys.RIGHT) || (Gdx.input.isKeyPressed(Keys.D)))) {
			frameNo = 1;
			dx = 1;
			dy = -1;
			if (isCellBlocked(charX + getWidth() * 1 / 2 + 10, charY)) {
				// System.out.println("sağ alt sağ bok");
				dx = 0;
			}
			if (isCellBlocked(charX + getWidth() * 1 / 2, charY - 10)) {
				// System.out.println("sağ alt alt bok");
				dy = 0;
			}
			if (isCellBlocked(charX + getWidth() * 1 / 2 + 10, charY + getHeight() * 1 / 3)) {
				// System.out.println("sağ üst sağ bok");
				dx = 0;
			}
			if (isCellBlocked(charX, charY - 10)) {
				// System.out.println("sol alt alt bok");
				dy = 0;
			}
			// collisionCheck();
			charX += speed * dash * Gdx.graphics.getDeltaTime() * dx * 0.7;
			charY += speed * dash * Gdx.graphics.getDeltaTime() * dy * 0.7;

		} else if ((Gdx.input.isKeyPressed(Keys.DOWN) || (Gdx.input.isKeyPressed(Keys.S)))
				&& (Gdx.input.isKeyPressed(Keys.LEFT) || (Gdx.input.isKeyPressed(Keys.A)))) {
			frameNo = 2;
			dx = -1;
			dy = -1;
			if (isCellBlocked(charX - 10, charY)) {
				// System.out.println("sol alt sol bok");
				dx = 0;
			}
			if (isCellBlocked(charX - 10, charY + getHeight() * 1 / 3)) {
				// System.out.println("sol üst sol bok");
				dx = 0;
			}
			if (isCellBlocked(charX + getWidth() * 1 / 2, charY - 10)) {
				// System.out.println("sağ alt alt bok");
				dy = 0;
			}
			// collisionCheck();
			charX += speed * dash * Gdx.graphics.getDeltaTime() * dx * 0.7;
			charY += speed * dash * Gdx.graphics.getDeltaTime() * dy * 0.7;

		} else if (Gdx.input.isKeyPressed(Keys.UP) || (Gdx.input.isKeyPressed(Keys.W))) {
			frameNo = 1;
			dx = 0;
			dy = 1;
			if (isCellBlocked(charX, charY + 10 + getHeight() * 1 / 3)) {
				// System.out.println("sol üst üst bok");
				dy = 0;
			}
			if (isCellBlocked(charX + getWidth() * 1 / 2, charY + 10 + getHeight() * 1 / 3)) {
				// System.out.println("sağ üst üst bok");
				dy = 0;
			}
			// collisionCheck();
			charX += speed * dash * Gdx.graphics.getDeltaTime() * dx;
			charY += speed * dash * Gdx.graphics.getDeltaTime() * dy;

		} else if (Gdx.input.isKeyPressed(Keys.RIGHT) || (Gdx.input.isKeyPressed(Keys.D))) {
			frameNo = 1;
			dx = 1;
			dy = 0;
			if (isCellBlocked(charX + getWidth() * 1 / 2 + 10, charY)) {
				// System.out.println("sağ alt sağ bok");
				dx = 0;
			}
			if (isCellBlocked(charX + getWidth() * 1 / 2 + 10, charY + getHeight() * 1 / 3)) {
				// System.out.println("sağ üst sağ bok");
				dx = 0;
			}
			// collisionCheck();
			charX += speed * dash * Gdx.graphics.getDeltaTime() * dx;
			charY += speed * dash * Gdx.graphics.getDeltaTime() * dy;

		} else if (Gdx.input.isKeyPressed(Keys.DOWN) || (Gdx.input.isKeyPressed(Keys.S))) {
			frameNo = 2;
			dx = 0;
			dy = -1;
			if (isCellBlocked(charX, charY - 10)) {
				// System.out.println("sol alt alt bok");
				dy = 0;
			}
			if (isCellBlocked(charX + getWidth() * 1 / 2, charY - 10)) {
				// System.out.println("sağ alt alt bok");
				dy = 0;
			}
			// collisionCheck();
			charX += speed * dash * Gdx.graphics.getDeltaTime() * dx;
			charY += speed * dash * Gdx.graphics.getDeltaTime() * dy;

		} else if (Gdx.input.isKeyPressed(Keys.LEFT) || (Gdx.input.isKeyPressed(Keys.A))) {
			frameNo = 2;
			dx = -1;
			dy = 0;
			if (isCellBlocked(charX - 10, charY + getHeight() * 1 / 3)) {
				// System.out.println("sol üst sol bok");
				dx = 0;
			}
			if (isCellBlocked(charX - 10, charY)) {
				// System.out.println("sol alt sol bok");
				dx = 0;
			}
			// collisionCheck();
			charX += speed * dash * Gdx.graphics.getDeltaTime() * dx;
			charY += speed * dash * Gdx.graphics.getDeltaTime() * dy;

		} else {
			frameNo = 0;
			dx = 0;
			dy = 0;
		}

		setPosition(charX, charY);
	}
	

	public int getHP() {
		return HP;
	}

	public void setHP(int hP) {
		HP = hP;
	}

	public boolean isShieldOn() {
		return isShieldOn;
	}

	public void setShieldOn(boolean isShieldOn) {
		this.isShieldOn = isShieldOn;
	}

	public TextureRegion GetHealthFrame() {
		return (healthBar[10 - (HP / 10)].getKeyFrame(stateTime));
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
