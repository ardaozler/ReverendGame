package me.javawarriors.reverend.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

public class Player extends Entity {
	float stateTime;
	// char properties
	int frameNo;
	float charX = 960 ;
	float charY = 540;
	int charWidthInPixels = 21;
	int charHeightInPixels = 27;
	float charWidth = charWidthInPixels * 3;
	float charHeight = charHeightInPixels * 3;
	float speed = 600;
	boolean colTopRight=false,colTopLeft=false,colBotRight=false,colBotLeft=false,colBot=false,colLeft=false,colTop=false,colRight=false;
	// char Animation properties
	Animation<TextureRegion>[] walk;
	private static final float charAnimationSpeed = 0.1f;
	
	//collision
	private TiledMapTileLayer collisionLayer;
	
	public Player(TiledMapTileLayer collisionLayer){
		this.collisionLayer=collisionLayer;
		frameNo = 0;
		walk = new Animation[5];
		TextureRegion[][] walkSpriteSheet = TextureRegion.split(new Texture("charAnim.png"), charWidthInPixels,
				charHeightInPixels);

		walk[frameNo] = new Animation(charAnimationSpeed, walkSpriteSheet[frameNo]);
		
	}
	
	public void Update(float delta) {
		float oldX=charX, oldY= charY;
		boolean collision=false;
		move();
		
		//top left
		collision=isCellBlocked(getX(),getY()+getHeight());
		
		//bot left
		if(!collision) {
			collision=isCellBlocked(getX(),getY());
		}
		//top right
		if(!collision) {
			collision=isCellBlocked(getX()+getWidth(),getY()+getHeight());
		}		
		//bot right
		if(!collision) {
			collision=isCellBlocked(getX()+getWidth(),getY());
		}
		
		if(collision) {
			charX=oldX;
			charY=oldY;
		}
		
		//kasiyo anlamadim 
		/*if(collision) {
			if(colTopRight||colTopLeft||colBotRight||colBotLeft) {
				charX=oldX;
				charY=oldY;
			}
			else if(colTop||colBot) {
				
				charY=oldY;
			}
			else if(colLeft||colRight) {
				charX=oldX;
				
			}
			
		}*/
		
	}
	private boolean isCellBlocked(float x,float y) {
		Cell cell=collisionLayer.getCell((int)(x/(collisionLayer.getTileWidth()*4)), (int)(y/(collisionLayer.getTileHeight()*4)));
		return cell !=null&& cell.getTile()!= null&& cell.getTile().getProperties().containsKey("blocked");
	}
	//COMMENTLADIGIM YERLER COLLISIONDAKI SU JITTERY OLAYINI DUZELTMEK ICIN AMA IMPLEMENTLEDIGIMDE KASIYOR  :(
	//BOOLEAN YERINE INT KOYSAK OLABILIR,SIMDI DUSUNDUM,KAFAMI SIKIYIM BU YAPTIGIM BOS ISE
	public void move() {
		
		if ((Gdx.input.isKeyPressed(Keys.RIGHT) || (Gdx.input.isKeyPressed(Keys.D)))
				&& (Gdx.input.isKeyPressed(Keys.UP) || (Gdx.input.isKeyPressed(Keys.W)))&&colTopRight==false) { //&&colTopRight==false
			charY += speed * Gdx.graphics.getDeltaTime() * 0.7;
			charX += speed * Gdx.graphics.getDeltaTime() * 0.7;
			stateTime += Gdx.graphics.getDeltaTime();
			//colTopRight=true;colTopLeft=false;colBotRight=false;colBotLeft=false;colBot=false;colLeft=false;colTop=false;colRight=false;
		} else if ((Gdx.input.isKeyPressed(Keys.UP) || (Gdx.input.isKeyPressed(Keys.W)))
				&& (Gdx.input.isKeyPressed(Keys.LEFT) || (Gdx.input.isKeyPressed(Keys.A)))&&colTopLeft==false) {//&&colTopLeft==false
			charY += speed * Gdx.graphics.getDeltaTime() * 0.7;
			charX -= speed * Gdx.graphics.getDeltaTime() * 0.7;
			stateTime += Gdx.graphics.getDeltaTime();
			//colTopRight=false;colTopLeft=true;colBotRight=false;colBotLeft=false;colBot=false;colLeft=false;colTop=false;colRight=false;
		} else if ((Gdx.input.isKeyPressed(Keys.DOWN) || (Gdx.input.isKeyPressed(Keys.S)))
				&& (Gdx.input.isKeyPressed(Keys.RIGHT) || (Gdx.input.isKeyPressed(Keys.D)))&&colBotRight==false) {//&&colBotRight==false
			charY -= speed * Gdx.graphics.getDeltaTime() * 0.7;
			charX += speed * Gdx.graphics.getDeltaTime() * 0.7;
			stateTime += Gdx.graphics.getDeltaTime();
			//colTopRight=false;colTopLeft=false;colBotRight=true;colBotLeft=false;colBot=false;colLeft=false;colTop=false;colRight=false;
		} else if ((Gdx.input.isKeyPressed(Keys.DOWN) || (Gdx.input.isKeyPressed(Keys.S)))
				&& (Gdx.input.isKeyPressed(Keys.LEFT) || (Gdx.input.isKeyPressed(Keys.A)))&&colBotLeft==false) {//&&colBotLeft==false
			charY -= speed * Gdx.graphics.getDeltaTime() * 0.7;
			charX -= speed * Gdx.graphics.getDeltaTime() * 0.7;
			stateTime += Gdx.graphics.getDeltaTime();
			//colTopRight=false;colTopLeft=false;colBotRight=false;colBotLeft=true;colBot=false;colLeft=false;colTop=false;colRight=false;
		} else if (Gdx.input.isKeyPressed(Keys.UP) || (Gdx.input.isKeyPressed(Keys.W))&&colTop==false) {//&&colTop==false
			charY += speed * Gdx.graphics.getDeltaTime();
			stateTime += Gdx.graphics.getDeltaTime();
			//colTopRight=false;colTopLeft=false;colBotRight=false;colBotLeft=false;colBot=false;colLeft=false;colTop=true;colRight=false;
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT) || (Gdx.input.isKeyPressed(Keys.D))&&colRight==false) {//&&colRight==false
			charX += speed * Gdx.graphics.getDeltaTime();
			stateTime += Gdx.graphics.getDeltaTime();
			//colTopRight=false;colTopLeft=false;colBotRight=false;colBotLeft=false;colBot=false;colLeft=false;colTop=false;colRight=true;
		} else if (Gdx.input.isKeyPressed(Keys.DOWN) || (Gdx.input.isKeyPressed(Keys.S))&&colLeft==false) {//&&colLeft==false
			charY -= speed * Gdx.graphics.getDeltaTime();
			stateTime += Gdx.graphics.getDeltaTime();
			//colTopRight=false;colTopLeft=false;colBotRight=false;colBotLeft=false;colBot=true;colLeft=false;colTop=false;colRight=false;
		} else if (Gdx.input.isKeyPressed(Keys.LEFT) || (Gdx.input.isKeyPressed(Keys.A))&&colBot==false) {//&&colBot==false
			charX -= speed * Gdx.graphics.getDeltaTime();
			stateTime += Gdx.graphics.getDeltaTime();
			//colTopRight=false;colTopLeft=false;colBotRight=false;colBotLeft=false;colBot=false;colLeft=true;colTop=false;colRight=false;
		}else {
			stateTime = 0;
		}
		setPosition(charX, charY);
	}
	
	public TextureRegion GetFrame() {
		return (walk[frameNo].getKeyFrame(stateTime, true));
	}
	
	public void draw(SpriteBatch spriteBatch) {
		Update(Gdx.graphics.getDeltaTime());
		super.draw(spriteBatch);
	}

	public float getWidth() {
		return charWidth;
	}
	public float getHeight() {
		return charHeight;
	}
}
