package me.javawarriors.reverend.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;

import me.javawarriors.reverend.ReverendGame;
import me.javawarriors.reverend.entities.Bullet;
import me.javawarriors.reverend.entities.Mob1;
import me.javawarriors.reverend.entities.Player;

public class GameScreen implements Screen {

	// map
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private Mob1 mob1;
	private Player player;
	private ArrayList<Bullet> bullets;
	ReverendGame game;

	public GameScreen(ReverendGame game) {

		this.game = game;
		bullets=new ArrayList<Bullet>();
	}

	@Override
	public void show() {
		TmxMapLoader loader = new TmxMapLoader();
		map = loader.load("trymob.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 4f);
		player = new Player((TiledMapTileLayer) map.getLayers().get(1), this);
		camera = new OrthographicCamera();
		mob1 = new Mob1((TiledMapTileLayer) map.getLayers().get(1), this);
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		player.Update(Gdx.graphics.getDeltaTime());
		mob1.Update(Gdx.graphics.getDeltaTime());
		// CAmera Smooth Follow lerp değerini değiştirerek kamera follow hızı
		// değiştri=========================

		float lerp = 10f;
		Vector3 position = camera.position;
		//position.x += (player.getX() - position.x + player.getWidth() / 2) * lerp * Gdx.graphics.getDeltaTime();
		//position.y += (player.getY() - position.y + player.getHeight() / 2) * lerp * Gdx.graphics.getDeltaTime();

		// sabit cam============================================================================================
		 camera.setToOrtho(false);
		 camera.position.set(player.getX(), player.getY(), 0);

		camera.update();

		renderer.setView(camera);
		renderer.render();

		renderer.getBatch().begin();
		for (Bullet bullet : bullets) {
			bullet.render((SpriteBatch) renderer.getBatch());
		}
		renderer.getBatch().draw(player.GetFrame(), player.getX(), player.getY(), player.getWidth(),
				player.getHeight());
		renderer.getBatch().draw(mob1.GetFrame(), mob1.getX(), mob1.getY(), mob1.getWidth(),
				player.getHeight());
		renderer.getBatch().end();

	}

	@Override
	public void resize(int width, int height) {
		camera.viewportHeight = height;
		camera.viewportWidth = width;
		camera.update();
	}

	public ArrayList<Bullet> getBullets() {
		return bullets;
	}

	public void setBullets(ArrayList<Bullet> bullets) {
		this.bullets = bullets;
	}
	

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		map.dispose();
		renderer.dispose();
	}

}
