package me.javawarriors.reverend.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Bullet extends Entity {

	public static final int Speed = 7;
	private static Texture texture;
	float x, y;
	double xkatsayısı,ykatsayısı;
	double Rot;
	float secondsElapsed;

	private boolean remove = false;

	public Bullet(float playerX, float playerY) {
		x = playerX;
		y = playerY;
		ykatsayısı = -(540 - 1080 + Gdx.input.getY());
		xkatsayısı = -(960 - Gdx.input.getX());
		if (texture == null) {
			texture = new Texture("Pew.png");
		}

	}

	public void update(float delta) {
		move();

		secondsElapsed += delta;

		if (secondsElapsed > 3) {
			remove = true;
		}
	}

	public boolean shouldRemove() {
		return remove;
	}

	@Override
	public void move() {
		x += xkatsayısı*Speed* Gdx.graphics.getDeltaTime();
		y += ykatsayısı*Speed* Gdx.graphics.getDeltaTime();
		System.out.println(x + " - " + y);
		setPosition(x, y);
	}

	public void render(Batch batch) {
		batch.draw(texture, x, y);
	}
}
