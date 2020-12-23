package me.javawarriors.reverend.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Bullet extends Entity {

	public static final int Speed = 5;
	private static Texture texture;
	float x, y;
	double Rot;

	private boolean remove = false;

	public Bullet(float playerX, float playerY) {
		x = playerX;
		y = playerY;
		//Rot = Math.atan((playerY - y) / (playerX - x));

		if (texture == null) {
			texture = new Texture("Pew.png");
		}

	}

	public void update(float delta) {
		move();

		if (x == Gdx.graphics.getHeight()) {
			remove = true;
		} else if (y == Gdx.graphics.getWidth()) {
			remove = true;
		}
	}

	public boolean shouldRemove() {
		return remove;
	}

	@Override
	public void move() {
		x += (speed * Gdx.graphics.getDeltaTime());/// Math.cos(Rot);
		y += (speed * Gdx.graphics.getDeltaTime());/// Math.cos(Rot);
		System.out.println(x + y);

	}

	public void render(Batch batch) {
		batch.draw(texture, x, y);
	}
}
