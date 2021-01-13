package me.javawarriors.reverend.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import me.javawarriors.reverend.screens.GameScreen;

public class Trap extends Entity {

	// Animations
	Animation<TextureRegion>[] trapPop;
	private static final float trapAnimationSpeed = 0.08f;
	float stateTime = 0;
	int frameNo = 0;
	float trapAnimTime = 0;

	float x, y, Px, Py;

	Boolean Active = true;
	Boolean Trapped = false;
	GameScreen screen;
	Sound TrapSound = Gdx.audio.newSound(Gdx.files.internal("trap.wav"));
	private boolean remove = false;
	Player player;

	public Trap(Player player, GameScreen GameScreen, float x, float y) {
		this.player = player;
		this.screen = GameScreen;
		this.x = x;
		this.y = y;
		
		trapPop = new Animation[4];
		TextureRegion[][] trapPopSpriteSheet = TextureRegion.split(new Texture("trap.png"), 16, 16);
		trapPop[0] = new Animation<>(0, trapPopSpriteSheet[0]);
		trapPop[1] = new Animation<>(trapAnimationSpeed, trapPopSpriteSheet[1]);
		trapPop[2] = new Animation<>(0, trapPopSpriteSheet[2]);

		screen.getTraps().add(this);

	}

	public void update(float delta) {
		float Px = screen.getPlayer().getX();
		float Py = screen.getPlayer().getY();
		stateTime += delta;

		if (Trapped) {
			trapAnimTime += delta;
			if (trapAnimTime >= 4 * trapAnimationSpeed) {
				frameNo = 2;
			}
		}

		if (Active) {
			if ((Px > x && Px < x + 50) && (Py > y && Py < y + 50)) {
				screen.getPlayer().HP -= 40;
				Trapped = true;
				Active = false;
				TrapSound.play();
				frameNo = 1;
				System.out.println("Get stickBugged");
			}
		}

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

	public Boolean getTrapped() {
		return Trapped;
	}

	public void setTrapped(Boolean trapped) {
		Trapped = trapped;
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
		return (trapPop[frameNo].getKeyFrame(stateTime, true));
	}
}
