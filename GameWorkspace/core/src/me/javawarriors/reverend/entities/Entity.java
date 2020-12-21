package me.javawarriors.reverend.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class Entity extends Sprite{
	float speed;
	
	public abstract void move();
}
