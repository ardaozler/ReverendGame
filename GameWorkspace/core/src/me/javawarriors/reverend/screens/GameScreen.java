package me.javawarriors.reverend.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import me.javawarriors.reverend.ReverendGame;
import me.javawarriors.reverend.entities.Player;

public class GameScreen implements Screen {

	// map
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	Player player = new Player();

	ReverendGame game;

	public GameScreen(ReverendGame game) {

		this.game = game;
	}

	@Override
	public void show() {
		TmxMapLoader loader = new TmxMapLoader();
		map = loader.load("try2.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 4f);

		camera = new OrthographicCamera();

	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		player.move();

		
		camera.setToOrtho(false);
		camera.position.set(player.getX(), player.getY(), 0);
		camera.update();
		
		renderer.setView(camera);
		renderer.render();
		
		renderer.getBatch().begin();
		renderer.getBatch().draw(player.GetFrame(), player.getX() , player.getY() , player.getWidth(),  player.getHeight());
		renderer.getBatch().end();
		

	}

	@Override
	public void resize(int width, int height) {
		camera.viewportHeight = 1080;
		camera.viewportWidth = 1920;
		camera.update();
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
