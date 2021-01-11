package me.javawarriors.reverend.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import me.javawarriors.reverend.screens.GameScreen;

public class Healing extends Entity {


	private static Texture texture;
	private static TextureRegion textureRegion;
	float x, y,Px,Py;
	
	Boolean Active=true;
	Boolean HealinVicinity=false;
	GameScreen screen;
	Sound water = Gdx.audio.newSound(Gdx.files.internal("watersound.wav"));
	private boolean remove = false;
	Player player;
	
	public Healing(Player player, GameScreen GameScreen ,float x,float y) {
		this.player=player;
		this.screen = GameScreen;
		
		
		this.x=x;
		this.y=y;
		
		if (texture == null) {
			texture = new Texture("Shield.png");
			textureRegion = new TextureRegion(texture);
		}
		screen.getHeals().add(this);

	}

	public void update(float delta) {
		float Px = screen.getPlayer().getX();
		float Py = screen.getPlayer().getY();
		
		if(Active) {
		if((Px>x && Px<x+150)&&(Py>y &&Py<y+150)) {
			
			screen.getPlayer().HealInVicinity=true;
			if(screen.getPlayer().Heal) {
				screen.getPlayer().HP=100;
				Active=false;
				// Texturei degiscek
			}
			water.play();
			System.out.println("Get splashed");
	}else {
		screen.getPlayer().HealInVicinity=false;
	}
		}
		

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

	public void render(SpriteBatch batch) {
		batch.draw(textureRegion, x, y-8, 23*3, 32*3);
	}
}
