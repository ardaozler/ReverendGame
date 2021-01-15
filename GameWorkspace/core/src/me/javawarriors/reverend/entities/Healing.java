package me.javawarriors.reverend.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import me.javawarriors.reverend.screens.GameScreen;

public class Healing extends Entity {

	private static Texture texture;
	private static TextureRegion textureRegion;
	float soundTime = 0;
	float x, y, Px, Py;
	float secondsElapsed;
	Boolean Active = true;
	Boolean HealinVicinity = false;
	GameScreen screen;
	Sound water = Gdx.audio.newSound(Gdx.files.internal("watersound.wav"));
	private boolean remove = false;
	Player player;
	boolean play = false;

	// Animation properties
	Animation<TextureRegion>[] fountain;
	private static final float AnimationSpeed = 0.15f;
	float stateTime;
	boolean inVicinity;
	int frameNo = 0;

	public Healing(Player player, GameScreen GameScreen, float x, float y) {
		this.player = player;
		this.screen = GameScreen;

		this.x = x;
		this.y = y;
		
		if (texture == null) {
			texture = new Texture("HealNotification.png");
			textureRegion = new TextureRegion(texture);
		}

		fountain = new Animation[2];
		TextureRegion[][] fountainSpriteSheet = TextureRegion.split(new Texture("fountain.png"), 16, 33);
		fountain[0] = new Animation<>(AnimationSpeed, fountainSpriteSheet[0]);
		fountain[1] = new Animation<>(0, fountainSpriteSheet[1]);

		screen.getHeals().add(this);

	}

	public void update(float delta) {
		float Px = screen.getPlayer().getX();
		float Py = screen.getPlayer().getY();
		if (Active) {
			stateTime += delta;
			if ((Px > x && Px < x + 150) && (Py > y && Py < y + 150)) {
				secondsElapsed += delta;
				if (!play || secondsElapsed > 4) {
					secondsElapsed = 0;
					water.play();
					play = true;
				}
				if (Active) {
					inVicinity = true;
				}

				if (Gdx.input.isKeyJustPressed(Keys.E)) {
					screen.getPlayer().setHP(100);
					frameNo = 1;
					stateTime = 0;
					Active = false;
					inVicinity = false;
				}

			} else {
				inVicinity = false;
				water.stop();

			}
		}

	}

	public boolean inHealRange() {
		return inVicinity;
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

	public Boolean getActive() {
		return Active;
	}

	public void setActive(Boolean active) {
		Active = active;
	}

	public boolean shouldRemove() {
		return remove;
	}

	public void setRemove(boolean remove) {
		this.remove = remove;
	}

	@Override
	public void move() {

	}

	public TextureRegion GetFrame() {
		return (fountain[frameNo].getKeyFrame(stateTime, true));
	}

	public void render(SpriteBatch batch) {
		batch.draw(textureRegion, player.getX(), player.getY() + 120,134, 14);
	}
}
