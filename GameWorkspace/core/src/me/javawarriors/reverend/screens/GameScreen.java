package me.javawarriors.reverend.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader.Uniform;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;

import me.javawarriors.reverend.ReverendGame;
import me.javawarriors.reverend.entities.Bullet;
import me.javawarriors.reverend.entities.Healing;
import me.javawarriors.reverend.entities.Mob1;
import me.javawarriors.reverend.entities.Mob2;
import me.javawarriors.reverend.entities.Player;
import me.javawarriors.reverend.entities.Shield;
import me.javawarriors.reverend.entities.Trap;

public class GameScreen implements Screen {

	// Shader
	private ShaderProgram shader;
	private boolean shaderON = false;
	// map
	private TiledMap map;
	// cam
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	// game
	private Mob1 mob1a, mob1b, mob1c, mob1d, mob1e, mob1f;
	private Mob2 mob2a;
	private Player player;
	private Trap trap;
	private Healing heal;
	private ArrayList<Bullet> bullets;
	private ArrayList<Bullet> pbullets;
	private ArrayList<Bullet> mbullets;
	private ArrayList<Shield> shields;
	private ArrayList<Mob1> mob1s;
	private ArrayList<Mob2> mob2s;
	private ArrayList<Trap> traps;
	private ArrayList<Healing> heals;
	ReverendGame game;

	// music
	Music music;
	boolean bgmusic = false;

	public GameScreen(ReverendGame game) {

		this.game = game;
		shields = new ArrayList<Shield>();
		bullets = new ArrayList<Bullet>();
		pbullets = new ArrayList<Bullet>();
		mbullets = new ArrayList<Bullet>();
		mob1s = new ArrayList<Mob1>();
		mob2s = new ArrayList<Mob2>();
		traps = new ArrayList<Trap>();
		heals = new ArrayList<Healing>();
		music = Gdx.audio.newMusic(Gdx.files.internal("bgmusic.mp3"));
		music.setVolume(0.1f);

	}

