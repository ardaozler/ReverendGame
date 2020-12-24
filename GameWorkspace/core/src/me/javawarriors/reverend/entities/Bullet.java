package me.javawarriors.reverend.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Bullet extends Entity {

	public static final int Speed = 500;
	private static Texture texture;
	float x, y;
	double xkatsayısı,ykatsayısı;
	double Rot;
	float secondsElapsed;

	private boolean remove = false;

	public Bullet(float playerX, float playerY) {
		x = playerX;
		y = playerY;
		ykatsayısı = (playerY - 1080 - Gdx.input.getY());
		xkatsayısı = (playerX - Gdx.input.getX());
		Rot = Math.atan2(xkatsayısı, ykatsayısı);
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
		//x += xkatsayısı*Speed* Gdx.graphics.getDeltaTime();
		//y += ykatsayısı*Speed* Gdx.graphics.getDeltaTime();
		x += (Speed * Gdx.graphics.getDeltaTime())* Math.cos(Rot);
		y += (Speed * Gdx.graphics.getDeltaTime())* Math.sin(Rot);
		System.out.println(x + " - " + y);
		setPosition(x, y);
	}

	public void render(Batch batch) {
		batch.draw(texture, x, y);
	}
}
