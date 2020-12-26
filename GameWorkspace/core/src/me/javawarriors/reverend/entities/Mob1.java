package me.javawarriors.reverend.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

import me.javawarriors.reverend.screens.GameScreen;

public class Mob1 extends Entity{

	

		GameScreen screen;
		// char properties
		float charX = 1852;
		float charY = 2600;
		int charWidthInPixels = 25;
		int charHeightInPixels = 29;
		float charWidth = charWidthInPixels * 4;
		float charHeight = charHeightInPixels * 4;
		float speed = 600;
		int dx = 0, dy = 0;
		int HP;
		int Speed=500;
		int dirx=0;
		int diry=0;
		double xkatsayisi, ykatsayisi, magnitude;
		int moveChange=0;
		// char Animation properties
		Animation<TextureRegion>[] walk;
		private static final float charAnimationSpeed = 0.15f;
		int frameNo;
		float stateTime;

		// collision
		private TiledMapTileLayer collisionLayer;

		// bullet
		//private ArrayList<Bullet> bullets;

		public Mob1(TiledMapTileLayer collisionLayer,GameScreen screen) {
			this.collisionLayer = collisionLayer;
			this.screen= screen;
			frameNo = 0;
			walk = new Animation[6];
			TextureRegion[][] walkSpriteSheet = TextureRegion.split(new Texture("charAnim.png"), charWidthInPixels,
					charHeightInPixels);

			walk[0] = new Animation<>(charAnimationSpeed, walkSpriteSheet[0]);
			walk[1] = new Animation<>(charAnimationSpeed, walkSpriteSheet[1]);
			 HP=100;
			

		}
		
		public boolean HitScan() {
			
			for(Bullet bullet: screen.getBullets()) {
			
				
				
			float x=bullet.getX()+bullet.getWidth();
				float y=bullet.getY()+bullet.getHeight();
				if(x>charX&& x<charX+charWidth&&y>charY&& y<charY+charHeight) {
					System.out.println(x);
					if(bullet.secondsElapsed>0.15) {
					bullet.setRemove(true);}
					return true;
					
				}
					
				}
			return false;
		}
		public void Update(float delta) {
			float oldX = charX, oldY = charY;
			boolean collision = false;
			move();

			

			// concurrent modification exception olmaması için ikinci array açıp looplama
			// bittikten sorna siliyoruz
			ArrayList<Bullet> bulletsToRemove = new ArrayList<Bullet>();
			for (Bullet bullet : screen.getBullets()) {

				bullet.update(Gdx.graphics.getDeltaTime());

				if (bullet.shouldRemove()) {
					bulletsToRemove.add(bullet);
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

		}
		
		
		public void Vicinity(Player player) {
			
			if(Math.abs(charX-screen.getPlayer().charX)<300 &&Math.abs(charY-screen.getPlayer().charY)<300) {
				shoot(charX,charY,collisionLayer);
			
			}
		}
		
		
		private void shoot(float playerX, float playerY, TiledMapTileLayer collisionLayer) {
			if (screen.getBullets().size() == 0 || screen.getBullets().get(screen.getBullets().size() - 1).secondsElapsed > 0.2) {
				screen.getBullets().add(new Bullet(playerX, playerY, collisionLayer));
			}

		}

		public ArrayList<Bullet> getMBullets() {
			return screen.getBullets();
		}

		private boolean isCellBlocked(float x, float y) {
			Cell cell = collisionLayer.getCell((int) (x / (collisionLayer.getTileWidth() * 4)),
					(int) (y / (collisionLayer.getTileHeight() * 4)));
			return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey("blocked");
		}
		
		
		@Override
		public void move() {
			
			moveChange++;
			if(moveChange>15) {
			moveChange=0;
			xkatsayisi = Math.random();
			ykatsayisi= Math.random();
			magnitude = Math.abs(Math.sqrt((xkatsayisi * xkatsayisi) + (ykatsayisi * ykatsayisi)));
			ykatsayisi = ykatsayisi / magnitude;
			xkatsayisi = xkatsayisi / magnitude;
			
			double random = Math.random();
			if(random<0.25) {
				dirx=-1;
				diry=-1;
			}else if(random>0.25 && random<0.5){
				dirx=-1;
				diry=1;
			}else if(random>0.5 && random<0.75){
				dirx=1;
				diry=-1;
			}else {
				dirx=1;
				diry=1;
				
			}
			}
			charX+= dirx*xkatsayisi*Speed*Gdx.graphics.getDeltaTime();
			charY+= diry*ykatsayisi*Speed*Gdx.graphics.getDeltaTime();
			
			setPosition(charX, charY);
			
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