	@Override
	public void show() {
		Gdx.gl.glClearColor(0, 0, 0, 1);

		TmxMapLoader loader = new TmxMapLoader();
		map = loader.load("map1.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 4f);
		player = new Player((TiledMapTileLayer) map.getLayers().get(3), this);
		camera = new OrthographicCamera();

		ShaderProgram.pedantic = false;
		shader = new ShaderProgram(Gdx.files.internal("shaders/vignette.vsh"),
				Gdx.files.internal("shaders/vignette.fsh"));
		System.out.println(shader.isCompiled() ? "shader compiled." : shader.getLog());

		trap = new Trap(player, this, 384, 384);
		heal = new Healing(player, this, 500, 500);
		heal = new Healing(player, this, 800, 800);
		heal = new Healing(player, this, 600, 1800);

		mob1a = new Mob1((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob1a", 600, 1800, 100);
		mob1a = new Mob1((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob1a", 3500, 500, 100);
		mob1a = new Mob1((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob1a", 3900, 600, 100);
		mob1a = new Mob1((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob1a", 4538, 295, 100);
		mob1a = new Mob1((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob1a", 7534, 1205, 100);
		mob1a = new Mob1((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob1a", 9149, 1170, 100);
		mob1a = new Mob1((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob1a", 8637, 307, 100);
		mob1a = new Mob1((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob1a", 3947, 3713, 100);
		mob1a = new Mob1((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob1a", 5535, 3524, 100);
		mob1a = new Mob1((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob1a", 5825, 3524, 100);
		mob2a = new Mob2((TiledMapTileLayer) map.getLayers().get(3), (TiledMapTileLayer) map.getLayers().get(0), this,
				"Mob2a", 600, 1800, 200, 20, 40, "bebe1.png");
		renderer.getBatch().setShader(shader);
	}

	@Override
	public void render(float delta) {
		if (!bgmusic) {
			music.play();
			bgmusic = true;
		}
		// camera.viewportHeight = 1920;
		// camera.viewportWidth = 1080;
		// camera.update();

		shader.setUniformf("innerRadius", (float) player.getHP() / 100);
		if (player.getHP() < 60) {
			shader.setUniformf("intensity", 0.9f);
		} else {
			shader.setUniformf("intensity", 0f);
		}
		shader.setUniformf("u_resolution", 1920, 1080);

		bullets.addAll(pbullets);
		bullets.addAll(mbullets);
		// Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		player.Update(Gdx.graphics.getDeltaTime());

		// CAmera Smooth Follow lerp değerini değiştirerek kamera follow hızı
		// değiştri=========================

		float lerp = 10f;
		Vector3 position = camera.position;
		// position.x += (player.getX() - position.x + player.getWidth() / 2) * lerp *
		// Gdx.graphics.getDeltaTime();
		// position.y += (player.getY() - position.y + player.getHeight() / 2) * lerp *
		// Gdx.graphics.getDeltaTime();

		// sabit
		// cam============================================================================================
		camera.setToOrtho(false);
		camera.position.set(player.getX(), player.getY(), 0);

		camera.update();
		renderer.setView(camera);
		renderer.render();
		renderer.getBatch().begin();

		for (Mob1 mob : mob1s) {
			mob.Update(Gdx.graphics.getDeltaTime());
			if (mob.isDead()) {
				mob.setInactive();
				;
			}
		}

		for (Mob1 mob1 : mob1s) {
			mob1.render((SpriteBatch) renderer.getBatch());
		}

		for (Mob2 mob : mob2s) {
			mob.Update(Gdx.graphics.getDeltaTime());
			if (mob.isDead()) {
				mob.setInactive();
			}
		}

		for (Mob2 mob2 : mob2s) {
			mob2.render((SpriteBatch) renderer.getBatch());
		}

		for (Bullet bullet : bullets) {
			bullet.render((SpriteBatch) renderer.getBatch());
		}
		for (Trap trap : traps) {
			trap.update(Gdx.graphics.getDeltaTime());
			renderer.getBatch().draw(trap.GetFrame(), trap.getX(), trap.getY(), 16 * 4, 16 * 4);
			// trap.render((SpriteBatch) renderer.getBatch());
		}
		for (Healing heal : heals) {
			heal.update(Gdx.graphics.getDeltaTime());
			heal.render((SpriteBatch) renderer.getBatch());

			renderer.getBatch().draw(player.GetHealthFrame(), camera.position.x - 800,
					camera.position.y - this.camera.viewportWidth / 4, 72 * 5, 25 * 5);
			renderer.getBatch().draw(player.GetFrame(), player.getX(), player.getY(), player.getWidth(),
					player.getHeight());
			for (Shield shield : shields) {
				shield.render((SpriteBatch) renderer.getBatch());
			}

		}
		// renderer.getBatch().draw(mob1.GetFrame(), mob1.getX(), mob1.getY(),
		// mob1.getWidth(),
		// player.getHeight());

		renderer.getBatch().end();

	}

	@Override
	public void resize(int width, int height) {
		camera.viewportHeight = height;
		camera.viewportWidth = width;
		// camera.update();
		shader.setUniformf("u_resolution", width, height);
	}

	public ArrayList<Shield> getShields() {
		return shields;
	}

	public void setShields(ArrayList<Shield> shields) {
		this.shields = shields;
	}

	public ArrayList<Bullet> getBullets() {
		return bullets;
	}

	public ArrayList<Bullet> getPbullets() {
		return pbullets;
	}

	public void setPbullets(ArrayList<Bullet> pbullets) {
		this.pbullets = pbullets;
	}

	public ArrayList<Bullet> getMbullets() {
		return mbullets;
	}

	public void setMbullets(ArrayList<Bullet> mbullets) {
		this.mbullets = mbullets;
	}

	public void setBullets(ArrayList<Bullet> bullets) {
		this.bullets = bullets;
	}

	public ArrayList<Mob2> getMob2s() {
		return mob2s;
	}

	public void setMob2s(ArrayList<Mob2> mob2s) {
		this.mob2s = mob2s;
	}

	public ArrayList<Mob1> getMob1s() {
		return mob1s;
	}

	public void setMob1s(ArrayList<Mob1> mob1s) {
		this.mob1s = mob1s;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public ArrayList<Trap> getTraps() {
		return traps;
	}

	public void setTraps(ArrayList<Trap> traps) {
		this.traps = traps;
	}

	public ArrayList<Healing> getHeals() {
		return heals;
	}

	public void setHeals(ArrayList<Healing> heals) {
		this.heals = heals;
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
